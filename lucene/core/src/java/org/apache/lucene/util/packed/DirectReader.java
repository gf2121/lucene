/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.util.packed;

import org.apache.lucene.store.RandomAccessInput;
import org.apache.lucene.util.LongValues;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Retrieves an instance previously written by {@link DirectWriter}
 *
 * <p>Example usage:
 *
 * <pre class="prettyprint">
 *   int bitsPerValue = 100;
 *   IndexInput in = dir.openInput("packed", IOContext.DEFAULT);
 *   LongValues values = DirectReader.getInstance(in.randomAccessSlice(start, end), bitsPerValue);
 *   for (int i = 0; i &lt; numValues; i++) {
 *     long value = values.get(i);
 *   }
 * </pre>
 *
 * @see DirectWriter
 */
public class DirectReader {

  static final int BLOCK_SHIFT = 7;
  private static final int BLOCK_SIZE = 1 << BLOCK_SHIFT;
  private static final int BLOCK_MASK = BLOCK_SIZE - 1;
  private static final int WARM_UP_SAMPLE_TIME = BLOCK_SIZE << 1;
  private static final int WARM_UP_DELTA_THRESHOLD = WARM_UP_SAMPLE_TIME << 1;

  /**
   * Retrieves an instance from the specified slice written decoding {@code bitsPerValue} for each
   * value
   */
  public static LongValues getInstance(RandomAccessInput slice, int bitsPerValue) {
    return getInstance(slice, bitsPerValue, 0);
  }

  /**
   * Retrieves an instance from the specified {@code offset} of the given slice decoding {@code
   * bitsPerValue} for each value
   */
  public static LongValues getInstance(RandomAccessInput slice, int bitsPerValue, long offset) {
    switch (bitsPerValue) {
      case 1:
        return new DirectPackedReader1(slice, offset);
      case 2:
        return new DirectPackedReader2(slice, offset);
      case 4:
        return new DirectPackedReader4(slice, offset);
      case 8:
        return new DirectPackedReader8(slice, offset);
      case 12:
        return new DirectPackedReader12(slice, offset);
      case 16:
        return new DirectPackedReader16(slice, offset);
      case 20:
        return new DirectPackedReader20(slice, offset);
      case 24:
        return new DirectPackedReader24(slice, offset);
      case 28:
        return new DirectPackedReader28(slice, offset);
      case 32:
        return new DirectPackedReader32(slice, offset);
      case 40:
        return new DirectPackedReader40(slice, offset);
      case 48:
        return new DirectPackedReader48(slice, offset);
      case 56:
        return new DirectPackedReader56(slice, offset);
      case 64:
        return new DirectPackedReader64(slice, offset);
      default:
        throw new IllegalArgumentException("unsupported bitsPerValue: " + bitsPerValue);
    }
  }

  /**
   * Retrieves an instance that is specialized for merges and is typically faster at sequential
   * access but slower at random access.
   */
  public static LongValues getMergeInstance(
          RandomAccessInput slice, int bitsPerValue, long numValues) {
    return getMergeInstance(slice, bitsPerValue, 0L, numValues);
  }

