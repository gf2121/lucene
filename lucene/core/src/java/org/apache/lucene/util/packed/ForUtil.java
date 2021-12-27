// This file has been automatically generated, DO NOT EDIT

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

import java.io.IOException;
import org.apache.lucene.store.DataInput;
import org.apache.lucene.store.DataOutput;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.MathUtil;

// Inspired from https://fulmicoton.com/posts/bitpacking/
// Encodes multiple integers in a long to get SIMD-like speedups.
// If bitsPerValue <= 8 then we pack 8 ints per long
// else if bitsPerValue <= 16 we pack 4 ints per long
// else we pack 2 ints per long
final class ForUtil {

  static final int BLOCK_SIZE = 128;
  static final int BLOCK_SIZE_LOG2 = MathUtil.log(BLOCK_SIZE, 2);
  private static final int BLOCK_SIZE_DIV_2 = BLOCK_SIZE >> 1;
  private static final int BLOCK_SIZE_DIV_4 = BLOCK_SIZE >> 2;
  static final int BLOCK_SIZE_DIV_8 = BLOCK_SIZE >> 3;
  private static final int BLOCK_SIZE_DIV_64 = BLOCK_SIZE >> 6;
  private static final int BLOCK_SIZE_DIV_8_MUL_1 = BLOCK_SIZE_DIV_8;
  private static final int BLOCK_SIZE_DIV_8_MUL_2 = BLOCK_SIZE_DIV_8 * 2;
  private static final int BLOCK_SIZE_DIV_8_MUL_3 = BLOCK_SIZE_DIV_8 * 3;
  private static final int BLOCK_SIZE_DIV_8_MUL_4 = BLOCK_SIZE_DIV_8 * 4;
  private static final int BLOCK_SIZE_DIV_8_MUL_5 = BLOCK_SIZE_DIV_8 * 5;
  private static final int BLOCK_SIZE_DIV_8_MUL_6 = BLOCK_SIZE_DIV_8 * 6;
  private static final int BLOCK_SIZE_DIV_8_MUL_7 = BLOCK_SIZE_DIV_8 * 7;
  private static final int BLOCK_SIZE_LOG2_MIN_3 = BLOCK_SIZE_LOG2 - 3;

  private static long expandMask32(long mask32) {
    return mask32 | (mask32 << 32);
  }

  private static long expandMask16(long mask16) {
    return expandMask32(mask16 | (mask16 << 16));
  }

  private static long expandMask8(long mask8) {
    return expandMask16(mask8 | (mask8 << 8));
  }

  private static long mask32(int bitsPerValue) {
    return expandMask32((1L << bitsPerValue) - 1);
  }

  private static long mask64(int bitsPerValue) {
    return (1L << bitsPerValue) - 1;
  }

  private static long mask16(int bitsPerValue) {
    return expandMask16((1L << bitsPerValue) - 1);
  }

  private static long mask8(int bitsPerValue) {
    return expandMask8((1L << bitsPerValue) - 1);
  }

