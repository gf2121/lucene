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
import org.apache.lucene.store.IndexInput;
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

def writeRemainderWithSIMDOptimize(bpv, next_primitive, remaining_bits_per_long, o, num_values, f):
    iteration = 1
    num_longs = bpv * num_values / remaining_bits_per_long
    while num_longs % 2 == 0 and num_values % 2 == 0:
        num_longs /= 2
        num_values /= 2
        iteration *= 2

    f.write('    shiftLongs(tmp, %d, tmp, 0, 0, MASK%d_%d);\n' % (iteration * num_longs, next_primitive, remaining_bits_per_long))
    f.write('    for (int iter = 0, tmpIdx = 0, longsIdx = %d; iter < %d; ++iter, tmpIdx += %d, longsIdx += %d) {\n' %(o, iteration, num_longs, num_values))
    tmp_idx = 0
    b = bpv
    b -= remaining_bits_per_long
    f.write('      long l0 = tmp[tmpIdx + %d] << %d;\n' %(tmp_idx, b))
    tmp_idx += 1
    while b >= remaining_bits_per_long:
        b -= remaining_bits_per_long
        f.write('      l0 |= tmp[tmpIdx + %d] << %d;\n' %(tmp_idx, b))
        tmp_idx += 1
    f.write('      longs[longsIdx + 0] = l0;\n')
    f.write('    }\n')


def writeRemainder(bpv, next_primitive, remaining_bits_per_long, o, num_values, f):
    iteration = 1
    num_longs = bpv * num_values / remaining_bits_per_long
    while num_longs % 2 == 0 and num_values % 2 == 0:
        num_longs /= 2
        num_values /= 2
        iteration *= 2
    f.write('    for (int iter = 0, tmpIdx = 0, longsIdx = %d; iter < %d; ++iter, tmpIdx += %d, longsIdx += %d) {\n' %(o, iteration, num_longs, num_values))
    i = 0
    remaining_bits = 0
    tmp_idx = 0
    for i in range(num_values):
        b = bpv
        if remaining_bits == 0:
            b -= remaining_bits_per_long
            f.write('      long l%d = (tmp[tmpIdx + %d] & MASK%d_%d) << %d;\n' %(i, tmp_idx, next_primitive, remaining_bits_per_long, b))
        else:
            b -= remaining_bits
            f.write('      long l%d = (tmp[tmpIdx + %d] & MASK%d_%d) << %d;\n' %(i, tmp_idx, next_primitive, remaining_bits, b))
        tmp_idx += 1
        while b >= remaining_bits_per_long:
            b -= remaining_bits_per_long
            f.write('      l%d |= (tmp[tmpIdx + %d] & MASK%d_%d) << %d;\n' %(i, tmp_idx, next_primitive, remaining_bits_per_long, b))
            tmp_idx += 1
        if b > 0:
            f.write('      l%d |= (tmp[tmpIdx + %d] >>> %d) & MASK%d_%d;\n' %(i, tmp_idx, remaining_bits_per_long-b, next_primitive, b))
            remaining_bits = remaining_bits_per_long-b
        f.write('      longs[longsIdx + %d] = l%d;\n' %(i, i))
    f.write('    }\n')


def writeDecode(bpv, f):
    next_primitive = 64
    if bpv <= 8:
        next_primitive = 8
    elif bpv <= 16:
        next_primitive = 16
    elif bpv <= 32:
        next_primitive = 32
    f.write('  private static void decode%d(DataInput in, long[] tmp, long[] longs) throws IOException {\n' %bpv)
    num_values_per_long = 64 / next_primitive
    if bpv == next_primitive:
        f.write('    in.readLongs(longs, 0, %d);\n' %(bpv*(BLOCK_SIZE / 64)))
    else:
        f.write('    in.readLongs(tmp, 0, %d);\n' %(bpv*(BLOCK_SIZE / 64)))
        shift = next_primitive - bpv
        o = 0
        while shift >= 0:
            f.write('    shiftLongs(tmp, %d, longs, %d, %d, MASK%d_%d);\n' %(bpv*(BLOCK_SIZE / 64), o, shift, next_primitive, bpv))
            o += bpv*(BLOCK_SIZE / 64)
            shift -= bpv
        if shift + bpv > 0:
            if bpv % (next_primitive % bpv) == 0:
                writeRemainderWithSIMDOptimize(bpv, next_primitive, shift + bpv, o, BLOCK_SIZE/num_values_per_long - o, f)
            else:
                writeRemainder(bpv, next_primitive, shift + bpv, o, BLOCK_SIZE/num_values_per_long - o, f)
    f.write('  }\n')

