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

  interface Decoder {
    void decode(DataInput in, long[] longs) throws IOException;
  }

  class Decoder1 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode1(in, tmp, longs);
      expand8(longs);
    }
  }

  class Decoder2 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode2(in, tmp, longs);
      expand8(longs);
    }
  }

  class Decoder4 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode4(in, tmp, longs);
      expand8(longs);
    }
  }

  static class Decoder8 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode8(in, longs);
      expand8(longs);
    }
  }

  class Decoder12 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode12(in, tmp, longs);
      expand16(longs);
    }
  }

  static class Decoder16 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode16(in, longs);
      expand16(longs);
    }
  }

  class Decoder20 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode20(in, tmp, longs);
      expand32(longs);
    }
  }

  class Decoder24 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode24(in, tmp, longs);
      expand32(longs);
    }
  }

  class Decoder28 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode28(in, tmp, longs);
      expand32(longs);
    }
  }

  static class Decoder32 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode32(in, longs);
      expand32(longs);
    }
  }

  class Decoder40 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode40(in, tmp, longs);
    }
  }

  class Decoder48 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode48(in, tmp, longs);
    }
  }

  class Decoder56 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode56(in, tmp, longs);
    }
  }

  static class Decoder64 implements Decoder {
    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode64(in, longs);
    }
  }

  Decoder decoder(int bitsPerValue) {
    switch (bitsPerValue) {
      case 1:
        return new Decoder1();
      case 2:
        return new Decoder2();
      case 4:
        return new Decoder4();
      case 8:
        return new Decoder8();
      case 12:
        return new Decoder12();
      case 16:
        return new Decoder16();
      case 20:
        return new Decoder20();
      case 24:
        return new Decoder24();
      case 28:
        return new Decoder28();
      case 32:
        return new Decoder32();
      case 40:
        return new Decoder40();
      case 48:
        return new Decoder48();
      case 56:
        return new Decoder56();
      case 64:
        return new Decoder64();
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

  private static void decode8(DataInput in, long[] longs) throws IOException {
    in.readLongs(longs, 0, 16);
  }

  private static void decode12(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 24);
    shiftLongs(tmp, 24, longs, 0, 4, MASK16_12);
    longs[24] = ((tmp[0] & MASK16_4) << 8) | ((tmp[1] & MASK16_4) << 4) | (tmp[2] & MASK16_4);
    longs[25] = ((tmp[3] & MASK16_4) << 8) | ((tmp[4] & MASK16_4) << 4) | (tmp[5] & MASK16_4);
    longs[26] = ((tmp[6] & MASK16_4) << 8) | ((tmp[7] & MASK16_4) << 4) | (tmp[8] & MASK16_4);
    longs[27] = ((tmp[9] & MASK16_4) << 8) | ((tmp[10] & MASK16_4) << 4) | (tmp[11] & MASK16_4);
    longs[28] = ((tmp[12] & MASK16_4) << 8) | ((tmp[13] & MASK16_4) << 4) | (tmp[14] & MASK16_4);
    longs[29] = ((tmp[15] & MASK16_4) << 8) | ((tmp[16] & MASK16_4) << 4) | (tmp[17] & MASK16_4);
    longs[30] = ((tmp[18] & MASK16_4) << 8) | ((tmp[19] & MASK16_4) << 4) | (tmp[20] & MASK16_4);
    longs[31] = ((tmp[21] & MASK16_4) << 8) | ((tmp[22] & MASK16_4) << 4) | (tmp[23] & MASK16_4);
  }

  private static void decode16(DataInput in, long[] longs) throws IOException {
    in.readLongs(longs, 0, 32);
  }

  private static void decode20(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 40);
    shiftLongs(tmp, 40, longs, 0, 12, MASK32_20);
    longs[40] = ((tmp[0] & MASK32_12) << 8) | ((tmp[1] >>> 4) & MASK32_8);
    longs[41] = ((tmp[1] & MASK32_4) << 16) | ((tmp[2] & MASK32_12) << 4) | ((tmp[3] >>> 8) & MASK32_4);
    longs[42] = ((tmp[3] & MASK32_8) << 12) | (tmp[4] & MASK32_12);
    longs[43] = ((tmp[5] & MASK32_12) << 8) | ((tmp[6] >>> 4) & MASK32_8);
    longs[44] = ((tmp[6] & MASK32_4) << 16) | ((tmp[7] & MASK32_12) << 4) | ((tmp[8] >>> 8) & MASK32_4);
    longs[45] = ((tmp[8] & MASK32_8) << 12) | (tmp[9] & MASK32_12);
    longs[46] = ((tmp[10] & MASK32_12) << 8) | ((tmp[11] >>> 4) & MASK32_8);
    longs[47] = ((tmp[11] & MASK32_4) << 16) | ((tmp[12] & MASK32_12) << 4) | ((tmp[13] >>> 8) & MASK32_4);
    longs[48] = ((tmp[13] & MASK32_8) << 12) | (tmp[14] & MASK32_12);
    longs[49] = ((tmp[15] & MASK32_12) << 8) | ((tmp[16] >>> 4) & MASK32_8);
    longs[50] = ((tmp[16] & MASK32_4) << 16) | ((tmp[17] & MASK32_12) << 4) | ((tmp[18] >>> 8) & MASK32_4);
    longs[51] = ((tmp[18] & MASK32_8) << 12) | (tmp[19] & MASK32_12);
    longs[52] = ((tmp[20] & MASK32_12) << 8) | ((tmp[21] >>> 4) & MASK32_8);
    longs[53] = ((tmp[21] & MASK32_4) << 16) | ((tmp[22] & MASK32_12) << 4) | ((tmp[23] >>> 8) & MASK32_4);
    longs[54] = ((tmp[23] & MASK32_8) << 12) | (tmp[24] & MASK32_12);
    longs[55] = ((tmp[25] & MASK32_12) << 8) | ((tmp[26] >>> 4) & MASK32_8);
    longs[56] = ((tmp[26] & MASK32_4) << 16) | ((tmp[27] & MASK32_12) << 4) | ((tmp[28] >>> 8) & MASK32_4);
    longs[57] = ((tmp[28] & MASK32_8) << 12) | (tmp[29] & MASK32_12);
    longs[58] = ((tmp[30] & MASK32_12) << 8) | ((tmp[31] >>> 4) & MASK32_8);
    longs[59] = ((tmp[31] & MASK32_4) << 16) | ((tmp[32] & MASK32_12) << 4) | ((tmp[33] >>> 8) & MASK32_4);
    longs[60] = ((tmp[33] & MASK32_8) << 12) | (tmp[34] & MASK32_12);
    longs[61] = ((tmp[35] & MASK32_12) << 8) | ((tmp[36] >>> 4) & MASK32_8);
    longs[62] = ((tmp[36] & MASK32_4) << 16) | ((tmp[37] & MASK32_12) << 4) | ((tmp[38] >>> 8) & MASK32_4);
    longs[63] = ((tmp[38] & MASK32_8) << 12) | (tmp[39] & MASK32_12);
  }

  private static void decode24(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 48);
    shiftLongs(tmp, 48, longs, 0, 8, MASK32_24);
    longs[48] = ((tmp[0] & MASK32_8) << 16) | ((tmp[1] & MASK32_8) << 8) | (tmp[2] & MASK32_8);
    longs[49] = ((tmp[3] & MASK32_8) << 16) | ((tmp[4] & MASK32_8) << 8) | (tmp[5] & MASK32_8);
    longs[50] = ((tmp[6] & MASK32_8) << 16) | ((tmp[7] & MASK32_8) << 8) | (tmp[8] & MASK32_8);
    longs[51] = ((tmp[9] & MASK32_8) << 16) | ((tmp[10] & MASK32_8) << 8) | (tmp[11] & MASK32_8);
    longs[52] = ((tmp[12] & MASK32_8) << 16) | ((tmp[13] & MASK32_8) << 8) | (tmp[14] & MASK32_8);
    longs[53] = ((tmp[15] & MASK32_8) << 16) | ((tmp[16] & MASK32_8) << 8) | (tmp[17] & MASK32_8);
    longs[54] = ((tmp[18] & MASK32_8) << 16) | ((tmp[19] & MASK32_8) << 8) | (tmp[20] & MASK32_8);
    longs[55] = ((tmp[21] & MASK32_8) << 16) | ((tmp[22] & MASK32_8) << 8) | (tmp[23] & MASK32_8);
    longs[56] = ((tmp[24] & MASK32_8) << 16) | ((tmp[25] & MASK32_8) << 8) | (tmp[26] & MASK32_8);
    longs[57] = ((tmp[27] & MASK32_8) << 16) | ((tmp[28] & MASK32_8) << 8) | (tmp[29] & MASK32_8);
    longs[58] = ((tmp[30] & MASK32_8) << 16) | ((tmp[31] & MASK32_8) << 8) | (tmp[32] & MASK32_8);
    longs[59] = ((tmp[33] & MASK32_8) << 16) | ((tmp[34] & MASK32_8) << 8) | (tmp[35] & MASK32_8);
    longs[60] = ((tmp[36] & MASK32_8) << 16) | ((tmp[37] & MASK32_8) << 8) | (tmp[38] & MASK32_8);
    longs[61] = ((tmp[39] & MASK32_8) << 16) | ((tmp[40] & MASK32_8) << 8) | (tmp[41] & MASK32_8);
    longs[62] = ((tmp[42] & MASK32_8) << 16) | ((tmp[43] & MASK32_8) << 8) | (tmp[44] & MASK32_8);
    longs[63] = ((tmp[45] & MASK32_8) << 16) | ((tmp[46] & MASK32_8) << 8) | (tmp[47] & MASK32_8);
  }

  private static void decode28(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 56);
    shiftLongs(tmp, 56, longs, 0, 4, MASK32_28);
    longs[56] = ((tmp[0] & MASK32_4) << 24) | ((tmp[1] & MASK32_4) << 20) | ((tmp[2] & MASK32_4) << 16) | ((tmp[3] & MASK32_4) << 12) | ((tmp[4] & MASK32_4) << 8) | ((tmp[5] & MASK32_4) << 4) | (tmp[6] & MASK32_4);
    longs[57] = ((tmp[7] & MASK32_4) << 24) | ((tmp[8] & MASK32_4) << 20) | ((tmp[9] & MASK32_4) << 16) | ((tmp[10] & MASK32_4) << 12) | ((tmp[11] & MASK32_4) << 8) | ((tmp[12] & MASK32_4) << 4) | (tmp[13] & MASK32_4);
    longs[58] = ((tmp[14] & MASK32_4) << 24) | ((tmp[15] & MASK32_4) << 20) | ((tmp[16] & MASK32_4) << 16) | ((tmp[17] & MASK32_4) << 12) | ((tmp[18] & MASK32_4) << 8) | ((tmp[19] & MASK32_4) << 4) | (tmp[20] & MASK32_4);
    longs[59] = ((tmp[21] & MASK32_4) << 24) | ((tmp[22] & MASK32_4) << 20) | ((tmp[23] & MASK32_4) << 16) | ((tmp[24] & MASK32_4) << 12) | ((tmp[25] & MASK32_4) << 8) | ((tmp[26] & MASK32_4) << 4) | (tmp[27] & MASK32_4);
    longs[60] = ((tmp[28] & MASK32_4) << 24) | ((tmp[29] & MASK32_4) << 20) | ((tmp[30] & MASK32_4) << 16) | ((tmp[31] & MASK32_4) << 12) | ((tmp[32] & MASK32_4) << 8) | ((tmp[33] & MASK32_4) << 4) | (tmp[34] & MASK32_4);
    longs[61] = ((tmp[35] & MASK32_4) << 24) | ((tmp[36] & MASK32_4) << 20) | ((tmp[37] & MASK32_4) << 16) | ((tmp[38] & MASK32_4) << 12) | ((tmp[39] & MASK32_4) << 8) | ((tmp[40] & MASK32_4) << 4) | (tmp[41] & MASK32_4);
    longs[62] = ((tmp[42] & MASK32_4) << 24) | ((tmp[43] & MASK32_4) << 20) | ((tmp[44] & MASK32_4) << 16) | ((tmp[45] & MASK32_4) << 12) | ((tmp[46] & MASK32_4) << 8) | ((tmp[47] & MASK32_4) << 4) | (tmp[48] & MASK32_4);
    longs[63] = ((tmp[49] & MASK32_4) << 24) | ((tmp[50] & MASK32_4) << 20) | ((tmp[51] & MASK32_4) << 16) | ((tmp[52] & MASK32_4) << 12) | ((tmp[53] & MASK32_4) << 8) | ((tmp[54] & MASK32_4) << 4) | (tmp[55] & MASK32_4);
  }

  private static void decode32(DataInput in, long[] longs) throws IOException {
    in.readLongs(longs, 0, 64);
  }

  private static void decode40(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 80);
    shiftLongs(tmp, 80, longs, 0, 24, MASK64_40);
    longs[80] = ((tmp[0] & MASK64_24) << 16) | ((tmp[1] >>> 8) & MASK64_16);
    longs[81] = ((tmp[1] & MASK64_8) << 32) | ((tmp[2] & MASK64_24) << 8) | ((tmp[3] >>> 16) & MASK64_8);
    longs[82] = ((tmp[3] & MASK64_16) << 24) | (tmp[4] & MASK64_24);
    longs[83] = ((tmp[5] & MASK64_24) << 16) | ((tmp[6] >>> 8) & MASK64_16);
    longs[84] = ((tmp[6] & MASK64_8) << 32) | ((tmp[7] & MASK64_24) << 8) | ((tmp[8] >>> 16) & MASK64_8);
    longs[85] = ((tmp[8] & MASK64_16) << 24) | (tmp[9] & MASK64_24);
    longs[86] = ((tmp[10] & MASK64_24) << 16) | ((tmp[11] >>> 8) & MASK64_16);
    longs[87] = ((tmp[11] & MASK64_8) << 32) | ((tmp[12] & MASK64_24) << 8) | ((tmp[13] >>> 16) & MASK64_8);
    longs[88] = ((tmp[13] & MASK64_16) << 24) | (tmp[14] & MASK64_24);
    longs[89] = ((tmp[15] & MASK64_24) << 16) | ((tmp[16] >>> 8) & MASK64_16);
    longs[90] = ((tmp[16] & MASK64_8) << 32) | ((tmp[17] & MASK64_24) << 8) | ((tmp[18] >>> 16) & MASK64_8);
    longs[91] = ((tmp[18] & MASK64_16) << 24) | (tmp[19] & MASK64_24);
    longs[92] = ((tmp[20] & MASK64_24) << 16) | ((tmp[21] >>> 8) & MASK64_16);
    longs[93] = ((tmp[21] & MASK64_8) << 32) | ((tmp[22] & MASK64_24) << 8) | ((tmp[23] >>> 16) & MASK64_8);
    longs[94] = ((tmp[23] & MASK64_16) << 24) | (tmp[24] & MASK64_24);
    longs[95] = ((tmp[25] & MASK64_24) << 16) | ((tmp[26] >>> 8) & MASK64_16);
    longs[96] = ((tmp[26] & MASK64_8) << 32) | ((tmp[27] & MASK64_24) << 8) | ((tmp[28] >>> 16) & MASK64_8);
    longs[97] = ((tmp[28] & MASK64_16) << 24) | (tmp[29] & MASK64_24);
    longs[98] = ((tmp[30] & MASK64_24) << 16) | ((tmp[31] >>> 8) & MASK64_16);
    longs[99] = ((tmp[31] & MASK64_8) << 32) | ((tmp[32] & MASK64_24) << 8) | ((tmp[33] >>> 16) & MASK64_8);
    longs[100] = ((tmp[33] & MASK64_16) << 24) | (tmp[34] & MASK64_24);
    longs[101] = ((tmp[35] & MASK64_24) << 16) | ((tmp[36] >>> 8) & MASK64_16);
    longs[102] = ((tmp[36] & MASK64_8) << 32) | ((tmp[37] & MASK64_24) << 8) | ((tmp[38] >>> 16) & MASK64_8);
    longs[103] = ((tmp[38] & MASK64_16) << 24) | (tmp[39] & MASK64_24);
    longs[104] = ((tmp[40] & MASK64_24) << 16) | ((tmp[41] >>> 8) & MASK64_16);
    longs[105] = ((tmp[41] & MASK64_8) << 32) | ((tmp[42] & MASK64_24) << 8) | ((tmp[43] >>> 16) & MASK64_8);
    longs[106] = ((tmp[43] & MASK64_16) << 24) | (tmp[44] & MASK64_24);
    longs[107] = ((tmp[45] & MASK64_24) << 16) | ((tmp[46] >>> 8) & MASK64_16);
    longs[108] = ((tmp[46] & MASK64_8) << 32) | ((tmp[47] & MASK64_24) << 8) | ((tmp[48] >>> 16) & MASK64_8);
    longs[109] = ((tmp[48] & MASK64_16) << 24) | (tmp[49] & MASK64_24);
    longs[110] = ((tmp[50] & MASK64_24) << 16) | ((tmp[51] >>> 8) & MASK64_16);
    longs[111] = ((tmp[51] & MASK64_8) << 32) | ((tmp[52] & MASK64_24) << 8) | ((tmp[53] >>> 16) & MASK64_8);
    longs[112] = ((tmp[53] & MASK64_16) << 24) | (tmp[54] & MASK64_24);
    longs[113] = ((tmp[55] & MASK64_24) << 16) | ((tmp[56] >>> 8) & MASK64_16);
    longs[114] = ((tmp[56] & MASK64_8) << 32) | ((tmp[57] & MASK64_24) << 8) | ((tmp[58] >>> 16) & MASK64_8);
    longs[115] = ((tmp[58] & MASK64_16) << 24) | (tmp[59] & MASK64_24);
    longs[116] = ((tmp[60] & MASK64_24) << 16) | ((tmp[61] >>> 8) & MASK64_16);
    longs[117] = ((tmp[61] & MASK64_8) << 32) | ((tmp[62] & MASK64_24) << 8) | ((tmp[63] >>> 16) & MASK64_8);
    longs[118] = ((tmp[63] & MASK64_16) << 24) | (tmp[64] & MASK64_24);
    longs[119] = ((tmp[65] & MASK64_24) << 16) | ((tmp[66] >>> 8) & MASK64_16);
    longs[120] = ((tmp[66] & MASK64_8) << 32) | ((tmp[67] & MASK64_24) << 8) | ((tmp[68] >>> 16) & MASK64_8);
    longs[121] = ((tmp[68] & MASK64_16) << 24) | (tmp[69] & MASK64_24);
    longs[122] = ((tmp[70] & MASK64_24) << 16) | ((tmp[71] >>> 8) & MASK64_16);
    longs[123] = ((tmp[71] & MASK64_8) << 32) | ((tmp[72] & MASK64_24) << 8) | ((tmp[73] >>> 16) & MASK64_8);
    longs[124] = ((tmp[73] & MASK64_16) << 24) | (tmp[74] & MASK64_24);
    longs[125] = ((tmp[75] & MASK64_24) << 16) | ((tmp[76] >>> 8) & MASK64_16);
    longs[126] = ((tmp[76] & MASK64_8) << 32) | ((tmp[77] & MASK64_24) << 8) | ((tmp[78] >>> 16) & MASK64_8);
    longs[127] = ((tmp[78] & MASK64_16) << 24) | (tmp[79] & MASK64_24);
  }

  private static void decode48(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 96);
    shiftLongs(tmp, 96, longs, 0, 16, MASK64_48);
    longs[96] = ((tmp[0] & MASK64_16) << 32) | ((tmp[1] & MASK64_16) << 16) | (tmp[2] & MASK64_16);
    longs[97] = ((tmp[3] & MASK64_16) << 32) | ((tmp[4] & MASK64_16) << 16) | (tmp[5] & MASK64_16);
    longs[98] = ((tmp[6] & MASK64_16) << 32) | ((tmp[7] & MASK64_16) << 16) | (tmp[8] & MASK64_16);
    longs[99] = ((tmp[9] & MASK64_16) << 32) | ((tmp[10] & MASK64_16) << 16) | (tmp[11] & MASK64_16);
    longs[100] = ((tmp[12] & MASK64_16) << 32) | ((tmp[13] & MASK64_16) << 16) | (tmp[14] & MASK64_16);
    longs[101] = ((tmp[15] & MASK64_16) << 32) | ((tmp[16] & MASK64_16) << 16) | (tmp[17] & MASK64_16);
    longs[102] = ((tmp[18] & MASK64_16) << 32) | ((tmp[19] & MASK64_16) << 16) | (tmp[20] & MASK64_16);
    longs[103] = ((tmp[21] & MASK64_16) << 32) | ((tmp[22] & MASK64_16) << 16) | (tmp[23] & MASK64_16);
    longs[104] = ((tmp[24] & MASK64_16) << 32) | ((tmp[25] & MASK64_16) << 16) | (tmp[26] & MASK64_16);
    longs[105] = ((tmp[27] & MASK64_16) << 32) | ((tmp[28] & MASK64_16) << 16) | (tmp[29] & MASK64_16);
    longs[106] = ((tmp[30] & MASK64_16) << 32) | ((tmp[31] & MASK64_16) << 16) | (tmp[32] & MASK64_16);
    longs[107] = ((tmp[33] & MASK64_16) << 32) | ((tmp[34] & MASK64_16) << 16) | (tmp[35] & MASK64_16);
    longs[108] = ((tmp[36] & MASK64_16) << 32) | ((tmp[37] & MASK64_16) << 16) | (tmp[38] & MASK64_16);
    longs[109] = ((tmp[39] & MASK64_16) << 32) | ((tmp[40] & MASK64_16) << 16) | (tmp[41] & MASK64_16);
    longs[110] = ((tmp[42] & MASK64_16) << 32) | ((tmp[43] & MASK64_16) << 16) | (tmp[44] & MASK64_16);
    longs[111] = ((tmp[45] & MASK64_16) << 32) | ((tmp[46] & MASK64_16) << 16) | (tmp[47] & MASK64_16);
    longs[112] = ((tmp[48] & MASK64_16) << 32) | ((tmp[49] & MASK64_16) << 16) | (tmp[50] & MASK64_16);
    longs[113] = ((tmp[51] & MASK64_16) << 32) | ((tmp[52] & MASK64_16) << 16) | (tmp[53] & MASK64_16);
    longs[114] = ((tmp[54] & MASK64_16) << 32) | ((tmp[55] & MASK64_16) << 16) | (tmp[56] & MASK64_16);
    longs[115] = ((tmp[57] & MASK64_16) << 32) | ((tmp[58] & MASK64_16) << 16) | (tmp[59] & MASK64_16);
    longs[116] = ((tmp[60] & MASK64_16) << 32) | ((tmp[61] & MASK64_16) << 16) | (tmp[62] & MASK64_16);
    longs[117] = ((tmp[63] & MASK64_16) << 32) | ((tmp[64] & MASK64_16) << 16) | (tmp[65] & MASK64_16);
    longs[118] = ((tmp[66] & MASK64_16) << 32) | ((tmp[67] & MASK64_16) << 16) | (tmp[68] & MASK64_16);
    longs[119] = ((tmp[69] & MASK64_16) << 32) | ((tmp[70] & MASK64_16) << 16) | (tmp[71] & MASK64_16);
    longs[120] = ((tmp[72] & MASK64_16) << 32) | ((tmp[73] & MASK64_16) << 16) | (tmp[74] & MASK64_16);
    longs[121] = ((tmp[75] & MASK64_16) << 32) | ((tmp[76] & MASK64_16) << 16) | (tmp[77] & MASK64_16);
    longs[122] = ((tmp[78] & MASK64_16) << 32) | ((tmp[79] & MASK64_16) << 16) | (tmp[80] & MASK64_16);
    longs[123] = ((tmp[81] & MASK64_16) << 32) | ((tmp[82] & MASK64_16) << 16) | (tmp[83] & MASK64_16);
    longs[124] = ((tmp[84] & MASK64_16) << 32) | ((tmp[85] & MASK64_16) << 16) | (tmp[86] & MASK64_16);
    longs[125] = ((tmp[87] & MASK64_16) << 32) | ((tmp[88] & MASK64_16) << 16) | (tmp[89] & MASK64_16);
    longs[126] = ((tmp[90] & MASK64_16) << 32) | ((tmp[91] & MASK64_16) << 16) | (tmp[92] & MASK64_16);
    longs[127] = ((tmp[93] & MASK64_16) << 32) | ((tmp[94] & MASK64_16) << 16) | (tmp[95] & MASK64_16);
  }

  private static void decode56(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 112);
    shiftLongs(tmp, 112, longs, 0, 8, MASK64_56);
    longs[112] = ((tmp[0] & MASK64_8) << 48) | ((tmp[1] & MASK64_8) << 40) | ((tmp[2] & MASK64_8) << 32) | ((tmp[3] & MASK64_8) << 24) | ((tmp[4] & MASK64_8) << 16) | ((tmp[5] & MASK64_8) << 8) | (tmp[6] & MASK64_8);
    longs[113] = ((tmp[7] & MASK64_8) << 48) | ((tmp[8] & MASK64_8) << 40) | ((tmp[9] & MASK64_8) << 32) | ((tmp[10] & MASK64_8) << 24) | ((tmp[11] & MASK64_8) << 16) | ((tmp[12] & MASK64_8) << 8) | (tmp[13] & MASK64_8);
    longs[114] = ((tmp[14] & MASK64_8) << 48) | ((tmp[15] & MASK64_8) << 40) | ((tmp[16] & MASK64_8) << 32) | ((tmp[17] & MASK64_8) << 24) | ((tmp[18] & MASK64_8) << 16) | ((tmp[19] & MASK64_8) << 8) | (tmp[20] & MASK64_8);
    longs[115] = ((tmp[21] & MASK64_8) << 48) | ((tmp[22] & MASK64_8) << 40) | ((tmp[23] & MASK64_8) << 32) | ((tmp[24] & MASK64_8) << 24) | ((tmp[25] & MASK64_8) << 16) | ((tmp[26] & MASK64_8) << 8) | (tmp[27] & MASK64_8);
    longs[116] = ((tmp[28] & MASK64_8) << 48) | ((tmp[29] & MASK64_8) << 40) | ((tmp[30] & MASK64_8) << 32) | ((tmp[31] & MASK64_8) << 24) | ((tmp[32] & MASK64_8) << 16) | ((tmp[33] & MASK64_8) << 8) | (tmp[34] & MASK64_8);
    longs[117] = ((tmp[35] & MASK64_8) << 48) | ((tmp[36] & MASK64_8) << 40) | ((tmp[37] & MASK64_8) << 32) | ((tmp[38] & MASK64_8) << 24) | ((tmp[39] & MASK64_8) << 16) | ((tmp[40] & MASK64_8) << 8) | (tmp[41] & MASK64_8);
    longs[118] = ((tmp[42] & MASK64_8) << 48) | ((tmp[43] & MASK64_8) << 40) | ((tmp[44] & MASK64_8) << 32) | ((tmp[45] & MASK64_8) << 24) | ((tmp[46] & MASK64_8) << 16) | ((tmp[47] & MASK64_8) << 8) | (tmp[48] & MASK64_8);
    longs[119] = ((tmp[49] & MASK64_8) << 48) | ((tmp[50] & MASK64_8) << 40) | ((tmp[51] & MASK64_8) << 32) | ((tmp[52] & MASK64_8) << 24) | ((tmp[53] & MASK64_8) << 16) | ((tmp[54] & MASK64_8) << 8) | (tmp[55] & MASK64_8);
    longs[120] = ((tmp[56] & MASK64_8) << 48) | ((tmp[57] & MASK64_8) << 40) | ((tmp[58] & MASK64_8) << 32) | ((tmp[59] & MASK64_8) << 24) | ((tmp[60] & MASK64_8) << 16) | ((tmp[61] & MASK64_8) << 8) | (tmp[62] & MASK64_8);
    longs[121] = ((tmp[63] & MASK64_8) << 48) | ((tmp[64] & MASK64_8) << 40) | ((tmp[65] & MASK64_8) << 32) | ((tmp[66] & MASK64_8) << 24) | ((tmp[67] & MASK64_8) << 16) | ((tmp[68] & MASK64_8) << 8) | (tmp[69] & MASK64_8);
    longs[122] = ((tmp[70] & MASK64_8) << 48) | ((tmp[71] & MASK64_8) << 40) | ((tmp[72] & MASK64_8) << 32) | ((tmp[73] & MASK64_8) << 24) | ((tmp[74] & MASK64_8) << 16) | ((tmp[75] & MASK64_8) << 8) | (tmp[76] & MASK64_8);
    longs[123] = ((tmp[77] & MASK64_8) << 48) | ((tmp[78] & MASK64_8) << 40) | ((tmp[79] & MASK64_8) << 32) | ((tmp[80] & MASK64_8) << 24) | ((tmp[81] & MASK64_8) << 16) | ((tmp[82] & MASK64_8) << 8) | (tmp[83] & MASK64_8);
    longs[124] = ((tmp[84] & MASK64_8) << 48) | ((tmp[85] & MASK64_8) << 40) | ((tmp[86] & MASK64_8) << 32) | ((tmp[87] & MASK64_8) << 24) | ((tmp[88] & MASK64_8) << 16) | ((tmp[89] & MASK64_8) << 8) | (tmp[90] & MASK64_8);
    longs[125] = ((tmp[91] & MASK64_8) << 48) | ((tmp[92] & MASK64_8) << 40) | ((tmp[93] & MASK64_8) << 32) | ((tmp[94] & MASK64_8) << 24) | ((tmp[95] & MASK64_8) << 16) | ((tmp[96] & MASK64_8) << 8) | (tmp[97] & MASK64_8);
    longs[126] = ((tmp[98] & MASK64_8) << 48) | ((tmp[99] & MASK64_8) << 40) | ((tmp[100] & MASK64_8) << 32) | ((tmp[101] & MASK64_8) << 24) | ((tmp[102] & MASK64_8) << 16) | ((tmp[103] & MASK64_8) << 8) | (tmp[104] & MASK64_8);
    longs[127] = ((tmp[105] & MASK64_8) << 48) | ((tmp[106] & MASK64_8) << 40) | ((tmp[107] & MASK64_8) << 32) | ((tmp[108] & MASK64_8) << 24) | ((tmp[109] & MASK64_8) << 16) | ((tmp[110] & MASK64_8) << 8) | (tmp[111] & MASK64_8);
  }

  private static void decode64(DataInput in, long[] longs) throws IOException {
    in.readLongs(longs, 0, 128);
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
  private static final long MASK16_4 = MASKS16[4];
  private static final long MASK16_12 = MASKS16[12];
  private static final long MASK32_4 = MASKS32[4];
  private static final long MASK32_8 = MASKS32[8];
  private static final long MASK32_12 = MASKS32[12];
  private static final long MASK32_20 = MASKS32[20];
  private static final long MASK32_24 = MASKS32[24];
  private static final long MASK32_28 = MASKS32[28];
  private static final long MASK64_8 = MASKS64[8];
  private static final long MASK64_16 = MASKS64[16];
  private static final long MASK64_24 = MASKS64[24];
  private static final long MASK64_40 = MASKS64[40];
  private static final long MASK64_48 = MASKS64[48];
  private static final long MASK64_56 = MASKS64[56];
}
