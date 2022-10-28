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

import org.apache.lucene.store.RandomAccessInput;
import java.io.IOException;

/** Unrolling block decoder */
class UnrollingDecoder {

  static void decode(RandomAccessInput src, long offset, long[] dst, int bpv) throws IOException {
    switch(bpv) {
      case 1:
        decode1(src, offset, dst);
        break;
      case 2:
        decode2(src, offset, dst);
        break;
      case 4:
        decode4(src, offset, dst);
        break;
      case 8:
        decode8(src, offset, dst);
        break;
      case 12:
        decode12(src, offset, dst);
        break;
      case 16:
        decode16(src, offset, dst);
        break;
      case 20:
        decode20(src, offset, dst);
        break;
      case 24:
        decode24(src, offset, dst);
        break;
      case 28:
        decode28(src, offset, dst);
        break;
      case 32:
        decode32(src, offset, dst);
        break;
      case 40:
        decode40(src, offset, dst);
        break;
      case 48:
        decode48(src, offset, dst);
        break;
      case 56:
        decode56(src, offset, dst);
        break;
      case 64:
        decode64(src, offset, dst);
        break;
      default:
        throw new AssertionError();
    }
  }

  static void decode1(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 1L;
      dst[1] = (l0 >>> 1) & 1L;
      dst[2] = (l0 >>> 2) & 1L;
      dst[3] = (l0 >>> 3) & 1L;
      dst[4] = (l0 >>> 4) & 1L;
      dst[5] = (l0 >>> 5) & 1L;
      dst[6] = (l0 >>> 6) & 1L;
      dst[7] = (l0 >>> 7) & 1L;
      dst[8] = (l0 >>> 8) & 1L;
      dst[9] = (l0 >>> 9) & 1L;
      dst[10] = (l0 >>> 10) & 1L;
      dst[11] = (l0 >>> 11) & 1L;
      dst[12] = (l0 >>> 12) & 1L;
      dst[13] = (l0 >>> 13) & 1L;
      dst[14] = (l0 >>> 14) & 1L;
      dst[15] = (l0 >>> 15) & 1L;
      dst[16] = (l0 >>> 16) & 1L;
      dst[17] = (l0 >>> 17) & 1L;
      dst[18] = (l0 >>> 18) & 1L;
      dst[19] = (l0 >>> 19) & 1L;
      dst[20] = (l0 >>> 20) & 1L;
      dst[21] = (l0 >>> 21) & 1L;
      dst[22] = (l0 >>> 22) & 1L;
      dst[23] = (l0 >>> 23) & 1L;
      dst[24] = (l0 >>> 24) & 1L;
      dst[25] = (l0 >>> 25) & 1L;
      dst[26] = (l0 >>> 26) & 1L;
      dst[27] = (l0 >>> 27) & 1L;
      dst[28] = (l0 >>> 28) & 1L;
      dst[29] = (l0 >>> 29) & 1L;
      dst[30] = (l0 >>> 30) & 1L;
      dst[31] = (l0 >>> 31) & 1L;
      dst[32] = (l0 >>> 32) & 1L;
      dst[33] = (l0 >>> 33) & 1L;
      dst[34] = (l0 >>> 34) & 1L;
      dst[35] = (l0 >>> 35) & 1L;
      dst[36] = (l0 >>> 36) & 1L;
      dst[37] = (l0 >>> 37) & 1L;
      dst[38] = (l0 >>> 38) & 1L;
      dst[39] = (l0 >>> 39) & 1L;
      dst[40] = (l0 >>> 40) & 1L;
      dst[41] = (l0 >>> 41) & 1L;
      dst[42] = (l0 >>> 42) & 1L;
      dst[43] = (l0 >>> 43) & 1L;
      dst[44] = (l0 >>> 44) & 1L;
      dst[45] = (l0 >>> 45) & 1L;
      dst[46] = (l0 >>> 46) & 1L;
      dst[47] = (l0 >>> 47) & 1L;
      dst[48] = (l0 >>> 48) & 1L;
      dst[49] = (l0 >>> 49) & 1L;
      dst[50] = (l0 >>> 50) & 1L;
      dst[51] = (l0 >>> 51) & 1L;
      dst[52] = (l0 >>> 52) & 1L;
      dst[53] = (l0 >>> 53) & 1L;
      dst[54] = (l0 >>> 54) & 1L;
      dst[55] = (l0 >>> 55) & 1L;
      dst[56] = (l0 >>> 56) & 1L;
      dst[57] = (l0 >>> 57) & 1L;
      dst[58] = (l0 >>> 58) & 1L;
      dst[59] = (l0 >>> 59) & 1L;
      dst[60] = (l0 >>> 60) & 1L;
      dst[61] = (l0 >>> 61) & 1L;
      dst[62] = (l0 >>> 62) & 1L;
      dst[63] = l0 >>> 63;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 1L;
      dst[65] = (l0 >>> 1) & 1L;
      dst[66] = (l0 >>> 2) & 1L;
      dst[67] = (l0 >>> 3) & 1L;
      dst[68] = (l0 >>> 4) & 1L;
      dst[69] = (l0 >>> 5) & 1L;
      dst[70] = (l0 >>> 6) & 1L;
      dst[71] = (l0 >>> 7) & 1L;
      dst[72] = (l0 >>> 8) & 1L;
      dst[73] = (l0 >>> 9) & 1L;
      dst[74] = (l0 >>> 10) & 1L;
      dst[75] = (l0 >>> 11) & 1L;
      dst[76] = (l0 >>> 12) & 1L;
      dst[77] = (l0 >>> 13) & 1L;
      dst[78] = (l0 >>> 14) & 1L;
      dst[79] = (l0 >>> 15) & 1L;
      dst[80] = (l0 >>> 16) & 1L;
      dst[81] = (l0 >>> 17) & 1L;
      dst[82] = (l0 >>> 18) & 1L;
      dst[83] = (l0 >>> 19) & 1L;
      dst[84] = (l0 >>> 20) & 1L;
      dst[85] = (l0 >>> 21) & 1L;
      dst[86] = (l0 >>> 22) & 1L;
      dst[87] = (l0 >>> 23) & 1L;
      dst[88] = (l0 >>> 24) & 1L;
      dst[89] = (l0 >>> 25) & 1L;
      dst[90] = (l0 >>> 26) & 1L;
      dst[91] = (l0 >>> 27) & 1L;
      dst[92] = (l0 >>> 28) & 1L;
      dst[93] = (l0 >>> 29) & 1L;
      dst[94] = (l0 >>> 30) & 1L;
      dst[95] = (l0 >>> 31) & 1L;
      dst[96] = (l0 >>> 32) & 1L;
      dst[97] = (l0 >>> 33) & 1L;
      dst[98] = (l0 >>> 34) & 1L;
      dst[99] = (l0 >>> 35) & 1L;
      dst[100] = (l0 >>> 36) & 1L;
      dst[101] = (l0 >>> 37) & 1L;
      dst[102] = (l0 >>> 38) & 1L;
      dst[103] = (l0 >>> 39) & 1L;
      dst[104] = (l0 >>> 40) & 1L;
      dst[105] = (l0 >>> 41) & 1L;
      dst[106] = (l0 >>> 42) & 1L;
      dst[107] = (l0 >>> 43) & 1L;
      dst[108] = (l0 >>> 44) & 1L;
      dst[109] = (l0 >>> 45) & 1L;
      dst[110] = (l0 >>> 46) & 1L;
      dst[111] = (l0 >>> 47) & 1L;
      dst[112] = (l0 >>> 48) & 1L;
      dst[113] = (l0 >>> 49) & 1L;
      dst[114] = (l0 >>> 50) & 1L;
      dst[115] = (l0 >>> 51) & 1L;
      dst[116] = (l0 >>> 52) & 1L;
      dst[117] = (l0 >>> 53) & 1L;
      dst[118] = (l0 >>> 54) & 1L;
      dst[119] = (l0 >>> 55) & 1L;
      dst[120] = (l0 >>> 56) & 1L;
      dst[121] = (l0 >>> 57) & 1L;
      dst[122] = (l0 >>> 58) & 1L;
      dst[123] = (l0 >>> 59) & 1L;
      dst[124] = (l0 >>> 60) & 1L;
      dst[125] = (l0 >>> 61) & 1L;
      dst[126] = (l0 >>> 62) & 1L;
      dst[127] = l0 >>> 63;
    }
  }

  static void decode2(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 3L;
      dst[1] = (l0 >>> 2) & 3L;
      dst[2] = (l0 >>> 4) & 3L;
      dst[3] = (l0 >>> 6) & 3L;
      dst[4] = (l0 >>> 8) & 3L;
      dst[5] = (l0 >>> 10) & 3L;
      dst[6] = (l0 >>> 12) & 3L;
      dst[7] = (l0 >>> 14) & 3L;
      dst[8] = (l0 >>> 16) & 3L;
      dst[9] = (l0 >>> 18) & 3L;
      dst[10] = (l0 >>> 20) & 3L;
      dst[11] = (l0 >>> 22) & 3L;
      dst[12] = (l0 >>> 24) & 3L;
      dst[13] = (l0 >>> 26) & 3L;
      dst[14] = (l0 >>> 28) & 3L;
      dst[15] = (l0 >>> 30) & 3L;
      dst[16] = (l0 >>> 32) & 3L;
      dst[17] = (l0 >>> 34) & 3L;
      dst[18] = (l0 >>> 36) & 3L;
      dst[19] = (l0 >>> 38) & 3L;
      dst[20] = (l0 >>> 40) & 3L;
      dst[21] = (l0 >>> 42) & 3L;
      dst[22] = (l0 >>> 44) & 3L;
      dst[23] = (l0 >>> 46) & 3L;
      dst[24] = (l0 >>> 48) & 3L;
      dst[25] = (l0 >>> 50) & 3L;
      dst[26] = (l0 >>> 52) & 3L;
      dst[27] = (l0 >>> 54) & 3L;
      dst[28] = (l0 >>> 56) & 3L;
      dst[29] = (l0 >>> 58) & 3L;
      dst[30] = (l0 >>> 60) & 3L;
      dst[31] = l0 >>> 62;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 3L;
      dst[33] = (l0 >>> 2) & 3L;
      dst[34] = (l0 >>> 4) & 3L;
      dst[35] = (l0 >>> 6) & 3L;
      dst[36] = (l0 >>> 8) & 3L;
      dst[37] = (l0 >>> 10) & 3L;
      dst[38] = (l0 >>> 12) & 3L;
      dst[39] = (l0 >>> 14) & 3L;
      dst[40] = (l0 >>> 16) & 3L;
      dst[41] = (l0 >>> 18) & 3L;
      dst[42] = (l0 >>> 20) & 3L;
      dst[43] = (l0 >>> 22) & 3L;
      dst[44] = (l0 >>> 24) & 3L;
      dst[45] = (l0 >>> 26) & 3L;
      dst[46] = (l0 >>> 28) & 3L;
      dst[47] = (l0 >>> 30) & 3L;
      dst[48] = (l0 >>> 32) & 3L;
      dst[49] = (l0 >>> 34) & 3L;
      dst[50] = (l0 >>> 36) & 3L;
      dst[51] = (l0 >>> 38) & 3L;
      dst[52] = (l0 >>> 40) & 3L;
      dst[53] = (l0 >>> 42) & 3L;
      dst[54] = (l0 >>> 44) & 3L;
      dst[55] = (l0 >>> 46) & 3L;
      dst[56] = (l0 >>> 48) & 3L;
      dst[57] = (l0 >>> 50) & 3L;
      dst[58] = (l0 >>> 52) & 3L;
      dst[59] = (l0 >>> 54) & 3L;
      dst[60] = (l0 >>> 56) & 3L;
      dst[61] = (l0 >>> 58) & 3L;
      dst[62] = (l0 >>> 60) & 3L;
      dst[63] = l0 >>> 62;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 3L;
      dst[65] = (l0 >>> 2) & 3L;
      dst[66] = (l0 >>> 4) & 3L;
      dst[67] = (l0 >>> 6) & 3L;
      dst[68] = (l0 >>> 8) & 3L;
      dst[69] = (l0 >>> 10) & 3L;
      dst[70] = (l0 >>> 12) & 3L;
      dst[71] = (l0 >>> 14) & 3L;
      dst[72] = (l0 >>> 16) & 3L;
      dst[73] = (l0 >>> 18) & 3L;
      dst[74] = (l0 >>> 20) & 3L;
      dst[75] = (l0 >>> 22) & 3L;
      dst[76] = (l0 >>> 24) & 3L;
      dst[77] = (l0 >>> 26) & 3L;
      dst[78] = (l0 >>> 28) & 3L;
      dst[79] = (l0 >>> 30) & 3L;
      dst[80] = (l0 >>> 32) & 3L;
      dst[81] = (l0 >>> 34) & 3L;
      dst[82] = (l0 >>> 36) & 3L;
      dst[83] = (l0 >>> 38) & 3L;
      dst[84] = (l0 >>> 40) & 3L;
      dst[85] = (l0 >>> 42) & 3L;
      dst[86] = (l0 >>> 44) & 3L;
      dst[87] = (l0 >>> 46) & 3L;
      dst[88] = (l0 >>> 48) & 3L;
      dst[89] = (l0 >>> 50) & 3L;
      dst[90] = (l0 >>> 52) & 3L;
      dst[91] = (l0 >>> 54) & 3L;
      dst[92] = (l0 >>> 56) & 3L;
      dst[93] = (l0 >>> 58) & 3L;
      dst[94] = (l0 >>> 60) & 3L;
      dst[95] = l0 >>> 62;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 3L;
      dst[97] = (l0 >>> 2) & 3L;
      dst[98] = (l0 >>> 4) & 3L;
      dst[99] = (l0 >>> 6) & 3L;
      dst[100] = (l0 >>> 8) & 3L;
      dst[101] = (l0 >>> 10) & 3L;
      dst[102] = (l0 >>> 12) & 3L;
      dst[103] = (l0 >>> 14) & 3L;
      dst[104] = (l0 >>> 16) & 3L;
      dst[105] = (l0 >>> 18) & 3L;
      dst[106] = (l0 >>> 20) & 3L;
      dst[107] = (l0 >>> 22) & 3L;
      dst[108] = (l0 >>> 24) & 3L;
      dst[109] = (l0 >>> 26) & 3L;
      dst[110] = (l0 >>> 28) & 3L;
      dst[111] = (l0 >>> 30) & 3L;
      dst[112] = (l0 >>> 32) & 3L;
      dst[113] = (l0 >>> 34) & 3L;
      dst[114] = (l0 >>> 36) & 3L;
      dst[115] = (l0 >>> 38) & 3L;
      dst[116] = (l0 >>> 40) & 3L;
      dst[117] = (l0 >>> 42) & 3L;
      dst[118] = (l0 >>> 44) & 3L;
      dst[119] = (l0 >>> 46) & 3L;
      dst[120] = (l0 >>> 48) & 3L;
      dst[121] = (l0 >>> 50) & 3L;
      dst[122] = (l0 >>> 52) & 3L;
      dst[123] = (l0 >>> 54) & 3L;
      dst[124] = (l0 >>> 56) & 3L;
      dst[125] = (l0 >>> 58) & 3L;
      dst[126] = (l0 >>> 60) & 3L;
      dst[127] = l0 >>> 62;
    }
  }

  static void decode4(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 15L;
      dst[1] = (l0 >>> 4) & 15L;
      dst[2] = (l0 >>> 8) & 15L;
      dst[3] = (l0 >>> 12) & 15L;
      dst[4] = (l0 >>> 16) & 15L;
      dst[5] = (l0 >>> 20) & 15L;
      dst[6] = (l0 >>> 24) & 15L;
      dst[7] = (l0 >>> 28) & 15L;
      dst[8] = (l0 >>> 32) & 15L;
      dst[9] = (l0 >>> 36) & 15L;
      dst[10] = (l0 >>> 40) & 15L;
      dst[11] = (l0 >>> 44) & 15L;
      dst[12] = (l0 >>> 48) & 15L;
      dst[13] = (l0 >>> 52) & 15L;
      dst[14] = (l0 >>> 56) & 15L;
      dst[15] = l0 >>> 60;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 15L;
      dst[17] = (l0 >>> 4) & 15L;
      dst[18] = (l0 >>> 8) & 15L;
      dst[19] = (l0 >>> 12) & 15L;
      dst[20] = (l0 >>> 16) & 15L;
      dst[21] = (l0 >>> 20) & 15L;
      dst[22] = (l0 >>> 24) & 15L;
      dst[23] = (l0 >>> 28) & 15L;
      dst[24] = (l0 >>> 32) & 15L;
      dst[25] = (l0 >>> 36) & 15L;
      dst[26] = (l0 >>> 40) & 15L;
      dst[27] = (l0 >>> 44) & 15L;
      dst[28] = (l0 >>> 48) & 15L;
      dst[29] = (l0 >>> 52) & 15L;
      dst[30] = (l0 >>> 56) & 15L;
      dst[31] = l0 >>> 60;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 15L;
      dst[33] = (l0 >>> 4) & 15L;
      dst[34] = (l0 >>> 8) & 15L;
      dst[35] = (l0 >>> 12) & 15L;
      dst[36] = (l0 >>> 16) & 15L;
      dst[37] = (l0 >>> 20) & 15L;
      dst[38] = (l0 >>> 24) & 15L;
      dst[39] = (l0 >>> 28) & 15L;
      dst[40] = (l0 >>> 32) & 15L;
      dst[41] = (l0 >>> 36) & 15L;
      dst[42] = (l0 >>> 40) & 15L;
      dst[43] = (l0 >>> 44) & 15L;
      dst[44] = (l0 >>> 48) & 15L;
      dst[45] = (l0 >>> 52) & 15L;
      dst[46] = (l0 >>> 56) & 15L;
      dst[47] = l0 >>> 60;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 15L;
      dst[49] = (l0 >>> 4) & 15L;
      dst[50] = (l0 >>> 8) & 15L;
      dst[51] = (l0 >>> 12) & 15L;
      dst[52] = (l0 >>> 16) & 15L;
      dst[53] = (l0 >>> 20) & 15L;
      dst[54] = (l0 >>> 24) & 15L;
      dst[55] = (l0 >>> 28) & 15L;
      dst[56] = (l0 >>> 32) & 15L;
      dst[57] = (l0 >>> 36) & 15L;
      dst[58] = (l0 >>> 40) & 15L;
      dst[59] = (l0 >>> 44) & 15L;
      dst[60] = (l0 >>> 48) & 15L;
      dst[61] = (l0 >>> 52) & 15L;
      dst[62] = (l0 >>> 56) & 15L;
      dst[63] = l0 >>> 60;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 15L;
      dst[65] = (l0 >>> 4) & 15L;
      dst[66] = (l0 >>> 8) & 15L;
      dst[67] = (l0 >>> 12) & 15L;
      dst[68] = (l0 >>> 16) & 15L;
      dst[69] = (l0 >>> 20) & 15L;
      dst[70] = (l0 >>> 24) & 15L;
      dst[71] = (l0 >>> 28) & 15L;
      dst[72] = (l0 >>> 32) & 15L;
      dst[73] = (l0 >>> 36) & 15L;
      dst[74] = (l0 >>> 40) & 15L;
      dst[75] = (l0 >>> 44) & 15L;
      dst[76] = (l0 >>> 48) & 15L;
      dst[77] = (l0 >>> 52) & 15L;
      dst[78] = (l0 >>> 56) & 15L;
      dst[79] = l0 >>> 60;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 15L;
      dst[81] = (l0 >>> 4) & 15L;
      dst[82] = (l0 >>> 8) & 15L;
      dst[83] = (l0 >>> 12) & 15L;
      dst[84] = (l0 >>> 16) & 15L;
      dst[85] = (l0 >>> 20) & 15L;
      dst[86] = (l0 >>> 24) & 15L;
      dst[87] = (l0 >>> 28) & 15L;
      dst[88] = (l0 >>> 32) & 15L;
      dst[89] = (l0 >>> 36) & 15L;
      dst[90] = (l0 >>> 40) & 15L;
      dst[91] = (l0 >>> 44) & 15L;
      dst[92] = (l0 >>> 48) & 15L;
      dst[93] = (l0 >>> 52) & 15L;
      dst[94] = (l0 >>> 56) & 15L;
      dst[95] = l0 >>> 60;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 15L;
      dst[97] = (l0 >>> 4) & 15L;
      dst[98] = (l0 >>> 8) & 15L;
      dst[99] = (l0 >>> 12) & 15L;
      dst[100] = (l0 >>> 16) & 15L;
      dst[101] = (l0 >>> 20) & 15L;
      dst[102] = (l0 >>> 24) & 15L;
      dst[103] = (l0 >>> 28) & 15L;
      dst[104] = (l0 >>> 32) & 15L;
      dst[105] = (l0 >>> 36) & 15L;
      dst[106] = (l0 >>> 40) & 15L;
      dst[107] = (l0 >>> 44) & 15L;
      dst[108] = (l0 >>> 48) & 15L;
      dst[109] = (l0 >>> 52) & 15L;
      dst[110] = (l0 >>> 56) & 15L;
      dst[111] = l0 >>> 60;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 15L;
      dst[113] = (l0 >>> 4) & 15L;
      dst[114] = (l0 >>> 8) & 15L;
      dst[115] = (l0 >>> 12) & 15L;
      dst[116] = (l0 >>> 16) & 15L;
      dst[117] = (l0 >>> 20) & 15L;
      dst[118] = (l0 >>> 24) & 15L;
      dst[119] = (l0 >>> 28) & 15L;
      dst[120] = (l0 >>> 32) & 15L;
      dst[121] = (l0 >>> 36) & 15L;
      dst[122] = (l0 >>> 40) & 15L;
      dst[123] = (l0 >>> 44) & 15L;
      dst[124] = (l0 >>> 48) & 15L;
      dst[125] = (l0 >>> 52) & 15L;
      dst[126] = (l0 >>> 56) & 15L;
      dst[127] = l0 >>> 60;
    }
  }

  static void decode8(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 255L;
      dst[1] = (l0 >>> 8) & 255L;
      dst[2] = (l0 >>> 16) & 255L;
      dst[3] = (l0 >>> 24) & 255L;
      dst[4] = (l0 >>> 32) & 255L;
      dst[5] = (l0 >>> 40) & 255L;
      dst[6] = (l0 >>> 48) & 255L;
      dst[7] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 & 255L;
      dst[9] = (l0 >>> 8) & 255L;
      dst[10] = (l0 >>> 16) & 255L;
      dst[11] = (l0 >>> 24) & 255L;
      dst[12] = (l0 >>> 32) & 255L;
      dst[13] = (l0 >>> 40) & 255L;
      dst[14] = (l0 >>> 48) & 255L;
      dst[15] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 255L;
      dst[17] = (l0 >>> 8) & 255L;
      dst[18] = (l0 >>> 16) & 255L;
      dst[19] = (l0 >>> 24) & 255L;
      dst[20] = (l0 >>> 32) & 255L;
      dst[21] = (l0 >>> 40) & 255L;
      dst[22] = (l0 >>> 48) & 255L;
      dst[23] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 & 255L;
      dst[25] = (l0 >>> 8) & 255L;
      dst[26] = (l0 >>> 16) & 255L;
      dst[27] = (l0 >>> 24) & 255L;
      dst[28] = (l0 >>> 32) & 255L;
      dst[29] = (l0 >>> 40) & 255L;
      dst[30] = (l0 >>> 48) & 255L;
      dst[31] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 255L;
      dst[33] = (l0 >>> 8) & 255L;
      dst[34] = (l0 >>> 16) & 255L;
      dst[35] = (l0 >>> 24) & 255L;
      dst[36] = (l0 >>> 32) & 255L;
      dst[37] = (l0 >>> 40) & 255L;
      dst[38] = (l0 >>> 48) & 255L;
      dst[39] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 & 255L;
      dst[41] = (l0 >>> 8) & 255L;
      dst[42] = (l0 >>> 16) & 255L;
      dst[43] = (l0 >>> 24) & 255L;
      dst[44] = (l0 >>> 32) & 255L;
      dst[45] = (l0 >>> 40) & 255L;
      dst[46] = (l0 >>> 48) & 255L;
      dst[47] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 255L;
      dst[49] = (l0 >>> 8) & 255L;
      dst[50] = (l0 >>> 16) & 255L;
      dst[51] = (l0 >>> 24) & 255L;
      dst[52] = (l0 >>> 32) & 255L;
      dst[53] = (l0 >>> 40) & 255L;
      dst[54] = (l0 >>> 48) & 255L;
      dst[55] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 & 255L;
      dst[57] = (l0 >>> 8) & 255L;
      dst[58] = (l0 >>> 16) & 255L;
      dst[59] = (l0 >>> 24) & 255L;
      dst[60] = (l0 >>> 32) & 255L;
      dst[61] = (l0 >>> 40) & 255L;
      dst[62] = (l0 >>> 48) & 255L;
      dst[63] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 255L;
      dst[65] = (l0 >>> 8) & 255L;
      dst[66] = (l0 >>> 16) & 255L;
      dst[67] = (l0 >>> 24) & 255L;
      dst[68] = (l0 >>> 32) & 255L;
      dst[69] = (l0 >>> 40) & 255L;
      dst[70] = (l0 >>> 48) & 255L;
      dst[71] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 & 255L;
      dst[73] = (l0 >>> 8) & 255L;
      dst[74] = (l0 >>> 16) & 255L;
      dst[75] = (l0 >>> 24) & 255L;
      dst[76] = (l0 >>> 32) & 255L;
      dst[77] = (l0 >>> 40) & 255L;
      dst[78] = (l0 >>> 48) & 255L;
      dst[79] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 255L;
      dst[81] = (l0 >>> 8) & 255L;
      dst[82] = (l0 >>> 16) & 255L;
      dst[83] = (l0 >>> 24) & 255L;
      dst[84] = (l0 >>> 32) & 255L;
      dst[85] = (l0 >>> 40) & 255L;
      dst[86] = (l0 >>> 48) & 255L;
      dst[87] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 & 255L;
      dst[89] = (l0 >>> 8) & 255L;
      dst[90] = (l0 >>> 16) & 255L;
      dst[91] = (l0 >>> 24) & 255L;
      dst[92] = (l0 >>> 32) & 255L;
      dst[93] = (l0 >>> 40) & 255L;
      dst[94] = (l0 >>> 48) & 255L;
      dst[95] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 255L;
      dst[97] = (l0 >>> 8) & 255L;
      dst[98] = (l0 >>> 16) & 255L;
      dst[99] = (l0 >>> 24) & 255L;
      dst[100] = (l0 >>> 32) & 255L;
      dst[101] = (l0 >>> 40) & 255L;
      dst[102] = (l0 >>> 48) & 255L;
      dst[103] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 & 255L;
      dst[105] = (l0 >>> 8) & 255L;
      dst[106] = (l0 >>> 16) & 255L;
      dst[107] = (l0 >>> 24) & 255L;
      dst[108] = (l0 >>> 32) & 255L;
      dst[109] = (l0 >>> 40) & 255L;
      dst[110] = (l0 >>> 48) & 255L;
      dst[111] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 255L;
      dst[113] = (l0 >>> 8) & 255L;
      dst[114] = (l0 >>> 16) & 255L;
      dst[115] = (l0 >>> 24) & 255L;
      dst[116] = (l0 >>> 32) & 255L;
      dst[117] = (l0 >>> 40) & 255L;
      dst[118] = (l0 >>> 48) & 255L;
      dst[119] = l0 >>> 56;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 & 255L;
      dst[121] = (l0 >>> 8) & 255L;
      dst[122] = (l0 >>> 16) & 255L;
      dst[123] = (l0 >>> 24) & 255L;
      dst[124] = (l0 >>> 32) & 255L;
      dst[125] = (l0 >>> 40) & 255L;
      dst[126] = (l0 >>> 48) & 255L;
      dst[127] = l0 >>> 56;
    }
  }

  static void decode12(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 4095L;
      dst[1] = (l0 >>> 12) & 4095L;
      dst[2] = (l0 >>> 24) & 4095L;
      dst[3] = (l0 >>> 36) & 4095L;
      dst[4] = (l0 >>> 48) & 4095L;
      dst[5] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[6] = (l1 >>> 8) & 4095L;
      dst[7] = (l1 >>> 20) & 4095L;
      dst[8] = (l1 >>> 32) & 4095L;
      dst[9] = (l1 >>> 44) & 4095L;
      dst[10] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[11] = (l2 >>> 4) & 4095L;
      dst[12] = (l2 >>> 16) & 4095L;
      dst[13] = (l2 >>> 28) & 4095L;
      dst[14] = (l2 >>> 40) & 4095L;
      dst[15] = l2 >>> 52;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 4095L;
      dst[17] = (l0 >>> 12) & 4095L;
      dst[18] = (l0 >>> 24) & 4095L;
      dst[19] = (l0 >>> 36) & 4095L;
      dst[20] = (l0 >>> 48) & 4095L;
      dst[21] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[22] = (l1 >>> 8) & 4095L;
      dst[23] = (l1 >>> 20) & 4095L;
      dst[24] = (l1 >>> 32) & 4095L;
      dst[25] = (l1 >>> 44) & 4095L;
      dst[26] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[27] = (l2 >>> 4) & 4095L;
      dst[28] = (l2 >>> 16) & 4095L;
      dst[29] = (l2 >>> 28) & 4095L;
      dst[30] = (l2 >>> 40) & 4095L;
      dst[31] = l2 >>> 52;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 4095L;
      dst[33] = (l0 >>> 12) & 4095L;
      dst[34] = (l0 >>> 24) & 4095L;
      dst[35] = (l0 >>> 36) & 4095L;
      dst[36] = (l0 >>> 48) & 4095L;
      dst[37] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[38] = (l1 >>> 8) & 4095L;
      dst[39] = (l1 >>> 20) & 4095L;
      dst[40] = (l1 >>> 32) & 4095L;
      dst[41] = (l1 >>> 44) & 4095L;
      dst[42] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[43] = (l2 >>> 4) & 4095L;
      dst[44] = (l2 >>> 16) & 4095L;
      dst[45] = (l2 >>> 28) & 4095L;
      dst[46] = (l2 >>> 40) & 4095L;
      dst[47] = l2 >>> 52;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 4095L;
      dst[49] = (l0 >>> 12) & 4095L;
      dst[50] = (l0 >>> 24) & 4095L;
      dst[51] = (l0 >>> 36) & 4095L;
      dst[52] = (l0 >>> 48) & 4095L;
      dst[53] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[54] = (l1 >>> 8) & 4095L;
      dst[55] = (l1 >>> 20) & 4095L;
      dst[56] = (l1 >>> 32) & 4095L;
      dst[57] = (l1 >>> 44) & 4095L;
      dst[58] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[59] = (l2 >>> 4) & 4095L;
      dst[60] = (l2 >>> 16) & 4095L;
      dst[61] = (l2 >>> 28) & 4095L;
      dst[62] = (l2 >>> 40) & 4095L;
      dst[63] = l2 >>> 52;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 4095L;
      dst[65] = (l0 >>> 12) & 4095L;
      dst[66] = (l0 >>> 24) & 4095L;
      dst[67] = (l0 >>> 36) & 4095L;
      dst[68] = (l0 >>> 48) & 4095L;
      dst[69] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[70] = (l1 >>> 8) & 4095L;
      dst[71] = (l1 >>> 20) & 4095L;
      dst[72] = (l1 >>> 32) & 4095L;
      dst[73] = (l1 >>> 44) & 4095L;
      dst[74] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[75] = (l2 >>> 4) & 4095L;
      dst[76] = (l2 >>> 16) & 4095L;
      dst[77] = (l2 >>> 28) & 4095L;
      dst[78] = (l2 >>> 40) & 4095L;
      dst[79] = l2 >>> 52;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 4095L;
      dst[81] = (l0 >>> 12) & 4095L;
      dst[82] = (l0 >>> 24) & 4095L;
      dst[83] = (l0 >>> 36) & 4095L;
      dst[84] = (l0 >>> 48) & 4095L;
      dst[85] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[86] = (l1 >>> 8) & 4095L;
      dst[87] = (l1 >>> 20) & 4095L;
      dst[88] = (l1 >>> 32) & 4095L;
      dst[89] = (l1 >>> 44) & 4095L;
      dst[90] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[91] = (l2 >>> 4) & 4095L;
      dst[92] = (l2 >>> 16) & 4095L;
      dst[93] = (l2 >>> 28) & 4095L;
      dst[94] = (l2 >>> 40) & 4095L;
      dst[95] = l2 >>> 52;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 4095L;
      dst[97] = (l0 >>> 12) & 4095L;
      dst[98] = (l0 >>> 24) & 4095L;
      dst[99] = (l0 >>> 36) & 4095L;
      dst[100] = (l0 >>> 48) & 4095L;
      dst[101] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[102] = (l1 >>> 8) & 4095L;
      dst[103] = (l1 >>> 20) & 4095L;
      dst[104] = (l1 >>> 32) & 4095L;
      dst[105] = (l1 >>> 44) & 4095L;
      dst[106] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[107] = (l2 >>> 4) & 4095L;
      dst[108] = (l2 >>> 16) & 4095L;
      dst[109] = (l2 >>> 28) & 4095L;
      dst[110] = (l2 >>> 40) & 4095L;
      dst[111] = l2 >>> 52;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 4095L;
      dst[113] = (l0 >>> 12) & 4095L;
      dst[114] = (l0 >>> 24) & 4095L;
      dst[115] = (l0 >>> 36) & 4095L;
      dst[116] = (l0 >>> 48) & 4095L;
      dst[117] = (l0 >>> 60) | ((l1 & 255L) << 4);
      dst[118] = (l1 >>> 8) & 4095L;
      dst[119] = (l1 >>> 20) & 4095L;
      dst[120] = (l1 >>> 32) & 4095L;
      dst[121] = (l1 >>> 44) & 4095L;
      dst[122] = (l1 >>> 56) | ((l2 & 15L) << 8);
      dst[123] = (l2 >>> 4) & 4095L;
      dst[124] = (l2 >>> 16) & 4095L;
      dst[125] = (l2 >>> 28) & 4095L;
      dst[126] = (l2 >>> 40) & 4095L;
      dst[127] = l2 >>> 52;
    }
  }

  static void decode16(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 65535L;
      dst[1] = (l0 >>> 16) & 65535L;
      dst[2] = (l0 >>> 32) & 65535L;
      dst[3] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[4] = l0 & 65535L;
      dst[5] = (l0 >>> 16) & 65535L;
      dst[6] = (l0 >>> 32) & 65535L;
      dst[7] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 & 65535L;
      dst[9] = (l0 >>> 16) & 65535L;
      dst[10] = (l0 >>> 32) & 65535L;
      dst[11] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[12] = l0 & 65535L;
      dst[13] = (l0 >>> 16) & 65535L;
      dst[14] = (l0 >>> 32) & 65535L;
      dst[15] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 65535L;
      dst[17] = (l0 >>> 16) & 65535L;
      dst[18] = (l0 >>> 32) & 65535L;
      dst[19] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[20] = l0 & 65535L;
      dst[21] = (l0 >>> 16) & 65535L;
      dst[22] = (l0 >>> 32) & 65535L;
      dst[23] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 & 65535L;
      dst[25] = (l0 >>> 16) & 65535L;
      dst[26] = (l0 >>> 32) & 65535L;
      dst[27] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[28] = l0 & 65535L;
      dst[29] = (l0 >>> 16) & 65535L;
      dst[30] = (l0 >>> 32) & 65535L;
      dst[31] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 65535L;
      dst[33] = (l0 >>> 16) & 65535L;
      dst[34] = (l0 >>> 32) & 65535L;
      dst[35] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[36] = l0 & 65535L;
      dst[37] = (l0 >>> 16) & 65535L;
      dst[38] = (l0 >>> 32) & 65535L;
      dst[39] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 & 65535L;
      dst[41] = (l0 >>> 16) & 65535L;
      dst[42] = (l0 >>> 32) & 65535L;
      dst[43] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[44] = l0 & 65535L;
      dst[45] = (l0 >>> 16) & 65535L;
      dst[46] = (l0 >>> 32) & 65535L;
      dst[47] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 65535L;
      dst[49] = (l0 >>> 16) & 65535L;
      dst[50] = (l0 >>> 32) & 65535L;
      dst[51] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[52] = l0 & 65535L;
      dst[53] = (l0 >>> 16) & 65535L;
      dst[54] = (l0 >>> 32) & 65535L;
      dst[55] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 & 65535L;
      dst[57] = (l0 >>> 16) & 65535L;
      dst[58] = (l0 >>> 32) & 65535L;
      dst[59] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[60] = l0 & 65535L;
      dst[61] = (l0 >>> 16) & 65535L;
      dst[62] = (l0 >>> 32) & 65535L;
      dst[63] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 65535L;
      dst[65] = (l0 >>> 16) & 65535L;
      dst[66] = (l0 >>> 32) & 65535L;
      dst[67] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[68] = l0 & 65535L;
      dst[69] = (l0 >>> 16) & 65535L;
      dst[70] = (l0 >>> 32) & 65535L;
      dst[71] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 & 65535L;
      dst[73] = (l0 >>> 16) & 65535L;
      dst[74] = (l0 >>> 32) & 65535L;
      dst[75] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[76] = l0 & 65535L;
      dst[77] = (l0 >>> 16) & 65535L;
      dst[78] = (l0 >>> 32) & 65535L;
      dst[79] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 65535L;
      dst[81] = (l0 >>> 16) & 65535L;
      dst[82] = (l0 >>> 32) & 65535L;
      dst[83] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[84] = l0 & 65535L;
      dst[85] = (l0 >>> 16) & 65535L;
      dst[86] = (l0 >>> 32) & 65535L;
      dst[87] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 & 65535L;
      dst[89] = (l0 >>> 16) & 65535L;
      dst[90] = (l0 >>> 32) & 65535L;
      dst[91] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[92] = l0 & 65535L;
      dst[93] = (l0 >>> 16) & 65535L;
      dst[94] = (l0 >>> 32) & 65535L;
      dst[95] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 65535L;
      dst[97] = (l0 >>> 16) & 65535L;
      dst[98] = (l0 >>> 32) & 65535L;
      dst[99] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[100] = l0 & 65535L;
      dst[101] = (l0 >>> 16) & 65535L;
      dst[102] = (l0 >>> 32) & 65535L;
      dst[103] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 & 65535L;
      dst[105] = (l0 >>> 16) & 65535L;
      dst[106] = (l0 >>> 32) & 65535L;
      dst[107] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[108] = l0 & 65535L;
      dst[109] = (l0 >>> 16) & 65535L;
      dst[110] = (l0 >>> 32) & 65535L;
      dst[111] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 65535L;
      dst[113] = (l0 >>> 16) & 65535L;
      dst[114] = (l0 >>> 32) & 65535L;
      dst[115] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[116] = l0 & 65535L;
      dst[117] = (l0 >>> 16) & 65535L;
      dst[118] = (l0 >>> 32) & 65535L;
      dst[119] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 & 65535L;
      dst[121] = (l0 >>> 16) & 65535L;
      dst[122] = (l0 >>> 32) & 65535L;
      dst[123] = l0 >>> 48;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[124] = l0 & 65535L;
      dst[125] = (l0 >>> 16) & 65535L;
      dst[126] = (l0 >>> 32) & 65535L;
      dst[127] = l0 >>> 48;
    }
  }

  static void decode20(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 1048575L;
      dst[1] = (l0 >>> 20) & 1048575L;
      dst[2] = (l0 >>> 40) & 1048575L;
      dst[3] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[4] = (l1 >>> 16) & 1048575L;
      dst[5] = (l1 >>> 36) & 1048575L;
      dst[6] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[7] = (l2 >>> 12) & 1048575L;
      dst[8] = (l2 >>> 32) & 1048575L;
      dst[9] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[10] = (l3 >>> 8) & 1048575L;
      dst[11] = (l3 >>> 28) & 1048575L;
      dst[12] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[13] = (l4 >>> 4) & 1048575L;
      dst[14] = (l4 >>> 24) & 1048575L;
      dst[15] = l4 >>> 44;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 1048575L;
      dst[17] = (l0 >>> 20) & 1048575L;
      dst[18] = (l0 >>> 40) & 1048575L;
      dst[19] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[20] = (l1 >>> 16) & 1048575L;
      dst[21] = (l1 >>> 36) & 1048575L;
      dst[22] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[23] = (l2 >>> 12) & 1048575L;
      dst[24] = (l2 >>> 32) & 1048575L;
      dst[25] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[26] = (l3 >>> 8) & 1048575L;
      dst[27] = (l3 >>> 28) & 1048575L;
      dst[28] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[29] = (l4 >>> 4) & 1048575L;
      dst[30] = (l4 >>> 24) & 1048575L;
      dst[31] = l4 >>> 44;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 1048575L;
      dst[33] = (l0 >>> 20) & 1048575L;
      dst[34] = (l0 >>> 40) & 1048575L;
      dst[35] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[36] = (l1 >>> 16) & 1048575L;
      dst[37] = (l1 >>> 36) & 1048575L;
      dst[38] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[39] = (l2 >>> 12) & 1048575L;
      dst[40] = (l2 >>> 32) & 1048575L;
      dst[41] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[42] = (l3 >>> 8) & 1048575L;
      dst[43] = (l3 >>> 28) & 1048575L;
      dst[44] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[45] = (l4 >>> 4) & 1048575L;
      dst[46] = (l4 >>> 24) & 1048575L;
      dst[47] = l4 >>> 44;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 1048575L;
      dst[49] = (l0 >>> 20) & 1048575L;
      dst[50] = (l0 >>> 40) & 1048575L;
      dst[51] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[52] = (l1 >>> 16) & 1048575L;
      dst[53] = (l1 >>> 36) & 1048575L;
      dst[54] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[55] = (l2 >>> 12) & 1048575L;
      dst[56] = (l2 >>> 32) & 1048575L;
      dst[57] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[58] = (l3 >>> 8) & 1048575L;
      dst[59] = (l3 >>> 28) & 1048575L;
      dst[60] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[61] = (l4 >>> 4) & 1048575L;
      dst[62] = (l4 >>> 24) & 1048575L;
      dst[63] = l4 >>> 44;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 1048575L;
      dst[65] = (l0 >>> 20) & 1048575L;
      dst[66] = (l0 >>> 40) & 1048575L;
      dst[67] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[68] = (l1 >>> 16) & 1048575L;
      dst[69] = (l1 >>> 36) & 1048575L;
      dst[70] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[71] = (l2 >>> 12) & 1048575L;
      dst[72] = (l2 >>> 32) & 1048575L;
      dst[73] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[74] = (l3 >>> 8) & 1048575L;
      dst[75] = (l3 >>> 28) & 1048575L;
      dst[76] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[77] = (l4 >>> 4) & 1048575L;
      dst[78] = (l4 >>> 24) & 1048575L;
      dst[79] = l4 >>> 44;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 1048575L;
      dst[81] = (l0 >>> 20) & 1048575L;
      dst[82] = (l0 >>> 40) & 1048575L;
      dst[83] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[84] = (l1 >>> 16) & 1048575L;
      dst[85] = (l1 >>> 36) & 1048575L;
      dst[86] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[87] = (l2 >>> 12) & 1048575L;
      dst[88] = (l2 >>> 32) & 1048575L;
      dst[89] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[90] = (l3 >>> 8) & 1048575L;
      dst[91] = (l3 >>> 28) & 1048575L;
      dst[92] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[93] = (l4 >>> 4) & 1048575L;
      dst[94] = (l4 >>> 24) & 1048575L;
      dst[95] = l4 >>> 44;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 1048575L;
      dst[97] = (l0 >>> 20) & 1048575L;
      dst[98] = (l0 >>> 40) & 1048575L;
      dst[99] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[100] = (l1 >>> 16) & 1048575L;
      dst[101] = (l1 >>> 36) & 1048575L;
      dst[102] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[103] = (l2 >>> 12) & 1048575L;
      dst[104] = (l2 >>> 32) & 1048575L;
      dst[105] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[106] = (l3 >>> 8) & 1048575L;
      dst[107] = (l3 >>> 28) & 1048575L;
      dst[108] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[109] = (l4 >>> 4) & 1048575L;
      dst[110] = (l4 >>> 24) & 1048575L;
      dst[111] = l4 >>> 44;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 1048575L;
      dst[113] = (l0 >>> 20) & 1048575L;
      dst[114] = (l0 >>> 40) & 1048575L;
      dst[115] = (l0 >>> 60) | ((l1 & 65535L) << 4);
      dst[116] = (l1 >>> 16) & 1048575L;
      dst[117] = (l1 >>> 36) & 1048575L;
      dst[118] = (l1 >>> 56) | ((l2 & 4095L) << 8);
      dst[119] = (l2 >>> 12) & 1048575L;
      dst[120] = (l2 >>> 32) & 1048575L;
      dst[121] = (l2 >>> 52) | ((l3 & 255L) << 12);
      dst[122] = (l3 >>> 8) & 1048575L;
      dst[123] = (l3 >>> 28) & 1048575L;
      dst[124] = (l3 >>> 48) | ((l4 & 15L) << 16);
      dst[125] = (l4 >>> 4) & 1048575L;
      dst[126] = (l4 >>> 24) & 1048575L;
      dst[127] = l4 >>> 44;
    }
  }

  static void decode24(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 16777215L;
      dst[1] = (l0 >>> 24) & 16777215L;
      dst[2] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[3] = (l1 >>> 8) & 16777215L;
      dst[4] = (l1 >>> 32) & 16777215L;
      dst[5] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[6] = (l2 >>> 16) & 16777215L;
      dst[7] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 & 16777215L;
      dst[9] = (l0 >>> 24) & 16777215L;
      dst[10] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[11] = (l1 >>> 8) & 16777215L;
      dst[12] = (l1 >>> 32) & 16777215L;
      dst[13] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[14] = (l2 >>> 16) & 16777215L;
      dst[15] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 16777215L;
      dst[17] = (l0 >>> 24) & 16777215L;
      dst[18] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[19] = (l1 >>> 8) & 16777215L;
      dst[20] = (l1 >>> 32) & 16777215L;
      dst[21] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[22] = (l2 >>> 16) & 16777215L;
      dst[23] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 & 16777215L;
      dst[25] = (l0 >>> 24) & 16777215L;
      dst[26] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[27] = (l1 >>> 8) & 16777215L;
      dst[28] = (l1 >>> 32) & 16777215L;
      dst[29] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[30] = (l2 >>> 16) & 16777215L;
      dst[31] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 16777215L;
      dst[33] = (l0 >>> 24) & 16777215L;
      dst[34] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[35] = (l1 >>> 8) & 16777215L;
      dst[36] = (l1 >>> 32) & 16777215L;
      dst[37] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[38] = (l2 >>> 16) & 16777215L;
      dst[39] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 & 16777215L;
      dst[41] = (l0 >>> 24) & 16777215L;
      dst[42] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[43] = (l1 >>> 8) & 16777215L;
      dst[44] = (l1 >>> 32) & 16777215L;
      dst[45] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[46] = (l2 >>> 16) & 16777215L;
      dst[47] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 16777215L;
      dst[49] = (l0 >>> 24) & 16777215L;
      dst[50] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[51] = (l1 >>> 8) & 16777215L;
      dst[52] = (l1 >>> 32) & 16777215L;
      dst[53] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[54] = (l2 >>> 16) & 16777215L;
      dst[55] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 & 16777215L;
      dst[57] = (l0 >>> 24) & 16777215L;
      dst[58] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[59] = (l1 >>> 8) & 16777215L;
      dst[60] = (l1 >>> 32) & 16777215L;
      dst[61] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[62] = (l2 >>> 16) & 16777215L;
      dst[63] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 16777215L;
      dst[65] = (l0 >>> 24) & 16777215L;
      dst[66] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[67] = (l1 >>> 8) & 16777215L;
      dst[68] = (l1 >>> 32) & 16777215L;
      dst[69] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[70] = (l2 >>> 16) & 16777215L;
      dst[71] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 & 16777215L;
      dst[73] = (l0 >>> 24) & 16777215L;
      dst[74] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[75] = (l1 >>> 8) & 16777215L;
      dst[76] = (l1 >>> 32) & 16777215L;
      dst[77] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[78] = (l2 >>> 16) & 16777215L;
      dst[79] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 16777215L;
      dst[81] = (l0 >>> 24) & 16777215L;
      dst[82] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[83] = (l1 >>> 8) & 16777215L;
      dst[84] = (l1 >>> 32) & 16777215L;
      dst[85] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[86] = (l2 >>> 16) & 16777215L;
      dst[87] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 & 16777215L;
      dst[89] = (l0 >>> 24) & 16777215L;
      dst[90] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[91] = (l1 >>> 8) & 16777215L;
      dst[92] = (l1 >>> 32) & 16777215L;
      dst[93] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[94] = (l2 >>> 16) & 16777215L;
      dst[95] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 16777215L;
      dst[97] = (l0 >>> 24) & 16777215L;
      dst[98] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[99] = (l1 >>> 8) & 16777215L;
      dst[100] = (l1 >>> 32) & 16777215L;
      dst[101] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[102] = (l2 >>> 16) & 16777215L;
      dst[103] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 & 16777215L;
      dst[105] = (l0 >>> 24) & 16777215L;
      dst[106] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[107] = (l1 >>> 8) & 16777215L;
      dst[108] = (l1 >>> 32) & 16777215L;
      dst[109] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[110] = (l2 >>> 16) & 16777215L;
      dst[111] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 16777215L;
      dst[113] = (l0 >>> 24) & 16777215L;
      dst[114] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[115] = (l1 >>> 8) & 16777215L;
      dst[116] = (l1 >>> 32) & 16777215L;
      dst[117] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[118] = (l2 >>> 16) & 16777215L;
      dst[119] = l2 >>> 40;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 & 16777215L;
      dst[121] = (l0 >>> 24) & 16777215L;
      dst[122] = (l0 >>> 48) | ((l1 & 255L) << 16);
      dst[123] = (l1 >>> 8) & 16777215L;
      dst[124] = (l1 >>> 32) & 16777215L;
      dst[125] = (l1 >>> 56) | ((l2 & 65535L) << 8);
      dst[126] = (l2 >>> 16) & 16777215L;
      dst[127] = l2 >>> 40;
    }
  }

  static void decode28(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 268435455L;
      dst[1] = (l0 >>> 28) & 268435455L;
      dst[2] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[3] = (l1 >>> 20) & 268435455L;
      dst[4] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[5] = (l2 >>> 12) & 268435455L;
      dst[6] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[7] = (l3 >>> 4) & 268435455L;
      dst[8] = (l3 >>> 32) & 268435455L;
      dst[9] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[10] = (l4 >>> 24) & 268435455L;
      dst[11] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[12] = (l5 >>> 16) & 268435455L;
      dst[13] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[14] = (l6 >>> 8) & 268435455L;
      dst[15] = l6 >>> 36;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 268435455L;
      dst[17] = (l0 >>> 28) & 268435455L;
      dst[18] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[19] = (l1 >>> 20) & 268435455L;
      dst[20] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[21] = (l2 >>> 12) & 268435455L;
      dst[22] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[23] = (l3 >>> 4) & 268435455L;
      dst[24] = (l3 >>> 32) & 268435455L;
      dst[25] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[26] = (l4 >>> 24) & 268435455L;
      dst[27] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[28] = (l5 >>> 16) & 268435455L;
      dst[29] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[30] = (l6 >>> 8) & 268435455L;
      dst[31] = l6 >>> 36;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 268435455L;
      dst[33] = (l0 >>> 28) & 268435455L;
      dst[34] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[35] = (l1 >>> 20) & 268435455L;
      dst[36] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[37] = (l2 >>> 12) & 268435455L;
      dst[38] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[39] = (l3 >>> 4) & 268435455L;
      dst[40] = (l3 >>> 32) & 268435455L;
      dst[41] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[42] = (l4 >>> 24) & 268435455L;
      dst[43] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[44] = (l5 >>> 16) & 268435455L;
      dst[45] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[46] = (l6 >>> 8) & 268435455L;
      dst[47] = l6 >>> 36;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 268435455L;
      dst[49] = (l0 >>> 28) & 268435455L;
      dst[50] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[51] = (l1 >>> 20) & 268435455L;
      dst[52] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[53] = (l2 >>> 12) & 268435455L;
      dst[54] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[55] = (l3 >>> 4) & 268435455L;
      dst[56] = (l3 >>> 32) & 268435455L;
      dst[57] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[58] = (l4 >>> 24) & 268435455L;
      dst[59] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[60] = (l5 >>> 16) & 268435455L;
      dst[61] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[62] = (l6 >>> 8) & 268435455L;
      dst[63] = l6 >>> 36;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 268435455L;
      dst[65] = (l0 >>> 28) & 268435455L;
      dst[66] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[67] = (l1 >>> 20) & 268435455L;
      dst[68] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[69] = (l2 >>> 12) & 268435455L;
      dst[70] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[71] = (l3 >>> 4) & 268435455L;
      dst[72] = (l3 >>> 32) & 268435455L;
      dst[73] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[74] = (l4 >>> 24) & 268435455L;
      dst[75] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[76] = (l5 >>> 16) & 268435455L;
      dst[77] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[78] = (l6 >>> 8) & 268435455L;
      dst[79] = l6 >>> 36;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 268435455L;
      dst[81] = (l0 >>> 28) & 268435455L;
      dst[82] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[83] = (l1 >>> 20) & 268435455L;
      dst[84] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[85] = (l2 >>> 12) & 268435455L;
      dst[86] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[87] = (l3 >>> 4) & 268435455L;
      dst[88] = (l3 >>> 32) & 268435455L;
      dst[89] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[90] = (l4 >>> 24) & 268435455L;
      dst[91] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[92] = (l5 >>> 16) & 268435455L;
      dst[93] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[94] = (l6 >>> 8) & 268435455L;
      dst[95] = l6 >>> 36;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 268435455L;
      dst[97] = (l0 >>> 28) & 268435455L;
      dst[98] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[99] = (l1 >>> 20) & 268435455L;
      dst[100] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[101] = (l2 >>> 12) & 268435455L;
      dst[102] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[103] = (l3 >>> 4) & 268435455L;
      dst[104] = (l3 >>> 32) & 268435455L;
      dst[105] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[106] = (l4 >>> 24) & 268435455L;
      dst[107] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[108] = (l5 >>> 16) & 268435455L;
      dst[109] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[110] = (l6 >>> 8) & 268435455L;
      dst[111] = l6 >>> 36;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 268435455L;
      dst[113] = (l0 >>> 28) & 268435455L;
      dst[114] = (l0 >>> 56) | ((l1 & 1048575L) << 8);
      dst[115] = (l1 >>> 20) & 268435455L;
      dst[116] = (l1 >>> 48) | ((l2 & 4095L) << 16);
      dst[117] = (l2 >>> 12) & 268435455L;
      dst[118] = (l2 >>> 40) | ((l3 & 15L) << 24);
      dst[119] = (l3 >>> 4) & 268435455L;
      dst[120] = (l3 >>> 32) & 268435455L;
      dst[121] = (l3 >>> 60) | ((l4 & 16777215L) << 4);
      dst[122] = (l4 >>> 24) & 268435455L;
      dst[123] = (l4 >>> 52) | ((l5 & 65535L) << 12);
      dst[124] = (l5 >>> 16) & 268435455L;
      dst[125] = (l5 >>> 44) | ((l6 & 255L) << 20);
      dst[126] = (l6 >>> 8) & 268435455L;
      dst[127] = l6 >>> 36;
    }
  }

  static void decode32(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 4294967295L;
      dst[1] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[2] = l0 & 4294967295L;
      dst[3] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[4] = l0 & 4294967295L;
      dst[5] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[6] = l0 & 4294967295L;
      dst[7] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 & 4294967295L;
      dst[9] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[10] = l0 & 4294967295L;
      dst[11] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[12] = l0 & 4294967295L;
      dst[13] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[14] = l0 & 4294967295L;
      dst[15] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 4294967295L;
      dst[17] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[18] = l0 & 4294967295L;
      dst[19] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[20] = l0 & 4294967295L;
      dst[21] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[22] = l0 & 4294967295L;
      dst[23] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 & 4294967295L;
      dst[25] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[26] = l0 & 4294967295L;
      dst[27] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[28] = l0 & 4294967295L;
      dst[29] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[30] = l0 & 4294967295L;
      dst[31] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 4294967295L;
      dst[33] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[34] = l0 & 4294967295L;
      dst[35] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[36] = l0 & 4294967295L;
      dst[37] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[38] = l0 & 4294967295L;
      dst[39] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 & 4294967295L;
      dst[41] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[42] = l0 & 4294967295L;
      dst[43] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[44] = l0 & 4294967295L;
      dst[45] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[46] = l0 & 4294967295L;
      dst[47] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 4294967295L;
      dst[49] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[50] = l0 & 4294967295L;
      dst[51] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[52] = l0 & 4294967295L;
      dst[53] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[54] = l0 & 4294967295L;
      dst[55] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 & 4294967295L;
      dst[57] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[58] = l0 & 4294967295L;
      dst[59] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[60] = l0 & 4294967295L;
      dst[61] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[62] = l0 & 4294967295L;
      dst[63] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 4294967295L;
      dst[65] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[66] = l0 & 4294967295L;
      dst[67] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[68] = l0 & 4294967295L;
      dst[69] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[70] = l0 & 4294967295L;
      dst[71] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 & 4294967295L;
      dst[73] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[74] = l0 & 4294967295L;
      dst[75] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[76] = l0 & 4294967295L;
      dst[77] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[78] = l0 & 4294967295L;
      dst[79] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 4294967295L;
      dst[81] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[82] = l0 & 4294967295L;
      dst[83] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[84] = l0 & 4294967295L;
      dst[85] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[86] = l0 & 4294967295L;
      dst[87] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 & 4294967295L;
      dst[89] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[90] = l0 & 4294967295L;
      dst[91] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[92] = l0 & 4294967295L;
      dst[93] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[94] = l0 & 4294967295L;
      dst[95] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 4294967295L;
      dst[97] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[98] = l0 & 4294967295L;
      dst[99] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[100] = l0 & 4294967295L;
      dst[101] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[102] = l0 & 4294967295L;
      dst[103] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 & 4294967295L;
      dst[105] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[106] = l0 & 4294967295L;
      dst[107] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[108] = l0 & 4294967295L;
      dst[109] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[110] = l0 & 4294967295L;
      dst[111] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 4294967295L;
      dst[113] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[114] = l0 & 4294967295L;
      dst[115] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[116] = l0 & 4294967295L;
      dst[117] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[118] = l0 & 4294967295L;
      dst[119] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 & 4294967295L;
      dst[121] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[122] = l0 & 4294967295L;
      dst[123] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[124] = l0 & 4294967295L;
      dst[125] = l0 >>> 32;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[126] = l0 & 4294967295L;
      dst[127] = l0 >>> 32;
    }
  }

  static void decode40(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 1099511627775L;
      dst[1] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[2] = (l1 >>> 16) & 1099511627775L;
      dst[3] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[4] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[5] = (l3 >>> 8) & 1099511627775L;
      dst[6] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[7] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 & 1099511627775L;
      dst[9] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[10] = (l1 >>> 16) & 1099511627775L;
      dst[11] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[12] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[13] = (l3 >>> 8) & 1099511627775L;
      dst[14] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[15] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 1099511627775L;
      dst[17] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[18] = (l1 >>> 16) & 1099511627775L;
      dst[19] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[20] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[21] = (l3 >>> 8) & 1099511627775L;
      dst[22] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[23] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 & 1099511627775L;
      dst[25] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[26] = (l1 >>> 16) & 1099511627775L;
      dst[27] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[28] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[29] = (l3 >>> 8) & 1099511627775L;
      dst[30] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[31] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 1099511627775L;
      dst[33] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[34] = (l1 >>> 16) & 1099511627775L;
      dst[35] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[36] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[37] = (l3 >>> 8) & 1099511627775L;
      dst[38] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[39] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 & 1099511627775L;
      dst[41] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[42] = (l1 >>> 16) & 1099511627775L;
      dst[43] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[44] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[45] = (l3 >>> 8) & 1099511627775L;
      dst[46] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[47] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 1099511627775L;
      dst[49] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[50] = (l1 >>> 16) & 1099511627775L;
      dst[51] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[52] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[53] = (l3 >>> 8) & 1099511627775L;
      dst[54] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[55] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 & 1099511627775L;
      dst[57] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[58] = (l1 >>> 16) & 1099511627775L;
      dst[59] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[60] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[61] = (l3 >>> 8) & 1099511627775L;
      dst[62] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[63] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 1099511627775L;
      dst[65] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[66] = (l1 >>> 16) & 1099511627775L;
      dst[67] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[68] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[69] = (l3 >>> 8) & 1099511627775L;
      dst[70] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[71] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 & 1099511627775L;
      dst[73] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[74] = (l1 >>> 16) & 1099511627775L;
      dst[75] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[76] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[77] = (l3 >>> 8) & 1099511627775L;
      dst[78] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[79] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 1099511627775L;
      dst[81] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[82] = (l1 >>> 16) & 1099511627775L;
      dst[83] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[84] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[85] = (l3 >>> 8) & 1099511627775L;
      dst[86] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[87] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 & 1099511627775L;
      dst[89] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[90] = (l1 >>> 16) & 1099511627775L;
      dst[91] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[92] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[93] = (l3 >>> 8) & 1099511627775L;
      dst[94] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[95] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 1099511627775L;
      dst[97] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[98] = (l1 >>> 16) & 1099511627775L;
      dst[99] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[100] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[101] = (l3 >>> 8) & 1099511627775L;
      dst[102] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[103] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 & 1099511627775L;
      dst[105] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[106] = (l1 >>> 16) & 1099511627775L;
      dst[107] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[108] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[109] = (l3 >>> 8) & 1099511627775L;
      dst[110] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[111] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 1099511627775L;
      dst[113] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[114] = (l1 >>> 16) & 1099511627775L;
      dst[115] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[116] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[117] = (l3 >>> 8) & 1099511627775L;
      dst[118] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[119] = l4 >>> 24;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 & 1099511627775L;
      dst[121] = (l0 >>> 40) | ((l1 & 65535L) << 24);
      dst[122] = (l1 >>> 16) & 1099511627775L;
      dst[123] = (l1 >>> 56) | ((l2 & 4294967295L) << 8);
      dst[124] = (l2 >>> 32) | ((l3 & 255L) << 32);
      dst[125] = (l3 >>> 8) & 1099511627775L;
      dst[126] = (l3 >>> 48) | ((l4 & 16777215L) << 16);
      dst[127] = l4 >>> 24;
    }
  }

  static void decode48(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 281474976710655L;
      dst[1] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[2] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[3] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[4] = l0 & 281474976710655L;
      dst[5] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[6] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[7] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 & 281474976710655L;
      dst[9] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[10] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[11] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[12] = l0 & 281474976710655L;
      dst[13] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[14] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[15] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 281474976710655L;
      dst[17] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[18] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[19] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[20] = l0 & 281474976710655L;
      dst[21] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[22] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[23] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 & 281474976710655L;
      dst[25] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[26] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[27] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[28] = l0 & 281474976710655L;
      dst[29] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[30] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[31] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 281474976710655L;
      dst[33] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[34] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[35] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[36] = l0 & 281474976710655L;
      dst[37] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[38] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[39] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 & 281474976710655L;
      dst[41] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[42] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[43] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[44] = l0 & 281474976710655L;
      dst[45] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[46] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[47] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 281474976710655L;
      dst[49] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[50] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[51] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[52] = l0 & 281474976710655L;
      dst[53] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[54] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[55] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 & 281474976710655L;
      dst[57] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[58] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[59] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[60] = l0 & 281474976710655L;
      dst[61] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[62] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[63] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 281474976710655L;
      dst[65] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[66] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[67] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[68] = l0 & 281474976710655L;
      dst[69] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[70] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[71] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 & 281474976710655L;
      dst[73] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[74] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[75] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[76] = l0 & 281474976710655L;
      dst[77] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[78] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[79] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 281474976710655L;
      dst[81] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[82] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[83] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[84] = l0 & 281474976710655L;
      dst[85] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[86] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[87] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 & 281474976710655L;
      dst[89] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[90] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[91] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[92] = l0 & 281474976710655L;
      dst[93] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[94] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[95] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 281474976710655L;
      dst[97] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[98] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[99] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[100] = l0 & 281474976710655L;
      dst[101] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[102] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[103] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 & 281474976710655L;
      dst[105] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[106] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[107] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[108] = l0 & 281474976710655L;
      dst[109] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[110] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[111] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 281474976710655L;
      dst[113] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[114] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[115] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[116] = l0 & 281474976710655L;
      dst[117] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[118] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[119] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 & 281474976710655L;
      dst[121] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[122] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[123] = l2 >>> 16;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      dst[124] = l0 & 281474976710655L;
      dst[125] = (l0 >>> 48) | ((l1 & 4294967295L) << 16);
      dst[126] = (l1 >>> 32) | ((l2 & 65535L) << 32);
      dst[127] = l2 >>> 16;
    }
  }

  static void decode56(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 & 72057594037927935L;
      dst[1] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[2] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[3] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[4] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[5] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[6] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[7] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 & 72057594037927935L;
      dst[9] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[10] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[11] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[12] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[13] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[14] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[15] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 & 72057594037927935L;
      dst[17] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[18] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[19] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[20] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[21] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[22] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[23] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 & 72057594037927935L;
      dst[25] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[26] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[27] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[28] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[29] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[30] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[31] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 & 72057594037927935L;
      dst[33] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[34] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[35] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[36] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[37] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[38] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[39] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 & 72057594037927935L;
      dst[41] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[42] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[43] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[44] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[45] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[46] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[47] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 & 72057594037927935L;
      dst[49] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[50] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[51] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[52] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[53] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[54] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[55] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 & 72057594037927935L;
      dst[57] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[58] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[59] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[60] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[61] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[62] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[63] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 & 72057594037927935L;
      dst[65] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[66] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[67] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[68] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[69] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[70] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[71] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 & 72057594037927935L;
      dst[73] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[74] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[75] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[76] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[77] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[78] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[79] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 & 72057594037927935L;
      dst[81] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[82] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[83] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[84] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[85] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[86] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[87] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 & 72057594037927935L;
      dst[89] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[90] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[91] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[92] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[93] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[94] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[95] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 & 72057594037927935L;
      dst[97] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[98] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[99] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[100] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[101] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[102] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[103] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 & 72057594037927935L;
      dst[105] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[106] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[107] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[108] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[109] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[110] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[111] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 & 72057594037927935L;
      dst[113] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[114] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[115] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[116] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[117] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[118] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[119] = l6 >>> 8;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      long l1 = src.readLong(pos);
      pos += Long.BYTES;
      long l2 = src.readLong(pos);
      pos += Long.BYTES;
      long l3 = src.readLong(pos);
      pos += Long.BYTES;
      long l4 = src.readLong(pos);
      pos += Long.BYTES;
      long l5 = src.readLong(pos);
      pos += Long.BYTES;
      long l6 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 & 72057594037927935L;
      dst[121] = (l0 >>> 56) | ((l1 & 281474976710655L) << 8);
      dst[122] = (l1 >>> 48) | ((l2 & 1099511627775L) << 16);
      dst[123] = (l2 >>> 40) | ((l3 & 4294967295L) << 24);
      dst[124] = (l3 >>> 32) | ((l4 & 16777215L) << 32);
      dst[125] = (l4 >>> 24) | ((l5 & 65535L) << 40);
      dst[126] = (l5 >>> 16) | ((l6 & 255L) << 48);
      dst[127] = l6 >>> 8;
    }
  }

  static void decode64(RandomAccessInput src, long offset, long[] dst) throws IOException {
    long pos = offset;
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[0] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[1] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[2] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[3] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[4] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[5] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[6] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[7] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[8] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[9] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[10] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[11] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[12] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[13] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[14] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[15] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[16] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[17] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[18] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[19] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[20] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[21] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[22] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[23] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[24] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[25] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[26] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[27] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[28] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[29] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[30] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[31] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[32] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[33] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[34] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[35] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[36] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[37] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[38] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[39] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[40] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[41] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[42] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[43] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[44] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[45] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[46] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[47] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[48] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[49] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[50] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[51] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[52] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[53] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[54] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[55] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[56] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[57] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[58] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[59] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[60] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[61] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[62] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[63] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[64] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[65] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[66] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[67] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[68] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[69] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[70] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[71] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[72] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[73] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[74] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[75] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[76] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[77] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[78] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[79] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[80] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[81] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[82] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[83] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[84] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[85] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[86] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[87] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[88] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[89] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[90] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[91] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[92] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[93] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[94] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[95] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[96] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[97] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[98] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[99] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[100] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[101] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[102] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[103] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[104] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[105] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[106] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[107] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[108] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[109] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[110] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[111] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[112] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[113] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[114] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[115] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[116] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[117] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[118] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[119] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[120] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[121] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[122] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[123] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[124] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[125] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[126] = l0 >>> 0;
    }
    {
      long l0 = src.readLong(pos);
      pos += Long.BYTES;
      dst[127] = l0 >>> 0;
    }
  }
}