def writeGet(bpv, f):
    next_primitive = 64
    if bpv <= 8:
        next_primitive = 8
    elif bpv <= 16:
        next_primitive = 16
    elif bpv <= 32:
        next_primitive = 32
    value_count_before_expand = BLOCK_SIZE / (64 / next_primitive)
    if next_primitive % bpv == 0:
        if bpv == 64:
            f.write('      in.seek(blockStartPointer + (blockIndex << 3));\n')
        else:
            f.write('      in.seek(blockStartPointer + ((blockIndex & %d) << 3));\n' % (bpv*(BLOCK_SIZE / 64)-1))
        if next_primitive != bpv:
            f.write('      return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_%d;\n' % bpv)
        else:
            if bpv == 64:
                f.write('      return in.readLong();\n')
            else:
                f.write('      return (in.readLong() >>> EXPAND_%d_SHIFT[blockIndex]) & MASK64_%d;\n' % (bpv, bpv))
    else :
        f.write('      int posBeforeExpand = blockIndex & %d;\n' % (value_count_before_expand - 1))
        f.write('      if (posBeforeExpand < %d) {\n' % (bpv*(BLOCK_SIZE / 64)))
        f.write('        in.seek(blockStartPointer + (posBeforeExpand << 3));\n' )
        if next_primitive == 64:
            f.write('        return (in.readLong() >>> %d) & MASK64_%d;\n' % (next_primitive - bpv, bpv))
        else:
            f.write('        return (in.readLong() >>> SHIFT_CACHE[blockIndex]) & MASK64_%d;\n' % (bpv))
        f.write('      } else {\n')
        writeRemainderGet(bpv, f)
        f.write('      }\n')

def writeRemainderGet(bpv, f):
    if bpv == 12:
        f.write("""        in.seek(blockStartPointer + (posBeforeExpand - 24) * 24);
        in.readLongs(tmp, 0, 3);
        long beforeExpand = ((tmp[0] & MASK16_4) << 8) | ((tmp[1] & MASK16_4) << 4) | (tmp[2] & MASK16_4);
        return (beforeExpand >> EXPAND_16_SHIFT[blockIndex]) & MASK64_12;\n""")
    elif bpv == 24:
        f.write("""        in.seek(blockStartPointer + (posBeforeExpand - 48) * 24);
        in.readLongs(tmp, 0, 3);
        long beforeExpand = ((tmp[0] & MASK32_8) << 16) | ((tmp[1] & MASK32_8) << 8) | (tmp[2] & MASK32_8);
        return (beforeExpand >> EXPAND_32_SHIFT[blockIndex]) & MASK64_24;\n""")
    elif bpv == 28:
        f.write("""        in.seek(blockStartPointer + (posBeforeExpand - 56) * 56);
        in.readLongs(tmp, 0, 7);
        shiftLongs(tmp, 7, tmp, 0, 0, MASK32_4);
        long beforeExpand = (tmp[0] << 24) | (tmp[1] << 20) | (tmp[2] << 16) | (tmp[3] << 12) | (tmp[4] << 8) | (tmp[5] << 4) | tmp[6];
        return (beforeExpand >> EXPAND_32_SHIFT[blockIndex]) & MASK64_28;\n""")
    elif bpv == 48:
        f.write("""        in.seek(blockStartPointer + (posBeforeExpand - 96) * 24);
        in.readLongs(tmp, 0, 3);
        return ((tmp[0] & MASK64_16) << 32) | ((tmp[1] & MASK64_16) << 16) | (tmp[2] & MASK64_16);\n""")
    elif bpv == 56:
        f.write("""        in.seek(blockStartPointer + (posBeforeExpand - 112) * 56);
        in.readLongs(tmp, 0, 7);
        shiftLongs(tmp, 7, tmp, 0, 0, MASK64_8);
        return (tmp[0] << 48) | (tmp[1] << 40) | (tmp[2] << 32) | (tmp[3] << 24) | (tmp[4] << 16) | (tmp[5] << 8) | tmp[6];\n""")
    else:
        f.write('        throw new IllegalArgumentException();\n')



