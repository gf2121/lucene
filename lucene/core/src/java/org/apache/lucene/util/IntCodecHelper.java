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
package org.apache.lucene.util;

import java.io.IOException;
import org.apache.lucene.store.DataOutput;
import org.apache.lucene.store.IndexInput;

/** encode ints 8/16/32 and decode them with auto-vectorization optimization */
public class IntCodecHelper {

  private static final int BLOCK_SIZE = 128;
  private static final int BLOCK_SIZE_MINUS_ONE = BLOCK_SIZE - 1;

  public static void writeInts8(DataOutput out, int count, int[] values, int offset)
      throws IOException {
    int k = 0;
    for (; k < count - BLOCK_SIZE_MINUS_ONE; k += BLOCK_SIZE) {
      int step = offset + k;
      for (int i = 0; i < 16; ++i) {
        long l =
            ((long) values[step + i] << 56)
                | ((long) values[step + 16 + i] << 48)
                | ((long) values[step + 32 + i] << 40)
                | ((long) values[step + 48 + i] << 32)
                | ((long) values[step + 64 + i] << 24)
                | ((long) values[step + 80 + i] << 16)
                | ((long) values[step + 96 + i] << 8)
                | (long) values[step + 112 + i];
        out.writeLong(l);
      }
    }
    for (; k < count; k++) {
      out.writeByte((byte) values[offset + k]);
    }
  }

  public static void writeInts16(DataOutput out, int count, int[] values, int offset)
      throws IOException {
    int k = 0;
    for (; k < count - BLOCK_SIZE_MINUS_ONE; k += BLOCK_SIZE) {
      int step = offset + k;
      for (int i = 0; i < 32; ++i) {
        long l =
            ((long) values[step + i] << 48)
                | ((long) values[step + 32 + i] << 32)
                | ((long) values[step + 64 + i] << 16)
                | (long) values[step + 96 + i];
        out.writeLong(l);
      }
    }
    for (; k < count; k++) {
      out.writeShort((short) values[offset + k]);
    }
  }

  public static void writeInts32(DataOutput out, int count, int[] values, int offset)
      throws IOException {
    int k = 0;
    for (; k < count - BLOCK_SIZE_MINUS_ONE; k += BLOCK_SIZE) {
      int step = offset + k;
      for (int i = 0; i < 64; ++i) {
        long l = ((long) values[step + i] << 32) | (long) values[step + 64 + i];
        out.writeLong(l);
      }
    }
    for (; k < count; k++) {
      out.writeInt(values[offset + k]);
    }
  }

  public static void readInts8(IndexInput in, int count, long[] values, int offset)
      throws IOException {
    int k = 0;
    for (; k < count - BLOCK_SIZE_MINUS_ONE; k += BLOCK_SIZE) {
      final int step = offset + k;
      in.readLongs(values, step, 16);
      for (int i = 0; i < 16; ++i) {
        final long l = values[step + i];
        values[step + i] = (l >>> 56) & 0xFFL;
        values[step + 16 + i] = (l >>> 48) & 0xFFL;
        values[step + 32 + i] = (l >>> 40) & 0xFFL;
        values[step + 48 + i] = (l >>> 32) & 0xFFL;
        values[step + 64 + i] = (l >>> 24) & 0xFFL;
        values[step + 80 + i] = (l >>> 16) & 0xFFL;
        values[step + 96 + i] = (l >>> 8) & 0xFFL;
        values[step + 112 + i] = l & 0xFFL;
      }
    }
    for (; k < count; k++) {
      values[offset + k] = Byte.toUnsignedInt(in.readByte());
    }
  }

  public static void readInts16(IndexInput in, int count, long[] values, int offset)
      throws IOException {
    int k = 0;
    for (; k < count - BLOCK_SIZE_MINUS_ONE; k += BLOCK_SIZE) {
      int step = offset + k;
      in.readLongs(values, step, 32);
      for (int i = 0; i < 32; ++i) {
        final long l = values[step + i];
        values[step + i] = (l >>> 48) & 0xFFFFL;
        values[step + 32 + i] = (l >>> 32) & 0xFFFFL;
        values[step + 64 + i] = (l >>> 16) & 0xFFFFL;
        values[step + 96 + i] = l & 0xFFFFL;
      }
    }
    for (; k < count; k++) {
      values[offset + k] = Short.toUnsignedInt(in.readShort());
    }
  }

  public static void readInts32(IndexInput in, int count, long[] values, int offset)
      throws IOException {
    int k = 0;
    for (; k < count - BLOCK_SIZE_MINUS_ONE; k += BLOCK_SIZE) {
      final int step = offset + k;
      in.readLongs(values, step, 64);
      for (int i = 0; i < 64; ++i) {
        final long l = values[step + i];
        values[step + i] = l >>> 32;
        values[step + 64 + i] = l & 0xFFFFFFFFL;
      }
    }
    for (; k < count; k++) {
      values[offset + k] = in.readInt();
    }
  }
}
