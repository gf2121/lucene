#! /usr/bin/env python

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from fractions import gcd
import math


"""Code generation for ForUtil.java"""

BLOCK_SIZE = 128
BITS_PER_VALUES = [1, 2, 4, 8, 12, 16, 20, 24, 28, 32, 40, 48, 56, 64]
OUTPUT_FILE = "ForUtil.java"
PRIMITIVE_SIZE = [8, 16, 32, 64]
USING_MASKS = {8:[], 16:[], 32:[], 64:[]}
HEADER = """// This file has been automatically generated, DO NOT EDIT

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

  static final int BLOCK_SIZE = """ + str(BLOCK_SIZE) + """;
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

"""


def writeRemainder(bpv, next_primitive, remaining_bits_per_long, o, num_values, f):
    iteration = 1
    num_longs = bpv * num_values / remaining_bits_per_long
    while num_longs % 2 == 0 and num_values % 2 == 0:
        num_longs /= 2
        num_values /= 2
        iteration *= 2
    iter = 0
    while iter < iteration:
        remaining_bits = 0
        tmp_idx = iter * num_longs
        for i in range(0, num_values):
            f.write('    longs[%d] =' % (iter * num_values + i + o))
            b = bpv
            if remaining_bits == 0:
                b -= remaining_bits_per_long
                f.write(' ((tmp[%d] & MASK%d_%d) << %d)' %(tmp_idx, next_primitive, remaining_bits_per_long, b))
                use_mask(next_primitive, remaining_bits_per_long)
            else:
                b -= remaining_bits
                f.write(' ((tmp[%d] & MASK%d_%d) << %d)' %(tmp_idx, next_primitive, remaining_bits, b))
                use_mask(next_primitive, remaining_bits)
            tmp_idx += 1
            while b >= remaining_bits_per_long:
                b -= remaining_bits_per_long
                if b != 0:
                    f.write(' | ((tmp[%d] & MASK%d_%d) << %d)' %(tmp_idx, next_primitive, remaining_bits_per_long, b))
                    use_mask(next_primitive, remaining_bits_per_long)
                else:
                    f.write(' | (tmp[%d] & MASK%d_%d)' %(tmp_idx, next_primitive, remaining_bits_per_long))
                    use_mask(next_primitive, remaining_bits_per_long)
                tmp_idx += 1
            if b > 0:
                f.write(' | ((tmp[%d] >>> %d) & MASK%d_%d)' %(tmp_idx, remaining_bits_per_long-b, next_primitive, b))
                use_mask(next_primitive, b)
                remaining_bits = remaining_bits_per_long-b
            f.write(';\n')
        iter += 1

def use_mask(next_primitive, bpv):
    USING_MASKS[next_primitive].append(bpv)

def writeDecode(bpv, f):
    next_primitive = 64
    if bpv <= 8:
        next_primitive = 8
    elif bpv <= 16:
        next_primitive = 16
    elif bpv <= 32:
        next_primitive = 32
    num_values_per_long = 64 / next_primitive
    if bpv == next_primitive:
        f.write('  private static void decode%d(DataInput in, long[] longs) throws IOException {\n' %bpv)
        f.write('    in.readLongs(longs, 0, %d);\n' %(bpv*(BLOCK_SIZE / 64)))
    else:
        f.write('  private static void decode%d(DataInput in, long[] tmp, long[] longs) throws IOException {\n' %bpv)
        f.write('    in.readLongs(tmp, 0, %d);\n' %(bpv*(BLOCK_SIZE / 64)))
        shift = next_primitive - bpv
        o = 0
        while shift >= 0:
            f.write('    shiftLongs(tmp, %d, longs, %d, %d, MASK%d_%d);\n' %(bpv*(BLOCK_SIZE / 64), o, shift, next_primitive, bpv))
            use_mask(next_primitive, bpv)
            o += bpv*(BLOCK_SIZE / 64)
            shift -= bpv
        if shift + bpv > 0:
            writeRemainder(bpv, next_primitive, shift + bpv, o, BLOCK_SIZE/num_values_per_long - o, f)
    f.write('  }\n')

if __name__ == '__main__':
    f = open(OUTPUT_FILE, 'w')
    f.write(HEADER)
    f.write("""  interface Decoder {
    void decode(DataInput in, long[] longs) throws IOException;
  }\n""")
    for bpv in BITS_PER_VALUES:
        next_primitive = 64
        if bpv <= 8:
            next_primitive = 8
        elif bpv <= 16:
            next_primitive = 16
        elif bpv <= 32:
            next_primitive = 32
        f.write('\n')
        if bpv == next_primitive:
            f.write('  static class Decoder%d implements Decoder {\n' % bpv)
        else:
            f.write('  class Decoder%d implements Decoder {\n' % bpv)
        f.write('    @Override\n')
        f.write('    public void decode(DataInput in, long[] longs) throws IOException {\n')
        if bpv != next_primitive:
            f.write('      decode%d(in, tmp, longs);\n' % bpv)
        else:
            f.write('      decode%d(in, longs);\n' % bpv)
        if next_primitive != 64:
            f.write('      expand%d(longs);\n' % next_primitive)
        f.write('    }\n')
        f.write('  }\n')

    f.write("""
  Decoder decoder(int bitsPerValue) {
    switch (bitsPerValue) {
""")
    for bpv in BITS_PER_VALUES:
        f.write('      case %d:\n' %bpv)
        f.write('        return new Decoder%d();\n' %bpv)
    f.write('      default:\n')
    f.write('        throw new AssertionError();\n')
    f.write('    }\n')
    f.write('  }\n')
    f.write('\n')

    for bpv in BITS_PER_VALUES:
        writeDecode(bpv, f)
        f.write('\n')

    for primitive_size in PRIMITIVE_SIZE:
        f.write('  private static final long[] MASKS%d = new long[%d];\n' %(primitive_size, primitive_size))
    f.write('\n')
    f.write('  static {\n')
    for primitive_size in PRIMITIVE_SIZE:
        f.write('    for (int i = 0; i < %d; ++i) {\n' %primitive_size)
        f.write('      MASKS%d[i] = mask%d(i);\n' %(primitive_size, primitive_size))
        f.write('    }\n')
    f.write('  }')
    f.write("""
  // mark values in array as final longs to avoid the cost of reading array, arrays should only be
  // used when the idx is a variable
""")
    for primitive in PRIMITIVE_SIZE:
        bpvs = USING_MASKS[primitive]
        bpvs = {}.fromkeys(bpvs).keys()
        bpvs.sort()
        for bpv in bpvs:
            f.write('  private static final long MASK%d_%d = MASKS%d[%d];\n' %(primitive, bpv, primitive, bpv))

    f.write('}\n')