  private static void expand8(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_8; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 56) & 0xFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_1 + i] = (l >>> 48) & 0xFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_2 + i] = (l >>> 40) & 0xFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_3 + i] = (l >>> 32) & 0xFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_4 + i] = (l >>> 24) & 0xFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_5 + i] = (l >>> 16) & 0xFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_6 + i] = (l >>> 8) & 0xFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_7 + i] = l & 0xFFL;
    }
  }

  private static void collapse8(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_8; ++i) {
      arr[i] =
          (arr[i] << 56)
              | (arr[BLOCK_SIZE_DIV_8_MUL_1 + i] << 48)
              | (arr[BLOCK_SIZE_DIV_8_MUL_2 + i] << 40)
              | (arr[BLOCK_SIZE_DIV_8_MUL_3 + i] << 32)
              | (arr[BLOCK_SIZE_DIV_8_MUL_4 + i] << 24)
              | (arr[BLOCK_SIZE_DIV_8_MUL_5 + i] << 16)
              | (arr[BLOCK_SIZE_DIV_8_MUL_6 + i] << 8)
              | arr[BLOCK_SIZE_DIV_8_MUL_7 + i];
    }
  }

  private static void expand16(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_4; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 48) & 0xFFFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_2 + i] = (l >>> 32) & 0xFFFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_4 + i] = (l >>> 16) & 0xFFFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_6 + i] = l & 0xFFFFL;
    }
  }

  private static void collapse16(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_4; ++i) {
      arr[i] =
          (arr[i] << 48)
              | (arr[BLOCK_SIZE_DIV_8_MUL_2 + i] << 32)
              | (arr[BLOCK_SIZE_DIV_8_MUL_4 + i] << 16)
              | arr[BLOCK_SIZE_DIV_8_MUL_6 + i];
    }
  }

  private static void expand32(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_2; ++i) {
      long l = arr[i];
      arr[i] = l >>> 32;
      arr[BLOCK_SIZE_DIV_8_MUL_4 + i] = l & 0xFFFFFFFFL;
    }
  }

  private static void collapse32(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_2; ++i) {
      arr[i] = (arr[i] << 32) | arr[BLOCK_SIZE_DIV_8_MUL_4 + i];
    }
  }

  private final long[] tmp;

  ForUtil() {
    this.tmp = new long[BLOCK_SIZE];
  }

  /** Encode 128 integers from {@code longs} into {@code out}. */
  void encode(long[] longs, int bitsPerValue, DataOutput out) throws IOException {
    final int nextPrimitive;
    final int numLongs;
    if (bitsPerValue <= 8) {
      nextPrimitive = 8;
      numLongs = BLOCK_SIZE_DIV_8;
      collapse8(longs);
    } else if (bitsPerValue <= 16) {
      nextPrimitive = 16;
      numLongs = BLOCK_SIZE_DIV_4;
      collapse16(longs);
    } else if (bitsPerValue <= 32) {
      nextPrimitive = 32;
      numLongs = BLOCK_SIZE_DIV_2;
      collapse32(longs);
    } else {
      nextPrimitive = 64;
      numLongs = BLOCK_SIZE;
    }

    final int numLongsPerShift = bitsPerValue * BLOCK_SIZE_DIV_64;
    int idx = 0;
    int shift = nextPrimitive - bitsPerValue;
    for (int i = 0; i < numLongsPerShift; ++i) {
      tmp[i] = longs[idx++] << shift;
    }
    for (shift = shift - bitsPerValue; shift >= 0; shift -= bitsPerValue) {
      for (int i = 0; i < numLongsPerShift; ++i) {
        tmp[i] |= longs[idx++] << shift;
      }
    }

    final int remainingBitsPerLong = shift + bitsPerValue;
    final long maskRemainingBitsPerLong;
    if (nextPrimitive == 8) {
      maskRemainingBitsPerLong = MASKS8[remainingBitsPerLong];
    } else if (nextPrimitive == 16) {
      maskRemainingBitsPerLong = MASKS16[remainingBitsPerLong];
    } else if (nextPrimitive == 32) {
      maskRemainingBitsPerLong = MASKS32[remainingBitsPerLong];
    } else {
      maskRemainingBitsPerLong = MASKS64[remainingBitsPerLong];
    }

    int tmpIdx = 0;
    int remainingBitsPerValue = bitsPerValue;
    while (idx < numLongs) {
      if (remainingBitsPerValue >= remainingBitsPerLong) {
        remainingBitsPerValue -= remainingBitsPerLong;
        tmp[tmpIdx++] |= (longs[idx] >>> remainingBitsPerValue) & maskRemainingBitsPerLong;
        if (remainingBitsPerValue == 0) {
          idx++;
          remainingBitsPerValue = bitsPerValue;
        }
      } else {
        final long mask1, mask2;
        if (nextPrimitive == 8) {
          mask1 = MASKS8[remainingBitsPerValue];
          mask2 = MASKS8[remainingBitsPerLong - remainingBitsPerValue];
        } else if (nextPrimitive == 16) {
          mask1 = MASKS16[remainingBitsPerValue];
          mask2 = MASKS16[remainingBitsPerLong - remainingBitsPerValue];
        } else if (nextPrimitive == 32) {
          mask1 = MASKS32[remainingBitsPerValue];
          mask2 = MASKS32[remainingBitsPerLong - remainingBitsPerValue];
        } else {
          mask1 = MASKS64[remainingBitsPerValue];
          mask2 = MASKS64[remainingBitsPerLong - remainingBitsPerValue];
        }
        tmp[tmpIdx] |= (longs[idx++] & mask1) << (remainingBitsPerLong - remainingBitsPerValue);
        remainingBitsPerValue = bitsPerValue - remainingBitsPerLong + remainingBitsPerValue;
        tmp[tmpIdx++] |= (longs[idx] >>> remainingBitsPerValue) & mask2;
      }
    }

    for (int i = 0; i < numLongsPerShift; ++i) {
      out.writeLong(tmp[i]);
    }
  }

  /** Number of bytes required to encode 128 integers of {@code bitsPerValue} bits per value. */
  int numBytes(int bitsPerValue) {
    return bitsPerValue << BLOCK_SIZE_LOG2_MIN_3;
  }

  /**
   * The pattern that this shiftLongs method applies is recognized by the C2 compiler, which
   * generates SIMD instructions for it in order to shift multiple longs at once.
   */
  private static void shiftLongs(long[] a, int count, long[] b, int bi, int shift, long mask) {
    for (int i = 0; i < count; ++i) {
      b[bi + i] = (a[i] >>> shift) & mask;
    }
  }

  private static final long[] MASKS8 = new long[8];
  private static final long[] MASKS16 = new long[16];
  private static final long[] MASKS32 = new long[32];
  private static final long[] MASKS64 = new long[64];

  static {
    for (int i = 0; i < 8; ++i) {
      MASKS8[i] = mask8(i);
    }
    for (int i = 0; i < 16; ++i) {
      MASKS16[i] = mask16(i);
    }
    for (int i = 0; i < 32; ++i) {
      MASKS32[i] = mask32(i);
    }
    for (int i = 0; i < 64; ++i) {
      MASKS64[i] = mask64(i);
    }
  }
  // mark values in array as final longs to avoid the cost of reading array, arrays should only be
  // used when the idx is a variable
  private static final long MASK8_1 = MASKS8[1];
  private static final long MASK8_2 = MASKS8[2];
  private static final long MASK8_4 = MASKS8[4];
  private static final long MASK16_1 = MASKS16[1];
  private static final long MASK16_2 = MASKS16[2];
  private static final long MASK16_4 = MASKS16[4];
  private static final long MASK16_8 = MASKS16[8];
  private static final long MASK16_12 = MASKS16[12];
  private static final long MASK32_1 = MASKS32[1];
  private static final long MASK32_2 = MASKS32[2];
  private static final long MASK32_4 = MASKS32[4];
  private static final long MASK32_8 = MASKS32[8];
  private static final long MASK32_12 = MASKS32[12];
  private static final long MASK32_16 = MASKS32[16];
  private static final long MASK32_20 = MASKS32[20];
  private static final long MASK32_24 = MASKS32[24];
  private static final long MASK32_28 = MASKS32[28];
  private static final long MASK64_1 = MASKS64[1];
  private static final long MASK64_2 = MASKS64[2];
  private static final long MASK64_4 = MASKS64[4];
  private static final long MASK64_8 = MASKS64[8];
  private static final long MASK64_12 = MASKS64[12];
  private static final long MASK64_16 = MASKS64[16];
  private static final long MASK64_20 = MASKS64[20];
  private static final long MASK64_24 = MASKS64[24];
  private static final long MASK64_28 = MASKS64[28];
  private static final long MASK64_32 = MASKS64[32];
  private static final long MASK64_40 = MASKS64[40];
  private static final long MASK64_48 = MASKS64[48];
  private static final long MASK64_56 = MASKS64[56];

  private static final long[] EXPAND_8_SHIFT = new long[BLOCK_SIZE];
  private static final long[] EXPAND_16_SHIFT = new long[BLOCK_SIZE];
  private static final long[] EXPAND_32_SHIFT = new long[BLOCK_SIZE];
  
  static {
    for(int i=0; i < BLOCK_SIZE; i++) {
      EXPAND_8_SHIFT[i] = ((127 ^ i) >>> 4) << 3;
      EXPAND_16_SHIFT[i] = ((127 ^ i) >>> 5) << 4;
      EXPAND_32_SHIFT[i] = ((127 ^ i) >>> 6) << 5;
    }
  }

  interface Decoder {
    long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException;
    void decode(DataInput in, long[] longs) throws IOException;
  }


  static class Decoder1 implements Decoder {

    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        int posBeforeExpand = i & 15;
        int shift1 = ((15 ^ posBeforeExpand) >>> 1) << 0;
        SHIFT_CACHE[i] = shift1 + EXPAND_8_SHIFT[i];
      }
    }

    private final long[] tmp;

    private Decoder1(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      in.seek(blockStartPointer + ((blockIndex & 1) << 3));
      return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_1;
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode1(in, tmp, longs);
      expand8(longs);
    }
  }

  static class Decoder2 implements Decoder {

    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        int posBeforeExpand = i & 15;
        int shift1 = ((15 ^ posBeforeExpand) >>> 2) << 1;
        SHIFT_CACHE[i] = shift1 + EXPAND_8_SHIFT[i];
      }
    }

    private final long[] tmp;

    private Decoder2(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      in.seek(blockStartPointer + ((blockIndex & 3) << 3));
      return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_2;
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode2(in, tmp, longs);
      expand8(longs);
    }
  }

  static class Decoder4 implements Decoder {

    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];
    private static final long[] POINTER_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        int posBeforeExpand = i & 15;
        int shift1 = ((15 ^ posBeforeExpand) >>> 3) << 2;
        SHIFT_CACHE[i] = shift1 + EXPAND_8_SHIFT[i];
        POINTER_CACHE[i] = (i & 7) << 3;
      }
    }

    private final long[] tmp;

    private Decoder4(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      in.seek(blockStartPointer + POINTER_CACHE[blockIndex]);
      return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_4;
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode4(in, tmp, longs);
      expand8(longs);
    }
  }

  static class Decoder8 implements Decoder {

    private final long[] tmp;

    private Decoder8(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      in.seek(blockStartPointer + ((blockIndex & 15) << 3));
      return (in.readLong() >>> EXPAND_8_SHIFT[blockIndex]) & MASK64_8;
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode8(in, tmp, longs);
      expand8(longs);
    }
  }

  static class Decoder12 implements Decoder {

    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];
    private static final long[] POINTER_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        SHIFT_CACHE[i] = EXPAND_16_SHIFT[i] + 4;
        int posBeforeExpand = i & 31;
        if (posBeforeExpand < 24) {
          POINTER_CACHE[i] = posBeforeExpand << 3;
        } else {
          POINTER_CACHE[i] = (posBeforeExpand - 24) * 24;
        }
      }
    }

    private final long[] tmp;

    private Decoder12(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      int posBeforeExpand = blockIndex & 31;
      in.seek(blockStartPointer + POINTER_CACHE[blockIndex]);
      if (posBeforeExpand < 24) {
        return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_12;
      } else {
        in.readLongs(tmp, 0, 3);
        long beforeExpand = ((tmp[0] & MASK16_4) << 8) | ((tmp[1] & MASK16_4) << 4) | (tmp[2] & MASK16_4);
        return (beforeExpand >> EXPAND_16_SHIFT[blockIndex]) & MASK64_12;
      }
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode12(in, tmp, longs);
      expand16(longs);
    }
  }

  static class Decoder16 implements Decoder {
    private static final long[] POINTER_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        POINTER_CACHE[i] = (i & 31) << 3;
      }
    }

    private final long[] tmp;

    private Decoder16(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      in.seek(blockStartPointer + POINTER_CACHE[blockIndex]);
      return (in.readLong() >>> EXPAND_16_SHIFT[blockIndex]) & MASK64_16;
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode16(in, tmp, longs);
      expand16(longs);
    }
  }

  static class Decoder20 implements Decoder {

    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        SHIFT_CACHE[i] = EXPAND_32_SHIFT[i] + 12;
      }
    }

    private final long[] tmp;

    private Decoder20(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      int posBeforeExpand = blockIndex & 63;
      if (posBeforeExpand < 40) {
        in.seek(blockStartPointer + (posBeforeExpand << 3));
        return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_20;
      } else {
        throw new IllegalArgumentException();
      }
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode20(in, tmp, longs);
      expand32(longs);
    }
  }

  static class Decoder24 implements Decoder {

    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];
    private static final long[] POINTER_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        SHIFT_CACHE[i] = EXPAND_32_SHIFT[i] + 8;
        int posBeforeExpand = i & 63;
        if (posBeforeExpand < 48) {
          POINTER_CACHE[i] = posBeforeExpand << 3;
        } else {
          POINTER_CACHE[i] = (posBeforeExpand - 48) * 24;
        }
      }
    }

    private final long[] tmp;

    private Decoder24(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      int posBeforeExpand = blockIndex & 63;
      in.seek(blockStartPointer + POINTER_CACHE[blockIndex]);
      if (posBeforeExpand < 48) {
        return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_24;
      } else {
        in.readLongs(tmp, 0, 3);
        long beforeExpand = ((tmp[0] & MASK32_8) << 16) | ((tmp[1] & MASK32_8) << 8) | (tmp[2] & MASK32_8);
        return (beforeExpand >> EXPAND_32_SHIFT[blockIndex]) & MASK64_24;
      }
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode24(in, tmp, longs);
      expand32(longs);
    }
  }

  static class Decoder28 implements Decoder {

    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];

    static {
      for(int i=0; i < BLOCK_SIZE; i++) {
        SHIFT_CACHE[i] = EXPAND_32_SHIFT[i] + 4;
      }
    }

    private final long[] tmp;

    private Decoder28(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      int posBeforeExpand = blockIndex & 63;
      if (posBeforeExpand < 56) {
        in.seek(blockStartPointer + (posBeforeExpand << 3));
        return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_28;
      } else {
        in.seek(blockStartPointer + (posBeforeExpand - 56) * 56);
        in.readLongs(tmp, 0, 7);
        shiftLongs(tmp, 7, tmp, 0, 0, MASK32_4);
        long beforeExpand = (tmp[0] << 24) | (tmp[1] << 20) | (tmp[2] << 16) | (tmp[3] << 12) | (tmp[4] << 8) | (tmp[5] << 4) | tmp[6];
        return (beforeExpand >> EXPAND_32_SHIFT[blockIndex]) & MASK64_28;
      }
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode28(in, tmp, longs);
      expand32(longs);
    }
  }

  static class Decoder32 implements Decoder {
    private final long[] tmp;

    private Decoder32(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      in.seek(blockStartPointer + ((blockIndex & 63) << 3));
      return (in.readLong() >>> EXPAND_32_SHIFT[blockIndex]) & MASK64_32;
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode32(in, tmp, longs);
      expand32(longs);
    }
  }

  static class Decoder40 implements Decoder {
    private final long[] tmp;

    private Decoder40(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      int posBeforeExpand = blockIndex & 127;
      if (posBeforeExpand < 80) {
        in.seek(blockStartPointer + (posBeforeExpand << 3));
        return (in.readLong() >>> 24) & MASK64_40;
      } else {
        throw new IllegalArgumentException();
      }
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode40(in, tmp, longs);
    }
  }

  static class Decoder48 implements Decoder {
    private final long[] tmp;

    private Decoder48(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      int posBeforeExpand = blockIndex & 127;
      if (posBeforeExpand < 96) {
        in.seek(blockStartPointer + (posBeforeExpand << 3));
        return (in.readLong() >>> 16) & MASK64_48;
      } else {
        in.seek(blockStartPointer + (posBeforeExpand - 96) * 24);
        in.readLongs(tmp, 0, 3);
        return ((tmp[0] & MASK64_16) << 32) | ((tmp[1] & MASK64_16) << 16) | (tmp[2] & MASK64_16);
      }
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode48(in, tmp, longs);
    }
  }

  static class Decoder56 implements Decoder {
    private final long[] tmp;

    private Decoder56(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      int posBeforeExpand = blockIndex & 127;
      if (posBeforeExpand < 112) {
        in.seek(blockStartPointer + (posBeforeExpand << 3));
        return (in.readLong() >>> 8) & MASK64_56;
      } else {
        in.seek(blockStartPointer + (posBeforeExpand - 112) * 56);
        in.readLongs(tmp, 0, 7);
        shiftLongs(tmp, 7, tmp, 0, 0, MASK64_8);
        return (tmp[0] << 48) | (tmp[1] << 40) | (tmp[2] << 32) | (tmp[3] << 24) | (tmp[4] << 16) | (tmp[5] << 8) | tmp[6];
      }
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode56(in, tmp, longs);
    }
  }

  static class Decoder64 implements Decoder {
    private final long[] tmp;

    private Decoder64(long[] tmp) {
      this.tmp = tmp;
    }

    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {
      in.seek(blockStartPointer + (blockIndex << 3));
      return in.readLong();
    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode64(in, tmp, longs);
    }
  }

  Decoder decoder(int bitsPerValue) {
    switch (bitsPerValue) {
      case 1:
        return new Decoder1(tmp);
      case 2:
        return new Decoder2(tmp);
      case 4:
        return new Decoder4(tmp);
      case 8:
        return new Decoder8(tmp);
      case 12:
        return new Decoder12(tmp);
      case 16:
        return new Decoder16(tmp);
      case 20:
        return new Decoder20(tmp);
      case 24:
        return new Decoder24(tmp);
      case 28:
        return new Decoder28(tmp);
      case 32:
        return new Decoder32(tmp);
      case 40:
        return new Decoder40(tmp);
      case 48:
        return new Decoder48(tmp);
      case 56:
        return new Decoder56(tmp);
      case 64:
        return new Decoder64(tmp);
      default:
        throw new AssertionError();
    }
  }

  private static void decode1(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 2);
    shiftLongs(tmp, 2, longs, 0, 7, MASK8_1);
    shiftLongs(tmp, 2, longs, 2, 6, MASK8_1);
    shiftLongs(tmp, 2, longs, 4, 5, MASK8_1);
    shiftLongs(tmp, 2, longs, 6, 4, MASK8_1);
    shiftLongs(tmp, 2, longs, 8, 3, MASK8_1);
    shiftLongs(tmp, 2, longs, 10, 2, MASK8_1);
    shiftLongs(tmp, 2, longs, 12, 1, MASK8_1);
    shiftLongs(tmp, 2, longs, 14, 0, MASK8_1);
  }

  private static void decode2(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 4);
    shiftLongs(tmp, 4, longs, 0, 6, MASK8_2);
    shiftLongs(tmp, 4, longs, 4, 4, MASK8_2);
    shiftLongs(tmp, 4, longs, 8, 2, MASK8_2);
    shiftLongs(tmp, 4, longs, 12, 0, MASK8_2);
  }

  private static void decode4(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 8);
    shiftLongs(tmp, 8, longs, 0, 4, MASK8_4);
    shiftLongs(tmp, 8, longs, 8, 0, MASK8_4);
  }

  private static void decode8(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 16);
  }

  private static void decode12(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 24);
    shiftLongs(tmp, 24, longs, 0, 4, MASK16_12);
    shiftLongs(tmp, 24, tmp, 0, 0, MASK16_4);
    for (int iter = 0, tmpIdx = 0, longsIdx = 24; iter < 8; ++iter, tmpIdx += 3, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 8;
      l0 |= tmp[tmpIdx + 1] << 4;
      l0 |= tmp[tmpIdx + 2] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode16(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 32);
  }

  private static void decode20(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 40);
    shiftLongs(tmp, 40, longs, 0, 12, MASK32_20);
    for (int iter = 0, tmpIdx = 0, longsIdx = 40; iter < 8; ++iter, tmpIdx += 5, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_12) << 8;
      l0 |= (tmp[tmpIdx + 1] >>> 4) & MASK32_8;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK32_4) << 16;
      l1 |= (tmp[tmpIdx + 2] & MASK32_12) << 4;
      l1 |= (tmp[tmpIdx + 3] >>> 8) & MASK32_4;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK32_8) << 12;
      l2 |= (tmp[tmpIdx + 4] & MASK32_12) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode24(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 48);
    shiftLongs(tmp, 48, longs, 0, 8, MASK32_24);
    shiftLongs(tmp, 48, tmp, 0, 0, MASK32_8);
    for (int iter = 0, tmpIdx = 0, longsIdx = 48; iter < 16; ++iter, tmpIdx += 3, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 16;
      l0 |= tmp[tmpIdx + 1] << 8;
      l0 |= tmp[tmpIdx + 2] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode28(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 56);
    shiftLongs(tmp, 56, longs, 0, 4, MASK32_28);
    shiftLongs(tmp, 56, tmp, 0, 0, MASK32_4);
    for (int iter = 0, tmpIdx = 0, longsIdx = 56; iter < 8; ++iter, tmpIdx += 7, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 24;
      l0 |= tmp[tmpIdx + 1] << 20;
      l0 |= tmp[tmpIdx + 2] << 16;
      l0 |= tmp[tmpIdx + 3] << 12;
      l0 |= tmp[tmpIdx + 4] << 8;
      l0 |= tmp[tmpIdx + 5] << 4;
      l0 |= tmp[tmpIdx + 6] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode32(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 64);
  }

  private static void decode40(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 80);
    shiftLongs(tmp, 80, longs, 0, 24, MASK64_40);
    for (int iter = 0, tmpIdx = 0, longsIdx = 80; iter < 16; ++iter, tmpIdx += 5, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_24) << 16;
      l0 |= (tmp[tmpIdx + 1] >>> 8) & MASK64_16;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_8) << 32;
      l1 |= (tmp[tmpIdx + 2] & MASK64_24) << 8;
      l1 |= (tmp[tmpIdx + 3] >>> 16) & MASK64_8;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK64_16) << 24;
      l2 |= (tmp[tmpIdx + 4] & MASK64_24) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode48(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 96);
    shiftLongs(tmp, 96, longs, 0, 16, MASK64_48);
    shiftLongs(tmp, 96, tmp, 0, 0, MASK64_16);
    for (int iter = 0, tmpIdx = 0, longsIdx = 96; iter < 32; ++iter, tmpIdx += 3, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 32;
      l0 |= tmp[tmpIdx + 1] << 16;
      l0 |= tmp[tmpIdx + 2] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode56(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 112);
    shiftLongs(tmp, 112, longs, 0, 8, MASK64_56);
    shiftLongs(tmp, 112, tmp, 0, 0, MASK64_8);
    for (int iter = 0, tmpIdx = 0, longsIdx = 112; iter < 16; ++iter, tmpIdx += 7, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 48;
      l0 |= tmp[tmpIdx + 1] << 40;
      l0 |= tmp[tmpIdx + 2] << 32;
      l0 |= tmp[tmpIdx + 3] << 24;
      l0 |= tmp[tmpIdx + 4] << 16;
      l0 |= tmp[tmpIdx + 5] << 8;
      l0 |= tmp[tmpIdx + 6] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode64(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 128);
  }

}
