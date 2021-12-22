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

  static final int BLOCK_SIZE = 64;
  static final int BLOCK_SIZE_LOG2 = MathUtil.log(BLOCK_SIZE, 2);
  private static final int BLOCK_SIZE_DIV_2 = BLOCK_SIZE >> 1;
  private static final int BLOCK_SIZE_DIV_4 = BLOCK_SIZE >> 2;
  private static final int BLOCK_SIZE_DIV_8 = BLOCK_SIZE >> 3;
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

  private static void expand8To32(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_8; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 24) & 0x000000FF000000FFL;
      arr[BLOCK_SIZE_DIV_8_MUL_1 + i] = (l >>> 16) & 0x000000FF000000FFL;
      arr[BLOCK_SIZE_DIV_8_MUL_2 + i] = (l >>> 8) & 0x000000FF000000FFL;
      arr[BLOCK_SIZE_DIV_8_MUL_3 + i] = l & 0x000000FF000000FFL;
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

  private static void expand16To32(long[] arr) {
    for (int i = 0; i < BLOCK_SIZE_DIV_4; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 16) & 0x0000FFFF0000FFFFL;
      arr[BLOCK_SIZE_DIV_8_MUL_2 + i] = l & 0x0000FFFF0000FFFFL;
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

  private final long[] tmp = new long[BLOCK_SIZE];

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
    } else if (bitsPerValue <= 32){
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

  private static void decodeSlow(int bitsPerValue, DataInput in, long[] tmp, long[] longs)
      throws IOException {
    final int numLongs = bitsPerValue * BLOCK_SIZE_DIV_64;
    in.readLongs(tmp, 0, numLongs);
    final long mask = MASKS32[bitsPerValue];
    int longsIdx = 0;
    int shift = 32 - bitsPerValue;
    for (; shift >= 0; shift -= bitsPerValue) {
      shiftLongs(tmp, numLongs, longs, longsIdx, shift, mask);
      longsIdx += numLongs;
    }
    final int remainingBitsPerLong = shift + bitsPerValue;
    final long mask32RemainingBitsPerLong = MASKS32[remainingBitsPerLong];
    int tmpIdx = 0;
    int remainingBits = remainingBitsPerLong;
    for (; longsIdx < BLOCK_SIZE_DIV_2; ++longsIdx) {
      int b = bitsPerValue - remainingBits;
      long l = (tmp[tmpIdx++] & MASKS32[remainingBits]) << b;
      while (b >= remainingBitsPerLong) {
        b -= remainingBitsPerLong;
        l |= (tmp[tmpIdx++] & mask32RemainingBitsPerLong) << b;
      }
      if (b > 0) {
        l |= (tmp[tmpIdx] >>> (remainingBitsPerLong - b)) & MASKS32[b];
        remainingBits = remainingBitsPerLong - b;
      } else {
        remainingBits = remainingBitsPerLong;
      }
      longs[longsIdx] = l;
    }
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
  private static final long MASK8_3 = MASKS8[3];
  private static final long MASK8_4 = MASKS8[4];
  private static final long MASK8_5 = MASKS8[5];
  private static final long MASK8_6 = MASKS8[6];
  private static final long MASK8_7 = MASKS8[7];
  private static final long MASK16_1 = MASKS16[1];
  private static final long MASK16_2 = MASKS16[2];
  private static final long MASK16_3 = MASKS16[3];
  private static final long MASK16_4 = MASKS16[4];
  private static final long MASK16_5 = MASKS16[5];
  private static final long MASK16_6 = MASKS16[6];
  private static final long MASK16_7 = MASKS16[7];
  private static final long MASK16_9 = MASKS16[9];
  private static final long MASK16_10 = MASKS16[10];
  private static final long MASK16_11 = MASKS16[11];
  private static final long MASK16_12 = MASKS16[12];
  private static final long MASK16_13 = MASKS16[13];
  private static final long MASK16_14 = MASKS16[14];
  private static final long MASK16_15 = MASKS16[15];
  private static final long MASK32_1 = MASKS32[1];
  private static final long MASK32_2 = MASKS32[2];
  private static final long MASK32_3 = MASKS32[3];
  private static final long MASK32_4 = MASKS32[4];
  private static final long MASK32_5 = MASKS32[5];
  private static final long MASK32_6 = MASKS32[6];
  private static final long MASK32_7 = MASKS32[7];
  private static final long MASK32_8 = MASKS32[8];
  private static final long MASK32_9 = MASKS32[9];
  private static final long MASK32_10 = MASKS32[10];
  private static final long MASK32_11 = MASKS32[11];
  private static final long MASK32_12 = MASKS32[12];
  private static final long MASK32_13 = MASKS32[13];
  private static final long MASK32_14 = MASKS32[14];
  private static final long MASK32_15 = MASKS32[15];
  private static final long MASK32_17 = MASKS32[17];
  private static final long MASK32_18 = MASKS32[18];
  private static final long MASK32_19 = MASKS32[19];
  private static final long MASK32_20 = MASKS32[20];
  private static final long MASK32_21 = MASKS32[21];
  private static final long MASK32_22 = MASKS32[22];
  private static final long MASK32_23 = MASKS32[23];
  private static final long MASK32_24 = MASKS32[24];
  private static final long MASK32_25 = MASKS32[25];
  private static final long MASK32_26 = MASKS32[26];
  private static final long MASK32_27 = MASKS32[27];
  private static final long MASK32_28 = MASKS32[28];
  private static final long MASK32_29 = MASKS32[29];
  private static final long MASK32_30 = MASKS32[30];
  private static final long MASK32_31 = MASKS32[31];
  private static final long MASK64_1 = MASKS64[1];
  private static final long MASK64_2 = MASKS64[2];
  private static final long MASK64_3 = MASKS64[3];
  private static final long MASK64_4 = MASKS64[4];
  private static final long MASK64_5 = MASKS64[5];
  private static final long MASK64_6 = MASKS64[6];
  private static final long MASK64_7 = MASKS64[7];
  private static final long MASK64_8 = MASKS64[8];
  private static final long MASK64_9 = MASKS64[9];
  private static final long MASK64_10 = MASKS64[10];
  private static final long MASK64_11 = MASKS64[11];
  private static final long MASK64_12 = MASKS64[12];
  private static final long MASK64_13 = MASKS64[13];
  private static final long MASK64_14 = MASKS64[14];
  private static final long MASK64_15 = MASKS64[15];
  private static final long MASK64_16 = MASKS64[16];
  private static final long MASK64_17 = MASKS64[17];
  private static final long MASK64_18 = MASKS64[18];
  private static final long MASK64_19 = MASKS64[19];
  private static final long MASK64_20 = MASKS64[20];
  private static final long MASK64_21 = MASKS64[21];
  private static final long MASK64_22 = MASKS64[22];
  private static final long MASK64_23 = MASKS64[23];
  private static final long MASK64_24 = MASKS64[24];
  private static final long MASK64_25 = MASKS64[25];
  private static final long MASK64_26 = MASKS64[26];
  private static final long MASK64_27 = MASKS64[27];
  private static final long MASK64_28 = MASKS64[28];
  private static final long MASK64_29 = MASKS64[29];
  private static final long MASK64_30 = MASKS64[30];
  private static final long MASK64_31 = MASKS64[31];
  private static final long MASK64_33 = MASKS64[33];
  private static final long MASK64_34 = MASKS64[34];
  private static final long MASK64_35 = MASKS64[35];
  private static final long MASK64_36 = MASKS64[36];
  private static final long MASK64_37 = MASKS64[37];
  private static final long MASK64_38 = MASKS64[38];
  private static final long MASK64_39 = MASKS64[39];
  private static final long MASK64_40 = MASKS64[40];
  private static final long MASK64_41 = MASKS64[41];
  private static final long MASK64_42 = MASKS64[42];
  private static final long MASK64_43 = MASKS64[43];
  private static final long MASK64_44 = MASKS64[44];
  private static final long MASK64_45 = MASKS64[45];
  private static final long MASK64_46 = MASKS64[46];
  private static final long MASK64_47 = MASKS64[47];
  private static final long MASK64_48 = MASKS64[48];
  private static final long MASK64_49 = MASKS64[49];
  private static final long MASK64_50 = MASKS64[50];
  private static final long MASK64_51 = MASKS64[51];
  private static final long MASK64_52 = MASKS64[52];
  private static final long MASK64_53 = MASKS64[53];
  private static final long MASK64_54 = MASKS64[54];
  private static final long MASK64_55 = MASKS64[55];
  private static final long MASK64_56 = MASKS64[56];
  private static final long MASK64_57 = MASKS64[57];
  private static final long MASK64_58 = MASKS64[58];
  private static final long MASK64_59 = MASKS64[59];
  private static final long MASK64_60 = MASKS64[60];
  private static final long MASK64_61 = MASKS64[61];
  private static final long MASK64_62 = MASKS64[62];
  private static final long MASK64_63 = MASKS64[63];

  /** Decode 128 integers into {@code longs}. */
  void decode(int bitsPerValue, DataInput in, long[] longs) throws IOException {
    switch (bitsPerValue) {
      case 1:
        decode1(in, tmp, longs);
        expand8(longs);
        break;
      case 2:
        decode2(in, tmp, longs);
        expand8(longs);
        break;
      case 3:
        decode3(in, tmp, longs);
        expand8(longs);
        break;
      case 4:
        decode4(in, tmp, longs);
        expand8(longs);
        break;
      case 5:
        decode5(in, tmp, longs);
        expand8(longs);
        break;
      case 6:
        decode6(in, tmp, longs);
        expand8(longs);
        break;
      case 7:
        decode7(in, tmp, longs);
        expand8(longs);
        break;
      case 8:
        decode8(in, tmp, longs);
        expand8(longs);
        break;
      case 9:
        decode9(in, tmp, longs);
        expand16(longs);
        break;
      case 10:
        decode10(in, tmp, longs);
        expand16(longs);
        break;
      case 11:
        decode11(in, tmp, longs);
        expand16(longs);
        break;
      case 12:
        decode12(in, tmp, longs);
        expand16(longs);
        break;
      case 13:
        decode13(in, tmp, longs);
        expand16(longs);
        break;
      case 14:
        decode14(in, tmp, longs);
        expand16(longs);
        break;
      case 15:
        decode15(in, tmp, longs);
        expand16(longs);
        break;
      case 16:
        decode16(in, tmp, longs);
        expand16(longs);
        break;
      case 17:
        decode17(in, tmp, longs);
        expand32(longs);
        break;
      case 18:
        decode18(in, tmp, longs);
        expand32(longs);
        break;
      case 19:
        decode19(in, tmp, longs);
        expand32(longs);
        break;
      case 20:
        decode20(in, tmp, longs);
        expand32(longs);
        break;
      case 21:
        decode21(in, tmp, longs);
        expand32(longs);
        break;
      case 22:
        decode22(in, tmp, longs);
        expand32(longs);
        break;
      case 23:
        decode23(in, tmp, longs);
        expand32(longs);
        break;
      case 24:
        decode24(in, tmp, longs);
        expand32(longs);
        break;
      case 25:
        decode25(in, tmp, longs);
        expand32(longs);
        break;
      case 26:
        decode26(in, tmp, longs);
        expand32(longs);
        break;
      case 27:
        decode27(in, tmp, longs);
        expand32(longs);
        break;
      case 28:
        decode28(in, tmp, longs);
        expand32(longs);
        break;
      case 29:
        decode29(in, tmp, longs);
        expand32(longs);
        break;
      case 30:
        decode30(in, tmp, longs);
        expand32(longs);
        break;
      case 31:
        decode31(in, tmp, longs);
        expand32(longs);
        break;
      case 32:
        decode32(in, tmp, longs);
        expand32(longs);
        break;
      case 33:
        decode33(in, tmp, longs);
        break;
      case 34:
        decode34(in, tmp, longs);
        break;
      case 35:
        decode35(in, tmp, longs);
        break;
      case 36:
        decode36(in, tmp, longs);
        break;
      case 37:
        decode37(in, tmp, longs);
        break;
      case 38:
        decode38(in, tmp, longs);
        break;
      case 39:
        decode39(in, tmp, longs);
        break;
      case 40:
        decode40(in, tmp, longs);
        break;
      case 41:
        decode41(in, tmp, longs);
        break;
      case 42:
        decode42(in, tmp, longs);
        break;
      case 43:
        decode43(in, tmp, longs);
        break;
      case 44:
        decode44(in, tmp, longs);
        break;
      case 45:
        decode45(in, tmp, longs);
        break;
      case 46:
        decode46(in, tmp, longs);
        break;
      case 47:
        decode47(in, tmp, longs);
        break;
      case 48:
        decode48(in, tmp, longs);
        break;
      case 49:
        decode49(in, tmp, longs);
        break;
      case 50:
        decode50(in, tmp, longs);
        break;
      case 51:
        decode51(in, tmp, longs);
        break;
      case 52:
        decode52(in, tmp, longs);
        break;
      case 53:
        decode53(in, tmp, longs);
        break;
      case 54:
        decode54(in, tmp, longs);
        break;
      case 55:
        decode55(in, tmp, longs);
        break;
      case 56:
        decode56(in, tmp, longs);
        break;
      case 57:
        decode57(in, tmp, longs);
        break;
      case 58:
        decode58(in, tmp, longs);
        break;
      case 59:
        decode59(in, tmp, longs);
        break;
      case 60:
        decode60(in, tmp, longs);
        break;
      case 61:
        decode61(in, tmp, longs);
        break;
      case 62:
        decode62(in, tmp, longs);
        break;
      case 63:
        decode63(in, tmp, longs);
        break;
      case 64:
        decode64(in, tmp, longs);
        break;
      default:
        decodeSlow(bitsPerValue, in, tmp, longs);
        expand32(longs);
        break;
    }
  }

  private static void decode1(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 1);
    shiftLongs(tmp, 1, longs, 0, 7, MASK8_1);
    shiftLongs(tmp, 1, longs, 1, 6, MASK8_1);
    shiftLongs(tmp, 1, longs, 2, 5, MASK8_1);
    shiftLongs(tmp, 1, longs, 3, 4, MASK8_1);
    shiftLongs(tmp, 1, longs, 4, 3, MASK8_1);
    shiftLongs(tmp, 1, longs, 5, 2, MASK8_1);
    shiftLongs(tmp, 1, longs, 6, 1, MASK8_1);
    shiftLongs(tmp, 1, longs, 7, 0, MASK8_1);
  }

  private static void decode2(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 2);
    shiftLongs(tmp, 2, longs, 0, 6, MASK8_2);
    shiftLongs(tmp, 2, longs, 2, 4, MASK8_2);
    shiftLongs(tmp, 2, longs, 4, 2, MASK8_2);
    shiftLongs(tmp, 2, longs, 6, 0, MASK8_2);
  }

  private static void decode3(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 3);
    shiftLongs(tmp, 3, longs, 0, 5, MASK8_3);
    shiftLongs(tmp, 3, longs, 3, 2, MASK8_3);
    for (int iter = 0, tmpIdx = 0, longsIdx = 6; iter < 1; ++iter, tmpIdx += 3, longsIdx += 2) {
      long l0 = (tmp[tmpIdx + 0] & MASK8_2) << 1;
      l0 |= (tmp[tmpIdx + 1] >>> 1) & MASK8_1;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK8_1) << 2;
      l1 |= (tmp[tmpIdx + 2] & MASK8_2) << 0;
      longs[longsIdx + 1] = l1;
    }
  }

  private static void decode4(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 4);
    shiftLongs(tmp, 4, longs, 0, 4, MASK8_4);
    shiftLongs(tmp, 4, longs, 4, 0, MASK8_4);
  }

  private static void decode5(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 5);
    shiftLongs(tmp, 5, longs, 0, 3, MASK8_5);
    for (int iter = 0, tmpIdx = 0, longsIdx = 5; iter < 1; ++iter, tmpIdx += 5, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK8_3) << 2;
      l0 |= (tmp[tmpIdx + 1] >>> 1) & MASK8_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK8_1) << 4;
      l1 |= (tmp[tmpIdx + 2] & MASK8_3) << 1;
      l1 |= (tmp[tmpIdx + 3] >>> 2) & MASK8_1;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK8_2) << 3;
      l2 |= (tmp[tmpIdx + 4] & MASK8_3) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode6(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 6);
    shiftLongs(tmp, 6, longs, 0, 2, MASK8_6);
    shiftLongs(tmp, 6, tmp, 0, 0, MASK8_2);
    for (int iter = 0, tmpIdx = 0, longsIdx = 6; iter < 2; ++iter, tmpIdx += 3, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 4;
      l0 |= tmp[tmpIdx + 1] << 2;
      l0 |= tmp[tmpIdx + 2] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode7(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 7);
    shiftLongs(tmp, 7, longs, 0, 1, MASK8_7);
    shiftLongs(tmp, 7, tmp, 0, 0, MASK8_1);
    for (int iter = 0, tmpIdx = 0, longsIdx = 7; iter < 1; ++iter, tmpIdx += 7, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 6;
      l0 |= tmp[tmpIdx + 1] << 5;
      l0 |= tmp[tmpIdx + 2] << 4;
      l0 |= tmp[tmpIdx + 3] << 3;
      l0 |= tmp[tmpIdx + 4] << 2;
      l0 |= tmp[tmpIdx + 5] << 1;
      l0 |= tmp[tmpIdx + 6] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode8(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 8);
  }

  private static void decode9(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 9);
    shiftLongs(tmp, 9, longs, 0, 7, MASK16_9);
    for (int iter = 0, tmpIdx = 0, longsIdx = 9; iter < 1; ++iter, tmpIdx += 9, longsIdx += 7) {
      long l0 = (tmp[tmpIdx + 0] & MASK16_7) << 2;
      l0 |= (tmp[tmpIdx + 1] >>> 5) & MASK16_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK16_5) << 4;
      l1 |= (tmp[tmpIdx + 2] >>> 3) & MASK16_4;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK16_3) << 6;
      l2 |= (tmp[tmpIdx + 3] >>> 1) & MASK16_6;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 3] & MASK16_1) << 8;
      l3 |= (tmp[tmpIdx + 4] & MASK16_7) << 1;
      l3 |= (tmp[tmpIdx + 5] >>> 6) & MASK16_1;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 5] & MASK16_6) << 3;
      l4 |= (tmp[tmpIdx + 6] >>> 4) & MASK16_3;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 6] & MASK16_4) << 5;
      l5 |= (tmp[tmpIdx + 7] >>> 2) & MASK16_5;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 7] & MASK16_2) << 7;
      l6 |= (tmp[tmpIdx + 8] & MASK16_7) << 0;
      longs[longsIdx + 6] = l6;
    }
  }

  private static void decode10(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 10);
    shiftLongs(tmp, 10, longs, 0, 6, MASK16_10);
    for (int iter = 0, tmpIdx = 0, longsIdx = 10; iter < 2; ++iter, tmpIdx += 5, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK16_6) << 4;
      l0 |= (tmp[tmpIdx + 1] >>> 2) & MASK16_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK16_2) << 8;
      l1 |= (tmp[tmpIdx + 2] & MASK16_6) << 2;
      l1 |= (tmp[tmpIdx + 3] >>> 4) & MASK16_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK16_4) << 6;
      l2 |= (tmp[tmpIdx + 4] & MASK16_6) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode11(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 11);
    shiftLongs(tmp, 11, longs, 0, 5, MASK16_11);
    for (int iter = 0, tmpIdx = 0, longsIdx = 11; iter < 1; ++iter, tmpIdx += 11, longsIdx += 5) {
      long l0 = (tmp[tmpIdx + 0] & MASK16_5) << 6;
      l0 |= (tmp[tmpIdx + 1] & MASK16_5) << 1;
      l0 |= (tmp[tmpIdx + 2] >>> 4) & MASK16_1;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK16_4) << 7;
      l1 |= (tmp[tmpIdx + 3] & MASK16_5) << 2;
      l1 |= (tmp[tmpIdx + 4] >>> 3) & MASK16_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 4] & MASK16_3) << 8;
      l2 |= (tmp[tmpIdx + 5] & MASK16_5) << 3;
      l2 |= (tmp[tmpIdx + 6] >>> 2) & MASK16_3;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 6] & MASK16_2) << 9;
      l3 |= (tmp[tmpIdx + 7] & MASK16_5) << 4;
      l3 |= (tmp[tmpIdx + 8] >>> 1) & MASK16_4;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 8] & MASK16_1) << 10;
      l4 |= (tmp[tmpIdx + 9] & MASK16_5) << 5;
      l4 |= (tmp[tmpIdx + 10] & MASK16_5) << 0;
      longs[longsIdx + 4] = l4;
    }
  }

  private static void decode12(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 12);
    shiftLongs(tmp, 12, longs, 0, 4, MASK16_12);
    shiftLongs(tmp, 12, tmp, 0, 0, MASK16_4);
    for (int iter = 0, tmpIdx = 0, longsIdx = 12; iter < 4; ++iter, tmpIdx += 3, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 8;
      l0 |= tmp[tmpIdx + 1] << 4;
      l0 |= tmp[tmpIdx + 2] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode13(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 13);
    shiftLongs(tmp, 13, longs, 0, 3, MASK16_13);
    for (int iter = 0, tmpIdx = 0, longsIdx = 13; iter < 1; ++iter, tmpIdx += 13, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK16_3) << 10;
      l0 |= (tmp[tmpIdx + 1] & MASK16_3) << 7;
      l0 |= (tmp[tmpIdx + 2] & MASK16_3) << 4;
      l0 |= (tmp[tmpIdx + 3] & MASK16_3) << 1;
      l0 |= (tmp[tmpIdx + 4] >>> 2) & MASK16_1;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 4] & MASK16_2) << 11;
      l1 |= (tmp[tmpIdx + 5] & MASK16_3) << 8;
      l1 |= (tmp[tmpIdx + 6] & MASK16_3) << 5;
      l1 |= (tmp[tmpIdx + 7] & MASK16_3) << 2;
      l1 |= (tmp[tmpIdx + 8] >>> 1) & MASK16_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 8] & MASK16_1) << 12;
      l2 |= (tmp[tmpIdx + 9] & MASK16_3) << 9;
      l2 |= (tmp[tmpIdx + 10] & MASK16_3) << 6;
      l2 |= (tmp[tmpIdx + 11] & MASK16_3) << 3;
      l2 |= (tmp[tmpIdx + 12] & MASK16_3) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode14(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 14);
    shiftLongs(tmp, 14, longs, 0, 2, MASK16_14);
    shiftLongs(tmp, 14, tmp, 0, 0, MASK16_2);
    for (int iter = 0, tmpIdx = 0, longsIdx = 14; iter < 2; ++iter, tmpIdx += 7, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 12;
      l0 |= tmp[tmpIdx + 1] << 10;
      l0 |= tmp[tmpIdx + 2] << 8;
      l0 |= tmp[tmpIdx + 3] << 6;
      l0 |= tmp[tmpIdx + 4] << 4;
      l0 |= tmp[tmpIdx + 5] << 2;
      l0 |= tmp[tmpIdx + 6] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode15(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 15);
    shiftLongs(tmp, 15, longs, 0, 1, MASK16_15);
    shiftLongs(tmp, 15, tmp, 0, 0, MASK16_1);
    for (int iter = 0, tmpIdx = 0, longsIdx = 15; iter < 1; ++iter, tmpIdx += 15, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 14;
      l0 |= tmp[tmpIdx + 1] << 13;
      l0 |= tmp[tmpIdx + 2] << 12;
      l0 |= tmp[tmpIdx + 3] << 11;
      l0 |= tmp[tmpIdx + 4] << 10;
      l0 |= tmp[tmpIdx + 5] << 9;
      l0 |= tmp[tmpIdx + 6] << 8;
      l0 |= tmp[tmpIdx + 7] << 7;
      l0 |= tmp[tmpIdx + 8] << 6;
      l0 |= tmp[tmpIdx + 9] << 5;
      l0 |= tmp[tmpIdx + 10] << 4;
      l0 |= tmp[tmpIdx + 11] << 3;
      l0 |= tmp[tmpIdx + 12] << 2;
      l0 |= tmp[tmpIdx + 13] << 1;
      l0 |= tmp[tmpIdx + 14] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode16(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 16);
  }

  private static void decode17(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 17);
    shiftLongs(tmp, 17, longs, 0, 15, MASK32_17);
    for (int iter = 0, tmpIdx = 0, longsIdx = 17; iter < 1; ++iter, tmpIdx += 17, longsIdx += 15) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_15) << 2;
      l0 |= (tmp[tmpIdx + 1] >>> 13) & MASK32_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK32_13) << 4;
      l1 |= (tmp[tmpIdx + 2] >>> 11) & MASK32_4;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK32_11) << 6;
      l2 |= (tmp[tmpIdx + 3] >>> 9) & MASK32_6;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 3] & MASK32_9) << 8;
      l3 |= (tmp[tmpIdx + 4] >>> 7) & MASK32_8;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 4] & MASK32_7) << 10;
      l4 |= (tmp[tmpIdx + 5] >>> 5) & MASK32_10;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 5] & MASK32_5) << 12;
      l5 |= (tmp[tmpIdx + 6] >>> 3) & MASK32_12;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 6] & MASK32_3) << 14;
      l6 |= (tmp[tmpIdx + 7] >>> 1) & MASK32_14;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 7] & MASK32_1) << 16;
      l7 |= (tmp[tmpIdx + 8] & MASK32_15) << 1;
      l7 |= (tmp[tmpIdx + 9] >>> 14) & MASK32_1;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 9] & MASK32_14) << 3;
      l8 |= (tmp[tmpIdx + 10] >>> 12) & MASK32_3;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 10] & MASK32_12) << 5;
      l9 |= (tmp[tmpIdx + 11] >>> 10) & MASK32_5;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 11] & MASK32_10) << 7;
      l10 |= (tmp[tmpIdx + 12] >>> 8) & MASK32_7;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 12] & MASK32_8) << 9;
      l11 |= (tmp[tmpIdx + 13] >>> 6) & MASK32_9;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 13] & MASK32_6) << 11;
      l12 |= (tmp[tmpIdx + 14] >>> 4) & MASK32_11;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 14] & MASK32_4) << 13;
      l13 |= (tmp[tmpIdx + 15] >>> 2) & MASK32_13;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 15] & MASK32_2) << 15;
      l14 |= (tmp[tmpIdx + 16] & MASK32_15) << 0;
      longs[longsIdx + 14] = l14;
    }
  }

  private static void decode18(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 18);
    shiftLongs(tmp, 18, longs, 0, 14, MASK32_18);
    for (int iter = 0, tmpIdx = 0, longsIdx = 18; iter < 2; ++iter, tmpIdx += 9, longsIdx += 7) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_14) << 4;
      l0 |= (tmp[tmpIdx + 1] >>> 10) & MASK32_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK32_10) << 8;
      l1 |= (tmp[tmpIdx + 2] >>> 6) & MASK32_8;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK32_6) << 12;
      l2 |= (tmp[tmpIdx + 3] >>> 2) & MASK32_12;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 3] & MASK32_2) << 16;
      l3 |= (tmp[tmpIdx + 4] & MASK32_14) << 2;
      l3 |= (tmp[tmpIdx + 5] >>> 12) & MASK32_2;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 5] & MASK32_12) << 6;
      l4 |= (tmp[tmpIdx + 6] >>> 8) & MASK32_6;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 6] & MASK32_8) << 10;
      l5 |= (tmp[tmpIdx + 7] >>> 4) & MASK32_10;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 7] & MASK32_4) << 14;
      l6 |= (tmp[tmpIdx + 8] & MASK32_14) << 0;
      longs[longsIdx + 6] = l6;
    }
  }

  private static void decode19(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 19);
    shiftLongs(tmp, 19, longs, 0, 13, MASK32_19);
    for (int iter = 0, tmpIdx = 0, longsIdx = 19; iter < 1; ++iter, tmpIdx += 19, longsIdx += 13) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_13) << 6;
      l0 |= (tmp[tmpIdx + 1] >>> 7) & MASK32_6;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK32_7) << 12;
      l1 |= (tmp[tmpIdx + 2] >>> 1) & MASK32_12;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK32_1) << 18;
      l2 |= (tmp[tmpIdx + 3] & MASK32_13) << 5;
      l2 |= (tmp[tmpIdx + 4] >>> 8) & MASK32_5;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 4] & MASK32_8) << 11;
      l3 |= (tmp[tmpIdx + 5] >>> 2) & MASK32_11;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 5] & MASK32_2) << 17;
      l4 |= (tmp[tmpIdx + 6] & MASK32_13) << 4;
      l4 |= (tmp[tmpIdx + 7] >>> 9) & MASK32_4;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 7] & MASK32_9) << 10;
      l5 |= (tmp[tmpIdx + 8] >>> 3) & MASK32_10;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 8] & MASK32_3) << 16;
      l6 |= (tmp[tmpIdx + 9] & MASK32_13) << 3;
      l6 |= (tmp[tmpIdx + 10] >>> 10) & MASK32_3;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 10] & MASK32_10) << 9;
      l7 |= (tmp[tmpIdx + 11] >>> 4) & MASK32_9;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 11] & MASK32_4) << 15;
      l8 |= (tmp[tmpIdx + 12] & MASK32_13) << 2;
      l8 |= (tmp[tmpIdx + 13] >>> 11) & MASK32_2;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 13] & MASK32_11) << 8;
      l9 |= (tmp[tmpIdx + 14] >>> 5) & MASK32_8;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 14] & MASK32_5) << 14;
      l10 |= (tmp[tmpIdx + 15] & MASK32_13) << 1;
      l10 |= (tmp[tmpIdx + 16] >>> 12) & MASK32_1;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 16] & MASK32_12) << 7;
      l11 |= (tmp[tmpIdx + 17] >>> 6) & MASK32_7;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 17] & MASK32_6) << 13;
      l12 |= (tmp[tmpIdx + 18] & MASK32_13) << 0;
      longs[longsIdx + 12] = l12;
    }
  }

  private static void decode20(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 20);
    shiftLongs(tmp, 20, longs, 0, 12, MASK32_20);
    for (int iter = 0, tmpIdx = 0, longsIdx = 20; iter < 4; ++iter, tmpIdx += 5, longsIdx += 3) {
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

  private static void decode21(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 21);
    shiftLongs(tmp, 21, longs, 0, 11, MASK32_21);
    for (int iter = 0, tmpIdx = 0, longsIdx = 21; iter < 1; ++iter, tmpIdx += 21, longsIdx += 11) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_11) << 10;
      l0 |= (tmp[tmpIdx + 1] >>> 1) & MASK32_10;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK32_1) << 20;
      l1 |= (tmp[tmpIdx + 2] & MASK32_11) << 9;
      l1 |= (tmp[tmpIdx + 3] >>> 2) & MASK32_9;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK32_2) << 19;
      l2 |= (tmp[tmpIdx + 4] & MASK32_11) << 8;
      l2 |= (tmp[tmpIdx + 5] >>> 3) & MASK32_8;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 5] & MASK32_3) << 18;
      l3 |= (tmp[tmpIdx + 6] & MASK32_11) << 7;
      l3 |= (tmp[tmpIdx + 7] >>> 4) & MASK32_7;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 7] & MASK32_4) << 17;
      l4 |= (tmp[tmpIdx + 8] & MASK32_11) << 6;
      l4 |= (tmp[tmpIdx + 9] >>> 5) & MASK32_6;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 9] & MASK32_5) << 16;
      l5 |= (tmp[tmpIdx + 10] & MASK32_11) << 5;
      l5 |= (tmp[tmpIdx + 11] >>> 6) & MASK32_5;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 11] & MASK32_6) << 15;
      l6 |= (tmp[tmpIdx + 12] & MASK32_11) << 4;
      l6 |= (tmp[tmpIdx + 13] >>> 7) & MASK32_4;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 13] & MASK32_7) << 14;
      l7 |= (tmp[tmpIdx + 14] & MASK32_11) << 3;
      l7 |= (tmp[tmpIdx + 15] >>> 8) & MASK32_3;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 15] & MASK32_8) << 13;
      l8 |= (tmp[tmpIdx + 16] & MASK32_11) << 2;
      l8 |= (tmp[tmpIdx + 17] >>> 9) & MASK32_2;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 17] & MASK32_9) << 12;
      l9 |= (tmp[tmpIdx + 18] & MASK32_11) << 1;
      l9 |= (tmp[tmpIdx + 19] >>> 10) & MASK32_1;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 19] & MASK32_10) << 11;
      l10 |= (tmp[tmpIdx + 20] & MASK32_11) << 0;
      longs[longsIdx + 10] = l10;
    }
  }

  private static void decode22(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 22);
    shiftLongs(tmp, 22, longs, 0, 10, MASK32_22);
    for (int iter = 0, tmpIdx = 0, longsIdx = 22; iter < 2; ++iter, tmpIdx += 11, longsIdx += 5) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_10) << 12;
      l0 |= (tmp[tmpIdx + 1] & MASK32_10) << 2;
      l0 |= (tmp[tmpIdx + 2] >>> 8) & MASK32_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK32_8) << 14;
      l1 |= (tmp[tmpIdx + 3] & MASK32_10) << 4;
      l1 |= (tmp[tmpIdx + 4] >>> 6) & MASK32_4;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 4] & MASK32_6) << 16;
      l2 |= (tmp[tmpIdx + 5] & MASK32_10) << 6;
      l2 |= (tmp[tmpIdx + 6] >>> 4) & MASK32_6;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 6] & MASK32_4) << 18;
      l3 |= (tmp[tmpIdx + 7] & MASK32_10) << 8;
      l3 |= (tmp[tmpIdx + 8] >>> 2) & MASK32_8;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 8] & MASK32_2) << 20;
      l4 |= (tmp[tmpIdx + 9] & MASK32_10) << 10;
      l4 |= (tmp[tmpIdx + 10] & MASK32_10) << 0;
      longs[longsIdx + 4] = l4;
    }
  }

  private static void decode23(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 23);
    shiftLongs(tmp, 23, longs, 0, 9, MASK32_23);
    for (int iter = 0, tmpIdx = 0, longsIdx = 23; iter < 1; ++iter, tmpIdx += 23, longsIdx += 9) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_9) << 14;
      l0 |= (tmp[tmpIdx + 1] & MASK32_9) << 5;
      l0 |= (tmp[tmpIdx + 2] >>> 4) & MASK32_5;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK32_4) << 19;
      l1 |= (tmp[tmpIdx + 3] & MASK32_9) << 10;
      l1 |= (tmp[tmpIdx + 4] & MASK32_9) << 1;
      l1 |= (tmp[tmpIdx + 5] >>> 8) & MASK32_1;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 5] & MASK32_8) << 15;
      l2 |= (tmp[tmpIdx + 6] & MASK32_9) << 6;
      l2 |= (tmp[tmpIdx + 7] >>> 3) & MASK32_6;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 7] & MASK32_3) << 20;
      l3 |= (tmp[tmpIdx + 8] & MASK32_9) << 11;
      l3 |= (tmp[tmpIdx + 9] & MASK32_9) << 2;
      l3 |= (tmp[tmpIdx + 10] >>> 7) & MASK32_2;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 10] & MASK32_7) << 16;
      l4 |= (tmp[tmpIdx + 11] & MASK32_9) << 7;
      l4 |= (tmp[tmpIdx + 12] >>> 2) & MASK32_7;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 12] & MASK32_2) << 21;
      l5 |= (tmp[tmpIdx + 13] & MASK32_9) << 12;
      l5 |= (tmp[tmpIdx + 14] & MASK32_9) << 3;
      l5 |= (tmp[tmpIdx + 15] >>> 6) & MASK32_3;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 15] & MASK32_6) << 17;
      l6 |= (tmp[tmpIdx + 16] & MASK32_9) << 8;
      l6 |= (tmp[tmpIdx + 17] >>> 1) & MASK32_8;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 17] & MASK32_1) << 22;
      l7 |= (tmp[tmpIdx + 18] & MASK32_9) << 13;
      l7 |= (tmp[tmpIdx + 19] & MASK32_9) << 4;
      l7 |= (tmp[tmpIdx + 20] >>> 5) & MASK32_4;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 20] & MASK32_5) << 18;
      l8 |= (tmp[tmpIdx + 21] & MASK32_9) << 9;
      l8 |= (tmp[tmpIdx + 22] & MASK32_9) << 0;
      longs[longsIdx + 8] = l8;
    }
  }

  private static void decode24(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 24);
    shiftLongs(tmp, 24, longs, 0, 8, MASK32_24);
    shiftLongs(tmp, 24, tmp, 0, 0, MASK32_8);
    for (int iter = 0, tmpIdx = 0, longsIdx = 24; iter < 8; ++iter, tmpIdx += 3, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 16;
      l0 |= tmp[tmpIdx + 1] << 8;
      l0 |= tmp[tmpIdx + 2] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode25(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 25);
    shiftLongs(tmp, 25, longs, 0, 7, MASK32_25);
    for (int iter = 0, tmpIdx = 0, longsIdx = 25; iter < 1; ++iter, tmpIdx += 25, longsIdx += 7) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_7) << 18;
      l0 |= (tmp[tmpIdx + 1] & MASK32_7) << 11;
      l0 |= (tmp[tmpIdx + 2] & MASK32_7) << 4;
      l0 |= (tmp[tmpIdx + 3] >>> 3) & MASK32_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 3] & MASK32_3) << 22;
      l1 |= (tmp[tmpIdx + 4] & MASK32_7) << 15;
      l1 |= (tmp[tmpIdx + 5] & MASK32_7) << 8;
      l1 |= (tmp[tmpIdx + 6] & MASK32_7) << 1;
      l1 |= (tmp[tmpIdx + 7] >>> 6) & MASK32_1;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 7] & MASK32_6) << 19;
      l2 |= (tmp[tmpIdx + 8] & MASK32_7) << 12;
      l2 |= (tmp[tmpIdx + 9] & MASK32_7) << 5;
      l2 |= (tmp[tmpIdx + 10] >>> 2) & MASK32_5;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 10] & MASK32_2) << 23;
      l3 |= (tmp[tmpIdx + 11] & MASK32_7) << 16;
      l3 |= (tmp[tmpIdx + 12] & MASK32_7) << 9;
      l3 |= (tmp[tmpIdx + 13] & MASK32_7) << 2;
      l3 |= (tmp[tmpIdx + 14] >>> 5) & MASK32_2;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 14] & MASK32_5) << 20;
      l4 |= (tmp[tmpIdx + 15] & MASK32_7) << 13;
      l4 |= (tmp[tmpIdx + 16] & MASK32_7) << 6;
      l4 |= (tmp[tmpIdx + 17] >>> 1) & MASK32_6;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 17] & MASK32_1) << 24;
      l5 |= (tmp[tmpIdx + 18] & MASK32_7) << 17;
      l5 |= (tmp[tmpIdx + 19] & MASK32_7) << 10;
      l5 |= (tmp[tmpIdx + 20] & MASK32_7) << 3;
      l5 |= (tmp[tmpIdx + 21] >>> 4) & MASK32_3;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 21] & MASK32_4) << 21;
      l6 |= (tmp[tmpIdx + 22] & MASK32_7) << 14;
      l6 |= (tmp[tmpIdx + 23] & MASK32_7) << 7;
      l6 |= (tmp[tmpIdx + 24] & MASK32_7) << 0;
      longs[longsIdx + 6] = l6;
    }
  }

  private static void decode26(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 26);
    shiftLongs(tmp, 26, longs, 0, 6, MASK32_26);
    for (int iter = 0, tmpIdx = 0, longsIdx = 26; iter < 2; ++iter, tmpIdx += 13, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_6) << 20;
      l0 |= (tmp[tmpIdx + 1] & MASK32_6) << 14;
      l0 |= (tmp[tmpIdx + 2] & MASK32_6) << 8;
      l0 |= (tmp[tmpIdx + 3] & MASK32_6) << 2;
      l0 |= (tmp[tmpIdx + 4] >>> 4) & MASK32_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 4] & MASK32_4) << 22;
      l1 |= (tmp[tmpIdx + 5] & MASK32_6) << 16;
      l1 |= (tmp[tmpIdx + 6] & MASK32_6) << 10;
      l1 |= (tmp[tmpIdx + 7] & MASK32_6) << 4;
      l1 |= (tmp[tmpIdx + 8] >>> 2) & MASK32_4;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 8] & MASK32_2) << 24;
      l2 |= (tmp[tmpIdx + 9] & MASK32_6) << 18;
      l2 |= (tmp[tmpIdx + 10] & MASK32_6) << 12;
      l2 |= (tmp[tmpIdx + 11] & MASK32_6) << 6;
      l2 |= (tmp[tmpIdx + 12] & MASK32_6) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode27(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 27);
    shiftLongs(tmp, 27, longs, 0, 5, MASK32_27);
    for (int iter = 0, tmpIdx = 0, longsIdx = 27; iter < 1; ++iter, tmpIdx += 27, longsIdx += 5) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_5) << 22;
      l0 |= (tmp[tmpIdx + 1] & MASK32_5) << 17;
      l0 |= (tmp[tmpIdx + 2] & MASK32_5) << 12;
      l0 |= (tmp[tmpIdx + 3] & MASK32_5) << 7;
      l0 |= (tmp[tmpIdx + 4] & MASK32_5) << 2;
      l0 |= (tmp[tmpIdx + 5] >>> 3) & MASK32_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 5] & MASK32_3) << 24;
      l1 |= (tmp[tmpIdx + 6] & MASK32_5) << 19;
      l1 |= (tmp[tmpIdx + 7] & MASK32_5) << 14;
      l1 |= (tmp[tmpIdx + 8] & MASK32_5) << 9;
      l1 |= (tmp[tmpIdx + 9] & MASK32_5) << 4;
      l1 |= (tmp[tmpIdx + 10] >>> 1) & MASK32_4;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 10] & MASK32_1) << 26;
      l2 |= (tmp[tmpIdx + 11] & MASK32_5) << 21;
      l2 |= (tmp[tmpIdx + 12] & MASK32_5) << 16;
      l2 |= (tmp[tmpIdx + 13] & MASK32_5) << 11;
      l2 |= (tmp[tmpIdx + 14] & MASK32_5) << 6;
      l2 |= (tmp[tmpIdx + 15] & MASK32_5) << 1;
      l2 |= (tmp[tmpIdx + 16] >>> 4) & MASK32_1;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 16] & MASK32_4) << 23;
      l3 |= (tmp[tmpIdx + 17] & MASK32_5) << 18;
      l3 |= (tmp[tmpIdx + 18] & MASK32_5) << 13;
      l3 |= (tmp[tmpIdx + 19] & MASK32_5) << 8;
      l3 |= (tmp[tmpIdx + 20] & MASK32_5) << 3;
      l3 |= (tmp[tmpIdx + 21] >>> 2) & MASK32_3;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 21] & MASK32_2) << 25;
      l4 |= (tmp[tmpIdx + 22] & MASK32_5) << 20;
      l4 |= (tmp[tmpIdx + 23] & MASK32_5) << 15;
      l4 |= (tmp[tmpIdx + 24] & MASK32_5) << 10;
      l4 |= (tmp[tmpIdx + 25] & MASK32_5) << 5;
      l4 |= (tmp[tmpIdx + 26] & MASK32_5) << 0;
      longs[longsIdx + 4] = l4;
    }
  }

  private static void decode28(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 28);
    shiftLongs(tmp, 28, longs, 0, 4, MASK32_28);
    shiftLongs(tmp, 28, tmp, 0, 0, MASK32_4);
    for (int iter = 0, tmpIdx = 0, longsIdx = 28; iter < 4; ++iter, tmpIdx += 7, longsIdx += 1) {
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

  private static void decode29(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 29);
    shiftLongs(tmp, 29, longs, 0, 3, MASK32_29);
    for (int iter = 0, tmpIdx = 0, longsIdx = 29; iter < 1; ++iter, tmpIdx += 29, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK32_3) << 26;
      l0 |= (tmp[tmpIdx + 1] & MASK32_3) << 23;
      l0 |= (tmp[tmpIdx + 2] & MASK32_3) << 20;
      l0 |= (tmp[tmpIdx + 3] & MASK32_3) << 17;
      l0 |= (tmp[tmpIdx + 4] & MASK32_3) << 14;
      l0 |= (tmp[tmpIdx + 5] & MASK32_3) << 11;
      l0 |= (tmp[tmpIdx + 6] & MASK32_3) << 8;
      l0 |= (tmp[tmpIdx + 7] & MASK32_3) << 5;
      l0 |= (tmp[tmpIdx + 8] & MASK32_3) << 2;
      l0 |= (tmp[tmpIdx + 9] >>> 1) & MASK32_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 9] & MASK32_1) << 28;
      l1 |= (tmp[tmpIdx + 10] & MASK32_3) << 25;
      l1 |= (tmp[tmpIdx + 11] & MASK32_3) << 22;
      l1 |= (tmp[tmpIdx + 12] & MASK32_3) << 19;
      l1 |= (tmp[tmpIdx + 13] & MASK32_3) << 16;
      l1 |= (tmp[tmpIdx + 14] & MASK32_3) << 13;
      l1 |= (tmp[tmpIdx + 15] & MASK32_3) << 10;
      l1 |= (tmp[tmpIdx + 16] & MASK32_3) << 7;
      l1 |= (tmp[tmpIdx + 17] & MASK32_3) << 4;
      l1 |= (tmp[tmpIdx + 18] & MASK32_3) << 1;
      l1 |= (tmp[tmpIdx + 19] >>> 2) & MASK32_1;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 19] & MASK32_2) << 27;
      l2 |= (tmp[tmpIdx + 20] & MASK32_3) << 24;
      l2 |= (tmp[tmpIdx + 21] & MASK32_3) << 21;
      l2 |= (tmp[tmpIdx + 22] & MASK32_3) << 18;
      l2 |= (tmp[tmpIdx + 23] & MASK32_3) << 15;
      l2 |= (tmp[tmpIdx + 24] & MASK32_3) << 12;
      l2 |= (tmp[tmpIdx + 25] & MASK32_3) << 9;
      l2 |= (tmp[tmpIdx + 26] & MASK32_3) << 6;
      l2 |= (tmp[tmpIdx + 27] & MASK32_3) << 3;
      l2 |= (tmp[tmpIdx + 28] & MASK32_3) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode30(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 30);
    shiftLongs(tmp, 30, longs, 0, 2, MASK32_30);
    shiftLongs(tmp, 30, tmp, 0, 0, MASK32_2);
    for (int iter = 0, tmpIdx = 0, longsIdx = 30; iter < 2; ++iter, tmpIdx += 15, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 28;
      l0 |= tmp[tmpIdx + 1] << 26;
      l0 |= tmp[tmpIdx + 2] << 24;
      l0 |= tmp[tmpIdx + 3] << 22;
      l0 |= tmp[tmpIdx + 4] << 20;
      l0 |= tmp[tmpIdx + 5] << 18;
      l0 |= tmp[tmpIdx + 6] << 16;
      l0 |= tmp[tmpIdx + 7] << 14;
      l0 |= tmp[tmpIdx + 8] << 12;
      l0 |= tmp[tmpIdx + 9] << 10;
      l0 |= tmp[tmpIdx + 10] << 8;
      l0 |= tmp[tmpIdx + 11] << 6;
      l0 |= tmp[tmpIdx + 12] << 4;
      l0 |= tmp[tmpIdx + 13] << 2;
      l0 |= tmp[tmpIdx + 14] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode31(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 31);
    shiftLongs(tmp, 31, longs, 0, 1, MASK32_31);
    shiftLongs(tmp, 31, tmp, 0, 0, MASK32_1);
    for (int iter = 0, tmpIdx = 0, longsIdx = 31; iter < 1; ++iter, tmpIdx += 31, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 30;
      l0 |= tmp[tmpIdx + 1] << 29;
      l0 |= tmp[tmpIdx + 2] << 28;
      l0 |= tmp[tmpIdx + 3] << 27;
      l0 |= tmp[tmpIdx + 4] << 26;
      l0 |= tmp[tmpIdx + 5] << 25;
      l0 |= tmp[tmpIdx + 6] << 24;
      l0 |= tmp[tmpIdx + 7] << 23;
      l0 |= tmp[tmpIdx + 8] << 22;
      l0 |= tmp[tmpIdx + 9] << 21;
      l0 |= tmp[tmpIdx + 10] << 20;
      l0 |= tmp[tmpIdx + 11] << 19;
      l0 |= tmp[tmpIdx + 12] << 18;
      l0 |= tmp[tmpIdx + 13] << 17;
      l0 |= tmp[tmpIdx + 14] << 16;
      l0 |= tmp[tmpIdx + 15] << 15;
      l0 |= tmp[tmpIdx + 16] << 14;
      l0 |= tmp[tmpIdx + 17] << 13;
      l0 |= tmp[tmpIdx + 18] << 12;
      l0 |= tmp[tmpIdx + 19] << 11;
      l0 |= tmp[tmpIdx + 20] << 10;
      l0 |= tmp[tmpIdx + 21] << 9;
      l0 |= tmp[tmpIdx + 22] << 8;
      l0 |= tmp[tmpIdx + 23] << 7;
      l0 |= tmp[tmpIdx + 24] << 6;
      l0 |= tmp[tmpIdx + 25] << 5;
      l0 |= tmp[tmpIdx + 26] << 4;
      l0 |= tmp[tmpIdx + 27] << 3;
      l0 |= tmp[tmpIdx + 28] << 2;
      l0 |= tmp[tmpIdx + 29] << 1;
      l0 |= tmp[tmpIdx + 30] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode32(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 32);
  }

  private static void decode33(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 33);
    shiftLongs(tmp, 33, longs, 0, 31, MASK64_33);
    for (int iter = 0, tmpIdx = 0, longsIdx = 33; iter < 1; ++iter, tmpIdx += 33, longsIdx += 31) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_31) << 2;
      l0 |= (tmp[tmpIdx + 1] >>> 29) & MASK64_2;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_29) << 4;
      l1 |= (tmp[tmpIdx + 2] >>> 27) & MASK64_4;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK64_27) << 6;
      l2 |= (tmp[tmpIdx + 3] >>> 25) & MASK64_6;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 3] & MASK64_25) << 8;
      l3 |= (tmp[tmpIdx + 4] >>> 23) & MASK64_8;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 4] & MASK64_23) << 10;
      l4 |= (tmp[tmpIdx + 5] >>> 21) & MASK64_10;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 5] & MASK64_21) << 12;
      l5 |= (tmp[tmpIdx + 6] >>> 19) & MASK64_12;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 6] & MASK64_19) << 14;
      l6 |= (tmp[tmpIdx + 7] >>> 17) & MASK64_14;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 7] & MASK64_17) << 16;
      l7 |= (tmp[tmpIdx + 8] >>> 15) & MASK64_16;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 8] & MASK64_15) << 18;
      l8 |= (tmp[tmpIdx + 9] >>> 13) & MASK64_18;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 9] & MASK64_13) << 20;
      l9 |= (tmp[tmpIdx + 10] >>> 11) & MASK64_20;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 10] & MASK64_11) << 22;
      l10 |= (tmp[tmpIdx + 11] >>> 9) & MASK64_22;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 11] & MASK64_9) << 24;
      l11 |= (tmp[tmpIdx + 12] >>> 7) & MASK64_24;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 12] & MASK64_7) << 26;
      l12 |= (tmp[tmpIdx + 13] >>> 5) & MASK64_26;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 13] & MASK64_5) << 28;
      l13 |= (tmp[tmpIdx + 14] >>> 3) & MASK64_28;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 14] & MASK64_3) << 30;
      l14 |= (tmp[tmpIdx + 15] >>> 1) & MASK64_30;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 15] & MASK64_1) << 32;
      l15 |= (tmp[tmpIdx + 16] & MASK64_31) << 1;
      l15 |= (tmp[tmpIdx + 17] >>> 30) & MASK64_1;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 17] & MASK64_30) << 3;
      l16 |= (tmp[tmpIdx + 18] >>> 28) & MASK64_3;
      longs[longsIdx + 16] = l16;
      long l17 = (tmp[tmpIdx + 18] & MASK64_28) << 5;
      l17 |= (tmp[tmpIdx + 19] >>> 26) & MASK64_5;
      longs[longsIdx + 17] = l17;
      long l18 = (tmp[tmpIdx + 19] & MASK64_26) << 7;
      l18 |= (tmp[tmpIdx + 20] >>> 24) & MASK64_7;
      longs[longsIdx + 18] = l18;
      long l19 = (tmp[tmpIdx + 20] & MASK64_24) << 9;
      l19 |= (tmp[tmpIdx + 21] >>> 22) & MASK64_9;
      longs[longsIdx + 19] = l19;
      long l20 = (tmp[tmpIdx + 21] & MASK64_22) << 11;
      l20 |= (tmp[tmpIdx + 22] >>> 20) & MASK64_11;
      longs[longsIdx + 20] = l20;
      long l21 = (tmp[tmpIdx + 22] & MASK64_20) << 13;
      l21 |= (tmp[tmpIdx + 23] >>> 18) & MASK64_13;
      longs[longsIdx + 21] = l21;
      long l22 = (tmp[tmpIdx + 23] & MASK64_18) << 15;
      l22 |= (tmp[tmpIdx + 24] >>> 16) & MASK64_15;
      longs[longsIdx + 22] = l22;
      long l23 = (tmp[tmpIdx + 24] & MASK64_16) << 17;
      l23 |= (tmp[tmpIdx + 25] >>> 14) & MASK64_17;
      longs[longsIdx + 23] = l23;
      long l24 = (tmp[tmpIdx + 25] & MASK64_14) << 19;
      l24 |= (tmp[tmpIdx + 26] >>> 12) & MASK64_19;
      longs[longsIdx + 24] = l24;
      long l25 = (tmp[tmpIdx + 26] & MASK64_12) << 21;
      l25 |= (tmp[tmpIdx + 27] >>> 10) & MASK64_21;
      longs[longsIdx + 25] = l25;
      long l26 = (tmp[tmpIdx + 27] & MASK64_10) << 23;
      l26 |= (tmp[tmpIdx + 28] >>> 8) & MASK64_23;
      longs[longsIdx + 26] = l26;
      long l27 = (tmp[tmpIdx + 28] & MASK64_8) << 25;
      l27 |= (tmp[tmpIdx + 29] >>> 6) & MASK64_25;
      longs[longsIdx + 27] = l27;
      long l28 = (tmp[tmpIdx + 29] & MASK64_6) << 27;
      l28 |= (tmp[tmpIdx + 30] >>> 4) & MASK64_27;
      longs[longsIdx + 28] = l28;
      long l29 = (tmp[tmpIdx + 30] & MASK64_4) << 29;
      l29 |= (tmp[tmpIdx + 31] >>> 2) & MASK64_29;
      longs[longsIdx + 29] = l29;
      long l30 = (tmp[tmpIdx + 31] & MASK64_2) << 31;
      l30 |= (tmp[tmpIdx + 32] & MASK64_31) << 0;
      longs[longsIdx + 30] = l30;
    }
  }

  private static void decode34(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 34);
    shiftLongs(tmp, 34, longs, 0, 30, MASK64_34);
    for (int iter = 0, tmpIdx = 0, longsIdx = 34; iter < 2; ++iter, tmpIdx += 17, longsIdx += 15) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_30) << 4;
      l0 |= (tmp[tmpIdx + 1] >>> 26) & MASK64_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_26) << 8;
      l1 |= (tmp[tmpIdx + 2] >>> 22) & MASK64_8;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK64_22) << 12;
      l2 |= (tmp[tmpIdx + 3] >>> 18) & MASK64_12;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 3] & MASK64_18) << 16;
      l3 |= (tmp[tmpIdx + 4] >>> 14) & MASK64_16;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 4] & MASK64_14) << 20;
      l4 |= (tmp[tmpIdx + 5] >>> 10) & MASK64_20;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 5] & MASK64_10) << 24;
      l5 |= (tmp[tmpIdx + 6] >>> 6) & MASK64_24;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 6] & MASK64_6) << 28;
      l6 |= (tmp[tmpIdx + 7] >>> 2) & MASK64_28;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 7] & MASK64_2) << 32;
      l7 |= (tmp[tmpIdx + 8] & MASK64_30) << 2;
      l7 |= (tmp[tmpIdx + 9] >>> 28) & MASK64_2;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 9] & MASK64_28) << 6;
      l8 |= (tmp[tmpIdx + 10] >>> 24) & MASK64_6;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 10] & MASK64_24) << 10;
      l9 |= (tmp[tmpIdx + 11] >>> 20) & MASK64_10;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 11] & MASK64_20) << 14;
      l10 |= (tmp[tmpIdx + 12] >>> 16) & MASK64_14;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 12] & MASK64_16) << 18;
      l11 |= (tmp[tmpIdx + 13] >>> 12) & MASK64_18;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 13] & MASK64_12) << 22;
      l12 |= (tmp[tmpIdx + 14] >>> 8) & MASK64_22;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 14] & MASK64_8) << 26;
      l13 |= (tmp[tmpIdx + 15] >>> 4) & MASK64_26;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 15] & MASK64_4) << 30;
      l14 |= (tmp[tmpIdx + 16] & MASK64_30) << 0;
      longs[longsIdx + 14] = l14;
    }
  }

  private static void decode35(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 35);
    shiftLongs(tmp, 35, longs, 0, 29, MASK64_35);
    for (int iter = 0, tmpIdx = 0, longsIdx = 35; iter < 1; ++iter, tmpIdx += 35, longsIdx += 29) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_29) << 6;
      l0 |= (tmp[tmpIdx + 1] >>> 23) & MASK64_6;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_23) << 12;
      l1 |= (tmp[tmpIdx + 2] >>> 17) & MASK64_12;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK64_17) << 18;
      l2 |= (tmp[tmpIdx + 3] >>> 11) & MASK64_18;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 3] & MASK64_11) << 24;
      l3 |= (tmp[tmpIdx + 4] >>> 5) & MASK64_24;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 4] & MASK64_5) << 30;
      l4 |= (tmp[tmpIdx + 5] & MASK64_29) << 1;
      l4 |= (tmp[tmpIdx + 6] >>> 28) & MASK64_1;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 6] & MASK64_28) << 7;
      l5 |= (tmp[tmpIdx + 7] >>> 22) & MASK64_7;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 7] & MASK64_22) << 13;
      l6 |= (tmp[tmpIdx + 8] >>> 16) & MASK64_13;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 8] & MASK64_16) << 19;
      l7 |= (tmp[tmpIdx + 9] >>> 10) & MASK64_19;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 9] & MASK64_10) << 25;
      l8 |= (tmp[tmpIdx + 10] >>> 4) & MASK64_25;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 10] & MASK64_4) << 31;
      l9 |= (tmp[tmpIdx + 11] & MASK64_29) << 2;
      l9 |= (tmp[tmpIdx + 12] >>> 27) & MASK64_2;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 12] & MASK64_27) << 8;
      l10 |= (tmp[tmpIdx + 13] >>> 21) & MASK64_8;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 13] & MASK64_21) << 14;
      l11 |= (tmp[tmpIdx + 14] >>> 15) & MASK64_14;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 14] & MASK64_15) << 20;
      l12 |= (tmp[tmpIdx + 15] >>> 9) & MASK64_20;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 15] & MASK64_9) << 26;
      l13 |= (tmp[tmpIdx + 16] >>> 3) & MASK64_26;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 16] & MASK64_3) << 32;
      l14 |= (tmp[tmpIdx + 17] & MASK64_29) << 3;
      l14 |= (tmp[tmpIdx + 18] >>> 26) & MASK64_3;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 18] & MASK64_26) << 9;
      l15 |= (tmp[tmpIdx + 19] >>> 20) & MASK64_9;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 19] & MASK64_20) << 15;
      l16 |= (tmp[tmpIdx + 20] >>> 14) & MASK64_15;
      longs[longsIdx + 16] = l16;
      long l17 = (tmp[tmpIdx + 20] & MASK64_14) << 21;
      l17 |= (tmp[tmpIdx + 21] >>> 8) & MASK64_21;
      longs[longsIdx + 17] = l17;
      long l18 = (tmp[tmpIdx + 21] & MASK64_8) << 27;
      l18 |= (tmp[tmpIdx + 22] >>> 2) & MASK64_27;
      longs[longsIdx + 18] = l18;
      long l19 = (tmp[tmpIdx + 22] & MASK64_2) << 33;
      l19 |= (tmp[tmpIdx + 23] & MASK64_29) << 4;
      l19 |= (tmp[tmpIdx + 24] >>> 25) & MASK64_4;
      longs[longsIdx + 19] = l19;
      long l20 = (tmp[tmpIdx + 24] & MASK64_25) << 10;
      l20 |= (tmp[tmpIdx + 25] >>> 19) & MASK64_10;
      longs[longsIdx + 20] = l20;
      long l21 = (tmp[tmpIdx + 25] & MASK64_19) << 16;
      l21 |= (tmp[tmpIdx + 26] >>> 13) & MASK64_16;
      longs[longsIdx + 21] = l21;
      long l22 = (tmp[tmpIdx + 26] & MASK64_13) << 22;
      l22 |= (tmp[tmpIdx + 27] >>> 7) & MASK64_22;
      longs[longsIdx + 22] = l22;
      long l23 = (tmp[tmpIdx + 27] & MASK64_7) << 28;
      l23 |= (tmp[tmpIdx + 28] >>> 1) & MASK64_28;
      longs[longsIdx + 23] = l23;
      long l24 = (tmp[tmpIdx + 28] & MASK64_1) << 34;
      l24 |= (tmp[tmpIdx + 29] & MASK64_29) << 5;
      l24 |= (tmp[tmpIdx + 30] >>> 24) & MASK64_5;
      longs[longsIdx + 24] = l24;
      long l25 = (tmp[tmpIdx + 30] & MASK64_24) << 11;
      l25 |= (tmp[tmpIdx + 31] >>> 18) & MASK64_11;
      longs[longsIdx + 25] = l25;
      long l26 = (tmp[tmpIdx + 31] & MASK64_18) << 17;
      l26 |= (tmp[tmpIdx + 32] >>> 12) & MASK64_17;
      longs[longsIdx + 26] = l26;
      long l27 = (tmp[tmpIdx + 32] & MASK64_12) << 23;
      l27 |= (tmp[tmpIdx + 33] >>> 6) & MASK64_23;
      longs[longsIdx + 27] = l27;
      long l28 = (tmp[tmpIdx + 33] & MASK64_6) << 29;
      l28 |= (tmp[tmpIdx + 34] & MASK64_29) << 0;
      longs[longsIdx + 28] = l28;
    }
  }

  private static void decode36(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 36);
    shiftLongs(tmp, 36, longs, 0, 28, MASK64_36);
    for (int iter = 0, tmpIdx = 0, longsIdx = 36; iter < 4; ++iter, tmpIdx += 9, longsIdx += 7) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_28) << 8;
      l0 |= (tmp[tmpIdx + 1] >>> 20) & MASK64_8;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_20) << 16;
      l1 |= (tmp[tmpIdx + 2] >>> 12) & MASK64_16;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK64_12) << 24;
      l2 |= (tmp[tmpIdx + 3] >>> 4) & MASK64_24;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 3] & MASK64_4) << 32;
      l3 |= (tmp[tmpIdx + 4] & MASK64_28) << 4;
      l3 |= (tmp[tmpIdx + 5] >>> 24) & MASK64_4;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 5] & MASK64_24) << 12;
      l4 |= (tmp[tmpIdx + 6] >>> 16) & MASK64_12;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 6] & MASK64_16) << 20;
      l5 |= (tmp[tmpIdx + 7] >>> 8) & MASK64_20;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 7] & MASK64_8) << 28;
      l6 |= (tmp[tmpIdx + 8] & MASK64_28) << 0;
      longs[longsIdx + 6] = l6;
    }
  }

  private static void decode37(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 37);
    shiftLongs(tmp, 37, longs, 0, 27, MASK64_37);
    for (int iter = 0, tmpIdx = 0, longsIdx = 37; iter < 1; ++iter, tmpIdx += 37, longsIdx += 27) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_27) << 10;
      l0 |= (tmp[tmpIdx + 1] >>> 17) & MASK64_10;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_17) << 20;
      l1 |= (tmp[tmpIdx + 2] >>> 7) & MASK64_20;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK64_7) << 30;
      l2 |= (tmp[tmpIdx + 3] & MASK64_27) << 3;
      l2 |= (tmp[tmpIdx + 4] >>> 24) & MASK64_3;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 4] & MASK64_24) << 13;
      l3 |= (tmp[tmpIdx + 5] >>> 14) & MASK64_13;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 5] & MASK64_14) << 23;
      l4 |= (tmp[tmpIdx + 6] >>> 4) & MASK64_23;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 6] & MASK64_4) << 33;
      l5 |= (tmp[tmpIdx + 7] & MASK64_27) << 6;
      l5 |= (tmp[tmpIdx + 8] >>> 21) & MASK64_6;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 8] & MASK64_21) << 16;
      l6 |= (tmp[tmpIdx + 9] >>> 11) & MASK64_16;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 9] & MASK64_11) << 26;
      l7 |= (tmp[tmpIdx + 10] >>> 1) & MASK64_26;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 10] & MASK64_1) << 36;
      l8 |= (tmp[tmpIdx + 11] & MASK64_27) << 9;
      l8 |= (tmp[tmpIdx + 12] >>> 18) & MASK64_9;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 12] & MASK64_18) << 19;
      l9 |= (tmp[tmpIdx + 13] >>> 8) & MASK64_19;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 13] & MASK64_8) << 29;
      l10 |= (tmp[tmpIdx + 14] & MASK64_27) << 2;
      l10 |= (tmp[tmpIdx + 15] >>> 25) & MASK64_2;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 15] & MASK64_25) << 12;
      l11 |= (tmp[tmpIdx + 16] >>> 15) & MASK64_12;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 16] & MASK64_15) << 22;
      l12 |= (tmp[tmpIdx + 17] >>> 5) & MASK64_22;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 17] & MASK64_5) << 32;
      l13 |= (tmp[tmpIdx + 18] & MASK64_27) << 5;
      l13 |= (tmp[tmpIdx + 19] >>> 22) & MASK64_5;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 19] & MASK64_22) << 15;
      l14 |= (tmp[tmpIdx + 20] >>> 12) & MASK64_15;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 20] & MASK64_12) << 25;
      l15 |= (tmp[tmpIdx + 21] >>> 2) & MASK64_25;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 21] & MASK64_2) << 35;
      l16 |= (tmp[tmpIdx + 22] & MASK64_27) << 8;
      l16 |= (tmp[tmpIdx + 23] >>> 19) & MASK64_8;
      longs[longsIdx + 16] = l16;
      long l17 = (tmp[tmpIdx + 23] & MASK64_19) << 18;
      l17 |= (tmp[tmpIdx + 24] >>> 9) & MASK64_18;
      longs[longsIdx + 17] = l17;
      long l18 = (tmp[tmpIdx + 24] & MASK64_9) << 28;
      l18 |= (tmp[tmpIdx + 25] & MASK64_27) << 1;
      l18 |= (tmp[tmpIdx + 26] >>> 26) & MASK64_1;
      longs[longsIdx + 18] = l18;
      long l19 = (tmp[tmpIdx + 26] & MASK64_26) << 11;
      l19 |= (tmp[tmpIdx + 27] >>> 16) & MASK64_11;
      longs[longsIdx + 19] = l19;
      long l20 = (tmp[tmpIdx + 27] & MASK64_16) << 21;
      l20 |= (tmp[tmpIdx + 28] >>> 6) & MASK64_21;
      longs[longsIdx + 20] = l20;
      long l21 = (tmp[tmpIdx + 28] & MASK64_6) << 31;
      l21 |= (tmp[tmpIdx + 29] & MASK64_27) << 4;
      l21 |= (tmp[tmpIdx + 30] >>> 23) & MASK64_4;
      longs[longsIdx + 21] = l21;
      long l22 = (tmp[tmpIdx + 30] & MASK64_23) << 14;
      l22 |= (tmp[tmpIdx + 31] >>> 13) & MASK64_14;
      longs[longsIdx + 22] = l22;
      long l23 = (tmp[tmpIdx + 31] & MASK64_13) << 24;
      l23 |= (tmp[tmpIdx + 32] >>> 3) & MASK64_24;
      longs[longsIdx + 23] = l23;
      long l24 = (tmp[tmpIdx + 32] & MASK64_3) << 34;
      l24 |= (tmp[tmpIdx + 33] & MASK64_27) << 7;
      l24 |= (tmp[tmpIdx + 34] >>> 20) & MASK64_7;
      longs[longsIdx + 24] = l24;
      long l25 = (tmp[tmpIdx + 34] & MASK64_20) << 17;
      l25 |= (tmp[tmpIdx + 35] >>> 10) & MASK64_17;
      longs[longsIdx + 25] = l25;
      long l26 = (tmp[tmpIdx + 35] & MASK64_10) << 27;
      l26 |= (tmp[tmpIdx + 36] & MASK64_27) << 0;
      longs[longsIdx + 26] = l26;
    }
  }

  private static void decode38(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 38);
    shiftLongs(tmp, 38, longs, 0, 26, MASK64_38);
    for (int iter = 0, tmpIdx = 0, longsIdx = 38; iter < 2; ++iter, tmpIdx += 19, longsIdx += 13) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_26) << 12;
      l0 |= (tmp[tmpIdx + 1] >>> 14) & MASK64_12;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_14) << 24;
      l1 |= (tmp[tmpIdx + 2] >>> 2) & MASK64_24;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 2] & MASK64_2) << 36;
      l2 |= (tmp[tmpIdx + 3] & MASK64_26) << 10;
      l2 |= (tmp[tmpIdx + 4] >>> 16) & MASK64_10;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 4] & MASK64_16) << 22;
      l3 |= (tmp[tmpIdx + 5] >>> 4) & MASK64_22;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 5] & MASK64_4) << 34;
      l4 |= (tmp[tmpIdx + 6] & MASK64_26) << 8;
      l4 |= (tmp[tmpIdx + 7] >>> 18) & MASK64_8;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 7] & MASK64_18) << 20;
      l5 |= (tmp[tmpIdx + 8] >>> 6) & MASK64_20;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 8] & MASK64_6) << 32;
      l6 |= (tmp[tmpIdx + 9] & MASK64_26) << 6;
      l6 |= (tmp[tmpIdx + 10] >>> 20) & MASK64_6;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 10] & MASK64_20) << 18;
      l7 |= (tmp[tmpIdx + 11] >>> 8) & MASK64_18;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 11] & MASK64_8) << 30;
      l8 |= (tmp[tmpIdx + 12] & MASK64_26) << 4;
      l8 |= (tmp[tmpIdx + 13] >>> 22) & MASK64_4;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 13] & MASK64_22) << 16;
      l9 |= (tmp[tmpIdx + 14] >>> 10) & MASK64_16;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 14] & MASK64_10) << 28;
      l10 |= (tmp[tmpIdx + 15] & MASK64_26) << 2;
      l10 |= (tmp[tmpIdx + 16] >>> 24) & MASK64_2;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 16] & MASK64_24) << 14;
      l11 |= (tmp[tmpIdx + 17] >>> 12) & MASK64_14;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 17] & MASK64_12) << 26;
      l12 |= (tmp[tmpIdx + 18] & MASK64_26) << 0;
      longs[longsIdx + 12] = l12;
    }
  }

  private static void decode39(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 39);
    shiftLongs(tmp, 39, longs, 0, 25, MASK64_39);
    for (int iter = 0, tmpIdx = 0, longsIdx = 39; iter < 1; ++iter, tmpIdx += 39, longsIdx += 25) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_25) << 14;
      l0 |= (tmp[tmpIdx + 1] >>> 11) & MASK64_14;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_11) << 28;
      l1 |= (tmp[tmpIdx + 2] & MASK64_25) << 3;
      l1 |= (tmp[tmpIdx + 3] >>> 22) & MASK64_3;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK64_22) << 17;
      l2 |= (tmp[tmpIdx + 4] >>> 8) & MASK64_17;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 4] & MASK64_8) << 31;
      l3 |= (tmp[tmpIdx + 5] & MASK64_25) << 6;
      l3 |= (tmp[tmpIdx + 6] >>> 19) & MASK64_6;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 6] & MASK64_19) << 20;
      l4 |= (tmp[tmpIdx + 7] >>> 5) & MASK64_20;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 7] & MASK64_5) << 34;
      l5 |= (tmp[tmpIdx + 8] & MASK64_25) << 9;
      l5 |= (tmp[tmpIdx + 9] >>> 16) & MASK64_9;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 9] & MASK64_16) << 23;
      l6 |= (tmp[tmpIdx + 10] >>> 2) & MASK64_23;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 10] & MASK64_2) << 37;
      l7 |= (tmp[tmpIdx + 11] & MASK64_25) << 12;
      l7 |= (tmp[tmpIdx + 12] >>> 13) & MASK64_12;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 12] & MASK64_13) << 26;
      l8 |= (tmp[tmpIdx + 13] & MASK64_25) << 1;
      l8 |= (tmp[tmpIdx + 14] >>> 24) & MASK64_1;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 14] & MASK64_24) << 15;
      l9 |= (tmp[tmpIdx + 15] >>> 10) & MASK64_15;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 15] & MASK64_10) << 29;
      l10 |= (tmp[tmpIdx + 16] & MASK64_25) << 4;
      l10 |= (tmp[tmpIdx + 17] >>> 21) & MASK64_4;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 17] & MASK64_21) << 18;
      l11 |= (tmp[tmpIdx + 18] >>> 7) & MASK64_18;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 18] & MASK64_7) << 32;
      l12 |= (tmp[tmpIdx + 19] & MASK64_25) << 7;
      l12 |= (tmp[tmpIdx + 20] >>> 18) & MASK64_7;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 20] & MASK64_18) << 21;
      l13 |= (tmp[tmpIdx + 21] >>> 4) & MASK64_21;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 21] & MASK64_4) << 35;
      l14 |= (tmp[tmpIdx + 22] & MASK64_25) << 10;
      l14 |= (tmp[tmpIdx + 23] >>> 15) & MASK64_10;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 23] & MASK64_15) << 24;
      l15 |= (tmp[tmpIdx + 24] >>> 1) & MASK64_24;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 24] & MASK64_1) << 38;
      l16 |= (tmp[tmpIdx + 25] & MASK64_25) << 13;
      l16 |= (tmp[tmpIdx + 26] >>> 12) & MASK64_13;
      longs[longsIdx + 16] = l16;
      long l17 = (tmp[tmpIdx + 26] & MASK64_12) << 27;
      l17 |= (tmp[tmpIdx + 27] & MASK64_25) << 2;
      l17 |= (tmp[tmpIdx + 28] >>> 23) & MASK64_2;
      longs[longsIdx + 17] = l17;
      long l18 = (tmp[tmpIdx + 28] & MASK64_23) << 16;
      l18 |= (tmp[tmpIdx + 29] >>> 9) & MASK64_16;
      longs[longsIdx + 18] = l18;
      long l19 = (tmp[tmpIdx + 29] & MASK64_9) << 30;
      l19 |= (tmp[tmpIdx + 30] & MASK64_25) << 5;
      l19 |= (tmp[tmpIdx + 31] >>> 20) & MASK64_5;
      longs[longsIdx + 19] = l19;
      long l20 = (tmp[tmpIdx + 31] & MASK64_20) << 19;
      l20 |= (tmp[tmpIdx + 32] >>> 6) & MASK64_19;
      longs[longsIdx + 20] = l20;
      long l21 = (tmp[tmpIdx + 32] & MASK64_6) << 33;
      l21 |= (tmp[tmpIdx + 33] & MASK64_25) << 8;
      l21 |= (tmp[tmpIdx + 34] >>> 17) & MASK64_8;
      longs[longsIdx + 21] = l21;
      long l22 = (tmp[tmpIdx + 34] & MASK64_17) << 22;
      l22 |= (tmp[tmpIdx + 35] >>> 3) & MASK64_22;
      longs[longsIdx + 22] = l22;
      long l23 = (tmp[tmpIdx + 35] & MASK64_3) << 36;
      l23 |= (tmp[tmpIdx + 36] & MASK64_25) << 11;
      l23 |= (tmp[tmpIdx + 37] >>> 14) & MASK64_11;
      longs[longsIdx + 23] = l23;
      long l24 = (tmp[tmpIdx + 37] & MASK64_14) << 25;
      l24 |= (tmp[tmpIdx + 38] & MASK64_25) << 0;
      longs[longsIdx + 24] = l24;
    }
  }

  private static void decode40(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 40);
    shiftLongs(tmp, 40, longs, 0, 24, MASK64_40);
    for (int iter = 0, tmpIdx = 0, longsIdx = 40; iter < 8; ++iter, tmpIdx += 5, longsIdx += 3) {
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

  private static void decode41(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 41);
    shiftLongs(tmp, 41, longs, 0, 23, MASK64_41);
    for (int iter = 0, tmpIdx = 0, longsIdx = 41; iter < 1; ++iter, tmpIdx += 41, longsIdx += 23) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_23) << 18;
      l0 |= (tmp[tmpIdx + 1] >>> 5) & MASK64_18;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_5) << 36;
      l1 |= (tmp[tmpIdx + 2] & MASK64_23) << 13;
      l1 |= (tmp[tmpIdx + 3] >>> 10) & MASK64_13;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK64_10) << 31;
      l2 |= (tmp[tmpIdx + 4] & MASK64_23) << 8;
      l2 |= (tmp[tmpIdx + 5] >>> 15) & MASK64_8;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 5] & MASK64_15) << 26;
      l3 |= (tmp[tmpIdx + 6] & MASK64_23) << 3;
      l3 |= (tmp[tmpIdx + 7] >>> 20) & MASK64_3;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 7] & MASK64_20) << 21;
      l4 |= (tmp[tmpIdx + 8] >>> 2) & MASK64_21;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 8] & MASK64_2) << 39;
      l5 |= (tmp[tmpIdx + 9] & MASK64_23) << 16;
      l5 |= (tmp[tmpIdx + 10] >>> 7) & MASK64_16;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 10] & MASK64_7) << 34;
      l6 |= (tmp[tmpIdx + 11] & MASK64_23) << 11;
      l6 |= (tmp[tmpIdx + 12] >>> 12) & MASK64_11;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 12] & MASK64_12) << 29;
      l7 |= (tmp[tmpIdx + 13] & MASK64_23) << 6;
      l7 |= (tmp[tmpIdx + 14] >>> 17) & MASK64_6;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 14] & MASK64_17) << 24;
      l8 |= (tmp[tmpIdx + 15] & MASK64_23) << 1;
      l8 |= (tmp[tmpIdx + 16] >>> 22) & MASK64_1;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 16] & MASK64_22) << 19;
      l9 |= (tmp[tmpIdx + 17] >>> 4) & MASK64_19;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 17] & MASK64_4) << 37;
      l10 |= (tmp[tmpIdx + 18] & MASK64_23) << 14;
      l10 |= (tmp[tmpIdx + 19] >>> 9) & MASK64_14;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 19] & MASK64_9) << 32;
      l11 |= (tmp[tmpIdx + 20] & MASK64_23) << 9;
      l11 |= (tmp[tmpIdx + 21] >>> 14) & MASK64_9;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 21] & MASK64_14) << 27;
      l12 |= (tmp[tmpIdx + 22] & MASK64_23) << 4;
      l12 |= (tmp[tmpIdx + 23] >>> 19) & MASK64_4;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 23] & MASK64_19) << 22;
      l13 |= (tmp[tmpIdx + 24] >>> 1) & MASK64_22;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 24] & MASK64_1) << 40;
      l14 |= (tmp[tmpIdx + 25] & MASK64_23) << 17;
      l14 |= (tmp[tmpIdx + 26] >>> 6) & MASK64_17;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 26] & MASK64_6) << 35;
      l15 |= (tmp[tmpIdx + 27] & MASK64_23) << 12;
      l15 |= (tmp[tmpIdx + 28] >>> 11) & MASK64_12;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 28] & MASK64_11) << 30;
      l16 |= (tmp[tmpIdx + 29] & MASK64_23) << 7;
      l16 |= (tmp[tmpIdx + 30] >>> 16) & MASK64_7;
      longs[longsIdx + 16] = l16;
      long l17 = (tmp[tmpIdx + 30] & MASK64_16) << 25;
      l17 |= (tmp[tmpIdx + 31] & MASK64_23) << 2;
      l17 |= (tmp[tmpIdx + 32] >>> 21) & MASK64_2;
      longs[longsIdx + 17] = l17;
      long l18 = (tmp[tmpIdx + 32] & MASK64_21) << 20;
      l18 |= (tmp[tmpIdx + 33] >>> 3) & MASK64_20;
      longs[longsIdx + 18] = l18;
      long l19 = (tmp[tmpIdx + 33] & MASK64_3) << 38;
      l19 |= (tmp[tmpIdx + 34] & MASK64_23) << 15;
      l19 |= (tmp[tmpIdx + 35] >>> 8) & MASK64_15;
      longs[longsIdx + 19] = l19;
      long l20 = (tmp[tmpIdx + 35] & MASK64_8) << 33;
      l20 |= (tmp[tmpIdx + 36] & MASK64_23) << 10;
      l20 |= (tmp[tmpIdx + 37] >>> 13) & MASK64_10;
      longs[longsIdx + 20] = l20;
      long l21 = (tmp[tmpIdx + 37] & MASK64_13) << 28;
      l21 |= (tmp[tmpIdx + 38] & MASK64_23) << 5;
      l21 |= (tmp[tmpIdx + 39] >>> 18) & MASK64_5;
      longs[longsIdx + 21] = l21;
      long l22 = (tmp[tmpIdx + 39] & MASK64_18) << 23;
      l22 |= (tmp[tmpIdx + 40] & MASK64_23) << 0;
      longs[longsIdx + 22] = l22;
    }
  }

  private static void decode42(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 42);
    shiftLongs(tmp, 42, longs, 0, 22, MASK64_42);
    for (int iter = 0, tmpIdx = 0, longsIdx = 42; iter < 2; ++iter, tmpIdx += 21, longsIdx += 11) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_22) << 20;
      l0 |= (tmp[tmpIdx + 1] >>> 2) & MASK64_20;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 1] & MASK64_2) << 40;
      l1 |= (tmp[tmpIdx + 2] & MASK64_22) << 18;
      l1 |= (tmp[tmpIdx + 3] >>> 4) & MASK64_18;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 3] & MASK64_4) << 38;
      l2 |= (tmp[tmpIdx + 4] & MASK64_22) << 16;
      l2 |= (tmp[tmpIdx + 5] >>> 6) & MASK64_16;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 5] & MASK64_6) << 36;
      l3 |= (tmp[tmpIdx + 6] & MASK64_22) << 14;
      l3 |= (tmp[tmpIdx + 7] >>> 8) & MASK64_14;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 7] & MASK64_8) << 34;
      l4 |= (tmp[tmpIdx + 8] & MASK64_22) << 12;
      l4 |= (tmp[tmpIdx + 9] >>> 10) & MASK64_12;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 9] & MASK64_10) << 32;
      l5 |= (tmp[tmpIdx + 10] & MASK64_22) << 10;
      l5 |= (tmp[tmpIdx + 11] >>> 12) & MASK64_10;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 11] & MASK64_12) << 30;
      l6 |= (tmp[tmpIdx + 12] & MASK64_22) << 8;
      l6 |= (tmp[tmpIdx + 13] >>> 14) & MASK64_8;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 13] & MASK64_14) << 28;
      l7 |= (tmp[tmpIdx + 14] & MASK64_22) << 6;
      l7 |= (tmp[tmpIdx + 15] >>> 16) & MASK64_6;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 15] & MASK64_16) << 26;
      l8 |= (tmp[tmpIdx + 16] & MASK64_22) << 4;
      l8 |= (tmp[tmpIdx + 17] >>> 18) & MASK64_4;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 17] & MASK64_18) << 24;
      l9 |= (tmp[tmpIdx + 18] & MASK64_22) << 2;
      l9 |= (tmp[tmpIdx + 19] >>> 20) & MASK64_2;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 19] & MASK64_20) << 22;
      l10 |= (tmp[tmpIdx + 20] & MASK64_22) << 0;
      longs[longsIdx + 10] = l10;
    }
  }

  private static void decode43(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 43);
    shiftLongs(tmp, 43, longs, 0, 21, MASK64_43);
    for (int iter = 0, tmpIdx = 0, longsIdx = 43; iter < 1; ++iter, tmpIdx += 43, longsIdx += 21) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_21) << 22;
      l0 |= (tmp[tmpIdx + 1] & MASK64_21) << 1;
      l0 |= (tmp[tmpIdx + 2] >>> 20) & MASK64_1;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK64_20) << 23;
      l1 |= (tmp[tmpIdx + 3] & MASK64_21) << 2;
      l1 |= (tmp[tmpIdx + 4] >>> 19) & MASK64_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 4] & MASK64_19) << 24;
      l2 |= (tmp[tmpIdx + 5] & MASK64_21) << 3;
      l2 |= (tmp[tmpIdx + 6] >>> 18) & MASK64_3;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 6] & MASK64_18) << 25;
      l3 |= (tmp[tmpIdx + 7] & MASK64_21) << 4;
      l3 |= (tmp[tmpIdx + 8] >>> 17) & MASK64_4;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 8] & MASK64_17) << 26;
      l4 |= (tmp[tmpIdx + 9] & MASK64_21) << 5;
      l4 |= (tmp[tmpIdx + 10] >>> 16) & MASK64_5;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 10] & MASK64_16) << 27;
      l5 |= (tmp[tmpIdx + 11] & MASK64_21) << 6;
      l5 |= (tmp[tmpIdx + 12] >>> 15) & MASK64_6;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 12] & MASK64_15) << 28;
      l6 |= (tmp[tmpIdx + 13] & MASK64_21) << 7;
      l6 |= (tmp[tmpIdx + 14] >>> 14) & MASK64_7;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 14] & MASK64_14) << 29;
      l7 |= (tmp[tmpIdx + 15] & MASK64_21) << 8;
      l7 |= (tmp[tmpIdx + 16] >>> 13) & MASK64_8;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 16] & MASK64_13) << 30;
      l8 |= (tmp[tmpIdx + 17] & MASK64_21) << 9;
      l8 |= (tmp[tmpIdx + 18] >>> 12) & MASK64_9;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 18] & MASK64_12) << 31;
      l9 |= (tmp[tmpIdx + 19] & MASK64_21) << 10;
      l9 |= (tmp[tmpIdx + 20] >>> 11) & MASK64_10;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 20] & MASK64_11) << 32;
      l10 |= (tmp[tmpIdx + 21] & MASK64_21) << 11;
      l10 |= (tmp[tmpIdx + 22] >>> 10) & MASK64_11;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 22] & MASK64_10) << 33;
      l11 |= (tmp[tmpIdx + 23] & MASK64_21) << 12;
      l11 |= (tmp[tmpIdx + 24] >>> 9) & MASK64_12;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 24] & MASK64_9) << 34;
      l12 |= (tmp[tmpIdx + 25] & MASK64_21) << 13;
      l12 |= (tmp[tmpIdx + 26] >>> 8) & MASK64_13;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 26] & MASK64_8) << 35;
      l13 |= (tmp[tmpIdx + 27] & MASK64_21) << 14;
      l13 |= (tmp[tmpIdx + 28] >>> 7) & MASK64_14;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 28] & MASK64_7) << 36;
      l14 |= (tmp[tmpIdx + 29] & MASK64_21) << 15;
      l14 |= (tmp[tmpIdx + 30] >>> 6) & MASK64_15;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 30] & MASK64_6) << 37;
      l15 |= (tmp[tmpIdx + 31] & MASK64_21) << 16;
      l15 |= (tmp[tmpIdx + 32] >>> 5) & MASK64_16;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 32] & MASK64_5) << 38;
      l16 |= (tmp[tmpIdx + 33] & MASK64_21) << 17;
      l16 |= (tmp[tmpIdx + 34] >>> 4) & MASK64_17;
      longs[longsIdx + 16] = l16;
      long l17 = (tmp[tmpIdx + 34] & MASK64_4) << 39;
      l17 |= (tmp[tmpIdx + 35] & MASK64_21) << 18;
      l17 |= (tmp[tmpIdx + 36] >>> 3) & MASK64_18;
      longs[longsIdx + 17] = l17;
      long l18 = (tmp[tmpIdx + 36] & MASK64_3) << 40;
      l18 |= (tmp[tmpIdx + 37] & MASK64_21) << 19;
      l18 |= (tmp[tmpIdx + 38] >>> 2) & MASK64_19;
      longs[longsIdx + 18] = l18;
      long l19 = (tmp[tmpIdx + 38] & MASK64_2) << 41;
      l19 |= (tmp[tmpIdx + 39] & MASK64_21) << 20;
      l19 |= (tmp[tmpIdx + 40] >>> 1) & MASK64_20;
      longs[longsIdx + 19] = l19;
      long l20 = (tmp[tmpIdx + 40] & MASK64_1) << 42;
      l20 |= (tmp[tmpIdx + 41] & MASK64_21) << 21;
      l20 |= (tmp[tmpIdx + 42] & MASK64_21) << 0;
      longs[longsIdx + 20] = l20;
    }
  }

  private static void decode44(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 44);
    shiftLongs(tmp, 44, longs, 0, 20, MASK64_44);
    for (int iter = 0, tmpIdx = 0, longsIdx = 44; iter < 4; ++iter, tmpIdx += 11, longsIdx += 5) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_20) << 24;
      l0 |= (tmp[tmpIdx + 1] & MASK64_20) << 4;
      l0 |= (tmp[tmpIdx + 2] >>> 16) & MASK64_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK64_16) << 28;
      l1 |= (tmp[tmpIdx + 3] & MASK64_20) << 8;
      l1 |= (tmp[tmpIdx + 4] >>> 12) & MASK64_8;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 4] & MASK64_12) << 32;
      l2 |= (tmp[tmpIdx + 5] & MASK64_20) << 12;
      l2 |= (tmp[tmpIdx + 6] >>> 8) & MASK64_12;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 6] & MASK64_8) << 36;
      l3 |= (tmp[tmpIdx + 7] & MASK64_20) << 16;
      l3 |= (tmp[tmpIdx + 8] >>> 4) & MASK64_16;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 8] & MASK64_4) << 40;
      l4 |= (tmp[tmpIdx + 9] & MASK64_20) << 20;
      l4 |= (tmp[tmpIdx + 10] & MASK64_20) << 0;
      longs[longsIdx + 4] = l4;
    }
  }

  private static void decode45(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 45);
    shiftLongs(tmp, 45, longs, 0, 19, MASK64_45);
    for (int iter = 0, tmpIdx = 0, longsIdx = 45; iter < 1; ++iter, tmpIdx += 45, longsIdx += 19) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_19) << 26;
      l0 |= (tmp[tmpIdx + 1] & MASK64_19) << 7;
      l0 |= (tmp[tmpIdx + 2] >>> 12) & MASK64_7;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK64_12) << 33;
      l1 |= (tmp[tmpIdx + 3] & MASK64_19) << 14;
      l1 |= (tmp[tmpIdx + 4] >>> 5) & MASK64_14;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 4] & MASK64_5) << 40;
      l2 |= (tmp[tmpIdx + 5] & MASK64_19) << 21;
      l2 |= (tmp[tmpIdx + 6] & MASK64_19) << 2;
      l2 |= (tmp[tmpIdx + 7] >>> 17) & MASK64_2;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 7] & MASK64_17) << 28;
      l3 |= (tmp[tmpIdx + 8] & MASK64_19) << 9;
      l3 |= (tmp[tmpIdx + 9] >>> 10) & MASK64_9;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 9] & MASK64_10) << 35;
      l4 |= (tmp[tmpIdx + 10] & MASK64_19) << 16;
      l4 |= (tmp[tmpIdx + 11] >>> 3) & MASK64_16;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 11] & MASK64_3) << 42;
      l5 |= (tmp[tmpIdx + 12] & MASK64_19) << 23;
      l5 |= (tmp[tmpIdx + 13] & MASK64_19) << 4;
      l5 |= (tmp[tmpIdx + 14] >>> 15) & MASK64_4;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 14] & MASK64_15) << 30;
      l6 |= (tmp[tmpIdx + 15] & MASK64_19) << 11;
      l6 |= (tmp[tmpIdx + 16] >>> 8) & MASK64_11;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 16] & MASK64_8) << 37;
      l7 |= (tmp[tmpIdx + 17] & MASK64_19) << 18;
      l7 |= (tmp[tmpIdx + 18] >>> 1) & MASK64_18;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 18] & MASK64_1) << 44;
      l8 |= (tmp[tmpIdx + 19] & MASK64_19) << 25;
      l8 |= (tmp[tmpIdx + 20] & MASK64_19) << 6;
      l8 |= (tmp[tmpIdx + 21] >>> 13) & MASK64_6;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 21] & MASK64_13) << 32;
      l9 |= (tmp[tmpIdx + 22] & MASK64_19) << 13;
      l9 |= (tmp[tmpIdx + 23] >>> 6) & MASK64_13;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 23] & MASK64_6) << 39;
      l10 |= (tmp[tmpIdx + 24] & MASK64_19) << 20;
      l10 |= (tmp[tmpIdx + 25] & MASK64_19) << 1;
      l10 |= (tmp[tmpIdx + 26] >>> 18) & MASK64_1;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 26] & MASK64_18) << 27;
      l11 |= (tmp[tmpIdx + 27] & MASK64_19) << 8;
      l11 |= (tmp[tmpIdx + 28] >>> 11) & MASK64_8;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 28] & MASK64_11) << 34;
      l12 |= (tmp[tmpIdx + 29] & MASK64_19) << 15;
      l12 |= (tmp[tmpIdx + 30] >>> 4) & MASK64_15;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 30] & MASK64_4) << 41;
      l13 |= (tmp[tmpIdx + 31] & MASK64_19) << 22;
      l13 |= (tmp[tmpIdx + 32] & MASK64_19) << 3;
      l13 |= (tmp[tmpIdx + 33] >>> 16) & MASK64_3;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 33] & MASK64_16) << 29;
      l14 |= (tmp[tmpIdx + 34] & MASK64_19) << 10;
      l14 |= (tmp[tmpIdx + 35] >>> 9) & MASK64_10;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 35] & MASK64_9) << 36;
      l15 |= (tmp[tmpIdx + 36] & MASK64_19) << 17;
      l15 |= (tmp[tmpIdx + 37] >>> 2) & MASK64_17;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 37] & MASK64_2) << 43;
      l16 |= (tmp[tmpIdx + 38] & MASK64_19) << 24;
      l16 |= (tmp[tmpIdx + 39] & MASK64_19) << 5;
      l16 |= (tmp[tmpIdx + 40] >>> 14) & MASK64_5;
      longs[longsIdx + 16] = l16;
      long l17 = (tmp[tmpIdx + 40] & MASK64_14) << 31;
      l17 |= (tmp[tmpIdx + 41] & MASK64_19) << 12;
      l17 |= (tmp[tmpIdx + 42] >>> 7) & MASK64_12;
      longs[longsIdx + 17] = l17;
      long l18 = (tmp[tmpIdx + 42] & MASK64_7) << 38;
      l18 |= (tmp[tmpIdx + 43] & MASK64_19) << 19;
      l18 |= (tmp[tmpIdx + 44] & MASK64_19) << 0;
      longs[longsIdx + 18] = l18;
    }
  }

  private static void decode46(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 46);
    shiftLongs(tmp, 46, longs, 0, 18, MASK64_46);
    for (int iter = 0, tmpIdx = 0, longsIdx = 46; iter < 2; ++iter, tmpIdx += 23, longsIdx += 9) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_18) << 28;
      l0 |= (tmp[tmpIdx + 1] & MASK64_18) << 10;
      l0 |= (tmp[tmpIdx + 2] >>> 8) & MASK64_10;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK64_8) << 38;
      l1 |= (tmp[tmpIdx + 3] & MASK64_18) << 20;
      l1 |= (tmp[tmpIdx + 4] & MASK64_18) << 2;
      l1 |= (tmp[tmpIdx + 5] >>> 16) & MASK64_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 5] & MASK64_16) << 30;
      l2 |= (tmp[tmpIdx + 6] & MASK64_18) << 12;
      l2 |= (tmp[tmpIdx + 7] >>> 6) & MASK64_12;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 7] & MASK64_6) << 40;
      l3 |= (tmp[tmpIdx + 8] & MASK64_18) << 22;
      l3 |= (tmp[tmpIdx + 9] & MASK64_18) << 4;
      l3 |= (tmp[tmpIdx + 10] >>> 14) & MASK64_4;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 10] & MASK64_14) << 32;
      l4 |= (tmp[tmpIdx + 11] & MASK64_18) << 14;
      l4 |= (tmp[tmpIdx + 12] >>> 4) & MASK64_14;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 12] & MASK64_4) << 42;
      l5 |= (tmp[tmpIdx + 13] & MASK64_18) << 24;
      l5 |= (tmp[tmpIdx + 14] & MASK64_18) << 6;
      l5 |= (tmp[tmpIdx + 15] >>> 12) & MASK64_6;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 15] & MASK64_12) << 34;
      l6 |= (tmp[tmpIdx + 16] & MASK64_18) << 16;
      l6 |= (tmp[tmpIdx + 17] >>> 2) & MASK64_16;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 17] & MASK64_2) << 44;
      l7 |= (tmp[tmpIdx + 18] & MASK64_18) << 26;
      l7 |= (tmp[tmpIdx + 19] & MASK64_18) << 8;
      l7 |= (tmp[tmpIdx + 20] >>> 10) & MASK64_8;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 20] & MASK64_10) << 36;
      l8 |= (tmp[tmpIdx + 21] & MASK64_18) << 18;
      l8 |= (tmp[tmpIdx + 22] & MASK64_18) << 0;
      longs[longsIdx + 8] = l8;
    }
  }

  private static void decode47(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 47);
    shiftLongs(tmp, 47, longs, 0, 17, MASK64_47);
    for (int iter = 0, tmpIdx = 0, longsIdx = 47; iter < 1; ++iter, tmpIdx += 47, longsIdx += 17) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_17) << 30;
      l0 |= (tmp[tmpIdx + 1] & MASK64_17) << 13;
      l0 |= (tmp[tmpIdx + 2] >>> 4) & MASK64_13;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 2] & MASK64_4) << 43;
      l1 |= (tmp[tmpIdx + 3] & MASK64_17) << 26;
      l1 |= (tmp[tmpIdx + 4] & MASK64_17) << 9;
      l1 |= (tmp[tmpIdx + 5] >>> 8) & MASK64_9;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 5] & MASK64_8) << 39;
      l2 |= (tmp[tmpIdx + 6] & MASK64_17) << 22;
      l2 |= (tmp[tmpIdx + 7] & MASK64_17) << 5;
      l2 |= (tmp[tmpIdx + 8] >>> 12) & MASK64_5;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 8] & MASK64_12) << 35;
      l3 |= (tmp[tmpIdx + 9] & MASK64_17) << 18;
      l3 |= (tmp[tmpIdx + 10] & MASK64_17) << 1;
      l3 |= (tmp[tmpIdx + 11] >>> 16) & MASK64_1;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 11] & MASK64_16) << 31;
      l4 |= (tmp[tmpIdx + 12] & MASK64_17) << 14;
      l4 |= (tmp[tmpIdx + 13] >>> 3) & MASK64_14;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 13] & MASK64_3) << 44;
      l5 |= (tmp[tmpIdx + 14] & MASK64_17) << 27;
      l5 |= (tmp[tmpIdx + 15] & MASK64_17) << 10;
      l5 |= (tmp[tmpIdx + 16] >>> 7) & MASK64_10;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 16] & MASK64_7) << 40;
      l6 |= (tmp[tmpIdx + 17] & MASK64_17) << 23;
      l6 |= (tmp[tmpIdx + 18] & MASK64_17) << 6;
      l6 |= (tmp[tmpIdx + 19] >>> 11) & MASK64_6;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 19] & MASK64_11) << 36;
      l7 |= (tmp[tmpIdx + 20] & MASK64_17) << 19;
      l7 |= (tmp[tmpIdx + 21] & MASK64_17) << 2;
      l7 |= (tmp[tmpIdx + 22] >>> 15) & MASK64_2;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 22] & MASK64_15) << 32;
      l8 |= (tmp[tmpIdx + 23] & MASK64_17) << 15;
      l8 |= (tmp[tmpIdx + 24] >>> 2) & MASK64_15;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 24] & MASK64_2) << 45;
      l9 |= (tmp[tmpIdx + 25] & MASK64_17) << 28;
      l9 |= (tmp[tmpIdx + 26] & MASK64_17) << 11;
      l9 |= (tmp[tmpIdx + 27] >>> 6) & MASK64_11;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 27] & MASK64_6) << 41;
      l10 |= (tmp[tmpIdx + 28] & MASK64_17) << 24;
      l10 |= (tmp[tmpIdx + 29] & MASK64_17) << 7;
      l10 |= (tmp[tmpIdx + 30] >>> 10) & MASK64_7;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 30] & MASK64_10) << 37;
      l11 |= (tmp[tmpIdx + 31] & MASK64_17) << 20;
      l11 |= (tmp[tmpIdx + 32] & MASK64_17) << 3;
      l11 |= (tmp[tmpIdx + 33] >>> 14) & MASK64_3;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 33] & MASK64_14) << 33;
      l12 |= (tmp[tmpIdx + 34] & MASK64_17) << 16;
      l12 |= (tmp[tmpIdx + 35] >>> 1) & MASK64_16;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 35] & MASK64_1) << 46;
      l13 |= (tmp[tmpIdx + 36] & MASK64_17) << 29;
      l13 |= (tmp[tmpIdx + 37] & MASK64_17) << 12;
      l13 |= (tmp[tmpIdx + 38] >>> 5) & MASK64_12;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 38] & MASK64_5) << 42;
      l14 |= (tmp[tmpIdx + 39] & MASK64_17) << 25;
      l14 |= (tmp[tmpIdx + 40] & MASK64_17) << 8;
      l14 |= (tmp[tmpIdx + 41] >>> 9) & MASK64_8;
      longs[longsIdx + 14] = l14;
      long l15 = (tmp[tmpIdx + 41] & MASK64_9) << 38;
      l15 |= (tmp[tmpIdx + 42] & MASK64_17) << 21;
      l15 |= (tmp[tmpIdx + 43] & MASK64_17) << 4;
      l15 |= (tmp[tmpIdx + 44] >>> 13) & MASK64_4;
      longs[longsIdx + 15] = l15;
      long l16 = (tmp[tmpIdx + 44] & MASK64_13) << 34;
      l16 |= (tmp[tmpIdx + 45] & MASK64_17) << 17;
      l16 |= (tmp[tmpIdx + 46] & MASK64_17) << 0;
      longs[longsIdx + 16] = l16;
    }
  }

  private static void decode48(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 48);
    shiftLongs(tmp, 48, longs, 0, 16, MASK64_48);
    shiftLongs(tmp, 48, tmp, 0, 0, MASK64_16);
    for (int iter = 0, tmpIdx = 0, longsIdx = 48; iter < 16; ++iter, tmpIdx += 3, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 32;
      l0 |= tmp[tmpIdx + 1] << 16;
      l0 |= tmp[tmpIdx + 2] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode49(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 49);
    shiftLongs(tmp, 49, longs, 0, 15, MASK64_49);
    for (int iter = 0, tmpIdx = 0, longsIdx = 49; iter < 1; ++iter, tmpIdx += 49, longsIdx += 15) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_15) << 34;
      l0 |= (tmp[tmpIdx + 1] & MASK64_15) << 19;
      l0 |= (tmp[tmpIdx + 2] & MASK64_15) << 4;
      l0 |= (tmp[tmpIdx + 3] >>> 11) & MASK64_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 3] & MASK64_11) << 38;
      l1 |= (tmp[tmpIdx + 4] & MASK64_15) << 23;
      l1 |= (tmp[tmpIdx + 5] & MASK64_15) << 8;
      l1 |= (tmp[tmpIdx + 6] >>> 7) & MASK64_8;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 6] & MASK64_7) << 42;
      l2 |= (tmp[tmpIdx + 7] & MASK64_15) << 27;
      l2 |= (tmp[tmpIdx + 8] & MASK64_15) << 12;
      l2 |= (tmp[tmpIdx + 9] >>> 3) & MASK64_12;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 9] & MASK64_3) << 46;
      l3 |= (tmp[tmpIdx + 10] & MASK64_15) << 31;
      l3 |= (tmp[tmpIdx + 11] & MASK64_15) << 16;
      l3 |= (tmp[tmpIdx + 12] & MASK64_15) << 1;
      l3 |= (tmp[tmpIdx + 13] >>> 14) & MASK64_1;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 13] & MASK64_14) << 35;
      l4 |= (tmp[tmpIdx + 14] & MASK64_15) << 20;
      l4 |= (tmp[tmpIdx + 15] & MASK64_15) << 5;
      l4 |= (tmp[tmpIdx + 16] >>> 10) & MASK64_5;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 16] & MASK64_10) << 39;
      l5 |= (tmp[tmpIdx + 17] & MASK64_15) << 24;
      l5 |= (tmp[tmpIdx + 18] & MASK64_15) << 9;
      l5 |= (tmp[tmpIdx + 19] >>> 6) & MASK64_9;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 19] & MASK64_6) << 43;
      l6 |= (tmp[tmpIdx + 20] & MASK64_15) << 28;
      l6 |= (tmp[tmpIdx + 21] & MASK64_15) << 13;
      l6 |= (tmp[tmpIdx + 22] >>> 2) & MASK64_13;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 22] & MASK64_2) << 47;
      l7 |= (tmp[tmpIdx + 23] & MASK64_15) << 32;
      l7 |= (tmp[tmpIdx + 24] & MASK64_15) << 17;
      l7 |= (tmp[tmpIdx + 25] & MASK64_15) << 2;
      l7 |= (tmp[tmpIdx + 26] >>> 13) & MASK64_2;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 26] & MASK64_13) << 36;
      l8 |= (tmp[tmpIdx + 27] & MASK64_15) << 21;
      l8 |= (tmp[tmpIdx + 28] & MASK64_15) << 6;
      l8 |= (tmp[tmpIdx + 29] >>> 9) & MASK64_6;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 29] & MASK64_9) << 40;
      l9 |= (tmp[tmpIdx + 30] & MASK64_15) << 25;
      l9 |= (tmp[tmpIdx + 31] & MASK64_15) << 10;
      l9 |= (tmp[tmpIdx + 32] >>> 5) & MASK64_10;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 32] & MASK64_5) << 44;
      l10 |= (tmp[tmpIdx + 33] & MASK64_15) << 29;
      l10 |= (tmp[tmpIdx + 34] & MASK64_15) << 14;
      l10 |= (tmp[tmpIdx + 35] >>> 1) & MASK64_14;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 35] & MASK64_1) << 48;
      l11 |= (tmp[tmpIdx + 36] & MASK64_15) << 33;
      l11 |= (tmp[tmpIdx + 37] & MASK64_15) << 18;
      l11 |= (tmp[tmpIdx + 38] & MASK64_15) << 3;
      l11 |= (tmp[tmpIdx + 39] >>> 12) & MASK64_3;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 39] & MASK64_12) << 37;
      l12 |= (tmp[tmpIdx + 40] & MASK64_15) << 22;
      l12 |= (tmp[tmpIdx + 41] & MASK64_15) << 7;
      l12 |= (tmp[tmpIdx + 42] >>> 8) & MASK64_7;
      longs[longsIdx + 12] = l12;
      long l13 = (tmp[tmpIdx + 42] & MASK64_8) << 41;
      l13 |= (tmp[tmpIdx + 43] & MASK64_15) << 26;
      l13 |= (tmp[tmpIdx + 44] & MASK64_15) << 11;
      l13 |= (tmp[tmpIdx + 45] >>> 4) & MASK64_11;
      longs[longsIdx + 13] = l13;
      long l14 = (tmp[tmpIdx + 45] & MASK64_4) << 45;
      l14 |= (tmp[tmpIdx + 46] & MASK64_15) << 30;
      l14 |= (tmp[tmpIdx + 47] & MASK64_15) << 15;
      l14 |= (tmp[tmpIdx + 48] & MASK64_15) << 0;
      longs[longsIdx + 14] = l14;
    }
  }

  private static void decode50(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 50);
    shiftLongs(tmp, 50, longs, 0, 14, MASK64_50);
    for (int iter = 0, tmpIdx = 0, longsIdx = 50; iter < 2; ++iter, tmpIdx += 25, longsIdx += 7) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_14) << 36;
      l0 |= (tmp[tmpIdx + 1] & MASK64_14) << 22;
      l0 |= (tmp[tmpIdx + 2] & MASK64_14) << 8;
      l0 |= (tmp[tmpIdx + 3] >>> 6) & MASK64_8;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 3] & MASK64_6) << 44;
      l1 |= (tmp[tmpIdx + 4] & MASK64_14) << 30;
      l1 |= (tmp[tmpIdx + 5] & MASK64_14) << 16;
      l1 |= (tmp[tmpIdx + 6] & MASK64_14) << 2;
      l1 |= (tmp[tmpIdx + 7] >>> 12) & MASK64_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 7] & MASK64_12) << 38;
      l2 |= (tmp[tmpIdx + 8] & MASK64_14) << 24;
      l2 |= (tmp[tmpIdx + 9] & MASK64_14) << 10;
      l2 |= (tmp[tmpIdx + 10] >>> 4) & MASK64_10;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 10] & MASK64_4) << 46;
      l3 |= (tmp[tmpIdx + 11] & MASK64_14) << 32;
      l3 |= (tmp[tmpIdx + 12] & MASK64_14) << 18;
      l3 |= (tmp[tmpIdx + 13] & MASK64_14) << 4;
      l3 |= (tmp[tmpIdx + 14] >>> 10) & MASK64_4;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 14] & MASK64_10) << 40;
      l4 |= (tmp[tmpIdx + 15] & MASK64_14) << 26;
      l4 |= (tmp[tmpIdx + 16] & MASK64_14) << 12;
      l4 |= (tmp[tmpIdx + 17] >>> 2) & MASK64_12;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 17] & MASK64_2) << 48;
      l5 |= (tmp[tmpIdx + 18] & MASK64_14) << 34;
      l5 |= (tmp[tmpIdx + 19] & MASK64_14) << 20;
      l5 |= (tmp[tmpIdx + 20] & MASK64_14) << 6;
      l5 |= (tmp[tmpIdx + 21] >>> 8) & MASK64_6;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 21] & MASK64_8) << 42;
      l6 |= (tmp[tmpIdx + 22] & MASK64_14) << 28;
      l6 |= (tmp[tmpIdx + 23] & MASK64_14) << 14;
      l6 |= (tmp[tmpIdx + 24] & MASK64_14) << 0;
      longs[longsIdx + 6] = l6;
    }
  }

  private static void decode51(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 51);
    shiftLongs(tmp, 51, longs, 0, 13, MASK64_51);
    for (int iter = 0, tmpIdx = 0, longsIdx = 51; iter < 1; ++iter, tmpIdx += 51, longsIdx += 13) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_13) << 38;
      l0 |= (tmp[tmpIdx + 1] & MASK64_13) << 25;
      l0 |= (tmp[tmpIdx + 2] & MASK64_13) << 12;
      l0 |= (tmp[tmpIdx + 3] >>> 1) & MASK64_12;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 3] & MASK64_1) << 50;
      l1 |= (tmp[tmpIdx + 4] & MASK64_13) << 37;
      l1 |= (tmp[tmpIdx + 5] & MASK64_13) << 24;
      l1 |= (tmp[tmpIdx + 6] & MASK64_13) << 11;
      l1 |= (tmp[tmpIdx + 7] >>> 2) & MASK64_11;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 7] & MASK64_2) << 49;
      l2 |= (tmp[tmpIdx + 8] & MASK64_13) << 36;
      l2 |= (tmp[tmpIdx + 9] & MASK64_13) << 23;
      l2 |= (tmp[tmpIdx + 10] & MASK64_13) << 10;
      l2 |= (tmp[tmpIdx + 11] >>> 3) & MASK64_10;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 11] & MASK64_3) << 48;
      l3 |= (tmp[tmpIdx + 12] & MASK64_13) << 35;
      l3 |= (tmp[tmpIdx + 13] & MASK64_13) << 22;
      l3 |= (tmp[tmpIdx + 14] & MASK64_13) << 9;
      l3 |= (tmp[tmpIdx + 15] >>> 4) & MASK64_9;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 15] & MASK64_4) << 47;
      l4 |= (tmp[tmpIdx + 16] & MASK64_13) << 34;
      l4 |= (tmp[tmpIdx + 17] & MASK64_13) << 21;
      l4 |= (tmp[tmpIdx + 18] & MASK64_13) << 8;
      l4 |= (tmp[tmpIdx + 19] >>> 5) & MASK64_8;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 19] & MASK64_5) << 46;
      l5 |= (tmp[tmpIdx + 20] & MASK64_13) << 33;
      l5 |= (tmp[tmpIdx + 21] & MASK64_13) << 20;
      l5 |= (tmp[tmpIdx + 22] & MASK64_13) << 7;
      l5 |= (tmp[tmpIdx + 23] >>> 6) & MASK64_7;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 23] & MASK64_6) << 45;
      l6 |= (tmp[tmpIdx + 24] & MASK64_13) << 32;
      l6 |= (tmp[tmpIdx + 25] & MASK64_13) << 19;
      l6 |= (tmp[tmpIdx + 26] & MASK64_13) << 6;
      l6 |= (tmp[tmpIdx + 27] >>> 7) & MASK64_6;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 27] & MASK64_7) << 44;
      l7 |= (tmp[tmpIdx + 28] & MASK64_13) << 31;
      l7 |= (tmp[tmpIdx + 29] & MASK64_13) << 18;
      l7 |= (tmp[tmpIdx + 30] & MASK64_13) << 5;
      l7 |= (tmp[tmpIdx + 31] >>> 8) & MASK64_5;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 31] & MASK64_8) << 43;
      l8 |= (tmp[tmpIdx + 32] & MASK64_13) << 30;
      l8 |= (tmp[tmpIdx + 33] & MASK64_13) << 17;
      l8 |= (tmp[tmpIdx + 34] & MASK64_13) << 4;
      l8 |= (tmp[tmpIdx + 35] >>> 9) & MASK64_4;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 35] & MASK64_9) << 42;
      l9 |= (tmp[tmpIdx + 36] & MASK64_13) << 29;
      l9 |= (tmp[tmpIdx + 37] & MASK64_13) << 16;
      l9 |= (tmp[tmpIdx + 38] & MASK64_13) << 3;
      l9 |= (tmp[tmpIdx + 39] >>> 10) & MASK64_3;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 39] & MASK64_10) << 41;
      l10 |= (tmp[tmpIdx + 40] & MASK64_13) << 28;
      l10 |= (tmp[tmpIdx + 41] & MASK64_13) << 15;
      l10 |= (tmp[tmpIdx + 42] & MASK64_13) << 2;
      l10 |= (tmp[tmpIdx + 43] >>> 11) & MASK64_2;
      longs[longsIdx + 10] = l10;
      long l11 = (tmp[tmpIdx + 43] & MASK64_11) << 40;
      l11 |= (tmp[tmpIdx + 44] & MASK64_13) << 27;
      l11 |= (tmp[tmpIdx + 45] & MASK64_13) << 14;
      l11 |= (tmp[tmpIdx + 46] & MASK64_13) << 1;
      l11 |= (tmp[tmpIdx + 47] >>> 12) & MASK64_1;
      longs[longsIdx + 11] = l11;
      long l12 = (tmp[tmpIdx + 47] & MASK64_12) << 39;
      l12 |= (tmp[tmpIdx + 48] & MASK64_13) << 26;
      l12 |= (tmp[tmpIdx + 49] & MASK64_13) << 13;
      l12 |= (tmp[tmpIdx + 50] & MASK64_13) << 0;
      longs[longsIdx + 12] = l12;
    }
  }

  private static void decode52(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 52);
    shiftLongs(tmp, 52, longs, 0, 12, MASK64_52);
    for (int iter = 0, tmpIdx = 0, longsIdx = 52; iter < 4; ++iter, tmpIdx += 13, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_12) << 40;
      l0 |= (tmp[tmpIdx + 1] & MASK64_12) << 28;
      l0 |= (tmp[tmpIdx + 2] & MASK64_12) << 16;
      l0 |= (tmp[tmpIdx + 3] & MASK64_12) << 4;
      l0 |= (tmp[tmpIdx + 4] >>> 8) & MASK64_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 4] & MASK64_8) << 44;
      l1 |= (tmp[tmpIdx + 5] & MASK64_12) << 32;
      l1 |= (tmp[tmpIdx + 6] & MASK64_12) << 20;
      l1 |= (tmp[tmpIdx + 7] & MASK64_12) << 8;
      l1 |= (tmp[tmpIdx + 8] >>> 4) & MASK64_8;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 8] & MASK64_4) << 48;
      l2 |= (tmp[tmpIdx + 9] & MASK64_12) << 36;
      l2 |= (tmp[tmpIdx + 10] & MASK64_12) << 24;
      l2 |= (tmp[tmpIdx + 11] & MASK64_12) << 12;
      l2 |= (tmp[tmpIdx + 12] & MASK64_12) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode53(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 53);
    shiftLongs(tmp, 53, longs, 0, 11, MASK64_53);
    for (int iter = 0, tmpIdx = 0, longsIdx = 53; iter < 1; ++iter, tmpIdx += 53, longsIdx += 11) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_11) << 42;
      l0 |= (tmp[tmpIdx + 1] & MASK64_11) << 31;
      l0 |= (tmp[tmpIdx + 2] & MASK64_11) << 20;
      l0 |= (tmp[tmpIdx + 3] & MASK64_11) << 9;
      l0 |= (tmp[tmpIdx + 4] >>> 2) & MASK64_9;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 4] & MASK64_2) << 51;
      l1 |= (tmp[tmpIdx + 5] & MASK64_11) << 40;
      l1 |= (tmp[tmpIdx + 6] & MASK64_11) << 29;
      l1 |= (tmp[tmpIdx + 7] & MASK64_11) << 18;
      l1 |= (tmp[tmpIdx + 8] & MASK64_11) << 7;
      l1 |= (tmp[tmpIdx + 9] >>> 4) & MASK64_7;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 9] & MASK64_4) << 49;
      l2 |= (tmp[tmpIdx + 10] & MASK64_11) << 38;
      l2 |= (tmp[tmpIdx + 11] & MASK64_11) << 27;
      l2 |= (tmp[tmpIdx + 12] & MASK64_11) << 16;
      l2 |= (tmp[tmpIdx + 13] & MASK64_11) << 5;
      l2 |= (tmp[tmpIdx + 14] >>> 6) & MASK64_5;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 14] & MASK64_6) << 47;
      l3 |= (tmp[tmpIdx + 15] & MASK64_11) << 36;
      l3 |= (tmp[tmpIdx + 16] & MASK64_11) << 25;
      l3 |= (tmp[tmpIdx + 17] & MASK64_11) << 14;
      l3 |= (tmp[tmpIdx + 18] & MASK64_11) << 3;
      l3 |= (tmp[tmpIdx + 19] >>> 8) & MASK64_3;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 19] & MASK64_8) << 45;
      l4 |= (tmp[tmpIdx + 20] & MASK64_11) << 34;
      l4 |= (tmp[tmpIdx + 21] & MASK64_11) << 23;
      l4 |= (tmp[tmpIdx + 22] & MASK64_11) << 12;
      l4 |= (tmp[tmpIdx + 23] & MASK64_11) << 1;
      l4 |= (tmp[tmpIdx + 24] >>> 10) & MASK64_1;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 24] & MASK64_10) << 43;
      l5 |= (tmp[tmpIdx + 25] & MASK64_11) << 32;
      l5 |= (tmp[tmpIdx + 26] & MASK64_11) << 21;
      l5 |= (tmp[tmpIdx + 27] & MASK64_11) << 10;
      l5 |= (tmp[tmpIdx + 28] >>> 1) & MASK64_10;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 28] & MASK64_1) << 52;
      l6 |= (tmp[tmpIdx + 29] & MASK64_11) << 41;
      l6 |= (tmp[tmpIdx + 30] & MASK64_11) << 30;
      l6 |= (tmp[tmpIdx + 31] & MASK64_11) << 19;
      l6 |= (tmp[tmpIdx + 32] & MASK64_11) << 8;
      l6 |= (tmp[tmpIdx + 33] >>> 3) & MASK64_8;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 33] & MASK64_3) << 50;
      l7 |= (tmp[tmpIdx + 34] & MASK64_11) << 39;
      l7 |= (tmp[tmpIdx + 35] & MASK64_11) << 28;
      l7 |= (tmp[tmpIdx + 36] & MASK64_11) << 17;
      l7 |= (tmp[tmpIdx + 37] & MASK64_11) << 6;
      l7 |= (tmp[tmpIdx + 38] >>> 5) & MASK64_6;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 38] & MASK64_5) << 48;
      l8 |= (tmp[tmpIdx + 39] & MASK64_11) << 37;
      l8 |= (tmp[tmpIdx + 40] & MASK64_11) << 26;
      l8 |= (tmp[tmpIdx + 41] & MASK64_11) << 15;
      l8 |= (tmp[tmpIdx + 42] & MASK64_11) << 4;
      l8 |= (tmp[tmpIdx + 43] >>> 7) & MASK64_4;
      longs[longsIdx + 8] = l8;
      long l9 = (tmp[tmpIdx + 43] & MASK64_7) << 46;
      l9 |= (tmp[tmpIdx + 44] & MASK64_11) << 35;
      l9 |= (tmp[tmpIdx + 45] & MASK64_11) << 24;
      l9 |= (tmp[tmpIdx + 46] & MASK64_11) << 13;
      l9 |= (tmp[tmpIdx + 47] & MASK64_11) << 2;
      l9 |= (tmp[tmpIdx + 48] >>> 9) & MASK64_2;
      longs[longsIdx + 9] = l9;
      long l10 = (tmp[tmpIdx + 48] & MASK64_9) << 44;
      l10 |= (tmp[tmpIdx + 49] & MASK64_11) << 33;
      l10 |= (tmp[tmpIdx + 50] & MASK64_11) << 22;
      l10 |= (tmp[tmpIdx + 51] & MASK64_11) << 11;
      l10 |= (tmp[tmpIdx + 52] & MASK64_11) << 0;
      longs[longsIdx + 10] = l10;
    }
  }

  private static void decode54(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 54);
    shiftLongs(tmp, 54, longs, 0, 10, MASK64_54);
    for (int iter = 0, tmpIdx = 0, longsIdx = 54; iter < 2; ++iter, tmpIdx += 27, longsIdx += 5) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_10) << 44;
      l0 |= (tmp[tmpIdx + 1] & MASK64_10) << 34;
      l0 |= (tmp[tmpIdx + 2] & MASK64_10) << 24;
      l0 |= (tmp[tmpIdx + 3] & MASK64_10) << 14;
      l0 |= (tmp[tmpIdx + 4] & MASK64_10) << 4;
      l0 |= (tmp[tmpIdx + 5] >>> 6) & MASK64_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 5] & MASK64_6) << 48;
      l1 |= (tmp[tmpIdx + 6] & MASK64_10) << 38;
      l1 |= (tmp[tmpIdx + 7] & MASK64_10) << 28;
      l1 |= (tmp[tmpIdx + 8] & MASK64_10) << 18;
      l1 |= (tmp[tmpIdx + 9] & MASK64_10) << 8;
      l1 |= (tmp[tmpIdx + 10] >>> 2) & MASK64_8;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 10] & MASK64_2) << 52;
      l2 |= (tmp[tmpIdx + 11] & MASK64_10) << 42;
      l2 |= (tmp[tmpIdx + 12] & MASK64_10) << 32;
      l2 |= (tmp[tmpIdx + 13] & MASK64_10) << 22;
      l2 |= (tmp[tmpIdx + 14] & MASK64_10) << 12;
      l2 |= (tmp[tmpIdx + 15] & MASK64_10) << 2;
      l2 |= (tmp[tmpIdx + 16] >>> 8) & MASK64_2;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 16] & MASK64_8) << 46;
      l3 |= (tmp[tmpIdx + 17] & MASK64_10) << 36;
      l3 |= (tmp[tmpIdx + 18] & MASK64_10) << 26;
      l3 |= (tmp[tmpIdx + 19] & MASK64_10) << 16;
      l3 |= (tmp[tmpIdx + 20] & MASK64_10) << 6;
      l3 |= (tmp[tmpIdx + 21] >>> 4) & MASK64_6;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 21] & MASK64_4) << 50;
      l4 |= (tmp[tmpIdx + 22] & MASK64_10) << 40;
      l4 |= (tmp[tmpIdx + 23] & MASK64_10) << 30;
      l4 |= (tmp[tmpIdx + 24] & MASK64_10) << 20;
      l4 |= (tmp[tmpIdx + 25] & MASK64_10) << 10;
      l4 |= (tmp[tmpIdx + 26] & MASK64_10) << 0;
      longs[longsIdx + 4] = l4;
    }
  }

  private static void decode55(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 55);
    shiftLongs(tmp, 55, longs, 0, 9, MASK64_55);
    for (int iter = 0, tmpIdx = 0, longsIdx = 55; iter < 1; ++iter, tmpIdx += 55, longsIdx += 9) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_9) << 46;
      l0 |= (tmp[tmpIdx + 1] & MASK64_9) << 37;
      l0 |= (tmp[tmpIdx + 2] & MASK64_9) << 28;
      l0 |= (tmp[tmpIdx + 3] & MASK64_9) << 19;
      l0 |= (tmp[tmpIdx + 4] & MASK64_9) << 10;
      l0 |= (tmp[tmpIdx + 5] & MASK64_9) << 1;
      l0 |= (tmp[tmpIdx + 6] >>> 8) & MASK64_1;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 6] & MASK64_8) << 47;
      l1 |= (tmp[tmpIdx + 7] & MASK64_9) << 38;
      l1 |= (tmp[tmpIdx + 8] & MASK64_9) << 29;
      l1 |= (tmp[tmpIdx + 9] & MASK64_9) << 20;
      l1 |= (tmp[tmpIdx + 10] & MASK64_9) << 11;
      l1 |= (tmp[tmpIdx + 11] & MASK64_9) << 2;
      l1 |= (tmp[tmpIdx + 12] >>> 7) & MASK64_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 12] & MASK64_7) << 48;
      l2 |= (tmp[tmpIdx + 13] & MASK64_9) << 39;
      l2 |= (tmp[tmpIdx + 14] & MASK64_9) << 30;
      l2 |= (tmp[tmpIdx + 15] & MASK64_9) << 21;
      l2 |= (tmp[tmpIdx + 16] & MASK64_9) << 12;
      l2 |= (tmp[tmpIdx + 17] & MASK64_9) << 3;
      l2 |= (tmp[tmpIdx + 18] >>> 6) & MASK64_3;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 18] & MASK64_6) << 49;
      l3 |= (tmp[tmpIdx + 19] & MASK64_9) << 40;
      l3 |= (tmp[tmpIdx + 20] & MASK64_9) << 31;
      l3 |= (tmp[tmpIdx + 21] & MASK64_9) << 22;
      l3 |= (tmp[tmpIdx + 22] & MASK64_9) << 13;
      l3 |= (tmp[tmpIdx + 23] & MASK64_9) << 4;
      l3 |= (tmp[tmpIdx + 24] >>> 5) & MASK64_4;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 24] & MASK64_5) << 50;
      l4 |= (tmp[tmpIdx + 25] & MASK64_9) << 41;
      l4 |= (tmp[tmpIdx + 26] & MASK64_9) << 32;
      l4 |= (tmp[tmpIdx + 27] & MASK64_9) << 23;
      l4 |= (tmp[tmpIdx + 28] & MASK64_9) << 14;
      l4 |= (tmp[tmpIdx + 29] & MASK64_9) << 5;
      l4 |= (tmp[tmpIdx + 30] >>> 4) & MASK64_5;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 30] & MASK64_4) << 51;
      l5 |= (tmp[tmpIdx + 31] & MASK64_9) << 42;
      l5 |= (tmp[tmpIdx + 32] & MASK64_9) << 33;
      l5 |= (tmp[tmpIdx + 33] & MASK64_9) << 24;
      l5 |= (tmp[tmpIdx + 34] & MASK64_9) << 15;
      l5 |= (tmp[tmpIdx + 35] & MASK64_9) << 6;
      l5 |= (tmp[tmpIdx + 36] >>> 3) & MASK64_6;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 36] & MASK64_3) << 52;
      l6 |= (tmp[tmpIdx + 37] & MASK64_9) << 43;
      l6 |= (tmp[tmpIdx + 38] & MASK64_9) << 34;
      l6 |= (tmp[tmpIdx + 39] & MASK64_9) << 25;
      l6 |= (tmp[tmpIdx + 40] & MASK64_9) << 16;
      l6 |= (tmp[tmpIdx + 41] & MASK64_9) << 7;
      l6 |= (tmp[tmpIdx + 42] >>> 2) & MASK64_7;
      longs[longsIdx + 6] = l6;
      long l7 = (tmp[tmpIdx + 42] & MASK64_2) << 53;
      l7 |= (tmp[tmpIdx + 43] & MASK64_9) << 44;
      l7 |= (tmp[tmpIdx + 44] & MASK64_9) << 35;
      l7 |= (tmp[tmpIdx + 45] & MASK64_9) << 26;
      l7 |= (tmp[tmpIdx + 46] & MASK64_9) << 17;
      l7 |= (tmp[tmpIdx + 47] & MASK64_9) << 8;
      l7 |= (tmp[tmpIdx + 48] >>> 1) & MASK64_8;
      longs[longsIdx + 7] = l7;
      long l8 = (tmp[tmpIdx + 48] & MASK64_1) << 54;
      l8 |= (tmp[tmpIdx + 49] & MASK64_9) << 45;
      l8 |= (tmp[tmpIdx + 50] & MASK64_9) << 36;
      l8 |= (tmp[tmpIdx + 51] & MASK64_9) << 27;
      l8 |= (tmp[tmpIdx + 52] & MASK64_9) << 18;
      l8 |= (tmp[tmpIdx + 53] & MASK64_9) << 9;
      l8 |= (tmp[tmpIdx + 54] & MASK64_9) << 0;
      longs[longsIdx + 8] = l8;
    }
  }

  private static void decode56(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 56);
    shiftLongs(tmp, 56, longs, 0, 8, MASK64_56);
    shiftLongs(tmp, 56, tmp, 0, 0, MASK64_8);
    for (int iter = 0, tmpIdx = 0, longsIdx = 56; iter < 8; ++iter, tmpIdx += 7, longsIdx += 1) {
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

  private static void decode57(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 57);
    shiftLongs(tmp, 57, longs, 0, 7, MASK64_57);
    for (int iter = 0, tmpIdx = 0, longsIdx = 57; iter < 1; ++iter, tmpIdx += 57, longsIdx += 7) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_7) << 50;
      l0 |= (tmp[tmpIdx + 1] & MASK64_7) << 43;
      l0 |= (tmp[tmpIdx + 2] & MASK64_7) << 36;
      l0 |= (tmp[tmpIdx + 3] & MASK64_7) << 29;
      l0 |= (tmp[tmpIdx + 4] & MASK64_7) << 22;
      l0 |= (tmp[tmpIdx + 5] & MASK64_7) << 15;
      l0 |= (tmp[tmpIdx + 6] & MASK64_7) << 8;
      l0 |= (tmp[tmpIdx + 7] & MASK64_7) << 1;
      l0 |= (tmp[tmpIdx + 8] >>> 6) & MASK64_1;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 8] & MASK64_6) << 51;
      l1 |= (tmp[tmpIdx + 9] & MASK64_7) << 44;
      l1 |= (tmp[tmpIdx + 10] & MASK64_7) << 37;
      l1 |= (tmp[tmpIdx + 11] & MASK64_7) << 30;
      l1 |= (tmp[tmpIdx + 12] & MASK64_7) << 23;
      l1 |= (tmp[tmpIdx + 13] & MASK64_7) << 16;
      l1 |= (tmp[tmpIdx + 14] & MASK64_7) << 9;
      l1 |= (tmp[tmpIdx + 15] & MASK64_7) << 2;
      l1 |= (tmp[tmpIdx + 16] >>> 5) & MASK64_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 16] & MASK64_5) << 52;
      l2 |= (tmp[tmpIdx + 17] & MASK64_7) << 45;
      l2 |= (tmp[tmpIdx + 18] & MASK64_7) << 38;
      l2 |= (tmp[tmpIdx + 19] & MASK64_7) << 31;
      l2 |= (tmp[tmpIdx + 20] & MASK64_7) << 24;
      l2 |= (tmp[tmpIdx + 21] & MASK64_7) << 17;
      l2 |= (tmp[tmpIdx + 22] & MASK64_7) << 10;
      l2 |= (tmp[tmpIdx + 23] & MASK64_7) << 3;
      l2 |= (tmp[tmpIdx + 24] >>> 4) & MASK64_3;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 24] & MASK64_4) << 53;
      l3 |= (tmp[tmpIdx + 25] & MASK64_7) << 46;
      l3 |= (tmp[tmpIdx + 26] & MASK64_7) << 39;
      l3 |= (tmp[tmpIdx + 27] & MASK64_7) << 32;
      l3 |= (tmp[tmpIdx + 28] & MASK64_7) << 25;
      l3 |= (tmp[tmpIdx + 29] & MASK64_7) << 18;
      l3 |= (tmp[tmpIdx + 30] & MASK64_7) << 11;
      l3 |= (tmp[tmpIdx + 31] & MASK64_7) << 4;
      l3 |= (tmp[tmpIdx + 32] >>> 3) & MASK64_4;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 32] & MASK64_3) << 54;
      l4 |= (tmp[tmpIdx + 33] & MASK64_7) << 47;
      l4 |= (tmp[tmpIdx + 34] & MASK64_7) << 40;
      l4 |= (tmp[tmpIdx + 35] & MASK64_7) << 33;
      l4 |= (tmp[tmpIdx + 36] & MASK64_7) << 26;
      l4 |= (tmp[tmpIdx + 37] & MASK64_7) << 19;
      l4 |= (tmp[tmpIdx + 38] & MASK64_7) << 12;
      l4 |= (tmp[tmpIdx + 39] & MASK64_7) << 5;
      l4 |= (tmp[tmpIdx + 40] >>> 2) & MASK64_5;
      longs[longsIdx + 4] = l4;
      long l5 = (tmp[tmpIdx + 40] & MASK64_2) << 55;
      l5 |= (tmp[tmpIdx + 41] & MASK64_7) << 48;
      l5 |= (tmp[tmpIdx + 42] & MASK64_7) << 41;
      l5 |= (tmp[tmpIdx + 43] & MASK64_7) << 34;
      l5 |= (tmp[tmpIdx + 44] & MASK64_7) << 27;
      l5 |= (tmp[tmpIdx + 45] & MASK64_7) << 20;
      l5 |= (tmp[tmpIdx + 46] & MASK64_7) << 13;
      l5 |= (tmp[tmpIdx + 47] & MASK64_7) << 6;
      l5 |= (tmp[tmpIdx + 48] >>> 1) & MASK64_6;
      longs[longsIdx + 5] = l5;
      long l6 = (tmp[tmpIdx + 48] & MASK64_1) << 56;
      l6 |= (tmp[tmpIdx + 49] & MASK64_7) << 49;
      l6 |= (tmp[tmpIdx + 50] & MASK64_7) << 42;
      l6 |= (tmp[tmpIdx + 51] & MASK64_7) << 35;
      l6 |= (tmp[tmpIdx + 52] & MASK64_7) << 28;
      l6 |= (tmp[tmpIdx + 53] & MASK64_7) << 21;
      l6 |= (tmp[tmpIdx + 54] & MASK64_7) << 14;
      l6 |= (tmp[tmpIdx + 55] & MASK64_7) << 7;
      l6 |= (tmp[tmpIdx + 56] & MASK64_7) << 0;
      longs[longsIdx + 6] = l6;
    }
  }

  private static void decode58(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 58);
    shiftLongs(tmp, 58, longs, 0, 6, MASK64_58);
    for (int iter = 0, tmpIdx = 0, longsIdx = 58; iter < 2; ++iter, tmpIdx += 29, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_6) << 52;
      l0 |= (tmp[tmpIdx + 1] & MASK64_6) << 46;
      l0 |= (tmp[tmpIdx + 2] & MASK64_6) << 40;
      l0 |= (tmp[tmpIdx + 3] & MASK64_6) << 34;
      l0 |= (tmp[tmpIdx + 4] & MASK64_6) << 28;
      l0 |= (tmp[tmpIdx + 5] & MASK64_6) << 22;
      l0 |= (tmp[tmpIdx + 6] & MASK64_6) << 16;
      l0 |= (tmp[tmpIdx + 7] & MASK64_6) << 10;
      l0 |= (tmp[tmpIdx + 8] & MASK64_6) << 4;
      l0 |= (tmp[tmpIdx + 9] >>> 2) & MASK64_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 9] & MASK64_2) << 56;
      l1 |= (tmp[tmpIdx + 10] & MASK64_6) << 50;
      l1 |= (tmp[tmpIdx + 11] & MASK64_6) << 44;
      l1 |= (tmp[tmpIdx + 12] & MASK64_6) << 38;
      l1 |= (tmp[tmpIdx + 13] & MASK64_6) << 32;
      l1 |= (tmp[tmpIdx + 14] & MASK64_6) << 26;
      l1 |= (tmp[tmpIdx + 15] & MASK64_6) << 20;
      l1 |= (tmp[tmpIdx + 16] & MASK64_6) << 14;
      l1 |= (tmp[tmpIdx + 17] & MASK64_6) << 8;
      l1 |= (tmp[tmpIdx + 18] & MASK64_6) << 2;
      l1 |= (tmp[tmpIdx + 19] >>> 4) & MASK64_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 19] & MASK64_4) << 54;
      l2 |= (tmp[tmpIdx + 20] & MASK64_6) << 48;
      l2 |= (tmp[tmpIdx + 21] & MASK64_6) << 42;
      l2 |= (tmp[tmpIdx + 22] & MASK64_6) << 36;
      l2 |= (tmp[tmpIdx + 23] & MASK64_6) << 30;
      l2 |= (tmp[tmpIdx + 24] & MASK64_6) << 24;
      l2 |= (tmp[tmpIdx + 25] & MASK64_6) << 18;
      l2 |= (tmp[tmpIdx + 26] & MASK64_6) << 12;
      l2 |= (tmp[tmpIdx + 27] & MASK64_6) << 6;
      l2 |= (tmp[tmpIdx + 28] & MASK64_6) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode59(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 59);
    shiftLongs(tmp, 59, longs, 0, 5, MASK64_59);
    for (int iter = 0, tmpIdx = 0, longsIdx = 59; iter < 1; ++iter, tmpIdx += 59, longsIdx += 5) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_5) << 54;
      l0 |= (tmp[tmpIdx + 1] & MASK64_5) << 49;
      l0 |= (tmp[tmpIdx + 2] & MASK64_5) << 44;
      l0 |= (tmp[tmpIdx + 3] & MASK64_5) << 39;
      l0 |= (tmp[tmpIdx + 4] & MASK64_5) << 34;
      l0 |= (tmp[tmpIdx + 5] & MASK64_5) << 29;
      l0 |= (tmp[tmpIdx + 6] & MASK64_5) << 24;
      l0 |= (tmp[tmpIdx + 7] & MASK64_5) << 19;
      l0 |= (tmp[tmpIdx + 8] & MASK64_5) << 14;
      l0 |= (tmp[tmpIdx + 9] & MASK64_5) << 9;
      l0 |= (tmp[tmpIdx + 10] & MASK64_5) << 4;
      l0 |= (tmp[tmpIdx + 11] >>> 1) & MASK64_4;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 11] & MASK64_1) << 58;
      l1 |= (tmp[tmpIdx + 12] & MASK64_5) << 53;
      l1 |= (tmp[tmpIdx + 13] & MASK64_5) << 48;
      l1 |= (tmp[tmpIdx + 14] & MASK64_5) << 43;
      l1 |= (tmp[tmpIdx + 15] & MASK64_5) << 38;
      l1 |= (tmp[tmpIdx + 16] & MASK64_5) << 33;
      l1 |= (tmp[tmpIdx + 17] & MASK64_5) << 28;
      l1 |= (tmp[tmpIdx + 18] & MASK64_5) << 23;
      l1 |= (tmp[tmpIdx + 19] & MASK64_5) << 18;
      l1 |= (tmp[tmpIdx + 20] & MASK64_5) << 13;
      l1 |= (tmp[tmpIdx + 21] & MASK64_5) << 8;
      l1 |= (tmp[tmpIdx + 22] & MASK64_5) << 3;
      l1 |= (tmp[tmpIdx + 23] >>> 2) & MASK64_3;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 23] & MASK64_2) << 57;
      l2 |= (tmp[tmpIdx + 24] & MASK64_5) << 52;
      l2 |= (tmp[tmpIdx + 25] & MASK64_5) << 47;
      l2 |= (tmp[tmpIdx + 26] & MASK64_5) << 42;
      l2 |= (tmp[tmpIdx + 27] & MASK64_5) << 37;
      l2 |= (tmp[tmpIdx + 28] & MASK64_5) << 32;
      l2 |= (tmp[tmpIdx + 29] & MASK64_5) << 27;
      l2 |= (tmp[tmpIdx + 30] & MASK64_5) << 22;
      l2 |= (tmp[tmpIdx + 31] & MASK64_5) << 17;
      l2 |= (tmp[tmpIdx + 32] & MASK64_5) << 12;
      l2 |= (tmp[tmpIdx + 33] & MASK64_5) << 7;
      l2 |= (tmp[tmpIdx + 34] & MASK64_5) << 2;
      l2 |= (tmp[tmpIdx + 35] >>> 3) & MASK64_2;
      longs[longsIdx + 2] = l2;
      long l3 = (tmp[tmpIdx + 35] & MASK64_3) << 56;
      l3 |= (tmp[tmpIdx + 36] & MASK64_5) << 51;
      l3 |= (tmp[tmpIdx + 37] & MASK64_5) << 46;
      l3 |= (tmp[tmpIdx + 38] & MASK64_5) << 41;
      l3 |= (tmp[tmpIdx + 39] & MASK64_5) << 36;
      l3 |= (tmp[tmpIdx + 40] & MASK64_5) << 31;
      l3 |= (tmp[tmpIdx + 41] & MASK64_5) << 26;
      l3 |= (tmp[tmpIdx + 42] & MASK64_5) << 21;
      l3 |= (tmp[tmpIdx + 43] & MASK64_5) << 16;
      l3 |= (tmp[tmpIdx + 44] & MASK64_5) << 11;
      l3 |= (tmp[tmpIdx + 45] & MASK64_5) << 6;
      l3 |= (tmp[tmpIdx + 46] & MASK64_5) << 1;
      l3 |= (tmp[tmpIdx + 47] >>> 4) & MASK64_1;
      longs[longsIdx + 3] = l3;
      long l4 = (tmp[tmpIdx + 47] & MASK64_4) << 55;
      l4 |= (tmp[tmpIdx + 48] & MASK64_5) << 50;
      l4 |= (tmp[tmpIdx + 49] & MASK64_5) << 45;
      l4 |= (tmp[tmpIdx + 50] & MASK64_5) << 40;
      l4 |= (tmp[tmpIdx + 51] & MASK64_5) << 35;
      l4 |= (tmp[tmpIdx + 52] & MASK64_5) << 30;
      l4 |= (tmp[tmpIdx + 53] & MASK64_5) << 25;
      l4 |= (tmp[tmpIdx + 54] & MASK64_5) << 20;
      l4 |= (tmp[tmpIdx + 55] & MASK64_5) << 15;
      l4 |= (tmp[tmpIdx + 56] & MASK64_5) << 10;
      l4 |= (tmp[tmpIdx + 57] & MASK64_5) << 5;
      l4 |= (tmp[tmpIdx + 58] & MASK64_5) << 0;
      longs[longsIdx + 4] = l4;
    }
  }

  private static void decode60(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 60);
    shiftLongs(tmp, 60, longs, 0, 4, MASK64_60);
    shiftLongs(tmp, 60, tmp, 0, 0, MASK64_4);
    for (int iter = 0, tmpIdx = 0, longsIdx = 60; iter < 4; ++iter, tmpIdx += 15, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 56;
      l0 |= tmp[tmpIdx + 1] << 52;
      l0 |= tmp[tmpIdx + 2] << 48;
      l0 |= tmp[tmpIdx + 3] << 44;
      l0 |= tmp[tmpIdx + 4] << 40;
      l0 |= tmp[tmpIdx + 5] << 36;
      l0 |= tmp[tmpIdx + 6] << 32;
      l0 |= tmp[tmpIdx + 7] << 28;
      l0 |= tmp[tmpIdx + 8] << 24;
      l0 |= tmp[tmpIdx + 9] << 20;
      l0 |= tmp[tmpIdx + 10] << 16;
      l0 |= tmp[tmpIdx + 11] << 12;
      l0 |= tmp[tmpIdx + 12] << 8;
      l0 |= tmp[tmpIdx + 13] << 4;
      l0 |= tmp[tmpIdx + 14] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode61(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 61);
    shiftLongs(tmp, 61, longs, 0, 3, MASK64_61);
    for (int iter = 0, tmpIdx = 0, longsIdx = 61; iter < 1; ++iter, tmpIdx += 61, longsIdx += 3) {
      long l0 = (tmp[tmpIdx + 0] & MASK64_3) << 58;
      l0 |= (tmp[tmpIdx + 1] & MASK64_3) << 55;
      l0 |= (tmp[tmpIdx + 2] & MASK64_3) << 52;
      l0 |= (tmp[tmpIdx + 3] & MASK64_3) << 49;
      l0 |= (tmp[tmpIdx + 4] & MASK64_3) << 46;
      l0 |= (tmp[tmpIdx + 5] & MASK64_3) << 43;
      l0 |= (tmp[tmpIdx + 6] & MASK64_3) << 40;
      l0 |= (tmp[tmpIdx + 7] & MASK64_3) << 37;
      l0 |= (tmp[tmpIdx + 8] & MASK64_3) << 34;
      l0 |= (tmp[tmpIdx + 9] & MASK64_3) << 31;
      l0 |= (tmp[tmpIdx + 10] & MASK64_3) << 28;
      l0 |= (tmp[tmpIdx + 11] & MASK64_3) << 25;
      l0 |= (tmp[tmpIdx + 12] & MASK64_3) << 22;
      l0 |= (tmp[tmpIdx + 13] & MASK64_3) << 19;
      l0 |= (tmp[tmpIdx + 14] & MASK64_3) << 16;
      l0 |= (tmp[tmpIdx + 15] & MASK64_3) << 13;
      l0 |= (tmp[tmpIdx + 16] & MASK64_3) << 10;
      l0 |= (tmp[tmpIdx + 17] & MASK64_3) << 7;
      l0 |= (tmp[tmpIdx + 18] & MASK64_3) << 4;
      l0 |= (tmp[tmpIdx + 19] & MASK64_3) << 1;
      l0 |= (tmp[tmpIdx + 20] >>> 2) & MASK64_1;
      longs[longsIdx + 0] = l0;
      long l1 = (tmp[tmpIdx + 20] & MASK64_2) << 59;
      l1 |= (tmp[tmpIdx + 21] & MASK64_3) << 56;
      l1 |= (tmp[tmpIdx + 22] & MASK64_3) << 53;
      l1 |= (tmp[tmpIdx + 23] & MASK64_3) << 50;
      l1 |= (tmp[tmpIdx + 24] & MASK64_3) << 47;
      l1 |= (tmp[tmpIdx + 25] & MASK64_3) << 44;
      l1 |= (tmp[tmpIdx + 26] & MASK64_3) << 41;
      l1 |= (tmp[tmpIdx + 27] & MASK64_3) << 38;
      l1 |= (tmp[tmpIdx + 28] & MASK64_3) << 35;
      l1 |= (tmp[tmpIdx + 29] & MASK64_3) << 32;
      l1 |= (tmp[tmpIdx + 30] & MASK64_3) << 29;
      l1 |= (tmp[tmpIdx + 31] & MASK64_3) << 26;
      l1 |= (tmp[tmpIdx + 32] & MASK64_3) << 23;
      l1 |= (tmp[tmpIdx + 33] & MASK64_3) << 20;
      l1 |= (tmp[tmpIdx + 34] & MASK64_3) << 17;
      l1 |= (tmp[tmpIdx + 35] & MASK64_3) << 14;
      l1 |= (tmp[tmpIdx + 36] & MASK64_3) << 11;
      l1 |= (tmp[tmpIdx + 37] & MASK64_3) << 8;
      l1 |= (tmp[tmpIdx + 38] & MASK64_3) << 5;
      l1 |= (tmp[tmpIdx + 39] & MASK64_3) << 2;
      l1 |= (tmp[tmpIdx + 40] >>> 1) & MASK64_2;
      longs[longsIdx + 1] = l1;
      long l2 = (tmp[tmpIdx + 40] & MASK64_1) << 60;
      l2 |= (tmp[tmpIdx + 41] & MASK64_3) << 57;
      l2 |= (tmp[tmpIdx + 42] & MASK64_3) << 54;
      l2 |= (tmp[tmpIdx + 43] & MASK64_3) << 51;
      l2 |= (tmp[tmpIdx + 44] & MASK64_3) << 48;
      l2 |= (tmp[tmpIdx + 45] & MASK64_3) << 45;
      l2 |= (tmp[tmpIdx + 46] & MASK64_3) << 42;
      l2 |= (tmp[tmpIdx + 47] & MASK64_3) << 39;
      l2 |= (tmp[tmpIdx + 48] & MASK64_3) << 36;
      l2 |= (tmp[tmpIdx + 49] & MASK64_3) << 33;
      l2 |= (tmp[tmpIdx + 50] & MASK64_3) << 30;
      l2 |= (tmp[tmpIdx + 51] & MASK64_3) << 27;
      l2 |= (tmp[tmpIdx + 52] & MASK64_3) << 24;
      l2 |= (tmp[tmpIdx + 53] & MASK64_3) << 21;
      l2 |= (tmp[tmpIdx + 54] & MASK64_3) << 18;
      l2 |= (tmp[tmpIdx + 55] & MASK64_3) << 15;
      l2 |= (tmp[tmpIdx + 56] & MASK64_3) << 12;
      l2 |= (tmp[tmpIdx + 57] & MASK64_3) << 9;
      l2 |= (tmp[tmpIdx + 58] & MASK64_3) << 6;
      l2 |= (tmp[tmpIdx + 59] & MASK64_3) << 3;
      l2 |= (tmp[tmpIdx + 60] & MASK64_3) << 0;
      longs[longsIdx + 2] = l2;
    }
  }

  private static void decode62(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 62);
    shiftLongs(tmp, 62, longs, 0, 2, MASK64_62);
    shiftLongs(tmp, 62, tmp, 0, 0, MASK64_2);
    for (int iter = 0, tmpIdx = 0, longsIdx = 62; iter < 2; ++iter, tmpIdx += 31, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 60;
      l0 |= tmp[tmpIdx + 1] << 58;
      l0 |= tmp[tmpIdx + 2] << 56;
      l0 |= tmp[tmpIdx + 3] << 54;
      l0 |= tmp[tmpIdx + 4] << 52;
      l0 |= tmp[tmpIdx + 5] << 50;
      l0 |= tmp[tmpIdx + 6] << 48;
      l0 |= tmp[tmpIdx + 7] << 46;
      l0 |= tmp[tmpIdx + 8] << 44;
      l0 |= tmp[tmpIdx + 9] << 42;
      l0 |= tmp[tmpIdx + 10] << 40;
      l0 |= tmp[tmpIdx + 11] << 38;
      l0 |= tmp[tmpIdx + 12] << 36;
      l0 |= tmp[tmpIdx + 13] << 34;
      l0 |= tmp[tmpIdx + 14] << 32;
      l0 |= tmp[tmpIdx + 15] << 30;
      l0 |= tmp[tmpIdx + 16] << 28;
      l0 |= tmp[tmpIdx + 17] << 26;
      l0 |= tmp[tmpIdx + 18] << 24;
      l0 |= tmp[tmpIdx + 19] << 22;
      l0 |= tmp[tmpIdx + 20] << 20;
      l0 |= tmp[tmpIdx + 21] << 18;
      l0 |= tmp[tmpIdx + 22] << 16;
      l0 |= tmp[tmpIdx + 23] << 14;
      l0 |= tmp[tmpIdx + 24] << 12;
      l0 |= tmp[tmpIdx + 25] << 10;
      l0 |= tmp[tmpIdx + 26] << 8;
      l0 |= tmp[tmpIdx + 27] << 6;
      l0 |= tmp[tmpIdx + 28] << 4;
      l0 |= tmp[tmpIdx + 29] << 2;
      l0 |= tmp[tmpIdx + 30] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode63(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 63);
    shiftLongs(tmp, 63, longs, 0, 1, MASK64_63);
    shiftLongs(tmp, 63, tmp, 0, 0, MASK64_1);
    for (int iter = 0, tmpIdx = 0, longsIdx = 63; iter < 1; ++iter, tmpIdx += 63, longsIdx += 1) {
      long l0 = tmp[tmpIdx + 0] << 62;
      l0 |= tmp[tmpIdx + 1] << 61;
      l0 |= tmp[tmpIdx + 2] << 60;
      l0 |= tmp[tmpIdx + 3] << 59;
      l0 |= tmp[tmpIdx + 4] << 58;
      l0 |= tmp[tmpIdx + 5] << 57;
      l0 |= tmp[tmpIdx + 6] << 56;
      l0 |= tmp[tmpIdx + 7] << 55;
      l0 |= tmp[tmpIdx + 8] << 54;
      l0 |= tmp[tmpIdx + 9] << 53;
      l0 |= tmp[tmpIdx + 10] << 52;
      l0 |= tmp[tmpIdx + 11] << 51;
      l0 |= tmp[tmpIdx + 12] << 50;
      l0 |= tmp[tmpIdx + 13] << 49;
      l0 |= tmp[tmpIdx + 14] << 48;
      l0 |= tmp[tmpIdx + 15] << 47;
      l0 |= tmp[tmpIdx + 16] << 46;
      l0 |= tmp[tmpIdx + 17] << 45;
      l0 |= tmp[tmpIdx + 18] << 44;
      l0 |= tmp[tmpIdx + 19] << 43;
      l0 |= tmp[tmpIdx + 20] << 42;
      l0 |= tmp[tmpIdx + 21] << 41;
      l0 |= tmp[tmpIdx + 22] << 40;
      l0 |= tmp[tmpIdx + 23] << 39;
      l0 |= tmp[tmpIdx + 24] << 38;
      l0 |= tmp[tmpIdx + 25] << 37;
      l0 |= tmp[tmpIdx + 26] << 36;
      l0 |= tmp[tmpIdx + 27] << 35;
      l0 |= tmp[tmpIdx + 28] << 34;
      l0 |= tmp[tmpIdx + 29] << 33;
      l0 |= tmp[tmpIdx + 30] << 32;
      l0 |= tmp[tmpIdx + 31] << 31;
      l0 |= tmp[tmpIdx + 32] << 30;
      l0 |= tmp[tmpIdx + 33] << 29;
      l0 |= tmp[tmpIdx + 34] << 28;
      l0 |= tmp[tmpIdx + 35] << 27;
      l0 |= tmp[tmpIdx + 36] << 26;
      l0 |= tmp[tmpIdx + 37] << 25;
      l0 |= tmp[tmpIdx + 38] << 24;
      l0 |= tmp[tmpIdx + 39] << 23;
      l0 |= tmp[tmpIdx + 40] << 22;
      l0 |= tmp[tmpIdx + 41] << 21;
      l0 |= tmp[tmpIdx + 42] << 20;
      l0 |= tmp[tmpIdx + 43] << 19;
      l0 |= tmp[tmpIdx + 44] << 18;
      l0 |= tmp[tmpIdx + 45] << 17;
      l0 |= tmp[tmpIdx + 46] << 16;
      l0 |= tmp[tmpIdx + 47] << 15;
      l0 |= tmp[tmpIdx + 48] << 14;
      l0 |= tmp[tmpIdx + 49] << 13;
      l0 |= tmp[tmpIdx + 50] << 12;
      l0 |= tmp[tmpIdx + 51] << 11;
      l0 |= tmp[tmpIdx + 52] << 10;
      l0 |= tmp[tmpIdx + 53] << 9;
      l0 |= tmp[tmpIdx + 54] << 8;
      l0 |= tmp[tmpIdx + 55] << 7;
      l0 |= tmp[tmpIdx + 56] << 6;
      l0 |= tmp[tmpIdx + 57] << 5;
      l0 |= tmp[tmpIdx + 58] << 4;
      l0 |= tmp[tmpIdx + 59] << 3;
      l0 |= tmp[tmpIdx + 60] << 2;
      l0 |= tmp[tmpIdx + 61] << 1;
      l0 |= tmp[tmpIdx + 62] << 0;
      longs[longsIdx + 0] = l0;
    }
  }

  private static void decode64(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 64);
  }
}
