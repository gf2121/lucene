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

/** Unrolling block decoder */
class UnrollingDecoder {

  static void decode1(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      dst[0] = l0;
      dst[1] = (l0 >>> 1);
      dst[2] = (l0 >>> 2);
      dst[3] = (l0 >>> 3);
      dst[4] = (l0 >>> 4);
      dst[5] = (l0 >>> 5);
      dst[6] = (l0 >>> 6);
      dst[7] = (l0 >>> 7);
      dst[8] = (l0 >>> 8);
      dst[9] = (l0 >>> 9);
      dst[10] = (l0 >>> 10);
      dst[11] = (l0 >>> 11);
      dst[12] = (l0 >>> 12);
      dst[13] = (l0 >>> 13);
      dst[14] = (l0 >>> 14);
      dst[15] = (l0 >>> 15);
      dst[16] = (l0 >>> 16);
      dst[17] = (l0 >>> 17);
      dst[18] = (l0 >>> 18);
      dst[19] = (l0 >>> 19);
      dst[20] = (l0 >>> 20);
      dst[21] = (l0 >>> 21);
      dst[22] = (l0 >>> 22);
      dst[23] = (l0 >>> 23);
      dst[24] = (l0 >>> 24);
      dst[25] = (l0 >>> 25);
      dst[26] = (l0 >>> 26);
      dst[27] = (l0 >>> 27);
      dst[28] = (l0 >>> 28);
      dst[29] = (l0 >>> 29);
      dst[30] = (l0 >>> 30);
      dst[31] = (l0 >>> 31);
      dst[32] = (l0 >>> 32);
      dst[33] = (l0 >>> 33);
      dst[34] = (l0 >>> 34);
      dst[35] = (l0 >>> 35);
      dst[36] = (l0 >>> 36);
      dst[37] = (l0 >>> 37);
      dst[38] = (l0 >>> 38);
      dst[39] = (l0 >>> 39);
      dst[40] = (l0 >>> 40);
      dst[41] = (l0 >>> 41);
      dst[42] = (l0 >>> 42);
      dst[43] = (l0 >>> 43);
      dst[44] = (l0 >>> 44);
      dst[45] = (l0 >>> 45);
      dst[46] = (l0 >>> 46);
      dst[47] = (l0 >>> 47);
      dst[48] = (l0 >>> 48);
      dst[49] = (l0 >>> 49);
      dst[50] = (l0 >>> 50);
      dst[51] = (l0 >>> 51);
      dst[52] = (l0 >>> 52);
      dst[53] = (l0 >>> 53);
      dst[54] = (l0 >>> 54);
      dst[55] = (l0 >>> 55);
      dst[56] = (l0 >>> 56);
      dst[57] = (l0 >>> 57);
      dst[58] = (l0 >>> 58);
      dst[59] = (l0 >>> 59);
      dst[60] = (l0 >>> 60);
      dst[61] = (l0 >>> 61);
      dst[62] = (l0 >>> 62);
      dst[63] = l0 >>> 63;
    }
    {
      final long l0 = src[1];
      dst[64] = l0;
      dst[65] = (l0 >>> 1);
      dst[66] = (l0 >>> 2);
      dst[67] = (l0 >>> 3);
      dst[68] = (l0 >>> 4);
      dst[69] = (l0 >>> 5);
      dst[70] = (l0 >>> 6);
      dst[71] = (l0 >>> 7);
      dst[72] = (l0 >>> 8);
      dst[73] = (l0 >>> 9);
      dst[74] = (l0 >>> 10);
      dst[75] = (l0 >>> 11);
      dst[76] = (l0 >>> 12);
      dst[77] = (l0 >>> 13);
      dst[78] = (l0 >>> 14);
      dst[79] = (l0 >>> 15);
      dst[80] = (l0 >>> 16);
      dst[81] = (l0 >>> 17);
      dst[82] = (l0 >>> 18);
      dst[83] = (l0 >>> 19);
      dst[84] = (l0 >>> 20);
      dst[85] = (l0 >>> 21);
      dst[86] = (l0 >>> 22);
      dst[87] = (l0 >>> 23);
      dst[88] = (l0 >>> 24);
      dst[89] = (l0 >>> 25);
      dst[90] = (l0 >>> 26);
      dst[91] = (l0 >>> 27);
      dst[92] = (l0 >>> 28);
      dst[93] = (l0 >>> 29);
      dst[94] = (l0 >>> 30);
      dst[95] = (l0 >>> 31);
      dst[96] = (l0 >>> 32);
      dst[97] = (l0 >>> 33);
      dst[98] = (l0 >>> 34);
      dst[99] = (l0 >>> 35);
      dst[100] = (l0 >>> 36);
      dst[101] = (l0 >>> 37);
      dst[102] = (l0 >>> 38);
      dst[103] = (l0 >>> 39);
      dst[104] = (l0 >>> 40);
      dst[105] = (l0 >>> 41);
      dst[106] = (l0 >>> 42);
      dst[107] = (l0 >>> 43);
      dst[108] = (l0 >>> 44);
      dst[109] = (l0 >>> 45);
      dst[110] = (l0 >>> 46);
      dst[111] = (l0 >>> 47);
      dst[112] = (l0 >>> 48);
      dst[113] = (l0 >>> 49);
      dst[114] = (l0 >>> 50);
      dst[115] = (l0 >>> 51);
      dst[116] = (l0 >>> 52);
      dst[117] = (l0 >>> 53);
      dst[118] = (l0 >>> 54);
      dst[119] = (l0 >>> 55);
      dst[120] = (l0 >>> 56);
      dst[121] = (l0 >>> 57);
      dst[122] = (l0 >>> 58);
      dst[123] = (l0 >>> 59);
      dst[124] = (l0 >>> 60);
      dst[125] = (l0 >>> 61);
      dst[126] = (l0 >>> 62);
      dst[127] = l0 >>> 63;
    }
    maskLongs(dst, 1L);
  }

  static void decode2(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      dst[0] = l0;
      dst[1] = (l0 >>> 2);
      dst[2] = (l0 >>> 4);
      dst[3] = (l0 >>> 6);
      dst[4] = (l0 >>> 8);
      dst[5] = (l0 >>> 10);
      dst[6] = (l0 >>> 12);
      dst[7] = (l0 >>> 14);
      dst[8] = (l0 >>> 16);
      dst[9] = (l0 >>> 18);
      dst[10] = (l0 >>> 20);
      dst[11] = (l0 >>> 22);
      dst[12] = (l0 >>> 24);
      dst[13] = (l0 >>> 26);
      dst[14] = (l0 >>> 28);
      dst[15] = (l0 >>> 30);
      dst[16] = (l0 >>> 32);
      dst[17] = (l0 >>> 34);
      dst[18] = (l0 >>> 36);
      dst[19] = (l0 >>> 38);
      dst[20] = (l0 >>> 40);
      dst[21] = (l0 >>> 42);
      dst[22] = (l0 >>> 44);
      dst[23] = (l0 >>> 46);
      dst[24] = (l0 >>> 48);
      dst[25] = (l0 >>> 50);
      dst[26] = (l0 >>> 52);
      dst[27] = (l0 >>> 54);
      dst[28] = (l0 >>> 56);
      dst[29] = (l0 >>> 58);
      dst[30] = (l0 >>> 60);
      dst[31] = l0 >>> 62;
    }
    {
      final long l0 = src[1];
      dst[32] = l0;
      dst[33] = (l0 >>> 2);
      dst[34] = (l0 >>> 4);
      dst[35] = (l0 >>> 6);
      dst[36] = (l0 >>> 8);
      dst[37] = (l0 >>> 10);
      dst[38] = (l0 >>> 12);
      dst[39] = (l0 >>> 14);
      dst[40] = (l0 >>> 16);
      dst[41] = (l0 >>> 18);
      dst[42] = (l0 >>> 20);
      dst[43] = (l0 >>> 22);
      dst[44] = (l0 >>> 24);
      dst[45] = (l0 >>> 26);
      dst[46] = (l0 >>> 28);
      dst[47] = (l0 >>> 30);
      dst[48] = (l0 >>> 32);
      dst[49] = (l0 >>> 34);
      dst[50] = (l0 >>> 36);
      dst[51] = (l0 >>> 38);
      dst[52] = (l0 >>> 40);
      dst[53] = (l0 >>> 42);
      dst[54] = (l0 >>> 44);
      dst[55] = (l0 >>> 46);
      dst[56] = (l0 >>> 48);
      dst[57] = (l0 >>> 50);
      dst[58] = (l0 >>> 52);
      dst[59] = (l0 >>> 54);
      dst[60] = (l0 >>> 56);
      dst[61] = (l0 >>> 58);
      dst[62] = (l0 >>> 60);
      dst[63] = l0 >>> 62;
    }
    {
      final long l0 = src[2];
      dst[64] = l0;
      dst[65] = (l0 >>> 2);
      dst[66] = (l0 >>> 4);
      dst[67] = (l0 >>> 6);
      dst[68] = (l0 >>> 8);
      dst[69] = (l0 >>> 10);
      dst[70] = (l0 >>> 12);
      dst[71] = (l0 >>> 14);
      dst[72] = (l0 >>> 16);
      dst[73] = (l0 >>> 18);
      dst[74] = (l0 >>> 20);
      dst[75] = (l0 >>> 22);
      dst[76] = (l0 >>> 24);
      dst[77] = (l0 >>> 26);
      dst[78] = (l0 >>> 28);
      dst[79] = (l0 >>> 30);
      dst[80] = (l0 >>> 32);
      dst[81] = (l0 >>> 34);
      dst[82] = (l0 >>> 36);
      dst[83] = (l0 >>> 38);
      dst[84] = (l0 >>> 40);
      dst[85] = (l0 >>> 42);
      dst[86] = (l0 >>> 44);
      dst[87] = (l0 >>> 46);
      dst[88] = (l0 >>> 48);
      dst[89] = (l0 >>> 50);
      dst[90] = (l0 >>> 52);
      dst[91] = (l0 >>> 54);
      dst[92] = (l0 >>> 56);
      dst[93] = (l0 >>> 58);
      dst[94] = (l0 >>> 60);
      dst[95] = l0 >>> 62;
    }
    {
      final long l0 = src[3];
      dst[96] = l0;
      dst[97] = (l0 >>> 2);
      dst[98] = (l0 >>> 4);
      dst[99] = (l0 >>> 6);
      dst[100] = (l0 >>> 8);
      dst[101] = (l0 >>> 10);
      dst[102] = (l0 >>> 12);
      dst[103] = (l0 >>> 14);
      dst[104] = (l0 >>> 16);
      dst[105] = (l0 >>> 18);
      dst[106] = (l0 >>> 20);
      dst[107] = (l0 >>> 22);
      dst[108] = (l0 >>> 24);
      dst[109] = (l0 >>> 26);
      dst[110] = (l0 >>> 28);
      dst[111] = (l0 >>> 30);
      dst[112] = (l0 >>> 32);
      dst[113] = (l0 >>> 34);
      dst[114] = (l0 >>> 36);
      dst[115] = (l0 >>> 38);
      dst[116] = (l0 >>> 40);
      dst[117] = (l0 >>> 42);
      dst[118] = (l0 >>> 44);
      dst[119] = (l0 >>> 46);
      dst[120] = (l0 >>> 48);
      dst[121] = (l0 >>> 50);
      dst[122] = (l0 >>> 52);
      dst[123] = (l0 >>> 54);
      dst[124] = (l0 >>> 56);
      dst[125] = (l0 >>> 58);
      dst[126] = (l0 >>> 60);
      dst[127] = l0 >>> 62;
    }
    maskLongs(dst, 3L);
  }

  static void decode4(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      dst[0] = l0;
      dst[1] = (l0 >>> 4);
      dst[2] = (l0 >>> 8);
      dst[3] = (l0 >>> 12);
      dst[4] = (l0 >>> 16);
      dst[5] = (l0 >>> 20);
      dst[6] = (l0 >>> 24);
      dst[7] = (l0 >>> 28);
      dst[8] = (l0 >>> 32);
      dst[9] = (l0 >>> 36);
      dst[10] = (l0 >>> 40);
      dst[11] = (l0 >>> 44);
      dst[12] = (l0 >>> 48);
      dst[13] = (l0 >>> 52);
      dst[14] = (l0 >>> 56);
      dst[15] = l0 >>> 60;
    }
    {
      final long l0 = src[1];
      dst[16] = l0;
      dst[17] = (l0 >>> 4);
      dst[18] = (l0 >>> 8);
      dst[19] = (l0 >>> 12);
      dst[20] = (l0 >>> 16);
      dst[21] = (l0 >>> 20);
      dst[22] = (l0 >>> 24);
      dst[23] = (l0 >>> 28);
      dst[24] = (l0 >>> 32);
      dst[25] = (l0 >>> 36);
      dst[26] = (l0 >>> 40);
      dst[27] = (l0 >>> 44);
      dst[28] = (l0 >>> 48);
      dst[29] = (l0 >>> 52);
      dst[30] = (l0 >>> 56);
      dst[31] = l0 >>> 60;
    }
    {
      final long l0 = src[2];
      dst[32] = l0;
      dst[33] = (l0 >>> 4);
      dst[34] = (l0 >>> 8);
      dst[35] = (l0 >>> 12);
      dst[36] = (l0 >>> 16);
      dst[37] = (l0 >>> 20);
      dst[38] = (l0 >>> 24);
      dst[39] = (l0 >>> 28);
      dst[40] = (l0 >>> 32);
      dst[41] = (l0 >>> 36);
      dst[42] = (l0 >>> 40);
      dst[43] = (l0 >>> 44);
      dst[44] = (l0 >>> 48);
      dst[45] = (l0 >>> 52);
      dst[46] = (l0 >>> 56);
      dst[47] = l0 >>> 60;
    }
    {
      final long l0 = src[3];
      dst[48] = l0;
      dst[49] = (l0 >>> 4);
      dst[50] = (l0 >>> 8);
      dst[51] = (l0 >>> 12);
      dst[52] = (l0 >>> 16);
      dst[53] = (l0 >>> 20);
      dst[54] = (l0 >>> 24);
      dst[55] = (l0 >>> 28);
      dst[56] = (l0 >>> 32);
      dst[57] = (l0 >>> 36);
      dst[58] = (l0 >>> 40);
      dst[59] = (l0 >>> 44);
      dst[60] = (l0 >>> 48);
      dst[61] = (l0 >>> 52);
      dst[62] = (l0 >>> 56);
      dst[63] = l0 >>> 60;
    }
    {
      final long l0 = src[4];
      dst[64] = l0;
      dst[65] = (l0 >>> 4);
      dst[66] = (l0 >>> 8);
      dst[67] = (l0 >>> 12);
      dst[68] = (l0 >>> 16);
      dst[69] = (l0 >>> 20);
      dst[70] = (l0 >>> 24);
      dst[71] = (l0 >>> 28);
      dst[72] = (l0 >>> 32);
      dst[73] = (l0 >>> 36);
      dst[74] = (l0 >>> 40);
      dst[75] = (l0 >>> 44);
      dst[76] = (l0 >>> 48);
      dst[77] = (l0 >>> 52);
      dst[78] = (l0 >>> 56);
      dst[79] = l0 >>> 60;
    }
    {
      final long l0 = src[5];
      dst[80] = l0;
      dst[81] = (l0 >>> 4);
      dst[82] = (l0 >>> 8);
      dst[83] = (l0 >>> 12);
      dst[84] = (l0 >>> 16);
      dst[85] = (l0 >>> 20);
      dst[86] = (l0 >>> 24);
      dst[87] = (l0 >>> 28);
      dst[88] = (l0 >>> 32);
      dst[89] = (l0 >>> 36);
      dst[90] = (l0 >>> 40);
      dst[91] = (l0 >>> 44);
      dst[92] = (l0 >>> 48);
      dst[93] = (l0 >>> 52);
      dst[94] = (l0 >>> 56);
      dst[95] = l0 >>> 60;
    }
    {
      final long l0 = src[6];
      dst[96] = l0;
      dst[97] = (l0 >>> 4);
      dst[98] = (l0 >>> 8);
      dst[99] = (l0 >>> 12);
      dst[100] = (l0 >>> 16);
      dst[101] = (l0 >>> 20);
      dst[102] = (l0 >>> 24);
      dst[103] = (l0 >>> 28);
      dst[104] = (l0 >>> 32);
      dst[105] = (l0 >>> 36);
      dst[106] = (l0 >>> 40);
      dst[107] = (l0 >>> 44);
      dst[108] = (l0 >>> 48);
      dst[109] = (l0 >>> 52);
      dst[110] = (l0 >>> 56);
      dst[111] = l0 >>> 60;
    }
    {
      final long l0 = src[7];
      dst[112] = l0;
      dst[113] = (l0 >>> 4);
      dst[114] = (l0 >>> 8);
      dst[115] = (l0 >>> 12);
      dst[116] = (l0 >>> 16);
      dst[117] = (l0 >>> 20);
      dst[118] = (l0 >>> 24);
      dst[119] = (l0 >>> 28);
      dst[120] = (l0 >>> 32);
      dst[121] = (l0 >>> 36);
      dst[122] = (l0 >>> 40);
      dst[123] = (l0 >>> 44);
      dst[124] = (l0 >>> 48);
      dst[125] = (l0 >>> 52);
      dst[126] = (l0 >>> 56);
      dst[127] = l0 >>> 60;
    }
    maskLongs(dst, 15L);
  }

  static void decode8(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      dst[0] = l0;
      dst[1] = (l0 >>> 8);
      dst[2] = (l0 >>> 16);
      dst[3] = (l0 >>> 24);
      dst[4] = (l0 >>> 32);
      dst[5] = (l0 >>> 40);
      dst[6] = (l0 >>> 48);
      dst[7] = l0 >>> 56;
    }
    {
      final long l0 = src[1];
      dst[8] = l0;
      dst[9] = (l0 >>> 8);
      dst[10] = (l0 >>> 16);
      dst[11] = (l0 >>> 24);
      dst[12] = (l0 >>> 32);
      dst[13] = (l0 >>> 40);
      dst[14] = (l0 >>> 48);
      dst[15] = l0 >>> 56;
    }
    {
      final long l0 = src[2];
      dst[16] = l0;
      dst[17] = (l0 >>> 8);
      dst[18] = (l0 >>> 16);
      dst[19] = (l0 >>> 24);
      dst[20] = (l0 >>> 32);
      dst[21] = (l0 >>> 40);
      dst[22] = (l0 >>> 48);
      dst[23] = l0 >>> 56;
    }
    {
      final long l0 = src[3];
      dst[24] = l0;
      dst[25] = (l0 >>> 8);
      dst[26] = (l0 >>> 16);
      dst[27] = (l0 >>> 24);
      dst[28] = (l0 >>> 32);
      dst[29] = (l0 >>> 40);
      dst[30] = (l0 >>> 48);
      dst[31] = l0 >>> 56;
    }
    {
      final long l0 = src[4];
      dst[32] = l0;
      dst[33] = (l0 >>> 8);
      dst[34] = (l0 >>> 16);
      dst[35] = (l0 >>> 24);
      dst[36] = (l0 >>> 32);
      dst[37] = (l0 >>> 40);
      dst[38] = (l0 >>> 48);
      dst[39] = l0 >>> 56;
    }
    {
      final long l0 = src[5];
      dst[40] = l0;
      dst[41] = (l0 >>> 8);
      dst[42] = (l0 >>> 16);
      dst[43] = (l0 >>> 24);
      dst[44] = (l0 >>> 32);
      dst[45] = (l0 >>> 40);
      dst[46] = (l0 >>> 48);
      dst[47] = l0 >>> 56;
    }
    {
      final long l0 = src[6];
      dst[48] = l0;
      dst[49] = (l0 >>> 8);
      dst[50] = (l0 >>> 16);
      dst[51] = (l0 >>> 24);
      dst[52] = (l0 >>> 32);
      dst[53] = (l0 >>> 40);
      dst[54] = (l0 >>> 48);
      dst[55] = l0 >>> 56;
    }
    {
      final long l0 = src[7];
      dst[56] = l0;
      dst[57] = (l0 >>> 8);
      dst[58] = (l0 >>> 16);
      dst[59] = (l0 >>> 24);
      dst[60] = (l0 >>> 32);
      dst[61] = (l0 >>> 40);
      dst[62] = (l0 >>> 48);
      dst[63] = l0 >>> 56;
    }
    {
      final long l0 = src[8];
      dst[64] = l0;
      dst[65] = (l0 >>> 8);
      dst[66] = (l0 >>> 16);
      dst[67] = (l0 >>> 24);
      dst[68] = (l0 >>> 32);
      dst[69] = (l0 >>> 40);
      dst[70] = (l0 >>> 48);
      dst[71] = l0 >>> 56;
    }
    {
      final long l0 = src[9];
      dst[72] = l0;
      dst[73] = (l0 >>> 8);
      dst[74] = (l0 >>> 16);
      dst[75] = (l0 >>> 24);
      dst[76] = (l0 >>> 32);
      dst[77] = (l0 >>> 40);
      dst[78] = (l0 >>> 48);
      dst[79] = l0 >>> 56;
    }
    {
      final long l0 = src[10];
      dst[80] = l0;
      dst[81] = (l0 >>> 8);
      dst[82] = (l0 >>> 16);
      dst[83] = (l0 >>> 24);
      dst[84] = (l0 >>> 32);
      dst[85] = (l0 >>> 40);
      dst[86] = (l0 >>> 48);
      dst[87] = l0 >>> 56;
    }
    {
      final long l0 = src[11];
      dst[88] = l0;
      dst[89] = (l0 >>> 8);
      dst[90] = (l0 >>> 16);
      dst[91] = (l0 >>> 24);
      dst[92] = (l0 >>> 32);
      dst[93] = (l0 >>> 40);
      dst[94] = (l0 >>> 48);
      dst[95] = l0 >>> 56;
    }
    {
      final long l0 = src[12];
      dst[96] = l0;
      dst[97] = (l0 >>> 8);
      dst[98] = (l0 >>> 16);
      dst[99] = (l0 >>> 24);
      dst[100] = (l0 >>> 32);
      dst[101] = (l0 >>> 40);
      dst[102] = (l0 >>> 48);
      dst[103] = l0 >>> 56;
    }
    {
      final long l0 = src[13];
      dst[104] = l0;
      dst[105] = (l0 >>> 8);
      dst[106] = (l0 >>> 16);
      dst[107] = (l0 >>> 24);
      dst[108] = (l0 >>> 32);
      dst[109] = (l0 >>> 40);
      dst[110] = (l0 >>> 48);
      dst[111] = l0 >>> 56;
    }
    {
      final long l0 = src[14];
      dst[112] = l0;
      dst[113] = (l0 >>> 8);
      dst[114] = (l0 >>> 16);
      dst[115] = (l0 >>> 24);
      dst[116] = (l0 >>> 32);
      dst[117] = (l0 >>> 40);
      dst[118] = (l0 >>> 48);
      dst[119] = l0 >>> 56;
    }
    {
      final long l0 = src[15];
      dst[120] = l0;
      dst[121] = (l0 >>> 8);
      dst[122] = (l0 >>> 16);
      dst[123] = (l0 >>> 24);
      dst[124] = (l0 >>> 32);
      dst[125] = (l0 >>> 40);
      dst[126] = (l0 >>> 48);
      dst[127] = l0 >>> 56;
    }
    maskLongs(dst, 255L);
  }

  static void decode12(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      final long l1 = src[1];
      final long l2 = src[2];
      dst[0] = l0;
      dst[1] = (l0 >>> 12);
      dst[2] = (l0 >>> 24);
      dst[3] = (l0 >>> 36);
      dst[4] = (l0 >>> 48);
      dst[5] = (l0 >>> 60) | (l1 << 4);
      dst[6] = (l1 >>> 8);
      dst[7] = (l1 >>> 20);
      dst[8] = (l1 >>> 32);
      dst[9] = (l1 >>> 44);
      dst[10] = (l1 >>> 56) | (l2 << 8);
      dst[11] = (l2 >>> 4);
      dst[12] = (l2 >>> 16);
      dst[13] = (l2 >>> 28);
      dst[14] = (l2 >>> 40);
      dst[15] = l2 >>> 52;
    }
    {
      final long l0 = src[3];
      final long l1 = src[4];
      final long l2 = src[5];
      dst[16] = l0;
      dst[17] = (l0 >>> 12);
      dst[18] = (l0 >>> 24);
      dst[19] = (l0 >>> 36);
      dst[20] = (l0 >>> 48);
      dst[21] = (l0 >>> 60) | (l1 << 4);
      dst[22] = (l1 >>> 8);
      dst[23] = (l1 >>> 20);
      dst[24] = (l1 >>> 32);
      dst[25] = (l1 >>> 44);
      dst[26] = (l1 >>> 56) | (l2 << 8);
      dst[27] = (l2 >>> 4);
      dst[28] = (l2 >>> 16);
      dst[29] = (l2 >>> 28);
      dst[30] = (l2 >>> 40);
      dst[31] = l2 >>> 52;
    }
    {
      final long l0 = src[6];
      final long l1 = src[7];
      final long l2 = src[8];
      dst[32] = l0;
      dst[33] = (l0 >>> 12);
      dst[34] = (l0 >>> 24);
      dst[35] = (l0 >>> 36);
      dst[36] = (l0 >>> 48);
      dst[37] = (l0 >>> 60) | (l1 << 4);
      dst[38] = (l1 >>> 8);
      dst[39] = (l1 >>> 20);
      dst[40] = (l1 >>> 32);
      dst[41] = (l1 >>> 44);
      dst[42] = (l1 >>> 56) | (l2 << 8);
      dst[43] = (l2 >>> 4);
      dst[44] = (l2 >>> 16);
      dst[45] = (l2 >>> 28);
      dst[46] = (l2 >>> 40);
      dst[47] = l2 >>> 52;
    }
    {
      final long l0 = src[9];
      final long l1 = src[10];
      final long l2 = src[11];
      dst[48] = l0;
      dst[49] = (l0 >>> 12);
      dst[50] = (l0 >>> 24);
      dst[51] = (l0 >>> 36);
      dst[52] = (l0 >>> 48);
      dst[53] = (l0 >>> 60) | (l1 << 4);
      dst[54] = (l1 >>> 8);
      dst[55] = (l1 >>> 20);
      dst[56] = (l1 >>> 32);
      dst[57] = (l1 >>> 44);
      dst[58] = (l1 >>> 56) | (l2 << 8);
      dst[59] = (l2 >>> 4);
      dst[60] = (l2 >>> 16);
      dst[61] = (l2 >>> 28);
      dst[62] = (l2 >>> 40);
      dst[63] = l2 >>> 52;
    }
    {
      final long l0 = src[12];
      final long l1 = src[13];
      final long l2 = src[14];
      dst[64] = l0;
      dst[65] = (l0 >>> 12);
      dst[66] = (l0 >>> 24);
      dst[67] = (l0 >>> 36);
      dst[68] = (l0 >>> 48);
      dst[69] = (l0 >>> 60) | (l1 << 4);
      dst[70] = (l1 >>> 8);
      dst[71] = (l1 >>> 20);
      dst[72] = (l1 >>> 32);
      dst[73] = (l1 >>> 44);
      dst[74] = (l1 >>> 56) | (l2 << 8);
      dst[75] = (l2 >>> 4);
      dst[76] = (l2 >>> 16);
      dst[77] = (l2 >>> 28);
      dst[78] = (l2 >>> 40);
      dst[79] = l2 >>> 52;
    }
    {
      final long l0 = src[15];
      final long l1 = src[16];
      final long l2 = src[17];
      dst[80] = l0;
      dst[81] = (l0 >>> 12);
      dst[82] = (l0 >>> 24);
      dst[83] = (l0 >>> 36);
      dst[84] = (l0 >>> 48);
      dst[85] = (l0 >>> 60) | (l1 << 4);
      dst[86] = (l1 >>> 8);
      dst[87] = (l1 >>> 20);
      dst[88] = (l1 >>> 32);
      dst[89] = (l1 >>> 44);
      dst[90] = (l1 >>> 56) | (l2 << 8);
      dst[91] = (l2 >>> 4);
      dst[92] = (l2 >>> 16);
      dst[93] = (l2 >>> 28);
      dst[94] = (l2 >>> 40);
      dst[95] = l2 >>> 52;
    }
    {
      final long l0 = src[18];
      final long l1 = src[19];
      final long l2 = src[20];
      dst[96] = l0;
      dst[97] = (l0 >>> 12);
      dst[98] = (l0 >>> 24);
      dst[99] = (l0 >>> 36);
      dst[100] = (l0 >>> 48);
      dst[101] = (l0 >>> 60) | (l1 << 4);
      dst[102] = (l1 >>> 8);
      dst[103] = (l1 >>> 20);
      dst[104] = (l1 >>> 32);
      dst[105] = (l1 >>> 44);
      dst[106] = (l1 >>> 56) | (l2 << 8);
      dst[107] = (l2 >>> 4);
      dst[108] = (l2 >>> 16);
      dst[109] = (l2 >>> 28);
      dst[110] = (l2 >>> 40);
      dst[111] = l2 >>> 52;
    }
    {
      final long l0 = src[21];
      final long l1 = src[22];
      final long l2 = src[23];
      dst[112] = l0;
      dst[113] = (l0 >>> 12);
      dst[114] = (l0 >>> 24);
      dst[115] = (l0 >>> 36);
      dst[116] = (l0 >>> 48);
      dst[117] = (l0 >>> 60) | (l1 << 4);
      dst[118] = (l1 >>> 8);
      dst[119] = (l1 >>> 20);
      dst[120] = (l1 >>> 32);
      dst[121] = (l1 >>> 44);
      dst[122] = (l1 >>> 56) | (l2 << 8);
      dst[123] = (l2 >>> 4);
      dst[124] = (l2 >>> 16);
      dst[125] = (l2 >>> 28);
      dst[126] = (l2 >>> 40);
      dst[127] = l2 >>> 52;
    }
    maskLongs(dst, 4095L);
  }

  static void decode16(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      dst[0] = l0;
      dst[1] = (l0 >>> 16);
      dst[2] = (l0 >>> 32);
      dst[3] = l0 >>> 48;
    }
    {
      final long l0 = src[1];
      dst[4] = l0;
      dst[5] = (l0 >>> 16);
      dst[6] = (l0 >>> 32);
      dst[7] = l0 >>> 48;
    }
    {
      final long l0 = src[2];
      dst[8] = l0;
      dst[9] = (l0 >>> 16);
      dst[10] = (l0 >>> 32);
      dst[11] = l0 >>> 48;
    }
    {
      final long l0 = src[3];
      dst[12] = l0;
      dst[13] = (l0 >>> 16);
      dst[14] = (l0 >>> 32);
      dst[15] = l0 >>> 48;
    }
    {
      final long l0 = src[4];
      dst[16] = l0;
      dst[17] = (l0 >>> 16);
      dst[18] = (l0 >>> 32);
      dst[19] = l0 >>> 48;
    }
    {
      final long l0 = src[5];
      dst[20] = l0;
      dst[21] = (l0 >>> 16);
      dst[22] = (l0 >>> 32);
      dst[23] = l0 >>> 48;
    }
    {
      final long l0 = src[6];
      dst[24] = l0;
      dst[25] = (l0 >>> 16);
      dst[26] = (l0 >>> 32);
      dst[27] = l0 >>> 48;
    }
    {
      final long l0 = src[7];
      dst[28] = l0;
      dst[29] = (l0 >>> 16);
      dst[30] = (l0 >>> 32);
      dst[31] = l0 >>> 48;
    }
    {
      final long l0 = src[8];
      dst[32] = l0;
      dst[33] = (l0 >>> 16);
      dst[34] = (l0 >>> 32);
      dst[35] = l0 >>> 48;
    }
    {
      final long l0 = src[9];
      dst[36] = l0;
      dst[37] = (l0 >>> 16);
      dst[38] = (l0 >>> 32);
      dst[39] = l0 >>> 48;
    }
    {
      final long l0 = src[10];
      dst[40] = l0;
      dst[41] = (l0 >>> 16);
      dst[42] = (l0 >>> 32);
      dst[43] = l0 >>> 48;
    }
    {
      final long l0 = src[11];
      dst[44] = l0;
      dst[45] = (l0 >>> 16);
      dst[46] = (l0 >>> 32);
      dst[47] = l0 >>> 48;
    }
    {
      final long l0 = src[12];
      dst[48] = l0;
      dst[49] = (l0 >>> 16);
      dst[50] = (l0 >>> 32);
      dst[51] = l0 >>> 48;
    }
    {
      final long l0 = src[13];
      dst[52] = l0;
      dst[53] = (l0 >>> 16);
      dst[54] = (l0 >>> 32);
      dst[55] = l0 >>> 48;
    }
    {
      final long l0 = src[14];
      dst[56] = l0;
      dst[57] = (l0 >>> 16);
      dst[58] = (l0 >>> 32);
      dst[59] = l0 >>> 48;
    }
    {
      final long l0 = src[15];
      dst[60] = l0;
      dst[61] = (l0 >>> 16);
      dst[62] = (l0 >>> 32);
      dst[63] = l0 >>> 48;
    }
    {
      final long l0 = src[16];
      dst[64] = l0;
      dst[65] = (l0 >>> 16);
      dst[66] = (l0 >>> 32);
      dst[67] = l0 >>> 48;
    }
    {
      final long l0 = src[17];
      dst[68] = l0;
      dst[69] = (l0 >>> 16);
      dst[70] = (l0 >>> 32);
      dst[71] = l0 >>> 48;
    }
    {
      final long l0 = src[18];
      dst[72] = l0;
      dst[73] = (l0 >>> 16);
      dst[74] = (l0 >>> 32);
      dst[75] = l0 >>> 48;
    }
    {
      final long l0 = src[19];
      dst[76] = l0;
      dst[77] = (l0 >>> 16);
      dst[78] = (l0 >>> 32);
      dst[79] = l0 >>> 48;
    }
    {
      final long l0 = src[20];
      dst[80] = l0;
      dst[81] = (l0 >>> 16);
      dst[82] = (l0 >>> 32);
      dst[83] = l0 >>> 48;
    }
    {
      final long l0 = src[21];
      dst[84] = l0;
      dst[85] = (l0 >>> 16);
      dst[86] = (l0 >>> 32);
      dst[87] = l0 >>> 48;
    }
    {
      final long l0 = src[22];
      dst[88] = l0;
      dst[89] = (l0 >>> 16);
      dst[90] = (l0 >>> 32);
      dst[91] = l0 >>> 48;
    }
    {
      final long l0 = src[23];
      dst[92] = l0;
      dst[93] = (l0 >>> 16);
      dst[94] = (l0 >>> 32);
      dst[95] = l0 >>> 48;
    }
    {
      final long l0 = src[24];
      dst[96] = l0;
      dst[97] = (l0 >>> 16);
      dst[98] = (l0 >>> 32);
      dst[99] = l0 >>> 48;
    }
    {
      final long l0 = src[25];
      dst[100] = l0;
      dst[101] = (l0 >>> 16);
      dst[102] = (l0 >>> 32);
      dst[103] = l0 >>> 48;
    }
    {
      final long l0 = src[26];
      dst[104] = l0;
      dst[105] = (l0 >>> 16);
      dst[106] = (l0 >>> 32);
      dst[107] = l0 >>> 48;
    }
    {
      final long l0 = src[27];
      dst[108] = l0;
      dst[109] = (l0 >>> 16);
      dst[110] = (l0 >>> 32);
      dst[111] = l0 >>> 48;
    }
    {
      final long l0 = src[28];
      dst[112] = l0;
      dst[113] = (l0 >>> 16);
      dst[114] = (l0 >>> 32);
      dst[115] = l0 >>> 48;
    }
    {
      final long l0 = src[29];
      dst[116] = l0;
      dst[117] = (l0 >>> 16);
      dst[118] = (l0 >>> 32);
      dst[119] = l0 >>> 48;
    }
    {
      final long l0 = src[30];
      dst[120] = l0;
      dst[121] = (l0 >>> 16);
      dst[122] = (l0 >>> 32);
      dst[123] = l0 >>> 48;
    }
    {
      final long l0 = src[31];
      dst[124] = l0;
      dst[125] = (l0 >>> 16);
      dst[126] = (l0 >>> 32);
      dst[127] = l0 >>> 48;
    }
    maskLongs(dst, 65535L);
  }

  static void decode20(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      final long l1 = src[1];
      final long l2 = src[2];
      final long l3 = src[3];
      final long l4 = src[4];
      dst[0] = l0;
      dst[1] = (l0 >>> 20);
      dst[2] = (l0 >>> 40);
      dst[3] = (l0 >>> 60) | (l1 << 4);
      dst[4] = (l1 >>> 16);
      dst[5] = (l1 >>> 36);
      dst[6] = (l1 >>> 56) | (l2 << 8);
      dst[7] = (l2 >>> 12);
      dst[8] = (l2 >>> 32);
      dst[9] = (l2 >>> 52) | (l3 << 12);
      dst[10] = (l3 >>> 8);
      dst[11] = (l3 >>> 28);
      dst[12] = (l3 >>> 48) | (l4 << 16);
      dst[13] = (l4 >>> 4);
      dst[14] = (l4 >>> 24);
      dst[15] = l4 >>> 44;
    }
    {
      final long l0 = src[5];
      final long l1 = src[6];
      final long l2 = src[7];
      final long l3 = src[8];
      final long l4 = src[9];
      dst[16] = l0;
      dst[17] = (l0 >>> 20);
      dst[18] = (l0 >>> 40);
      dst[19] = (l0 >>> 60) | (l1 << 4);
      dst[20] = (l1 >>> 16);
      dst[21] = (l1 >>> 36);
      dst[22] = (l1 >>> 56) | (l2 << 8);
      dst[23] = (l2 >>> 12);
      dst[24] = (l2 >>> 32);
      dst[25] = (l2 >>> 52) | (l3 << 12);
      dst[26] = (l3 >>> 8);
      dst[27] = (l3 >>> 28);
      dst[28] = (l3 >>> 48) | (l4 << 16);
      dst[29] = (l4 >>> 4);
      dst[30] = (l4 >>> 24);
      dst[31] = l4 >>> 44;
    }
    {
      final long l0 = src[10];
      final long l1 = src[11];
      final long l2 = src[12];
      final long l3 = src[13];
      final long l4 = src[14];
      dst[32] = l0;
      dst[33] = (l0 >>> 20);
      dst[34] = (l0 >>> 40);
      dst[35] = (l0 >>> 60) | (l1 << 4);
      dst[36] = (l1 >>> 16);
      dst[37] = (l1 >>> 36);
      dst[38] = (l1 >>> 56) | (l2 << 8);
      dst[39] = (l2 >>> 12);
      dst[40] = (l2 >>> 32);
      dst[41] = (l2 >>> 52) | (l3 << 12);
      dst[42] = (l3 >>> 8);
      dst[43] = (l3 >>> 28);
      dst[44] = (l3 >>> 48) | (l4 << 16);
      dst[45] = (l4 >>> 4);
      dst[46] = (l4 >>> 24);
      dst[47] = l4 >>> 44;
    }
    {
      final long l0 = src[15];
      final long l1 = src[16];
      final long l2 = src[17];
      final long l3 = src[18];
      final long l4 = src[19];
      dst[48] = l0;
      dst[49] = (l0 >>> 20);
      dst[50] = (l0 >>> 40);
      dst[51] = (l0 >>> 60) | (l1 << 4);
      dst[52] = (l1 >>> 16);
      dst[53] = (l1 >>> 36);
      dst[54] = (l1 >>> 56) | (l2 << 8);
      dst[55] = (l2 >>> 12);
      dst[56] = (l2 >>> 32);
      dst[57] = (l2 >>> 52) | (l3 << 12);
      dst[58] = (l3 >>> 8);
      dst[59] = (l3 >>> 28);
      dst[60] = (l3 >>> 48) | (l4 << 16);
      dst[61] = (l4 >>> 4);
      dst[62] = (l4 >>> 24);
      dst[63] = l4 >>> 44;
    }
    {
      final long l0 = src[20];
      final long l1 = src[21];
      final long l2 = src[22];
      final long l3 = src[23];
      final long l4 = src[24];
      dst[64] = l0;
      dst[65] = (l0 >>> 20);
      dst[66] = (l0 >>> 40);
      dst[67] = (l0 >>> 60) | (l1 << 4);
      dst[68] = (l1 >>> 16);
      dst[69] = (l1 >>> 36);
      dst[70] = (l1 >>> 56) | (l2 << 8);
      dst[71] = (l2 >>> 12);
      dst[72] = (l2 >>> 32);
      dst[73] = (l2 >>> 52) | (l3 << 12);
      dst[74] = (l3 >>> 8);
      dst[75] = (l3 >>> 28);
      dst[76] = (l3 >>> 48) | (l4 << 16);
      dst[77] = (l4 >>> 4);
      dst[78] = (l4 >>> 24);
      dst[79] = l4 >>> 44;
    }
    {
      final long l0 = src[25];
      final long l1 = src[26];
      final long l2 = src[27];
      final long l3 = src[28];
      final long l4 = src[29];
      dst[80] = l0;
      dst[81] = (l0 >>> 20);
      dst[82] = (l0 >>> 40);
      dst[83] = (l0 >>> 60) | (l1 << 4);
      dst[84] = (l1 >>> 16);
      dst[85] = (l1 >>> 36);
      dst[86] = (l1 >>> 56) | (l2 << 8);
      dst[87] = (l2 >>> 12);
      dst[88] = (l2 >>> 32);
      dst[89] = (l2 >>> 52) | (l3 << 12);
      dst[90] = (l3 >>> 8);
      dst[91] = (l3 >>> 28);
      dst[92] = (l3 >>> 48) | (l4 << 16);
      dst[93] = (l4 >>> 4);
      dst[94] = (l4 >>> 24);
      dst[95] = l4 >>> 44;
    }
    {
      final long l0 = src[30];
      final long l1 = src[31];
      final long l2 = src[32];
      final long l3 = src[33];
      final long l4 = src[34];
      dst[96] = l0;
      dst[97] = (l0 >>> 20);
      dst[98] = (l0 >>> 40);
      dst[99] = (l0 >>> 60) | (l1 << 4);
      dst[100] = (l1 >>> 16);
      dst[101] = (l1 >>> 36);
      dst[102] = (l1 >>> 56) | (l2 << 8);
      dst[103] = (l2 >>> 12);
      dst[104] = (l2 >>> 32);
      dst[105] = (l2 >>> 52) | (l3 << 12);
      dst[106] = (l3 >>> 8);
      dst[107] = (l3 >>> 28);
      dst[108] = (l3 >>> 48) | (l4 << 16);
      dst[109] = (l4 >>> 4);
      dst[110] = (l4 >>> 24);
      dst[111] = l4 >>> 44;
    }
    {
      final long l0 = src[35];
      final long l1 = src[36];
      final long l2 = src[37];
      final long l3 = src[38];
      final long l4 = src[39];
      dst[112] = l0;
      dst[113] = (l0 >>> 20);
      dst[114] = (l0 >>> 40);
      dst[115] = (l0 >>> 60) | (l1 << 4);
      dst[116] = (l1 >>> 16);
      dst[117] = (l1 >>> 36);
      dst[118] = (l1 >>> 56) | (l2 << 8);
      dst[119] = (l2 >>> 12);
      dst[120] = (l2 >>> 32);
      dst[121] = (l2 >>> 52) | (l3 << 12);
      dst[122] = (l3 >>> 8);
      dst[123] = (l3 >>> 28);
      dst[124] = (l3 >>> 48) | (l4 << 16);
      dst[125] = (l4 >>> 4);
      dst[126] = (l4 >>> 24);
      dst[127] = l4 >>> 44;
    }
    maskLongs(dst, 1048575L);
  }

  static void decode24(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      final long l1 = src[1];
      final long l2 = src[2];
      dst[0] = l0;
      dst[1] = (l0 >>> 24);
      dst[2] = (l0 >>> 48) | (l1 << 16);
      dst[3] = (l1 >>> 8);
      dst[4] = (l1 >>> 32);
      dst[5] = (l1 >>> 56) | (l2 << 8);
      dst[6] = (l2 >>> 16);
      dst[7] = l2 >>> 40;
    }
    {
      final long l0 = src[3];
      final long l1 = src[4];
      final long l2 = src[5];
      dst[8] = l0;
      dst[9] = (l0 >>> 24);
      dst[10] = (l0 >>> 48) | (l1 << 16);
      dst[11] = (l1 >>> 8);
      dst[12] = (l1 >>> 32);
      dst[13] = (l1 >>> 56) | (l2 << 8);
      dst[14] = (l2 >>> 16);
      dst[15] = l2 >>> 40;
    }
    {
      final long l0 = src[6];
      final long l1 = src[7];
      final long l2 = src[8];
      dst[16] = l0;
      dst[17] = (l0 >>> 24);
      dst[18] = (l0 >>> 48) | (l1 << 16);
      dst[19] = (l1 >>> 8);
      dst[20] = (l1 >>> 32);
      dst[21] = (l1 >>> 56) | (l2 << 8);
      dst[22] = (l2 >>> 16);
      dst[23] = l2 >>> 40;
    }
    {
      final long l0 = src[9];
      final long l1 = src[10];
      final long l2 = src[11];
      dst[24] = l0;
      dst[25] = (l0 >>> 24);
      dst[26] = (l0 >>> 48) | (l1 << 16);
      dst[27] = (l1 >>> 8);
      dst[28] = (l1 >>> 32);
      dst[29] = (l1 >>> 56) | (l2 << 8);
      dst[30] = (l2 >>> 16);
      dst[31] = l2 >>> 40;
    }
    {
      final long l0 = src[12];
      final long l1 = src[13];
      final long l2 = src[14];
      dst[32] = l0;
      dst[33] = (l0 >>> 24);
      dst[34] = (l0 >>> 48) | (l1 << 16);
      dst[35] = (l1 >>> 8);
      dst[36] = (l1 >>> 32);
      dst[37] = (l1 >>> 56) | (l2 << 8);
      dst[38] = (l2 >>> 16);
      dst[39] = l2 >>> 40;
    }
    {
      final long l0 = src[15];
      final long l1 = src[16];
      final long l2 = src[17];
      dst[40] = l0;
      dst[41] = (l0 >>> 24);
      dst[42] = (l0 >>> 48) | (l1 << 16);
      dst[43] = (l1 >>> 8);
      dst[44] = (l1 >>> 32);
      dst[45] = (l1 >>> 56) | (l2 << 8);
      dst[46] = (l2 >>> 16);
      dst[47] = l2 >>> 40;
    }
    {
      final long l0 = src[18];
      final long l1 = src[19];
      final long l2 = src[20];
      dst[48] = l0;
      dst[49] = (l0 >>> 24);
      dst[50] = (l0 >>> 48) | (l1 << 16);
      dst[51] = (l1 >>> 8);
      dst[52] = (l1 >>> 32);
      dst[53] = (l1 >>> 56) | (l2 << 8);
      dst[54] = (l2 >>> 16);
      dst[55] = l2 >>> 40;
    }
    {
      final long l0 = src[21];
      final long l1 = src[22];
      final long l2 = src[23];
      dst[56] = l0;
      dst[57] = (l0 >>> 24);
      dst[58] = (l0 >>> 48) | (l1 << 16);
      dst[59] = (l1 >>> 8);
      dst[60] = (l1 >>> 32);
      dst[61] = (l1 >>> 56) | (l2 << 8);
      dst[62] = (l2 >>> 16);
      dst[63] = l2 >>> 40;
    }
    {
      final long l0 = src[24];
      final long l1 = src[25];
      final long l2 = src[26];
      dst[64] = l0;
      dst[65] = (l0 >>> 24);
      dst[66] = (l0 >>> 48) | (l1 << 16);
      dst[67] = (l1 >>> 8);
      dst[68] = (l1 >>> 32);
      dst[69] = (l1 >>> 56) | (l2 << 8);
      dst[70] = (l2 >>> 16);
      dst[71] = l2 >>> 40;
    }
    {
      final long l0 = src[27];
      final long l1 = src[28];
      final long l2 = src[29];
      dst[72] = l0;
      dst[73] = (l0 >>> 24);
      dst[74] = (l0 >>> 48) | (l1 << 16);
      dst[75] = (l1 >>> 8);
      dst[76] = (l1 >>> 32);
      dst[77] = (l1 >>> 56) | (l2 << 8);
      dst[78] = (l2 >>> 16);
      dst[79] = l2 >>> 40;
    }
    {
      final long l0 = src[30];
      final long l1 = src[31];
      final long l2 = src[32];
      dst[80] = l0;
      dst[81] = (l0 >>> 24);
      dst[82] = (l0 >>> 48) | (l1 << 16);
      dst[83] = (l1 >>> 8);
      dst[84] = (l1 >>> 32);
      dst[85] = (l1 >>> 56) | (l2 << 8);
      dst[86] = (l2 >>> 16);
      dst[87] = l2 >>> 40;
    }
    {
      final long l0 = src[33];
      final long l1 = src[34];
      final long l2 = src[35];
      dst[88] = l0;
      dst[89] = (l0 >>> 24);
      dst[90] = (l0 >>> 48) | (l1 << 16);
      dst[91] = (l1 >>> 8);
      dst[92] = (l1 >>> 32);
      dst[93] = (l1 >>> 56) | (l2 << 8);
      dst[94] = (l2 >>> 16);
      dst[95] = l2 >>> 40;
    }
    {
      final long l0 = src[36];
      final long l1 = src[37];
      final long l2 = src[38];
      dst[96] = l0;
      dst[97] = (l0 >>> 24);
      dst[98] = (l0 >>> 48) | (l1 << 16);
      dst[99] = (l1 >>> 8);
      dst[100] = (l1 >>> 32);
      dst[101] = (l1 >>> 56) | (l2 << 8);
      dst[102] = (l2 >>> 16);
      dst[103] = l2 >>> 40;
    }
    {
      final long l0 = src[39];
      final long l1 = src[40];
      final long l2 = src[41];
      dst[104] = l0;
      dst[105] = (l0 >>> 24);
      dst[106] = (l0 >>> 48) | (l1 << 16);
      dst[107] = (l1 >>> 8);
      dst[108] = (l1 >>> 32);
      dst[109] = (l1 >>> 56) | (l2 << 8);
      dst[110] = (l2 >>> 16);
      dst[111] = l2 >>> 40;
    }
    {
      final long l0 = src[42];
      final long l1 = src[43];
      final long l2 = src[44];
      dst[112] = l0;
      dst[113] = (l0 >>> 24);
      dst[114] = (l0 >>> 48) | (l1 << 16);
      dst[115] = (l1 >>> 8);
      dst[116] = (l1 >>> 32);
      dst[117] = (l1 >>> 56) | (l2 << 8);
      dst[118] = (l2 >>> 16);
      dst[119] = l2 >>> 40;
    }
    {
      final long l0 = src[45];
      final long l1 = src[46];
      final long l2 = src[47];
      dst[120] = l0;
      dst[121] = (l0 >>> 24);
      dst[122] = (l0 >>> 48) | (l1 << 16);
      dst[123] = (l1 >>> 8);
      dst[124] = (l1 >>> 32);
      dst[125] = (l1 >>> 56) | (l2 << 8);
      dst[126] = (l2 >>> 16);
      dst[127] = l2 >>> 40;
    }
    maskLongs(dst, 16777215L);
  }

  static void decode28(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      final long l1 = src[1];
      final long l2 = src[2];
      final long l3 = src[3];
      final long l4 = src[4];
      final long l5 = src[5];
      final long l6 = src[6];
      dst[0] = l0;
      dst[1] = (l0 >>> 28);
      dst[2] = (l0 >>> 56) | (l1 << 8);
      dst[3] = (l1 >>> 20);
      dst[4] = (l1 >>> 48) | (l2 << 16);
      dst[5] = (l2 >>> 12);
      dst[6] = (l2 >>> 40) | (l3 << 24);
      dst[7] = (l3 >>> 4);
      dst[8] = (l3 >>> 32);
      dst[9] = (l3 >>> 60) | (l4 << 4);
      dst[10] = (l4 >>> 24);
      dst[11] = (l4 >>> 52) | (l5 << 12);
      dst[12] = (l5 >>> 16);
      dst[13] = (l5 >>> 44) | (l6 << 20);
      dst[14] = (l6 >>> 8);
      dst[15] = l6 >>> 36;
    }
    {
      final long l0 = src[7];
      final long l1 = src[8];
      final long l2 = src[9];
      final long l3 = src[10];
      final long l4 = src[11];
      final long l5 = src[12];
      final long l6 = src[13];
      dst[16] = l0;
      dst[17] = (l0 >>> 28);
      dst[18] = (l0 >>> 56) | (l1 << 8);
      dst[19] = (l1 >>> 20);
      dst[20] = (l1 >>> 48) | (l2 << 16);
      dst[21] = (l2 >>> 12);
      dst[22] = (l2 >>> 40) | (l3 << 24);
      dst[23] = (l3 >>> 4);
      dst[24] = (l3 >>> 32);
      dst[25] = (l3 >>> 60) | (l4 << 4);
      dst[26] = (l4 >>> 24);
      dst[27] = (l4 >>> 52) | (l5 << 12);
      dst[28] = (l5 >>> 16);
      dst[29] = (l5 >>> 44) | (l6 << 20);
      dst[30] = (l6 >>> 8);
      dst[31] = l6 >>> 36;
    }
    {
      final long l0 = src[14];
      final long l1 = src[15];
      final long l2 = src[16];
      final long l3 = src[17];
      final long l4 = src[18];
      final long l5 = src[19];
      final long l6 = src[20];
      dst[32] = l0;
      dst[33] = (l0 >>> 28);
      dst[34] = (l0 >>> 56) | (l1 << 8);
      dst[35] = (l1 >>> 20);
      dst[36] = (l1 >>> 48) | (l2 << 16);
      dst[37] = (l2 >>> 12);
      dst[38] = (l2 >>> 40) | (l3 << 24);
      dst[39] = (l3 >>> 4);
      dst[40] = (l3 >>> 32);
      dst[41] = (l3 >>> 60) | (l4 << 4);
      dst[42] = (l4 >>> 24);
      dst[43] = (l4 >>> 52) | (l5 << 12);
      dst[44] = (l5 >>> 16);
      dst[45] = (l5 >>> 44) | (l6 << 20);
      dst[46] = (l6 >>> 8);
      dst[47] = l6 >>> 36;
    }
    {
      final long l0 = src[21];
      final long l1 = src[22];
      final long l2 = src[23];
      final long l3 = src[24];
      final long l4 = src[25];
      final long l5 = src[26];
      final long l6 = src[27];
      dst[48] = l0;
      dst[49] = (l0 >>> 28);
      dst[50] = (l0 >>> 56) | (l1 << 8);
      dst[51] = (l1 >>> 20);
      dst[52] = (l1 >>> 48) | (l2 << 16);
      dst[53] = (l2 >>> 12);
      dst[54] = (l2 >>> 40) | (l3 << 24);
      dst[55] = (l3 >>> 4);
      dst[56] = (l3 >>> 32);
      dst[57] = (l3 >>> 60) | (l4 << 4);
      dst[58] = (l4 >>> 24);
      dst[59] = (l4 >>> 52) | (l5 << 12);
      dst[60] = (l5 >>> 16);
      dst[61] = (l5 >>> 44) | (l6 << 20);
      dst[62] = (l6 >>> 8);
      dst[63] = l6 >>> 36;
    }
    {
      final long l0 = src[28];
      final long l1 = src[29];
      final long l2 = src[30];
      final long l3 = src[31];
      final long l4 = src[32];
      final long l5 = src[33];
      final long l6 = src[34];
      dst[64] = l0;
      dst[65] = (l0 >>> 28);
      dst[66] = (l0 >>> 56) | (l1 << 8);
      dst[67] = (l1 >>> 20);
      dst[68] = (l1 >>> 48) | (l2 << 16);
      dst[69] = (l2 >>> 12);
      dst[70] = (l2 >>> 40) | (l3 << 24);
      dst[71] = (l3 >>> 4);
      dst[72] = (l3 >>> 32);
      dst[73] = (l3 >>> 60) | (l4 << 4);
      dst[74] = (l4 >>> 24);
      dst[75] = (l4 >>> 52) | (l5 << 12);
      dst[76] = (l5 >>> 16);
      dst[77] = (l5 >>> 44) | (l6 << 20);
      dst[78] = (l6 >>> 8);
      dst[79] = l6 >>> 36;
    }
    {
      final long l0 = src[35];
      final long l1 = src[36];
      final long l2 = src[37];
      final long l3 = src[38];
      final long l4 = src[39];
      final long l5 = src[40];
      final long l6 = src[41];
      dst[80] = l0;
      dst[81] = (l0 >>> 28);
      dst[82] = (l0 >>> 56) | (l1 << 8);
      dst[83] = (l1 >>> 20);
      dst[84] = (l1 >>> 48) | (l2 << 16);
      dst[85] = (l2 >>> 12);
      dst[86] = (l2 >>> 40) | (l3 << 24);
      dst[87] = (l3 >>> 4);
      dst[88] = (l3 >>> 32);
      dst[89] = (l3 >>> 60) | (l4 << 4);
      dst[90] = (l4 >>> 24);
      dst[91] = (l4 >>> 52) | (l5 << 12);
      dst[92] = (l5 >>> 16);
      dst[93] = (l5 >>> 44) | (l6 << 20);
      dst[94] = (l6 >>> 8);
      dst[95] = l6 >>> 36;
    }
    {
      final long l0 = src[42];
      final long l1 = src[43];
      final long l2 = src[44];
      final long l3 = src[45];
      final long l4 = src[46];
      final long l5 = src[47];
      final long l6 = src[48];
      dst[96] = l0;
      dst[97] = (l0 >>> 28);
      dst[98] = (l0 >>> 56) | (l1 << 8);
      dst[99] = (l1 >>> 20);
      dst[100] = (l1 >>> 48) | (l2 << 16);
      dst[101] = (l2 >>> 12);
      dst[102] = (l2 >>> 40) | (l3 << 24);
      dst[103] = (l3 >>> 4);
      dst[104] = (l3 >>> 32);
      dst[105] = (l3 >>> 60) | (l4 << 4);
      dst[106] = (l4 >>> 24);
      dst[107] = (l4 >>> 52) | (l5 << 12);
      dst[108] = (l5 >>> 16);
      dst[109] = (l5 >>> 44) | (l6 << 20);
      dst[110] = (l6 >>> 8);
      dst[111] = l6 >>> 36;
    }
    {
      final long l0 = src[49];
      final long l1 = src[50];
      final long l2 = src[51];
      final long l3 = src[52];
      final long l4 = src[53];
      final long l5 = src[54];
      final long l6 = src[55];
      dst[112] = l0;
      dst[113] = (l0 >>> 28);
      dst[114] = (l0 >>> 56) | (l1 << 8);
      dst[115] = (l1 >>> 20);
      dst[116] = (l1 >>> 48) | (l2 << 16);
      dst[117] = (l2 >>> 12);
      dst[118] = (l2 >>> 40) | (l3 << 24);
      dst[119] = (l3 >>> 4);
      dst[120] = (l3 >>> 32);
      dst[121] = (l3 >>> 60) | (l4 << 4);
      dst[122] = (l4 >>> 24);
      dst[123] = (l4 >>> 52) | (l5 << 12);
      dst[124] = (l5 >>> 16);
      dst[125] = (l5 >>> 44) | (l6 << 20);
      dst[126] = (l6 >>> 8);
      dst[127] = l6 >>> 36;
    }
    maskLongs(dst, 268435455L);
  }

  static void decode32(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      dst[0] = l0;
      dst[1] = l0 >>> 32;
    }
    {
      final long l0 = src[1];
      dst[2] = l0;
      dst[3] = l0 >>> 32;
    }
    {
      final long l0 = src[2];
      dst[4] = l0;
      dst[5] = l0 >>> 32;
    }
    {
      final long l0 = src[3];
      dst[6] = l0;
      dst[7] = l0 >>> 32;
    }
    {
      final long l0 = src[4];
      dst[8] = l0;
      dst[9] = l0 >>> 32;
    }
    {
      final long l0 = src[5];
      dst[10] = l0;
      dst[11] = l0 >>> 32;
    }
    {
      final long l0 = src[6];
      dst[12] = l0;
      dst[13] = l0 >>> 32;
    }
    {
      final long l0 = src[7];
      dst[14] = l0;
      dst[15] = l0 >>> 32;
    }
    {
      final long l0 = src[8];
      dst[16] = l0;
      dst[17] = l0 >>> 32;
    }
    {
      final long l0 = src[9];
      dst[18] = l0;
      dst[19] = l0 >>> 32;
    }
    {
      final long l0 = src[10];
      dst[20] = l0;
      dst[21] = l0 >>> 32;
    }
    {
      final long l0 = src[11];
      dst[22] = l0;
      dst[23] = l0 >>> 32;
    }
    {
      final long l0 = src[12];
      dst[24] = l0;
      dst[25] = l0 >>> 32;
    }
    {
      final long l0 = src[13];
      dst[26] = l0;
      dst[27] = l0 >>> 32;
    }
    {
      final long l0 = src[14];
      dst[28] = l0;
      dst[29] = l0 >>> 32;
    }
    {
      final long l0 = src[15];
      dst[30] = l0;
      dst[31] = l0 >>> 32;
    }
    {
      final long l0 = src[16];
      dst[32] = l0;
      dst[33] = l0 >>> 32;
    }
    {
      final long l0 = src[17];
      dst[34] = l0;
      dst[35] = l0 >>> 32;
    }
    {
      final long l0 = src[18];
      dst[36] = l0;
      dst[37] = l0 >>> 32;
    }
    {
      final long l0 = src[19];
      dst[38] = l0;
      dst[39] = l0 >>> 32;
    }
    {
      final long l0 = src[20];
      dst[40] = l0;
      dst[41] = l0 >>> 32;
    }
    {
      final long l0 = src[21];
      dst[42] = l0;
      dst[43] = l0 >>> 32;
    }
    {
      final long l0 = src[22];
      dst[44] = l0;
      dst[45] = l0 >>> 32;
    }
    {
      final long l0 = src[23];
      dst[46] = l0;
      dst[47] = l0 >>> 32;
    }
    {
      final long l0 = src[24];
      dst[48] = l0;
      dst[49] = l0 >>> 32;
    }
    {
      final long l0 = src[25];
      dst[50] = l0;
      dst[51] = l0 >>> 32;
    }
    {
      final long l0 = src[26];
      dst[52] = l0;
      dst[53] = l0 >>> 32;
    }
    {
      final long l0 = src[27];
      dst[54] = l0;
      dst[55] = l0 >>> 32;
    }
    {
      final long l0 = src[28];
      dst[56] = l0;
      dst[57] = l0 >>> 32;
    }
    {
      final long l0 = src[29];
      dst[58] = l0;
      dst[59] = l0 >>> 32;
    }
    {
      final long l0 = src[30];
      dst[60] = l0;
      dst[61] = l0 >>> 32;
    }
    {
      final long l0 = src[31];
      dst[62] = l0;
      dst[63] = l0 >>> 32;
    }
    {
      final long l0 = src[32];
      dst[64] = l0;
      dst[65] = l0 >>> 32;
    }
    {
      final long l0 = src[33];
      dst[66] = l0;
      dst[67] = l0 >>> 32;
    }
    {
      final long l0 = src[34];
      dst[68] = l0;
      dst[69] = l0 >>> 32;
    }
    {
      final long l0 = src[35];
      dst[70] = l0;
      dst[71] = l0 >>> 32;
    }
    {
      final long l0 = src[36];
      dst[72] = l0;
      dst[73] = l0 >>> 32;
    }
    {
      final long l0 = src[37];
      dst[74] = l0;
      dst[75] = l0 >>> 32;
    }
    {
      final long l0 = src[38];
      dst[76] = l0;
      dst[77] = l0 >>> 32;
    }
    {
      final long l0 = src[39];
      dst[78] = l0;
      dst[79] = l0 >>> 32;
    }
    {
      final long l0 = src[40];
      dst[80] = l0;
      dst[81] = l0 >>> 32;
    }
    {
      final long l0 = src[41];
      dst[82] = l0;
      dst[83] = l0 >>> 32;
    }
    {
      final long l0 = src[42];
      dst[84] = l0;
      dst[85] = l0 >>> 32;
    }
    {
      final long l0 = src[43];
      dst[86] = l0;
      dst[87] = l0 >>> 32;
    }
    {
      final long l0 = src[44];
      dst[88] = l0;
      dst[89] = l0 >>> 32;
    }
    {
      final long l0 = src[45];
      dst[90] = l0;
      dst[91] = l0 >>> 32;
    }
    {
      final long l0 = src[46];
      dst[92] = l0;
      dst[93] = l0 >>> 32;
    }
    {
      final long l0 = src[47];
      dst[94] = l0;
      dst[95] = l0 >>> 32;
    }
    {
      final long l0 = src[48];
      dst[96] = l0;
      dst[97] = l0 >>> 32;
    }
    {
      final long l0 = src[49];
      dst[98] = l0;
      dst[99] = l0 >>> 32;
    }
    {
      final long l0 = src[50];
      dst[100] = l0;
      dst[101] = l0 >>> 32;
    }
    {
      final long l0 = src[51];
      dst[102] = l0;
      dst[103] = l0 >>> 32;
    }
    {
      final long l0 = src[52];
      dst[104] = l0;
      dst[105] = l0 >>> 32;
    }
    {
      final long l0 = src[53];
      dst[106] = l0;
      dst[107] = l0 >>> 32;
    }
    {
      final long l0 = src[54];
      dst[108] = l0;
      dst[109] = l0 >>> 32;
    }
    {
      final long l0 = src[55];
      dst[110] = l0;
      dst[111] = l0 >>> 32;
    }
    {
      final long l0 = src[56];
      dst[112] = l0;
      dst[113] = l0 >>> 32;
    }
    {
      final long l0 = src[57];
      dst[114] = l0;
      dst[115] = l0 >>> 32;
    }
    {
      final long l0 = src[58];
      dst[116] = l0;
      dst[117] = l0 >>> 32;
    }
    {
      final long l0 = src[59];
      dst[118] = l0;
      dst[119] = l0 >>> 32;
    }
    {
      final long l0 = src[60];
      dst[120] = l0;
      dst[121] = l0 >>> 32;
    }
    {
      final long l0 = src[61];
      dst[122] = l0;
      dst[123] = l0 >>> 32;
    }
    {
      final long l0 = src[62];
      dst[124] = l0;
      dst[125] = l0 >>> 32;
    }
    {
      final long l0 = src[63];
      dst[126] = l0;
      dst[127] = l0 >>> 32;
    }
    maskLongs(dst, 4294967295L);
  }

  static void decode40(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      final long l1 = src[1];
      final long l2 = src[2];
      final long l3 = src[3];
      final long l4 = src[4];
      dst[0] = l0;
      dst[1] = (l0 >>> 40) | (l1 << 24);
      dst[2] = (l1 >>> 16);
      dst[3] = (l1 >>> 56) | (l2 << 8);
      dst[4] = (l2 >>> 32) | (l3 << 32);
      dst[5] = (l3 >>> 8);
      dst[6] = (l3 >>> 48) | (l4 << 16);
      dst[7] = l4 >>> 24;
    }
    {
      final long l0 = src[5];
      final long l1 = src[6];
      final long l2 = src[7];
      final long l3 = src[8];
      final long l4 = src[9];
      dst[8] = l0;
      dst[9] = (l0 >>> 40) | (l1 << 24);
      dst[10] = (l1 >>> 16);
      dst[11] = (l1 >>> 56) | (l2 << 8);
      dst[12] = (l2 >>> 32) | (l3 << 32);
      dst[13] = (l3 >>> 8);
      dst[14] = (l3 >>> 48) | (l4 << 16);
      dst[15] = l4 >>> 24;
    }
    {
      final long l0 = src[10];
      final long l1 = src[11];
      final long l2 = src[12];
      final long l3 = src[13];
      final long l4 = src[14];
      dst[16] = l0;
      dst[17] = (l0 >>> 40) | (l1 << 24);
      dst[18] = (l1 >>> 16);
      dst[19] = (l1 >>> 56) | (l2 << 8);
      dst[20] = (l2 >>> 32) | (l3 << 32);
      dst[21] = (l3 >>> 8);
      dst[22] = (l3 >>> 48) | (l4 << 16);
      dst[23] = l4 >>> 24;
    }
    {
      final long l0 = src[15];
      final long l1 = src[16];
      final long l2 = src[17];
      final long l3 = src[18];
      final long l4 = src[19];
      dst[24] = l0;
      dst[25] = (l0 >>> 40) | (l1 << 24);
      dst[26] = (l1 >>> 16);
      dst[27] = (l1 >>> 56) | (l2 << 8);
      dst[28] = (l2 >>> 32) | (l3 << 32);
      dst[29] = (l3 >>> 8);
      dst[30] = (l3 >>> 48) | (l4 << 16);
      dst[31] = l4 >>> 24;
    }
    {
      final long l0 = src[20];
      final long l1 = src[21];
      final long l2 = src[22];
      final long l3 = src[23];
      final long l4 = src[24];
      dst[32] = l0;
      dst[33] = (l0 >>> 40) | (l1 << 24);
      dst[34] = (l1 >>> 16);
      dst[35] = (l1 >>> 56) | (l2 << 8);
      dst[36] = (l2 >>> 32) | (l3 << 32);
      dst[37] = (l3 >>> 8);
      dst[38] = (l3 >>> 48) | (l4 << 16);
      dst[39] = l4 >>> 24;
    }
    {
      final long l0 = src[25];
      final long l1 = src[26];
      final long l2 = src[27];
      final long l3 = src[28];
      final long l4 = src[29];
      dst[40] = l0;
      dst[41] = (l0 >>> 40) | (l1 << 24);
      dst[42] = (l1 >>> 16);
      dst[43] = (l1 >>> 56) | (l2 << 8);
      dst[44] = (l2 >>> 32) | (l3 << 32);
      dst[45] = (l3 >>> 8);
      dst[46] = (l3 >>> 48) | (l4 << 16);
      dst[47] = l4 >>> 24;
    }
    {
      final long l0 = src[30];
      final long l1 = src[31];
      final long l2 = src[32];
      final long l3 = src[33];
      final long l4 = src[34];
      dst[48] = l0;
      dst[49] = (l0 >>> 40) | (l1 << 24);
      dst[50] = (l1 >>> 16);
      dst[51] = (l1 >>> 56) | (l2 << 8);
      dst[52] = (l2 >>> 32) | (l3 << 32);
      dst[53] = (l3 >>> 8);
      dst[54] = (l3 >>> 48) | (l4 << 16);
      dst[55] = l4 >>> 24;
    }
    {
      final long l0 = src[35];
      final long l1 = src[36];
      final long l2 = src[37];
      final long l3 = src[38];
      final long l4 = src[39];
      dst[56] = l0;
      dst[57] = (l0 >>> 40) | (l1 << 24);
      dst[58] = (l1 >>> 16);
      dst[59] = (l1 >>> 56) | (l2 << 8);
      dst[60] = (l2 >>> 32) | (l3 << 32);
      dst[61] = (l3 >>> 8);
      dst[62] = (l3 >>> 48) | (l4 << 16);
      dst[63] = l4 >>> 24;
    }
    {
      final long l0 = src[40];
      final long l1 = src[41];
      final long l2 = src[42];
      final long l3 = src[43];
      final long l4 = src[44];
      dst[64] = l0;
      dst[65] = (l0 >>> 40) | (l1 << 24);
      dst[66] = (l1 >>> 16);
      dst[67] = (l1 >>> 56) | (l2 << 8);
      dst[68] = (l2 >>> 32) | (l3 << 32);
      dst[69] = (l3 >>> 8);
      dst[70] = (l3 >>> 48) | (l4 << 16);
      dst[71] = l4 >>> 24;
    }
    {
      final long l0 = src[45];
      final long l1 = src[46];
      final long l2 = src[47];
      final long l3 = src[48];
      final long l4 = src[49];
      dst[72] = l0;
      dst[73] = (l0 >>> 40) | (l1 << 24);
      dst[74] = (l1 >>> 16);
      dst[75] = (l1 >>> 56) | (l2 << 8);
      dst[76] = (l2 >>> 32) | (l3 << 32);
      dst[77] = (l3 >>> 8);
      dst[78] = (l3 >>> 48) | (l4 << 16);
      dst[79] = l4 >>> 24;
    }
    {
      final long l0 = src[50];
      final long l1 = src[51];
      final long l2 = src[52];
      final long l3 = src[53];
      final long l4 = src[54];
      dst[80] = l0;
      dst[81] = (l0 >>> 40) | (l1 << 24);
      dst[82] = (l1 >>> 16);
      dst[83] = (l1 >>> 56) | (l2 << 8);
      dst[84] = (l2 >>> 32) | (l3 << 32);
      dst[85] = (l3 >>> 8);
      dst[86] = (l3 >>> 48) | (l4 << 16);
      dst[87] = l4 >>> 24;
    }
    {
      final long l0 = src[55];
      final long l1 = src[56];
      final long l2 = src[57];
      final long l3 = src[58];
      final long l4 = src[59];
      dst[88] = l0;
      dst[89] = (l0 >>> 40) | (l1 << 24);
      dst[90] = (l1 >>> 16);
      dst[91] = (l1 >>> 56) | (l2 << 8);
      dst[92] = (l2 >>> 32) | (l3 << 32);
      dst[93] = (l3 >>> 8);
      dst[94] = (l3 >>> 48) | (l4 << 16);
      dst[95] = l4 >>> 24;
    }
    {
      final long l0 = src[60];
      final long l1 = src[61];
      final long l2 = src[62];
      final long l3 = src[63];
      final long l4 = src[64];
      dst[96] = l0;
      dst[97] = (l0 >>> 40) | (l1 << 24);
      dst[98] = (l1 >>> 16);
      dst[99] = (l1 >>> 56) | (l2 << 8);
      dst[100] = (l2 >>> 32) | (l3 << 32);
      dst[101] = (l3 >>> 8);
      dst[102] = (l3 >>> 48) | (l4 << 16);
      dst[103] = l4 >>> 24;
    }
    {
      final long l0 = src[65];
      final long l1 = src[66];
      final long l2 = src[67];
      final long l3 = src[68];
      final long l4 = src[69];
      dst[104] = l0;
      dst[105] = (l0 >>> 40) | (l1 << 24);
      dst[106] = (l1 >>> 16);
      dst[107] = (l1 >>> 56) | (l2 << 8);
      dst[108] = (l2 >>> 32) | (l3 << 32);
      dst[109] = (l3 >>> 8);
      dst[110] = (l3 >>> 48) | (l4 << 16);
      dst[111] = l4 >>> 24;
    }
    {
      final long l0 = src[70];
      final long l1 = src[71];
      final long l2 = src[72];
      final long l3 = src[73];
      final long l4 = src[74];
      dst[112] = l0;
      dst[113] = (l0 >>> 40) | (l1 << 24);
      dst[114] = (l1 >>> 16);
      dst[115] = (l1 >>> 56) | (l2 << 8);
      dst[116] = (l2 >>> 32) | (l3 << 32);
      dst[117] = (l3 >>> 8);
      dst[118] = (l3 >>> 48) | (l4 << 16);
      dst[119] = l4 >>> 24;
    }
    {
      final long l0 = src[75];
      final long l1 = src[76];
      final long l2 = src[77];
      final long l3 = src[78];
      final long l4 = src[79];
      dst[120] = l0;
      dst[121] = (l0 >>> 40) | (l1 << 24);
      dst[122] = (l1 >>> 16);
      dst[123] = (l1 >>> 56) | (l2 << 8);
      dst[124] = (l2 >>> 32) | (l3 << 32);
      dst[125] = (l3 >>> 8);
      dst[126] = (l3 >>> 48) | (l4 << 16);
      dst[127] = l4 >>> 24;
    }
    maskLongs(dst, 1099511627775L);
  }

  static void decode48(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      final long l1 = src[1];
      final long l2 = src[2];
      dst[0] = l0;
      dst[1] = (l0 >>> 48) | (l1 << 16);
      dst[2] = (l1 >>> 32) | (l2 << 32);
      dst[3] = l2 >>> 16;
    }
    {
      final long l0 = src[3];
      final long l1 = src[4];
      final long l2 = src[5];
      dst[4] = l0;
      dst[5] = (l0 >>> 48) | (l1 << 16);
      dst[6] = (l1 >>> 32) | (l2 << 32);
      dst[7] = l2 >>> 16;
    }
    {
      final long l0 = src[6];
      final long l1 = src[7];
      final long l2 = src[8];
      dst[8] = l0;
      dst[9] = (l0 >>> 48) | (l1 << 16);
      dst[10] = (l1 >>> 32) | (l2 << 32);
      dst[11] = l2 >>> 16;
    }
    {
      final long l0 = src[9];
      final long l1 = src[10];
      final long l2 = src[11];
      dst[12] = l0;
      dst[13] = (l0 >>> 48) | (l1 << 16);
      dst[14] = (l1 >>> 32) | (l2 << 32);
      dst[15] = l2 >>> 16;
    }
    {
      final long l0 = src[12];
      final long l1 = src[13];
      final long l2 = src[14];
      dst[16] = l0;
      dst[17] = (l0 >>> 48) | (l1 << 16);
      dst[18] = (l1 >>> 32) | (l2 << 32);
      dst[19] = l2 >>> 16;
    }
    {
      final long l0 = src[15];
      final long l1 = src[16];
      final long l2 = src[17];
      dst[20] = l0;
      dst[21] = (l0 >>> 48) | (l1 << 16);
      dst[22] = (l1 >>> 32) | (l2 << 32);
      dst[23] = l2 >>> 16;
    }
    {
      final long l0 = src[18];
      final long l1 = src[19];
      final long l2 = src[20];
      dst[24] = l0;
      dst[25] = (l0 >>> 48) | (l1 << 16);
      dst[26] = (l1 >>> 32) | (l2 << 32);
      dst[27] = l2 >>> 16;
    }
    {
      final long l0 = src[21];
      final long l1 = src[22];
      final long l2 = src[23];
      dst[28] = l0;
      dst[29] = (l0 >>> 48) | (l1 << 16);
      dst[30] = (l1 >>> 32) | (l2 << 32);
      dst[31] = l2 >>> 16;
    }
    {
      final long l0 = src[24];
      final long l1 = src[25];
      final long l2 = src[26];
      dst[32] = l0;
      dst[33] = (l0 >>> 48) | (l1 << 16);
      dst[34] = (l1 >>> 32) | (l2 << 32);
      dst[35] = l2 >>> 16;
    }
    {
      final long l0 = src[27];
      final long l1 = src[28];
      final long l2 = src[29];
      dst[36] = l0;
      dst[37] = (l0 >>> 48) | (l1 << 16);
      dst[38] = (l1 >>> 32) | (l2 << 32);
      dst[39] = l2 >>> 16;
    }
    {
      final long l0 = src[30];
      final long l1 = src[31];
      final long l2 = src[32];
      dst[40] = l0;
      dst[41] = (l0 >>> 48) | (l1 << 16);
      dst[42] = (l1 >>> 32) | (l2 << 32);
      dst[43] = l2 >>> 16;
    }
    {
      final long l0 = src[33];
      final long l1 = src[34];
      final long l2 = src[35];
      dst[44] = l0;
      dst[45] = (l0 >>> 48) | (l1 << 16);
      dst[46] = (l1 >>> 32) | (l2 << 32);
      dst[47] = l2 >>> 16;
    }
    {
      final long l0 = src[36];
      final long l1 = src[37];
      final long l2 = src[38];
      dst[48] = l0;
      dst[49] = (l0 >>> 48) | (l1 << 16);
      dst[50] = (l1 >>> 32) | (l2 << 32);
      dst[51] = l2 >>> 16;
    }
    {
      final long l0 = src[39];
      final long l1 = src[40];
      final long l2 = src[41];
      dst[52] = l0;
      dst[53] = (l0 >>> 48) | (l1 << 16);
      dst[54] = (l1 >>> 32) | (l2 << 32);
      dst[55] = l2 >>> 16;
    }
    {
      final long l0 = src[42];
      final long l1 = src[43];
      final long l2 = src[44];
      dst[56] = l0;
      dst[57] = (l0 >>> 48) | (l1 << 16);
      dst[58] = (l1 >>> 32) | (l2 << 32);
      dst[59] = l2 >>> 16;
    }
    {
      final long l0 = src[45];
      final long l1 = src[46];
      final long l2 = src[47];
      dst[60] = l0;
      dst[61] = (l0 >>> 48) | (l1 << 16);
      dst[62] = (l1 >>> 32) | (l2 << 32);
      dst[63] = l2 >>> 16;
    }
    {
      final long l0 = src[48];
      final long l1 = src[49];
      final long l2 = src[50];
      dst[64] = l0;
      dst[65] = (l0 >>> 48) | (l1 << 16);
      dst[66] = (l1 >>> 32) | (l2 << 32);
      dst[67] = l2 >>> 16;
    }
    {
      final long l0 = src[51];
      final long l1 = src[52];
      final long l2 = src[53];
      dst[68] = l0;
      dst[69] = (l0 >>> 48) | (l1 << 16);
      dst[70] = (l1 >>> 32) | (l2 << 32);
      dst[71] = l2 >>> 16;
    }
    {
      final long l0 = src[54];
      final long l1 = src[55];
      final long l2 = src[56];
      dst[72] = l0;
      dst[73] = (l0 >>> 48) | (l1 << 16);
      dst[74] = (l1 >>> 32) | (l2 << 32);
      dst[75] = l2 >>> 16;
    }
    {
      final long l0 = src[57];
      final long l1 = src[58];
      final long l2 = src[59];
      dst[76] = l0;
      dst[77] = (l0 >>> 48) | (l1 << 16);
      dst[78] = (l1 >>> 32) | (l2 << 32);
      dst[79] = l2 >>> 16;
    }
    {
      final long l0 = src[60];
      final long l1 = src[61];
      final long l2 = src[62];
      dst[80] = l0;
      dst[81] = (l0 >>> 48) | (l1 << 16);
      dst[82] = (l1 >>> 32) | (l2 << 32);
      dst[83] = l2 >>> 16;
    }
    {
      final long l0 = src[63];
      final long l1 = src[64];
      final long l2 = src[65];
      dst[84] = l0;
      dst[85] = (l0 >>> 48) | (l1 << 16);
      dst[86] = (l1 >>> 32) | (l2 << 32);
      dst[87] = l2 >>> 16;
    }
    {
      final long l0 = src[66];
      final long l1 = src[67];
      final long l2 = src[68];
      dst[88] = l0;
      dst[89] = (l0 >>> 48) | (l1 << 16);
      dst[90] = (l1 >>> 32) | (l2 << 32);
      dst[91] = l2 >>> 16;
    }
    {
      final long l0 = src[69];
      final long l1 = src[70];
      final long l2 = src[71];
      dst[92] = l0;
      dst[93] = (l0 >>> 48) | (l1 << 16);
      dst[94] = (l1 >>> 32) | (l2 << 32);
      dst[95] = l2 >>> 16;
    }
    {
      final long l0 = src[72];
      final long l1 = src[73];
      final long l2 = src[74];
      dst[96] = l0;
      dst[97] = (l0 >>> 48) | (l1 << 16);
      dst[98] = (l1 >>> 32) | (l2 << 32);
      dst[99] = l2 >>> 16;
    }
    {
      final long l0 = src[75];
      final long l1 = src[76];
      final long l2 = src[77];
      dst[100] = l0;
      dst[101] = (l0 >>> 48) | (l1 << 16);
      dst[102] = (l1 >>> 32) | (l2 << 32);
      dst[103] = l2 >>> 16;
    }
    {
      final long l0 = src[78];
      final long l1 = src[79];
      final long l2 = src[80];
      dst[104] = l0;
      dst[105] = (l0 >>> 48) | (l1 << 16);
      dst[106] = (l1 >>> 32) | (l2 << 32);
      dst[107] = l2 >>> 16;
    }
    {
      final long l0 = src[81];
      final long l1 = src[82];
      final long l2 = src[83];
      dst[108] = l0;
      dst[109] = (l0 >>> 48) | (l1 << 16);
      dst[110] = (l1 >>> 32) | (l2 << 32);
      dst[111] = l2 >>> 16;
    }
    {
      final long l0 = src[84];
      final long l1 = src[85];
      final long l2 = src[86];
      dst[112] = l0;
      dst[113] = (l0 >>> 48) | (l1 << 16);
      dst[114] = (l1 >>> 32) | (l2 << 32);
      dst[115] = l2 >>> 16;
    }
    {
      final long l0 = src[87];
      final long l1 = src[88];
      final long l2 = src[89];
      dst[116] = l0;
      dst[117] = (l0 >>> 48) | (l1 << 16);
      dst[118] = (l1 >>> 32) | (l2 << 32);
      dst[119] = l2 >>> 16;
    }
    {
      final long l0 = src[90];
      final long l1 = src[91];
      final long l2 = src[92];
      dst[120] = l0;
      dst[121] = (l0 >>> 48) | (l1 << 16);
      dst[122] = (l1 >>> 32) | (l2 << 32);
      dst[123] = l2 >>> 16;
    }
    {
      final long l0 = src[93];
      final long l1 = src[94];
      final long l2 = src[95];
      dst[124] = l0;
      dst[125] = (l0 >>> 48) | (l1 << 16);
      dst[126] = (l1 >>> 32) | (l2 << 32);
      dst[127] = l2 >>> 16;
    }
    maskLongs(dst, 281474976710655L);
  }

  static void decode56(long[] src, long[] dst) {
    {
      final long l0 = src[0];
      final long l1 = src[1];
      final long l2 = src[2];
      final long l3 = src[3];
      final long l4 = src[4];
      final long l5 = src[5];
      final long l6 = src[6];
      dst[0] = l0;
      dst[1] = (l0 >>> 56) | (l1 << 8);
      dst[2] = (l1 >>> 48) | (l2 << 16);
      dst[3] = (l2 >>> 40) | (l3 << 24);
      dst[4] = (l3 >>> 32) | (l4 << 32);
      dst[5] = (l4 >>> 24) | (l5 << 40);
      dst[6] = (l5 >>> 16) | (l6 << 48);
      dst[7] = l6 >>> 8;
    }
    {
      final long l0 = src[7];
      final long l1 = src[8];
      final long l2 = src[9];
      final long l3 = src[10];
      final long l4 = src[11];
      final long l5 = src[12];
      final long l6 = src[13];
      dst[8] = l0;
      dst[9] = (l0 >>> 56) | (l1 << 8);
      dst[10] = (l1 >>> 48) | (l2 << 16);
      dst[11] = (l2 >>> 40) | (l3 << 24);
      dst[12] = (l3 >>> 32) | (l4 << 32);
      dst[13] = (l4 >>> 24) | (l5 << 40);
      dst[14] = (l5 >>> 16) | (l6 << 48);
      dst[15] = l6 >>> 8;
    }
    {
      final long l0 = src[14];
      final long l1 = src[15];
      final long l2 = src[16];
      final long l3 = src[17];
      final long l4 = src[18];
      final long l5 = src[19];
      final long l6 = src[20];
      dst[16] = l0;
      dst[17] = (l0 >>> 56) | (l1 << 8);
      dst[18] = (l1 >>> 48) | (l2 << 16);
      dst[19] = (l2 >>> 40) | (l3 << 24);
      dst[20] = (l3 >>> 32) | (l4 << 32);
      dst[21] = (l4 >>> 24) | (l5 << 40);
      dst[22] = (l5 >>> 16) | (l6 << 48);
      dst[23] = l6 >>> 8;
    }
    {
      final long l0 = src[21];
      final long l1 = src[22];
      final long l2 = src[23];
      final long l3 = src[24];
      final long l4 = src[25];
      final long l5 = src[26];
      final long l6 = src[27];
      dst[24] = l0;
      dst[25] = (l0 >>> 56) | (l1 << 8);
      dst[26] = (l1 >>> 48) | (l2 << 16);
      dst[27] = (l2 >>> 40) | (l3 << 24);
      dst[28] = (l3 >>> 32) | (l4 << 32);
      dst[29] = (l4 >>> 24) | (l5 << 40);
      dst[30] = (l5 >>> 16) | (l6 << 48);
      dst[31] = l6 >>> 8;
    }
    {
      final long l0 = src[28];
      final long l1 = src[29];
      final long l2 = src[30];
      final long l3 = src[31];
      final long l4 = src[32];
      final long l5 = src[33];
      final long l6 = src[34];
      dst[32] = l0;
      dst[33] = (l0 >>> 56) | (l1 << 8);
      dst[34] = (l1 >>> 48) | (l2 << 16);
      dst[35] = (l2 >>> 40) | (l3 << 24);
      dst[36] = (l3 >>> 32) | (l4 << 32);
      dst[37] = (l4 >>> 24) | (l5 << 40);
      dst[38] = (l5 >>> 16) | (l6 << 48);
      dst[39] = l6 >>> 8;
    }
    {
      final long l0 = src[35];
      final long l1 = src[36];
      final long l2 = src[37];
      final long l3 = src[38];
      final long l4 = src[39];
      final long l5 = src[40];
      final long l6 = src[41];
      dst[40] = l0;
      dst[41] = (l0 >>> 56) | (l1 << 8);
      dst[42] = (l1 >>> 48) | (l2 << 16);
      dst[43] = (l2 >>> 40) | (l3 << 24);
      dst[44] = (l3 >>> 32) | (l4 << 32);
      dst[45] = (l4 >>> 24) | (l5 << 40);
      dst[46] = (l5 >>> 16) | (l6 << 48);
      dst[47] = l6 >>> 8;
    }
    {
      final long l0 = src[42];
      final long l1 = src[43];
      final long l2 = src[44];
      final long l3 = src[45];
      final long l4 = src[46];
      final long l5 = src[47];
      final long l6 = src[48];
      dst[48] = l0;
      dst[49] = (l0 >>> 56) | (l1 << 8);
      dst[50] = (l1 >>> 48) | (l2 << 16);
      dst[51] = (l2 >>> 40) | (l3 << 24);
      dst[52] = (l3 >>> 32) | (l4 << 32);
      dst[53] = (l4 >>> 24) | (l5 << 40);
      dst[54] = (l5 >>> 16) | (l6 << 48);
      dst[55] = l6 >>> 8;
    }
    {
      final long l0 = src[49];
      final long l1 = src[50];
      final long l2 = src[51];
      final long l3 = src[52];
      final long l4 = src[53];
      final long l5 = src[54];
      final long l6 = src[55];
      dst[56] = l0;
      dst[57] = (l0 >>> 56) | (l1 << 8);
      dst[58] = (l1 >>> 48) | (l2 << 16);
      dst[59] = (l2 >>> 40) | (l3 << 24);
      dst[60] = (l3 >>> 32) | (l4 << 32);
      dst[61] = (l4 >>> 24) | (l5 << 40);
      dst[62] = (l5 >>> 16) | (l6 << 48);
      dst[63] = l6 >>> 8;
    }
    {
      final long l0 = src[56];
      final long l1 = src[57];
      final long l2 = src[58];
      final long l3 = src[59];
      final long l4 = src[60];
      final long l5 = src[61];
      final long l6 = src[62];
      dst[64] = l0;
      dst[65] = (l0 >>> 56) | (l1 << 8);
      dst[66] = (l1 >>> 48) | (l2 << 16);
      dst[67] = (l2 >>> 40) | (l3 << 24);
      dst[68] = (l3 >>> 32) | (l4 << 32);
      dst[69] = (l4 >>> 24) | (l5 << 40);
      dst[70] = (l5 >>> 16) | (l6 << 48);
      dst[71] = l6 >>> 8;
    }
    {
      final long l0 = src[63];
      final long l1 = src[64];
      final long l2 = src[65];
      final long l3 = src[66];
      final long l4 = src[67];
      final long l5 = src[68];
      final long l6 = src[69];
      dst[72] = l0;
      dst[73] = (l0 >>> 56) | (l1 << 8);
      dst[74] = (l1 >>> 48) | (l2 << 16);
      dst[75] = (l2 >>> 40) | (l3 << 24);
      dst[76] = (l3 >>> 32) | (l4 << 32);
      dst[77] = (l4 >>> 24) | (l5 << 40);
      dst[78] = (l5 >>> 16) | (l6 << 48);
      dst[79] = l6 >>> 8;
    }
    {
      final long l0 = src[70];
      final long l1 = src[71];
      final long l2 = src[72];
      final long l3 = src[73];
      final long l4 = src[74];
      final long l5 = src[75];
      final long l6 = src[76];
      dst[80] = l0;
      dst[81] = (l0 >>> 56) | (l1 << 8);
      dst[82] = (l1 >>> 48) | (l2 << 16);
      dst[83] = (l2 >>> 40) | (l3 << 24);
      dst[84] = (l3 >>> 32) | (l4 << 32);
      dst[85] = (l4 >>> 24) | (l5 << 40);
      dst[86] = (l5 >>> 16) | (l6 << 48);
      dst[87] = l6 >>> 8;
    }
    {
      final long l0 = src[77];
      final long l1 = src[78];
      final long l2 = src[79];
      final long l3 = src[80];
      final long l4 = src[81];
      final long l5 = src[82];
      final long l6 = src[83];
      dst[88] = l0;
      dst[89] = (l0 >>> 56) | (l1 << 8);
      dst[90] = (l1 >>> 48) | (l2 << 16);
      dst[91] = (l2 >>> 40) | (l3 << 24);
      dst[92] = (l3 >>> 32) | (l4 << 32);
      dst[93] = (l4 >>> 24) | (l5 << 40);
      dst[94] = (l5 >>> 16) | (l6 << 48);
      dst[95] = l6 >>> 8;
    }
    {
      final long l0 = src[84];
      final long l1 = src[85];
      final long l2 = src[86];
      final long l3 = src[87];
      final long l4 = src[88];
      final long l5 = src[89];
      final long l6 = src[90];
      dst[96] = l0;
      dst[97] = (l0 >>> 56) | (l1 << 8);
      dst[98] = (l1 >>> 48) | (l2 << 16);
      dst[99] = (l2 >>> 40) | (l3 << 24);
      dst[100] = (l3 >>> 32) | (l4 << 32);
      dst[101] = (l4 >>> 24) | (l5 << 40);
      dst[102] = (l5 >>> 16) | (l6 << 48);
      dst[103] = l6 >>> 8;
    }
    {
      final long l0 = src[91];
      final long l1 = src[92];
      final long l2 = src[93];
      final long l3 = src[94];
      final long l4 = src[95];
      final long l5 = src[96];
      final long l6 = src[97];
      dst[104] = l0;
      dst[105] = (l0 >>> 56) | (l1 << 8);
      dst[106] = (l1 >>> 48) | (l2 << 16);
      dst[107] = (l2 >>> 40) | (l3 << 24);
      dst[108] = (l3 >>> 32) | (l4 << 32);
      dst[109] = (l4 >>> 24) | (l5 << 40);
      dst[110] = (l5 >>> 16) | (l6 << 48);
      dst[111] = l6 >>> 8;
    }
    {
      final long l0 = src[98];
      final long l1 = src[99];
      final long l2 = src[100];
      final long l3 = src[101];
      final long l4 = src[102];
      final long l5 = src[103];
      final long l6 = src[104];
      dst[112] = l0;
      dst[113] = (l0 >>> 56) | (l1 << 8);
      dst[114] = (l1 >>> 48) | (l2 << 16);
      dst[115] = (l2 >>> 40) | (l3 << 24);
      dst[116] = (l3 >>> 32) | (l4 << 32);
      dst[117] = (l4 >>> 24) | (l5 << 40);
      dst[118] = (l5 >>> 16) | (l6 << 48);
      dst[119] = l6 >>> 8;
    }
    {
      final long l0 = src[105];
      final long l1 = src[106];
      final long l2 = src[107];
      final long l3 = src[108];
      final long l4 = src[109];
      final long l5 = src[110];
      final long l6 = src[111];
      dst[120] = l0;
      dst[121] = (l0 >>> 56) | (l1 << 8);
      dst[122] = (l1 >>> 48) | (l2 << 16);
      dst[123] = (l2 >>> 40) | (l3 << 24);
      dst[124] = (l3 >>> 32) | (l4 << 32);
      dst[125] = (l4 >>> 24) | (l5 << 40);
      dst[126] = (l5 >>> 16) | (l6 << 48);
      dst[127] = l6 >>> 8;
    }
    maskLongs(dst, 72057594037927935L);
  }

  private static void maskLongs(final long[] l, final long mask) {
    for (int i=0; i<128; i++) {
      l[i] = l[i] & mask;
    }
  }
}
