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
package org.apache.lucene.codecs.lucene90;

import java.io.IOException;
import org.apache.lucene.store.DataInput;
import org.apache.lucene.store.DataOutput;

// Inspired from https://fulmicoton.com/posts/bitpacking/
// Encodes multiple integers in a long to get SIMD-like speedups.
// If bitsPerValue <= 8 then we pack 8 ints per long
// else if bitsPerValue <= 16 we pack 4 ints per long
// else we pack 2 ints per long
final class ForUtil {

  static final int BLOCK_SIZE = 128;
  private static final int BLOCK_SIZE_LOG2 = 7;

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

  private static long mask16(int bitsPerValue) {
    return expandMask16((1L << bitsPerValue) - 1);
  }

  private static long mask8(int bitsPerValue) {
    return expandMask8((1L << bitsPerValue) - 1);
  }

  private static void expand8(long[] arr) {
    for (int i = 0; i < 16; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 56) & 0xFFL;
      arr[16 + i] = (l >>> 48) & 0xFFL;
      arr[32 + i] = (l >>> 40) & 0xFFL;
      arr[48 + i] = (l >>> 32) & 0xFFL;
      arr[64 + i] = (l >>> 24) & 0xFFL;
      arr[80 + i] = (l >>> 16) & 0xFFL;
      arr[96 + i] = (l >>> 8) & 0xFFL;
      arr[112 + i] = l & 0xFFL;
    }
  }

  private static void expand8To32(long[] arr) {
    for (int i = 0; i < 16; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 24) & 0x000000FF000000FFL;
      arr[16 + i] = (l >>> 16) & 0x000000FF000000FFL;
      arr[32 + i] = (l >>> 8) & 0x000000FF000000FFL;
      arr[48 + i] = l & 0x000000FF000000FFL;
    }
  }

  private static void collapse8(long[] arr) {
    for (int i = 0; i < 16; ++i) {
      arr[i] =
          (arr[i] << 56)
              | (arr[16 + i] << 48)
              | (arr[32 + i] << 40)
              | (arr[48 + i] << 32)
              | (arr[64 + i] << 24)
              | (arr[80 + i] << 16)
              | (arr[96 + i] << 8)
              | arr[112 + i];
    }
  }

  private static void expand16(long[] arr) {
    for (int i = 0; i < 32; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 48) & 0xFFFFL;
      arr[32 + i] = (l >>> 32) & 0xFFFFL;
      arr[64 + i] = (l >>> 16) & 0xFFFFL;
      arr[96 + i] = l & 0xFFFFL;
    }
  }

  private static void expand16To32(long[] arr) {
    for (int i = 0; i < 32; ++i) {
      long l = arr[i];
      arr[i] = (l >>> 16) & 0x0000FFFF0000FFFFL;
      arr[32 + i] = l & 0x0000FFFF0000FFFFL;
    }
  }

  private static void collapse16(long[] arr) {
    for (int i = 0; i < 32; ++i) {
      arr[i] = (arr[i] << 48) | (arr[32 + i] << 32) | (arr[64 + i] << 16) | arr[96 + i];
    }
  }

  private static void expand32(long[] arr) {
    for (int i = 0; i < 64; ++i) {
      long l = arr[i];
      arr[i] = l >>> 32;
      arr[64 + i] = l & 0xFFFFFFFFL;
    }
  }

  private static void collapse32(long[] arr) {
    for (int i = 0; i < 64; ++i) {
      arr[i] = (arr[i] << 32) | arr[64 + i];
    }
  }

  private final long[] tmp = new long[BLOCK_SIZE / 2];

  /** Encode 128 integers from {@code longs} into {@code out}. */
  void encode(long[] longs, int bitsPerValue, DataOutput out) throws IOException {
    final int nextPrimitive;
    final int numLongs;
    if (bitsPerValue <= 8) {
      nextPrimitive = 8;
      numLongs = BLOCK_SIZE / 8;
      collapse8(longs);
    } else if (bitsPerValue <= 16) {
      nextPrimitive = 16;
      numLongs = BLOCK_SIZE / 4;
      collapse16(longs);
    } else {
      nextPrimitive = 32;
      numLongs = BLOCK_SIZE / 2;
      collapse32(longs);
    }

    final int numLongsPerShift = bitsPerValue * 2;
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
    } else {
      maskRemainingBitsPerLong = MASKS32[remainingBitsPerLong];
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
        } else {
          mask1 = MASKS32[remainingBitsPerValue];
          mask2 = MASKS32[remainingBitsPerLong - remainingBitsPerValue];
        }
        tmp[tmpIdx] |= (longs[idx++] & mask1) << (remainingBitsPerLong - remainingBitsPerValue);
        remainingBitsPerValue = bitsPerValue - remainingBitsPerLong + remainingBitsPerValue;
        tmp[tmpIdx++] |= (longs[idx] >>> remainingBitsPerValue) & mask2;
      }
    }

    for (int i = 0; i < numLongsPerShift; ++i) {
      // Java longs are big endian and we want to read little endian longs, so we need to reverse
      // bytes
      long l = tmp[i];
      out.writeLong(l);
    }
  }

  /** Number of bytes required to encode 128 integers of {@code bitsPerValue} bits per value. */
  int numBytes(int bitsPerValue) {
    return bitsPerValue << (BLOCK_SIZE_LOG2 - 3);
  }

  private static void decodeSlow(int bitsPerValue, DataInput in, long[] tmp, long[] longs)
      throws IOException {
    final int numLongs = bitsPerValue << 1;
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
    for (; longsIdx < BLOCK_SIZE / 2; ++longsIdx) {
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
      default:
        decodeSlow(bitsPerValue, in, tmp, longs);
        expand32(longs);
        break;
    }
  }

  /**
   * Decodes 128 integers into 64 {@code longs} such that each long contains two values, each
   * represented with 32 bits. Values [0..63] are encoded in the high-order bits of {@code longs}
   * [0..63], and values [64..127] are encoded in the low-order bits of {@code longs} [0..63]. This
   * representation may allow subsequent operations to be performed on two values at a time.
   */
  void decodeTo32(int bitsPerValue, DataInput in, long[] longs) throws IOException {
    switch (bitsPerValue) {
      case 1:
        decode1(in, tmp, longs);
        expand8To32(longs);
        break;
      case 2:
        decode2(in, tmp, longs);
        expand8To32(longs);
        break;
      case 3:
        decode3(in, tmp, longs);
        expand8To32(longs);
        break;
      case 4:
        decode4(in, tmp, longs);
        expand8To32(longs);
        break;
      case 5:
        decode5(in, tmp, longs);
        expand8To32(longs);
        break;
      case 6:
        decode6(in, tmp, longs);
        expand8To32(longs);
        break;
      case 7:
        decode7(in, tmp, longs);
        expand8To32(longs);
        break;
      case 8:
        decode8(in, tmp, longs);
        expand8To32(longs);
        break;
      case 9:
        decode9(in, tmp, longs);
        expand16To32(longs);
        break;
      case 10:
        decode10(in, tmp, longs);
        expand16To32(longs);
        break;
      case 11:
        decode11(in, tmp, longs);
        expand16To32(longs);
        break;
      case 12:
        decode12(in, tmp, longs);
        expand16To32(longs);
        break;
      case 13:
        decode13(in, tmp, longs);
        expand16To32(longs);
        break;
      case 14:
        decode14(in, tmp, longs);
        expand16To32(longs);
        break;
      case 15:
        decode15(in, tmp, longs);
        expand16To32(longs);
        break;
      case 16:
        decode16(in, tmp, longs);
        expand16To32(longs);
        break;
      case 17:
        decode17(in, tmp, longs);
        break;
      case 18:
        decode18(in, tmp, longs);
        break;
      case 19:
        decode19(in, tmp, longs);
        break;
      case 20:
        decode20(in, tmp, longs);
        break;
      case 21:
        decode21(in, tmp, longs);
        break;
      case 22:
        decode22(in, tmp, longs);
        break;
      case 23:
        decode23(in, tmp, longs);
        break;
      case 24:
        decode24(in, tmp, longs);
        break;
      default:
        decodeSlow(bitsPerValue, in, tmp, longs);
        break;
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

  private static void decode3(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 6);
    shiftLongs(tmp, 6, longs, 0, 5, MASK8_3);
    shiftLongs(tmp, 6, longs, 6, 2, MASK8_3);
    longs[12] = ((tmp[0] & MASK8_2) << 1) | ((tmp[1] >>> 1) & MASK8_1);
    longs[13] = ((tmp[1] & MASK8_1) << 2) | (tmp[2] & MASK8_2);
    longs[14] = ((tmp[3] & MASK8_2) << 1) | ((tmp[4] >>> 1) & MASK8_1);
    longs[15] = ((tmp[4] & MASK8_1) << 2) | (tmp[5] & MASK8_2);
  }

  private static void decode4(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 8);
    shiftLongs(tmp, 8, longs, 0, 4, MASK8_4);
    shiftLongs(tmp, 8, longs, 8, 0, MASK8_4);
  }

  private static void decode5(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 10);
    shiftLongs(tmp, 10, longs, 0, 3, MASK8_5);
    longs[10] = ((tmp[0] & MASK8_3) << 2) | ((tmp[1] >>> 1) & MASK8_2);
    longs[11] = ((tmp[1] & MASK8_1) << 4) | ((tmp[2] & MASK8_3) << 1) | ((tmp[3] >>> 2) & MASK8_1);
    longs[12] = ((tmp[3] & MASK8_2) << 3) | (tmp[4] & MASK8_3);
    longs[13] = ((tmp[5] & MASK8_3) << 2) | ((tmp[6] >>> 1) & MASK8_2);
    longs[14] = ((tmp[6] & MASK8_1) << 4) | ((tmp[7] & MASK8_3) << 1) | ((tmp[8] >>> 2) & MASK8_1);
    longs[15] = ((tmp[8] & MASK8_2) << 3) | (tmp[9] & MASK8_3);
  }

  private static void decode6(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 12);
    shiftLongs(tmp, 12, longs, 0, 2, MASK8_6);
    longs[12] = ((tmp[0] & MASK8_2) << 4) | ((tmp[1] & MASK8_2) << 2) | (tmp[2] & MASK8_2);
    longs[13] = ((tmp[3] & MASK8_2) << 4) | ((tmp[4] & MASK8_2) << 2) | (tmp[5] & MASK8_2);
    longs[14] = ((tmp[6] & MASK8_2) << 4) | ((tmp[7] & MASK8_2) << 2) | (tmp[8] & MASK8_2);
    longs[15] = ((tmp[9] & MASK8_2) << 4) | ((tmp[10] & MASK8_2) << 2) | (tmp[11] & MASK8_2);
  }

  private static void decode7(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 14);
    shiftLongs(tmp, 14, longs, 0, 1, MASK8_7);
    longs[14] = ((tmp[0] & MASK8_1) << 6) | ((tmp[1] & MASK8_1) << 5) | ((tmp[2] & MASK8_1) << 4) | ((tmp[3] & MASK8_1) << 3) | ((tmp[4] & MASK8_1) << 2) | ((tmp[5] & MASK8_1) << 1) | (tmp[6] & MASK8_1);
    longs[15] = ((tmp[7] & MASK8_1) << 6) | ((tmp[8] & MASK8_1) << 5) | ((tmp[9] & MASK8_1) << 4) | ((tmp[10] & MASK8_1) << 3) | ((tmp[11] & MASK8_1) << 2) | ((tmp[12] & MASK8_1) << 1) | (tmp[13] & MASK8_1);
  }

  private static void decode8(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 16);
  }

  private static void decode9(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 18);
    shiftLongs(tmp, 18, longs, 0, 7, MASK16_9);
    longs[18] = ((tmp[0] & MASK16_7) << 2) | ((tmp[1] >>> 5) & MASK16_2);
    longs[19] = ((tmp[1] & MASK16_5) << 4) | ((tmp[2] >>> 3) & MASK16_4);
    longs[20] = ((tmp[2] & MASK16_3) << 6) | ((tmp[3] >>> 1) & MASK16_6);
    longs[21] = ((tmp[3] & MASK16_1) << 8) | ((tmp[4] & MASK16_7) << 1) | ((tmp[5] >>> 6) & MASK16_1);
    longs[22] = ((tmp[5] & MASK16_6) << 3) | ((tmp[6] >>> 4) & MASK16_3);
    longs[23] = ((tmp[6] & MASK16_4) << 5) | ((tmp[7] >>> 2) & MASK16_5);
    longs[24] = ((tmp[7] & MASK16_2) << 7) | (tmp[8] & MASK16_7);
    longs[25] = ((tmp[9] & MASK16_7) << 2) | ((tmp[10] >>> 5) & MASK16_2);
    longs[26] = ((tmp[10] & MASK16_5) << 4) | ((tmp[11] >>> 3) & MASK16_4);
    longs[27] = ((tmp[11] & MASK16_3) << 6) | ((tmp[12] >>> 1) & MASK16_6);
    longs[28] = ((tmp[12] & MASK16_1) << 8) | ((tmp[13] & MASK16_7) << 1) | ((tmp[14] >>> 6) & MASK16_1);
    longs[29] = ((tmp[14] & MASK16_6) << 3) | ((tmp[15] >>> 4) & MASK16_3);
    longs[30] = ((tmp[15] & MASK16_4) << 5) | ((tmp[16] >>> 2) & MASK16_5);
    longs[31] = ((tmp[16] & MASK16_2) << 7) | (tmp[17] & MASK16_7);
  }

  private static void decode10(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 20);
    shiftLongs(tmp, 20, longs, 0, 6, MASK16_10);
    longs[20] = ((tmp[0] & MASK16_6) << 4) | ((tmp[1] >>> 2) & MASK16_4);
    longs[21] = ((tmp[1] & MASK16_2) << 8) | ((tmp[2] & MASK16_6) << 2) | ((tmp[3] >>> 4) & MASK16_2);
    longs[22] = ((tmp[3] & MASK16_4) << 6) | (tmp[4] & MASK16_6);
    longs[23] = ((tmp[5] & MASK16_6) << 4) | ((tmp[6] >>> 2) & MASK16_4);
    longs[24] = ((tmp[6] & MASK16_2) << 8) | ((tmp[7] & MASK16_6) << 2) | ((tmp[8] >>> 4) & MASK16_2);
    longs[25] = ((tmp[8] & MASK16_4) << 6) | (tmp[9] & MASK16_6);
    longs[26] = ((tmp[10] & MASK16_6) << 4) | ((tmp[11] >>> 2) & MASK16_4);
    longs[27] = ((tmp[11] & MASK16_2) << 8) | ((tmp[12] & MASK16_6) << 2) | ((tmp[13] >>> 4) & MASK16_2);
    longs[28] = ((tmp[13] & MASK16_4) << 6) | (tmp[14] & MASK16_6);
    longs[29] = ((tmp[15] & MASK16_6) << 4) | ((tmp[16] >>> 2) & MASK16_4);
    longs[30] = ((tmp[16] & MASK16_2) << 8) | ((tmp[17] & MASK16_6) << 2) | ((tmp[18] >>> 4) & MASK16_2);
    longs[31] = ((tmp[18] & MASK16_4) << 6) | (tmp[19] & MASK16_6);
  }

  private static void decode11(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 22);
    shiftLongs(tmp, 22, longs, 0, 5, MASK16_11);
    longs[22] = ((tmp[0] & MASK16_5) << 6) | ((tmp[1] & MASK16_5) << 1) | ((tmp[2] >>> 4) & MASK16_1);
    longs[23] = ((tmp[2] & MASK16_4) << 7) | ((tmp[3] & MASK16_5) << 2) | ((tmp[4] >>> 3) & MASK16_2);
    longs[24] = ((tmp[4] & MASK16_3) << 8) | ((tmp[5] & MASK16_5) << 3) | ((tmp[6] >>> 2) & MASK16_3);
    longs[25] = ((tmp[6] & MASK16_2) << 9) | ((tmp[7] & MASK16_5) << 4) | ((tmp[8] >>> 1) & MASK16_4);
    longs[26] = ((tmp[8] & MASK16_1) << 10) | ((tmp[9] & MASK16_5) << 5) | (tmp[10] & MASK16_5);
    longs[27] = ((tmp[11] & MASK16_5) << 6) | ((tmp[12] & MASK16_5) << 1) | ((tmp[13] >>> 4) & MASK16_1);
    longs[28] = ((tmp[13] & MASK16_4) << 7) | ((tmp[14] & MASK16_5) << 2) | ((tmp[15] >>> 3) & MASK16_2);
    longs[29] = ((tmp[15] & MASK16_3) << 8) | ((tmp[16] & MASK16_5) << 3) | ((tmp[17] >>> 2) & MASK16_3);
    longs[30] = ((tmp[17] & MASK16_2) << 9) | ((tmp[18] & MASK16_5) << 4) | ((tmp[19] >>> 1) & MASK16_4);
    longs[31] = ((tmp[19] & MASK16_1) << 10) | ((tmp[20] & MASK16_5) << 5) | (tmp[21] & MASK16_5);
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

  private static void decode13(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 26);
    shiftLongs(tmp, 26, longs, 0, 3, MASK16_13);
    longs[26] = ((tmp[0] & MASK16_3) << 10) | ((tmp[1] & MASK16_3) << 7) | ((tmp[2] & MASK16_3) << 4) | ((tmp[3] & MASK16_3) << 1) | ((tmp[4] >>> 2) & MASK16_1);
    longs[27] = ((tmp[4] & MASK16_2) << 11) | ((tmp[5] & MASK16_3) << 8) | ((tmp[6] & MASK16_3) << 5) | ((tmp[7] & MASK16_3) << 2) | ((tmp[8] >>> 1) & MASK16_2);
    longs[28] = ((tmp[8] & MASK16_1) << 12) | ((tmp[9] & MASK16_3) << 9) | ((tmp[10] & MASK16_3) << 6) | ((tmp[11] & MASK16_3) << 3) | (tmp[12] & MASK16_3);
    longs[29] = ((tmp[13] & MASK16_3) << 10) | ((tmp[14] & MASK16_3) << 7) | ((tmp[15] & MASK16_3) << 4) | ((tmp[16] & MASK16_3) << 1) | ((tmp[17] >>> 2) & MASK16_1);
    longs[30] = ((tmp[17] & MASK16_2) << 11) | ((tmp[18] & MASK16_3) << 8) | ((tmp[19] & MASK16_3) << 5) | ((tmp[20] & MASK16_3) << 2) | ((tmp[21] >>> 1) & MASK16_2);
    longs[31] = ((tmp[21] & MASK16_1) << 12) | ((tmp[22] & MASK16_3) << 9) | ((tmp[23] & MASK16_3) << 6) | ((tmp[24] & MASK16_3) << 3) | (tmp[25] & MASK16_3);
  }

  private static void decode14(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 28);
    shiftLongs(tmp, 28, longs, 0, 2, MASK16_14);
    longs[28] = ((tmp[0] & MASK16_2) << 12) | ((tmp[1] & MASK16_2) << 10) | ((tmp[2] & MASK16_2) << 8) | ((tmp[3] & MASK16_2) << 6) | ((tmp[4] & MASK16_2) << 4) | ((tmp[5] & MASK16_2) << 2) | (tmp[6] & MASK16_2);
    longs[29] = ((tmp[7] & MASK16_2) << 12) | ((tmp[8] & MASK16_2) << 10) | ((tmp[9] & MASK16_2) << 8) | ((tmp[10] & MASK16_2) << 6) | ((tmp[11] & MASK16_2) << 4) | ((tmp[12] & MASK16_2) << 2) | (tmp[13] & MASK16_2);
    longs[30] = ((tmp[14] & MASK16_2) << 12) | ((tmp[15] & MASK16_2) << 10) | ((tmp[16] & MASK16_2) << 8) | ((tmp[17] & MASK16_2) << 6) | ((tmp[18] & MASK16_2) << 4) | ((tmp[19] & MASK16_2) << 2) | (tmp[20] & MASK16_2);
    longs[31] = ((tmp[21] & MASK16_2) << 12) | ((tmp[22] & MASK16_2) << 10) | ((tmp[23] & MASK16_2) << 8) | ((tmp[24] & MASK16_2) << 6) | ((tmp[25] & MASK16_2) << 4) | ((tmp[26] & MASK16_2) << 2) | (tmp[27] & MASK16_2);
  }

  private static void decode15(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 30);
    shiftLongs(tmp, 30, longs, 0, 1, MASK16_15);
    longs[30] = ((tmp[0] & MASK16_1) << 14) | ((tmp[1] & MASK16_1) << 13) | ((tmp[2] & MASK16_1) << 12) | ((tmp[3] & MASK16_1) << 11) | ((tmp[4] & MASK16_1) << 10) | ((tmp[5] & MASK16_1) << 9) | ((tmp[6] & MASK16_1) << 8) | ((tmp[7] & MASK16_1) << 7) | ((tmp[8] & MASK16_1) << 6) | ((tmp[9] & MASK16_1) << 5) | ((tmp[10] & MASK16_1) << 4) | ((tmp[11] & MASK16_1) << 3) | ((tmp[12] & MASK16_1) << 2) | ((tmp[13] & MASK16_1) << 1) | (tmp[14] & MASK16_1);
    longs[31] = ((tmp[15] & MASK16_1) << 14) | ((tmp[16] & MASK16_1) << 13) | ((tmp[17] & MASK16_1) << 12) | ((tmp[18] & MASK16_1) << 11) | ((tmp[19] & MASK16_1) << 10) | ((tmp[20] & MASK16_1) << 9) | ((tmp[21] & MASK16_1) << 8) | ((tmp[22] & MASK16_1) << 7) | ((tmp[23] & MASK16_1) << 6) | ((tmp[24] & MASK16_1) << 5) | ((tmp[25] & MASK16_1) << 4) | ((tmp[26] & MASK16_1) << 3) | ((tmp[27] & MASK16_1) << 2) | ((tmp[28] & MASK16_1) << 1) | (tmp[29] & MASK16_1);
  }

  private static void decode16(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(longs, 0, 32);
  }

  private static void decode17(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 34);
    shiftLongs(tmp, 34, longs, 0, 15, MASK32_17);
    longs[34] = ((tmp[0] & MASK32_15) << 2) | ((tmp[1] >>> 13) & MASK32_2);
    longs[35] = ((tmp[1] & MASK32_13) << 4) | ((tmp[2] >>> 11) & MASK32_4);
    longs[36] = ((tmp[2] & MASK32_11) << 6) | ((tmp[3] >>> 9) & MASK32_6);
    longs[37] = ((tmp[3] & MASK32_9) << 8) | ((tmp[4] >>> 7) & MASK32_8);
    longs[38] = ((tmp[4] & MASK32_7) << 10) | ((tmp[5] >>> 5) & MASK32_10);
    longs[39] = ((tmp[5] & MASK32_5) << 12) | ((tmp[6] >>> 3) & MASK32_12);
    longs[40] = ((tmp[6] & MASK32_3) << 14) | ((tmp[7] >>> 1) & MASK32_14);
    longs[41] = ((tmp[7] & MASK32_1) << 16) | ((tmp[8] & MASK32_15) << 1) | ((tmp[9] >>> 14) & MASK32_1);
    longs[42] = ((tmp[9] & MASK32_14) << 3) | ((tmp[10] >>> 12) & MASK32_3);
    longs[43] = ((tmp[10] & MASK32_12) << 5) | ((tmp[11] >>> 10) & MASK32_5);
    longs[44] = ((tmp[11] & MASK32_10) << 7) | ((tmp[12] >>> 8) & MASK32_7);
    longs[45] = ((tmp[12] & MASK32_8) << 9) | ((tmp[13] >>> 6) & MASK32_9);
    longs[46] = ((tmp[13] & MASK32_6) << 11) | ((tmp[14] >>> 4) & MASK32_11);
    longs[47] = ((tmp[14] & MASK32_4) << 13) | ((tmp[15] >>> 2) & MASK32_13);
    longs[48] = ((tmp[15] & MASK32_2) << 15) | (tmp[16] & MASK32_15);
    longs[49] = ((tmp[17] & MASK32_15) << 2) | ((tmp[18] >>> 13) & MASK32_2);
    longs[50] = ((tmp[18] & MASK32_13) << 4) | ((tmp[19] >>> 11) & MASK32_4);
    longs[51] = ((tmp[19] & MASK32_11) << 6) | ((tmp[20] >>> 9) & MASK32_6);
    longs[52] = ((tmp[20] & MASK32_9) << 8) | ((tmp[21] >>> 7) & MASK32_8);
    longs[53] = ((tmp[21] & MASK32_7) << 10) | ((tmp[22] >>> 5) & MASK32_10);
    longs[54] = ((tmp[22] & MASK32_5) << 12) | ((tmp[23] >>> 3) & MASK32_12);
    longs[55] = ((tmp[23] & MASK32_3) << 14) | ((tmp[24] >>> 1) & MASK32_14);
    longs[56] = ((tmp[24] & MASK32_1) << 16) | ((tmp[25] & MASK32_15) << 1) | ((tmp[26] >>> 14) & MASK32_1);
    longs[57] = ((tmp[26] & MASK32_14) << 3) | ((tmp[27] >>> 12) & MASK32_3);
    longs[58] = ((tmp[27] & MASK32_12) << 5) | ((tmp[28] >>> 10) & MASK32_5);
    longs[59] = ((tmp[28] & MASK32_10) << 7) | ((tmp[29] >>> 8) & MASK32_7);
    longs[60] = ((tmp[29] & MASK32_8) << 9) | ((tmp[30] >>> 6) & MASK32_9);
    longs[61] = ((tmp[30] & MASK32_6) << 11) | ((tmp[31] >>> 4) & MASK32_11);
    longs[62] = ((tmp[31] & MASK32_4) << 13) | ((tmp[32] >>> 2) & MASK32_13);
    longs[63] = ((tmp[32] & MASK32_2) << 15) | (tmp[33] & MASK32_15);
  }

  private static void decode18(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 36);
    shiftLongs(tmp, 36, longs, 0, 14, MASK32_18);
    longs[36] = ((tmp[0] & MASK32_14) << 4) | ((tmp[1] >>> 10) & MASK32_4);
    longs[37] = ((tmp[1] & MASK32_10) << 8) | ((tmp[2] >>> 6) & MASK32_8);
    longs[38] = ((tmp[2] & MASK32_6) << 12) | ((tmp[3] >>> 2) & MASK32_12);
    longs[39] = ((tmp[3] & MASK32_2) << 16) | ((tmp[4] & MASK32_14) << 2) | ((tmp[5] >>> 12) & MASK32_2);
    longs[40] = ((tmp[5] & MASK32_12) << 6) | ((tmp[6] >>> 8) & MASK32_6);
    longs[41] = ((tmp[6] & MASK32_8) << 10) | ((tmp[7] >>> 4) & MASK32_10);
    longs[42] = ((tmp[7] & MASK32_4) << 14) | (tmp[8] & MASK32_14);
    longs[43] = ((tmp[9] & MASK32_14) << 4) | ((tmp[10] >>> 10) & MASK32_4);
    longs[44] = ((tmp[10] & MASK32_10) << 8) | ((tmp[11] >>> 6) & MASK32_8);
    longs[45] = ((tmp[11] & MASK32_6) << 12) | ((tmp[12] >>> 2) & MASK32_12);
    longs[46] = ((tmp[12] & MASK32_2) << 16) | ((tmp[13] & MASK32_14) << 2) | ((tmp[14] >>> 12) & MASK32_2);
    longs[47] = ((tmp[14] & MASK32_12) << 6) | ((tmp[15] >>> 8) & MASK32_6);
    longs[48] = ((tmp[15] & MASK32_8) << 10) | ((tmp[16] >>> 4) & MASK32_10);
    longs[49] = ((tmp[16] & MASK32_4) << 14) | (tmp[17] & MASK32_14);
    longs[50] = ((tmp[18] & MASK32_14) << 4) | ((tmp[19] >>> 10) & MASK32_4);
    longs[51] = ((tmp[19] & MASK32_10) << 8) | ((tmp[20] >>> 6) & MASK32_8);
    longs[52] = ((tmp[20] & MASK32_6) << 12) | ((tmp[21] >>> 2) & MASK32_12);
    longs[53] = ((tmp[21] & MASK32_2) << 16) | ((tmp[22] & MASK32_14) << 2) | ((tmp[23] >>> 12) & MASK32_2);
    longs[54] = ((tmp[23] & MASK32_12) << 6) | ((tmp[24] >>> 8) & MASK32_6);
    longs[55] = ((tmp[24] & MASK32_8) << 10) | ((tmp[25] >>> 4) & MASK32_10);
    longs[56] = ((tmp[25] & MASK32_4) << 14) | (tmp[26] & MASK32_14);
    longs[57] = ((tmp[27] & MASK32_14) << 4) | ((tmp[28] >>> 10) & MASK32_4);
    longs[58] = ((tmp[28] & MASK32_10) << 8) | ((tmp[29] >>> 6) & MASK32_8);
    longs[59] = ((tmp[29] & MASK32_6) << 12) | ((tmp[30] >>> 2) & MASK32_12);
    longs[60] = ((tmp[30] & MASK32_2) << 16) | ((tmp[31] & MASK32_14) << 2) | ((tmp[32] >>> 12) & MASK32_2);
    longs[61] = ((tmp[32] & MASK32_12) << 6) | ((tmp[33] >>> 8) & MASK32_6);
    longs[62] = ((tmp[33] & MASK32_8) << 10) | ((tmp[34] >>> 4) & MASK32_10);
    longs[63] = ((tmp[34] & MASK32_4) << 14) | (tmp[35] & MASK32_14);
  }

  private static void decode19(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 38);
    shiftLongs(tmp, 38, longs, 0, 13, MASK32_19);
    longs[38] = ((tmp[0] & MASK32_13) << 6) | ((tmp[1] >>> 7) & MASK32_6);
    longs[39] = ((tmp[1] & MASK32_7) << 12) | ((tmp[2] >>> 1) & MASK32_12);
    longs[40] = ((tmp[2] & MASK32_1) << 18) | ((tmp[3] & MASK32_13) << 5) | ((tmp[4] >>> 8) & MASK32_5);
    longs[41] = ((tmp[4] & MASK32_8) << 11) | ((tmp[5] >>> 2) & MASK32_11);
    longs[42] = ((tmp[5] & MASK32_2) << 17) | ((tmp[6] & MASK32_13) << 4) | ((tmp[7] >>> 9) & MASK32_4);
    longs[43] = ((tmp[7] & MASK32_9) << 10) | ((tmp[8] >>> 3) & MASK32_10);
    longs[44] = ((tmp[8] & MASK32_3) << 16) | ((tmp[9] & MASK32_13) << 3) | ((tmp[10] >>> 10) & MASK32_3);
    longs[45] = ((tmp[10] & MASK32_10) << 9) | ((tmp[11] >>> 4) & MASK32_9);
    longs[46] = ((tmp[11] & MASK32_4) << 15) | ((tmp[12] & MASK32_13) << 2) | ((tmp[13] >>> 11) & MASK32_2);
    longs[47] = ((tmp[13] & MASK32_11) << 8) | ((tmp[14] >>> 5) & MASK32_8);
    longs[48] = ((tmp[14] & MASK32_5) << 14) | ((tmp[15] & MASK32_13) << 1) | ((tmp[16] >>> 12) & MASK32_1);
    longs[49] = ((tmp[16] & MASK32_12) << 7) | ((tmp[17] >>> 6) & MASK32_7);
    longs[50] = ((tmp[17] & MASK32_6) << 13) | (tmp[18] & MASK32_13);
    longs[51] = ((tmp[19] & MASK32_13) << 6) | ((tmp[20] >>> 7) & MASK32_6);
    longs[52] = ((tmp[20] & MASK32_7) << 12) | ((tmp[21] >>> 1) & MASK32_12);
    longs[53] = ((tmp[21] & MASK32_1) << 18) | ((tmp[22] & MASK32_13) << 5) | ((tmp[23] >>> 8) & MASK32_5);
    longs[54] = ((tmp[23] & MASK32_8) << 11) | ((tmp[24] >>> 2) & MASK32_11);
    longs[55] = ((tmp[24] & MASK32_2) << 17) | ((tmp[25] & MASK32_13) << 4) | ((tmp[26] >>> 9) & MASK32_4);
    longs[56] = ((tmp[26] & MASK32_9) << 10) | ((tmp[27] >>> 3) & MASK32_10);
    longs[57] = ((tmp[27] & MASK32_3) << 16) | ((tmp[28] & MASK32_13) << 3) | ((tmp[29] >>> 10) & MASK32_3);
    longs[58] = ((tmp[29] & MASK32_10) << 9) | ((tmp[30] >>> 4) & MASK32_9);
    longs[59] = ((tmp[30] & MASK32_4) << 15) | ((tmp[31] & MASK32_13) << 2) | ((tmp[32] >>> 11) & MASK32_2);
    longs[60] = ((tmp[32] & MASK32_11) << 8) | ((tmp[33] >>> 5) & MASK32_8);
    longs[61] = ((tmp[33] & MASK32_5) << 14) | ((tmp[34] & MASK32_13) << 1) | ((tmp[35] >>> 12) & MASK32_1);
    longs[62] = ((tmp[35] & MASK32_12) << 7) | ((tmp[36] >>> 6) & MASK32_7);
    longs[63] = ((tmp[36] & MASK32_6) << 13) | (tmp[37] & MASK32_13);
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

  private static void decode21(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 42);
    shiftLongs(tmp, 42, longs, 0, 11, MASK32_21);
    longs[42] = ((tmp[0] & MASK32_11) << 10) | ((tmp[1] >>> 1) & MASK32_10);
    longs[43] = ((tmp[1] & MASK32_1) << 20) | ((tmp[2] & MASK32_11) << 9) | ((tmp[3] >>> 2) & MASK32_9);
    longs[44] = ((tmp[3] & MASK32_2) << 19) | ((tmp[4] & MASK32_11) << 8) | ((tmp[5] >>> 3) & MASK32_8);
    longs[45] = ((tmp[5] & MASK32_3) << 18) | ((tmp[6] & MASK32_11) << 7) | ((tmp[7] >>> 4) & MASK32_7);
    longs[46] = ((tmp[7] & MASK32_4) << 17) | ((tmp[8] & MASK32_11) << 6) | ((tmp[9] >>> 5) & MASK32_6);
    longs[47] = ((tmp[9] & MASK32_5) << 16) | ((tmp[10] & MASK32_11) << 5) | ((tmp[11] >>> 6) & MASK32_5);
    longs[48] = ((tmp[11] & MASK32_6) << 15) | ((tmp[12] & MASK32_11) << 4) | ((tmp[13] >>> 7) & MASK32_4);
    longs[49] = ((tmp[13] & MASK32_7) << 14) | ((tmp[14] & MASK32_11) << 3) | ((tmp[15] >>> 8) & MASK32_3);
    longs[50] = ((tmp[15] & MASK32_8) << 13) | ((tmp[16] & MASK32_11) << 2) | ((tmp[17] >>> 9) & MASK32_2);
    longs[51] = ((tmp[17] & MASK32_9) << 12) | ((tmp[18] & MASK32_11) << 1) | ((tmp[19] >>> 10) & MASK32_1);
    longs[52] = ((tmp[19] & MASK32_10) << 11) | (tmp[20] & MASK32_11);
    longs[53] = ((tmp[21] & MASK32_11) << 10) | ((tmp[22] >>> 1) & MASK32_10);
    longs[54] = ((tmp[22] & MASK32_1) << 20) | ((tmp[23] & MASK32_11) << 9) | ((tmp[24] >>> 2) & MASK32_9);
    longs[55] = ((tmp[24] & MASK32_2) << 19) | ((tmp[25] & MASK32_11) << 8) | ((tmp[26] >>> 3) & MASK32_8);
    longs[56] = ((tmp[26] & MASK32_3) << 18) | ((tmp[27] & MASK32_11) << 7) | ((tmp[28] >>> 4) & MASK32_7);
    longs[57] = ((tmp[28] & MASK32_4) << 17) | ((tmp[29] & MASK32_11) << 6) | ((tmp[30] >>> 5) & MASK32_6);
    longs[58] = ((tmp[30] & MASK32_5) << 16) | ((tmp[31] & MASK32_11) << 5) | ((tmp[32] >>> 6) & MASK32_5);
    longs[59] = ((tmp[32] & MASK32_6) << 15) | ((tmp[33] & MASK32_11) << 4) | ((tmp[34] >>> 7) & MASK32_4);
    longs[60] = ((tmp[34] & MASK32_7) << 14) | ((tmp[35] & MASK32_11) << 3) | ((tmp[36] >>> 8) & MASK32_3);
    longs[61] = ((tmp[36] & MASK32_8) << 13) | ((tmp[37] & MASK32_11) << 2) | ((tmp[38] >>> 9) & MASK32_2);
    longs[62] = ((tmp[38] & MASK32_9) << 12) | ((tmp[39] & MASK32_11) << 1) | ((tmp[40] >>> 10) & MASK32_1);
    longs[63] = ((tmp[40] & MASK32_10) << 11) | (tmp[41] & MASK32_11);
  }

  private static void decode22(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 44);
    shiftLongs(tmp, 44, longs, 0, 10, MASK32_22);
    longs[44] = ((tmp[0] & MASK32_10) << 12) | ((tmp[1] & MASK32_10) << 2) | ((tmp[2] >>> 8) & MASK32_2);
    longs[45] = ((tmp[2] & MASK32_8) << 14) | ((tmp[3] & MASK32_10) << 4) | ((tmp[4] >>> 6) & MASK32_4);
    longs[46] = ((tmp[4] & MASK32_6) << 16) | ((tmp[5] & MASK32_10) << 6) | ((tmp[6] >>> 4) & MASK32_6);
    longs[47] = ((tmp[6] & MASK32_4) << 18) | ((tmp[7] & MASK32_10) << 8) | ((tmp[8] >>> 2) & MASK32_8);
    longs[48] = ((tmp[8] & MASK32_2) << 20) | ((tmp[9] & MASK32_10) << 10) | (tmp[10] & MASK32_10);
    longs[49] = ((tmp[11] & MASK32_10) << 12) | ((tmp[12] & MASK32_10) << 2) | ((tmp[13] >>> 8) & MASK32_2);
    longs[50] = ((tmp[13] & MASK32_8) << 14) | ((tmp[14] & MASK32_10) << 4) | ((tmp[15] >>> 6) & MASK32_4);
    longs[51] = ((tmp[15] & MASK32_6) << 16) | ((tmp[16] & MASK32_10) << 6) | ((tmp[17] >>> 4) & MASK32_6);
    longs[52] = ((tmp[17] & MASK32_4) << 18) | ((tmp[18] & MASK32_10) << 8) | ((tmp[19] >>> 2) & MASK32_8);
    longs[53] = ((tmp[19] & MASK32_2) << 20) | ((tmp[20] & MASK32_10) << 10) | (tmp[21] & MASK32_10);
    longs[54] = ((tmp[22] & MASK32_10) << 12) | ((tmp[23] & MASK32_10) << 2) | ((tmp[24] >>> 8) & MASK32_2);
    longs[55] = ((tmp[24] & MASK32_8) << 14) | ((tmp[25] & MASK32_10) << 4) | ((tmp[26] >>> 6) & MASK32_4);
    longs[56] = ((tmp[26] & MASK32_6) << 16) | ((tmp[27] & MASK32_10) << 6) | ((tmp[28] >>> 4) & MASK32_6);
    longs[57] = ((tmp[28] & MASK32_4) << 18) | ((tmp[29] & MASK32_10) << 8) | ((tmp[30] >>> 2) & MASK32_8);
    longs[58] = ((tmp[30] & MASK32_2) << 20) | ((tmp[31] & MASK32_10) << 10) | (tmp[32] & MASK32_10);
    longs[59] = ((tmp[33] & MASK32_10) << 12) | ((tmp[34] & MASK32_10) << 2) | ((tmp[35] >>> 8) & MASK32_2);
    longs[60] = ((tmp[35] & MASK32_8) << 14) | ((tmp[36] & MASK32_10) << 4) | ((tmp[37] >>> 6) & MASK32_4);
    longs[61] = ((tmp[37] & MASK32_6) << 16) | ((tmp[38] & MASK32_10) << 6) | ((tmp[39] >>> 4) & MASK32_6);
    longs[62] = ((tmp[39] & MASK32_4) << 18) | ((tmp[40] & MASK32_10) << 8) | ((tmp[41] >>> 2) & MASK32_8);
    longs[63] = ((tmp[41] & MASK32_2) << 20) | ((tmp[42] & MASK32_10) << 10) | (tmp[43] & MASK32_10);
  }

  private static void decode23(DataInput in, long[] tmp, long[] longs) throws IOException {
    in.readLongs(tmp, 0, 46);
    shiftLongs(tmp, 46, longs, 0, 9, MASK32_23);
    longs[46] = ((tmp[0] & MASK32_9) << 14) | ((tmp[1] & MASK32_9) << 5) | ((tmp[2] >>> 4) & MASK32_5);
    longs[47] = ((tmp[2] & MASK32_4) << 19) | ((tmp[3] & MASK32_9) << 10) | ((tmp[4] & MASK32_9) << 1) | ((tmp[5] >>> 8) & MASK32_1);
    longs[48] = ((tmp[5] & MASK32_8) << 15) | ((tmp[6] & MASK32_9) << 6) | ((tmp[7] >>> 3) & MASK32_6);
    longs[49] = ((tmp[7] & MASK32_3) << 20) | ((tmp[8] & MASK32_9) << 11) | ((tmp[9] & MASK32_9) << 2) | ((tmp[10] >>> 7) & MASK32_2);
    longs[50] = ((tmp[10] & MASK32_7) << 16) | ((tmp[11] & MASK32_9) << 7) | ((tmp[12] >>> 2) & MASK32_7);
    longs[51] = ((tmp[12] & MASK32_2) << 21) | ((tmp[13] & MASK32_9) << 12) | ((tmp[14] & MASK32_9) << 3) | ((tmp[15] >>> 6) & MASK32_3);
    longs[52] = ((tmp[15] & MASK32_6) << 17) | ((tmp[16] & MASK32_9) << 8) | ((tmp[17] >>> 1) & MASK32_8);
    longs[53] = ((tmp[17] & MASK32_1) << 22) | ((tmp[18] & MASK32_9) << 13) | ((tmp[19] & MASK32_9) << 4) | ((tmp[20] >>> 5) & MASK32_4);
    longs[54] = ((tmp[20] & MASK32_5) << 18) | ((tmp[21] & MASK32_9) << 9) | (tmp[22] & MASK32_9);
    longs[55] = ((tmp[23] & MASK32_9) << 14) | ((tmp[24] & MASK32_9) << 5) | ((tmp[25] >>> 4) & MASK32_5);
    longs[56] = ((tmp[25] & MASK32_4) << 19) | ((tmp[26] & MASK32_9) << 10) | ((tmp[27] & MASK32_9) << 1) | ((tmp[28] >>> 8) & MASK32_1);
    longs[57] = ((tmp[28] & MASK32_8) << 15) | ((tmp[29] & MASK32_9) << 6) | ((tmp[30] >>> 3) & MASK32_6);
    longs[58] = ((tmp[30] & MASK32_3) << 20) | ((tmp[31] & MASK32_9) << 11) | ((tmp[32] & MASK32_9) << 2) | ((tmp[33] >>> 7) & MASK32_2);
    longs[59] = ((tmp[33] & MASK32_7) << 16) | ((tmp[34] & MASK32_9) << 7) | ((tmp[35] >>> 2) & MASK32_7);
    longs[60] = ((tmp[35] & MASK32_2) << 21) | ((tmp[36] & MASK32_9) << 12) | ((tmp[37] & MASK32_9) << 3) | ((tmp[38] >>> 6) & MASK32_3);
    longs[61] = ((tmp[38] & MASK32_6) << 17) | ((tmp[39] & MASK32_9) << 8) | ((tmp[40] >>> 1) & MASK32_8);
    longs[62] = ((tmp[40] & MASK32_1) << 22) | ((tmp[41] & MASK32_9) << 13) | ((tmp[42] & MASK32_9) << 4) | ((tmp[43] >>> 5) & MASK32_4);
    longs[63] = ((tmp[43] & MASK32_5) << 18) | ((tmp[44] & MASK32_9) << 9) | (tmp[45] & MASK32_9);
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
}