if __name__ == '__main__':
    f = open(OUTPUT_FILE, 'w')
    f.write(HEADER)
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
    for primitive_size in PRIMITIVE_SIZE:
        for bpv in BITS_PER_VALUES:
            if bpv < primitive_size:
                f.write('  private static final long MASK%d_%d = MASKS%d[%d];\n' %(primitive_size, bpv, primitive_size, bpv))
    f.write('\n')
    f.write("""  private static final long[] EXPAND_8_SHIFT = new long[BLOCK_SIZE];
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
  }\n""")
    f.write('\n')
    for bpv in BITS_PER_VALUES:
        next_primitive = 64
        if bpv <= 8:
            next_primitive = 8
        elif bpv <= 16:
            next_primitive = 16
        elif bpv <= 32:
            next_primitive = 32
        f.write("""\n  static class Decoder""" + str(bpv) + """ implements Decoder {\n""")
        if next_primitive != 64 and bpv != next_primitive:
            f.write("""\n    private static final long[] SHIFT_CACHE = new long[BLOCK_SIZE];\n""")
            f.write("""\n    static {\n""")
            f.write("""      for(int i=0; i < BLOCK_SIZE; i++) {\n""")
            value_count_before_expand = BLOCK_SIZE / (64 / next_primitive)
            if next_primitive % bpv == 0 :
                f.write('        int posBeforeExpand = i & %d;\n' % (value_count_before_expand - 1))
                f.write('        int shift1 = ((%d ^ posBeforeExpand) >>> %d) << %d;\n' % (value_count_before_expand - 1, math.log(bpv, 2) + 1, math.log(bpv, 2)))
                f.write('        SHIFT_CACHE[i] = shift1 + EXPAND_%d_SHIFT[i];\n' % next_primitive)
            else :
                f.write('        SHIFT_CACHE[i] = EXPAND_%d_SHIFT[i] + %d;\n' % (next_primitive, next_primitive - bpv))
            f.write("""      }\n""")
            f.write("""    }\n\n""")
        f.write("""    private final long[] tmp;\n\n""")
        f.write("""    private Decoder%d(long[] tmp) {\n""" % bpv)
        f.write("""      this.tmp = tmp;\n""")
        f.write("""    }\n""")
        f.write("""
    @Override
    public long get(IndexInput in, int blockIndex, long blockStartPointer) throws IOException {\n""")
        writeGet(bpv, f)
        f.write("""    }

    @Override
    public void decode(DataInput in, long[] longs) throws IOException {
      decode""" + str(bpv) + """(in, tmp, longs);"""
                + ('\n      expand%d(longs);' % next_primitive if next_primitive != 64 else '') + """
    }
  }\n""")

    f.write("""
  Decoder decoder(int bitsPerValue) throws IOException {
    switch (bitsPerValue) {
""")
    for bpv in BITS_PER_VALUES:
        f.write('      case %d:\n' %bpv)
        f.write('        return new Decoder%d(tmp);\n' %bpv)
    f.write('      default:\n')
    f.write('        throw new AssertionError();\n')
    f.write('    }\n')
    f.write('  }\n')



    f.write('\n')
    for bpv in BITS_PER_VALUES:
        writeDecode(bpv, f)
        f.write('\n')

    f.write('}\n')