  /**
   * Retrieves an instance that is specialized for merges and is typically faster at sequential
   * access.
   */
  public static LongValues getMergeInstance(
          RandomAccessInput slice, int bitsPerValue, long baseOffset, long numValues) {
    return new LongValues() {

      private final long[] buffer = new long[BLOCK_SIZE];
      private long blockIndex = -1;

      @Override
      public long get(long index) {
        assert index < numValues;
        final long blockIndex = index >>> BLOCK_SHIFT;
        if (this.blockIndex != blockIndex) {
          try {
            fillBuffer(blockIndex << BLOCK_SHIFT);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
          this.blockIndex = blockIndex;
        }
        return buffer[(int) (index & BLOCK_MASK)];
      }

      private void fillBuffer(long index) throws IOException {
        // NOTE: we're not allowed to read more than 3 bytes past the last value
        if (index >= numValues - BLOCK_SIZE) {
          // 128 values left or less
          final LongValues slowInstance = getInstance(slice, bitsPerValue, baseOffset);
          final int numValuesLastBlock = Math.toIntExact(numValues - index);
          for (int i = 0; i < numValuesLastBlock; ++i) {
            buffer[i] = slowInstance.get(index + i);
          }
        } else if ((bitsPerValue & 0x07) == 0) {
          // bitsPerValue is a multiple of 8: 8, 16, 24, 32, 30, 48, 56, 64
          final int bytesPerValue = bitsPerValue / Byte.SIZE;
          final long mask = bitsPerValue == 64 ? ~0L : (1L << bitsPerValue) - 1;
          long offset = baseOffset + (index * bitsPerValue) / 8;
          for (int i = 0; i < BLOCK_SIZE; ++i) {
            if (bitsPerValue > Integer.SIZE) {
              buffer[i] = slice.readLong(offset) & mask;
            } else if (bitsPerValue > Short.SIZE) {
              buffer[i] = slice.readInt(offset) & mask;
            } else if (bitsPerValue > Byte.SIZE) {
              buffer[i] = Short.toUnsignedLong(slice.readShort(offset));
            } else {
              buffer[i] = Byte.toUnsignedLong(slice.readByte(offset));
            }
            offset += bytesPerValue;
          }
        } else if (bitsPerValue < 8) {
          // bitsPerValue is 1, 2 or 4
          final int valuesPerLong = Long.SIZE / bitsPerValue;
          final long mask = (1L << bitsPerValue) - 1;
          long offset = baseOffset + (index * bitsPerValue) / 8;
          int i = 0;
          for (int l = 0; l < 2 * bitsPerValue; ++l) {
            final long bits = slice.readLong(offset);
            for (int j = 0; j < valuesPerLong; ++j) {
              buffer[i++] = (bits >>> (j * bitsPerValue)) & mask;
            }
            offset += Long.BYTES;
          }
        } else {
          // bitsPerValue is 12, 20 or 28
          // Read values 2 by 2
          final int numBytesFor2Values = bitsPerValue * 2 / Byte.SIZE;
          final long mask = (1L << bitsPerValue) - 1;
          long offset = baseOffset + (index * bitsPerValue) / 8;
          for (int i = 0; i < BLOCK_SIZE; i += 2) {
            final long l;
            if (numBytesFor2Values > Integer.BYTES) {
              l = slice.readLong(offset);
            } else {
              l = slice.readInt(offset);
            }
            buffer[i] = l & mask;
            buffer[i + 1] = (l >>> bitsPerValue) & mask;
            offset += numBytesFor2Values;
          }
        }
      }
    };
  }

  static final class DirectPackedReader1 extends LongValues {
    private static final int BPV = 1;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader1(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    private long doGet(long index) {
      try {
        int shift = (int) (index & 7);
        return (in.readByte(offset + (index >>> 3)) >>> shift) & 0x1;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      for (int i = 0; i < TMP_LENGTH; i++) {
        long l = tmp[i];
        int pos = i << 6;
        int end = pos + NUM_VALUES_PER_LONG;
        while (pos < end) {
          buffer[pos++] = l & 1L;
          l >>>= BPV;
        }
      }
    }
  }

  static final class DirectPackedReader2 extends LongValues {
    private static final int BPV = 2;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader2(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }


    public long doGet(long index) {
      try {
        int shift = ((int) (index & 3)) << 1;
        return (in.readByte(offset + (index >>> 2)) >>> shift) & 0x3;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      for (int i = 0; i < TMP_LENGTH; i++) {
        long l = tmp[i];
        int pos = i << 5;
        int end = pos + NUM_VALUES_PER_LONG;
        while (pos < end) {
          buffer[pos++] = l & 3L;
          l >>>= BPV;
        }
      }
    }
  }

  static final class DirectPackedReader4 extends LongValues {
    private static final int BPV = 4;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader4(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        int shift = (int) (index & 1) << 2;
        return (in.readByte(offset + (index >>> 1)) >>> shift) & 0xF;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      for (int i = 0; i < TMP_LENGTH; i++) {
        long l = tmp[i];
        int pos = i << 4;
        int end = pos + NUM_VALUES_PER_LONG;
        while (pos < end) {
          buffer[pos++] = l & 0xFL;
          l >>>= BPV;
        }
      }
    }
  }

  static final class DirectPackedReader8 extends LongValues {
    private static final int BPV = 8;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader8(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        return in.readByte(offset + index) & 0xFF;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      for (int i = 0; i < TMP_LENGTH; i++) {
        long l = tmp[i];
        int pos = i << 3;
        int end = pos + NUM_VALUES_PER_LONG;
        while (pos < end) {
          buffer[pos++] = l & 0xFFL;
          l >>>= BPV;
        }
      }
    }
  }

  static final class DirectPackedReader12 extends LongValues {
    private static final int BPV = 12;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader12(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        long offset = (index * 12) >>> 3;
        int shift = (int) (index & 1) << 2;
        return (in.readShort(this.offset + offset) >>> shift) & 0xFFF;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      int pos = 0, tmpIndex = -1;
      while (pos < BLOCK_SIZE) {
        buffer[pos++] = tmp[++tmpIndex] & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 12) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 24) & 0XFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 36) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 48) & 0xFFFL;
        buffer[pos++] = ((tmp[tmpIndex] >>> 60) & 0xFFFL) | ((tmp[++tmpIndex] & 0xFFL) << 4);
        buffer[pos++] = (tmp[tmpIndex] >>> 8) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 20) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 32) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 44) & 0xFFFL;
        buffer[pos++] = ((tmp[tmpIndex] >>> 56) & 0xFFFL) | ((tmp[++tmpIndex] & 0xFL) << 8);
        buffer[pos++] = (tmp[tmpIndex] >>> 4) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 16) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 28) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 40) & 0xFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 52) & 0xFFFL;
      }
    }
  }

  static final class DirectPackedReader16 extends LongValues {
    private static final int BPV = 16;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader16(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        return in.readShort(offset + (index << 1)) & 0xFFFF;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      for (int i = 0; i < TMP_LENGTH; i++) {
        long l = tmp[i];
        int pos = i << 2;
        int end = pos + NUM_VALUES_PER_LONG;
        while (pos < end) {
          buffer[pos++] = l & 0xFFFFL;
          l >>>= BPV;
        }
      }
    }
  }

  static final class DirectPackedReader20 extends LongValues {
    private static final int BPV = 20;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader20(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        long offset = (index * 20) >>> 3;
        int shift = (int) (index & 1) << 2;
        return (in.readInt(this.offset + offset) >>> shift) & 0xFFFFF;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      int pos = 0, tmpIndex = -1;
      while (pos < BLOCK_SIZE) {
        buffer[pos++] = tmp[++tmpIndex] & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 20) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 40) & 0XFFFFFL | ((tmp[++tmpIndex] & 0xFFFFL) << 4);
        buffer[pos++] = (tmp[tmpIndex] >>> 16) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 36) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 56) & 0xFFFFFL | ((tmp[++tmpIndex] & 0xFFFL) << 8);
        buffer[pos++] = (tmp[tmpIndex] >>> 12) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 32) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 52) & 0xFFFFFL | ((tmp[++tmpIndex] & 0xFFL) << 12);
        buffer[pos++] = (tmp[tmpIndex] >>> 8) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 28) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 48) & 0xFFFFFL | ((tmp[++tmpIndex] & 0xFL) << 16);
        buffer[pos++] = (tmp[tmpIndex] >>> 4) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 24) & 0xFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 44) & 0xFFFFFL;
      }
    }
  }

  static final class DirectPackedReader24 extends LongValues {
    private static final int BPV = 24;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader24(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        return in.readInt(this.offset + index * 3) & 0xFFFFFF;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      int pos = 0, tmpIndex = -1;
      while (pos < BLOCK_SIZE) {
        buffer[pos++] = tmp[++tmpIndex] & 0xFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 24) & 0xFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 48) & 0XFFFFFFL | ((tmp[++tmpIndex] & 0xFFL) << 16);
        buffer[pos++] = (tmp[tmpIndex] >>> 8) & 0xFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 32) & 0xFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 56) & 0xFFFFFFL | ((tmp[++tmpIndex] & 0xFFFFL) << 8);
        buffer[pos++] = (tmp[tmpIndex] >>> 16) & 0xFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 40) & 0xFFFFFFL;
      }
    }
  }

  static final class DirectPackedReader28 extends LongValues {
    private static final int BPV = 28;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader28(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }


    public long doGet(long index) {
      try {
        long offset = (index * 28) >>> 3;
        int shift = (int) (index & 1) << 2;
        return (in.readInt(this.offset + offset) >>> shift) & 0xFFFFFFF;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }


    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      int pos = 0, tmpIndex = -1;
      while (pos < BLOCK_SIZE) {
        buffer[pos++] = tmp[++tmpIndex] & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 28) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 56) & 0XFFFFFFFL | ((tmp[++tmpIndex] & 0xFFFFFL) << 8);
        buffer[pos++] = (tmp[tmpIndex] >>> 20) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 48) & 0xFFFFFFFL | ((tmp[++tmpIndex] & 0xFFFL) << 16);
        buffer[pos++] = (tmp[tmpIndex] >>> 12) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 40) & 0xFFFFFFFL | ((tmp[++tmpIndex] & 0xFL) << 24);
        buffer[pos++] = (tmp[tmpIndex] >>> 4) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 32) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 60) & 0xFFFFFFFL | ((tmp[++tmpIndex] & 0xFFFFFFL) << 4);
        buffer[pos++] = (tmp[tmpIndex] >>> 24) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 52) & 0xFFFFFFFL | ((tmp[++tmpIndex] & 0xFFFFL) << 12);
        buffer[pos++] = (tmp[tmpIndex] >>> 16) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 44) & 0xFFFFFFFL | ((tmp[++tmpIndex] & 0xFFL) << 20);
        buffer[pos++] = (tmp[tmpIndex] >>> 8) & 0xFFFFFFFL;
        buffer[pos++] = (tmp[tmpIndex] >>> 36) & 0xFFFFFFFL;
      }
    }
  }

  static final class DirectPackedReader32 extends LongValues {
    private static final int BPV = 32;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader32(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        return in.readInt(this.offset + (index << 2)) & 0xFFFFFFFFL;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, tmp, 0, TMP_LENGTH);
      for (int i = 0; i < TMP_LENGTH; i++) {
        long l = tmp[i];
        int pos = i << 1;
        int end = pos + NUM_VALUES_PER_LONG;
        while (pos < end) {
          buffer[pos++] = l & 0xFFFFFFFFL;
          l >>>= BPV;
        }
      }
    }
  }

  static final class DirectPackedReader40 extends LongValues {
    final RandomAccessInput in;
    final long offset;

    DirectPackedReader40(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    @Override
    public long get(long index) {
      try {
        return in.readLong(this.offset + index * 5) & 0xFFFFFFFFFFL;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  static final class DirectPackedReader48 extends LongValues {
    final RandomAccessInput in;
    final long offset;

    DirectPackedReader48(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    @Override
    public long get(long index) {
      try {
        return in.readLong(this.offset + index * 6) & 0xFFFFFFFFFFFFL;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  static final class DirectPackedReader56 extends LongValues {
    final RandomAccessInput in;
    final long offset;

    DirectPackedReader56(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    @Override
    public long get(long index) {
      try {
        return in.readLong(this.offset + index * 7) & 0xFFFFFFFFFFFFFFL;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  static final class DirectPackedReader64 extends LongValues {
    private static final int BPV = 64;
    private static final int BLOCK_BYTES = BLOCK_SIZE * BPV / Byte.SIZE;
    private static final int TMP_LENGTH = BLOCK_BYTES / Long.BYTES;
    private static final int NUM_VALUES_PER_LONG = Long.SIZE / BPV;
    private final long[] buffer = new long[BLOCK_SIZE];
    private final long[] tmp = new long[TMP_LENGTH];
    private final RandomAccessInput in;
    private final long offset;
    private boolean checking = true;
    private boolean warm = false;
    private long firstIndex;
    private long lastIndex;
    private int counter = -1;
    private long currentBlock = -1;

    public DirectPackedReader64(RandomAccessInput in, long offset) {
      this.in = in;
      this.offset = offset;
    }

    private void check(long index) {
      if (counter == -1) {
        firstIndex = index;
      }
      if (index < lastIndex) {
        checking = false;
      } else {
        lastIndex = index;
        if (++counter == WARM_UP_SAMPLE_TIME) {
          warm = index - firstIndex <= WARM_UP_DELTA_THRESHOLD;
          checking = false;
        }
      }
    }

    @Override
    public long get(long index) {
      if (checking) {
        check(index);
      }
      if (warm) {
        return warm(index);
      } else {
        return doGet(index);
      }
    }

    private long warm(long index) {
      final long block = index >> BLOCK_SHIFT;
      if (block != currentBlock) {
        currentBlock = block;
        try {
          fillBuffer(block);
        } catch (@SuppressWarnings("unused") Exception e) {
          warm = false;
          return doGet(index);
        }
      }
      return buffer[(int) (index & BLOCK_MASK)];
    }

    public long doGet(long index) {
      try {
        return in.readLong(offset + (index << 3));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    void fillBuffer(long block) throws IOException {
      in.readLongs(offset + BLOCK_BYTES * block, buffer, 0, BLOCK_SIZE);
    }
  }
}
