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

import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.LongValues;

import java.io.IOException;

/**
 * Retrieves an instance previously written by {@link BlockWriter}.
 *
 * <p>The difference between {@link BlockReader} and {@link DirectReader} is that a block with a
 * size of 128 is pre-read, which is very helpful for dense reading, However the overhead for sparse
 * reading is not very high, thanks to the efficient {@link ForUtil}.
 *
 * <p>Example usage:
 *
 * <pre class="prettyprint">
 *   int bitsPerValue = 10;
 *   int numValues = 100;
 *   IndexInput in = dir.openInput("packed", IOContext.DEFAULT);
 *   LongValues values = new BlockReader(in.slice("", start, end), bitsPerValue, numValues);
 *   for (int i = 0; i &lt; numValues; i++) {
 *     long value = values.get(i);
 *   }
 * </pre>
 *
 * @see BlockWriter
 */
public class BlockReader extends LongValues {

  public static final int BLOCK_SIZE = ForUtil.BLOCK_SIZE;
  private static final int BLOCK_MASK = ForUtil.BLOCK_SIZE - 1;
  private static final int SAMPLE_TIME = 32;
  private static final int SAMPLE_DELTA_THRESHOLD = SAMPLE_TIME << 6;

  private final int bpv;
  private final int blockBytes;
  private final ForUtil.Decoder decoder;
  private final IndexInput input;
  private final long[] buffer;
  private final long offset;
  private final long numValues;
  private final long remainderIndex;

  private LongValues remainderReader;
  private long currentBlock = -1;
  private boolean checking = true;
  private boolean doWarm = true;
  private long firstIndex;
  private long lastIndex = -1;
  private int counter = 0;

  public BlockReader(IndexInput input, int bpv, long numValues) {
    this(input, bpv, 0, numValues);
  }

  public BlockReader(IndexInput input, int bpv, long offset, long numValues) {
    this(input, bpv, offset, new ForUtil(), new long[BLOCK_SIZE], numValues);
  }

  public BlockReader(
          IndexInput input, int bpv, long offset, ForUtil forUtil, long[] buffer, long numValues) {
    this.bpv = bpv;
    this.buffer = buffer;
    this.input = input;
    this.blockBytes = forUtil.numBytes(bpv);
    this.offset = offset;
    this.numValues = numValues;
    this.remainderIndex = numValues - (int) (numValues & BLOCK_MASK);
    this.decoder = forUtil.decoder(bpv);
  }

  @Override
  public long get(long index) {
    assert index >= 0 && index < numValues;
    assert index >= lastIndex;
    lastIndex = index;
    try {
      if (index >= remainderIndex) {
        return readRemainder(index);
      }
      return warm(index);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void check(long index) {
    if (counter == 0) {
      firstIndex = index;
    }
    if (counter == SAMPLE_TIME) {
      if (index - firstIndex > SAMPLE_DELTA_THRESHOLD) {
        new Exception().printStackTrace();
        doWarm = false;
      }
      checking = false;
    }
    counter++;
  }

  private long doGet(long index) throws IOException {
    long block = index >>> ForUtil.BLOCK_SIZE_LOG2;
    return decoder.get(input, (int) (index & BLOCK_MASK), offset + block * blockBytes);
  }

  private long warm(long index) throws IOException {
    long block = index >>> ForUtil.BLOCK_SIZE_LOG2;
    if (block != currentBlock) {
      input.seek(offset + block * blockBytes);
      decoder.decode(input, buffer);
      this.currentBlock = block;
    }
    return buffer[(int) (index & BLOCK_MASK)];
  }

  private long readRemainder(long index) throws IOException {
    if (remainderReader == null) {
      remainderReader = DirectReader.getInstance(input.randomAccessSlice(0, input.length()), bpv);
    }
    return remainderReader.get(index);
  }
}
