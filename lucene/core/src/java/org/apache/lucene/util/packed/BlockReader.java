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
 * NOTE: this reader need to be used in forward reading case.
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
  // if we only get less than 4 times in the first block, we assume it is a sparse reading and do not warm up.
  private static final int SAMPLE_THRESHOLD = 4;

  private final int blockBytes;
  private final ForUtil.Decoder decoder;
  private final IndexInput input;
  private final long[] buffer;
  private final long offset;
  private final long numValues;

  private long currentBlock = -1;
  private boolean checking = true;
  private boolean doWarm = true;
  private long maxCheckIndex;
  private int counter = 0;

  public BlockReader(IndexInput input, int bpv, long numValues) {
    this(input, bpv, 0, numValues);
  }

  public BlockReader(IndexInput input, int bpv, long offset, long numValues) {
    this(input, bpv, offset, new ForUtil(), new long[BLOCK_SIZE], numValues);
  }

  public BlockReader(
          IndexInput input, int bpv, long offset, ForUtil forUtil, long[] buffer, long numValues) {
    this.buffer = buffer;
    this.input = input;
    this.blockBytes = forUtil.numBytes(bpv);
    this.offset = offset;
    this.numValues = numValues;
    this.decoder = forUtil.decoder(bpv);
  }

  @Override
  public long get(long index) {
    assert index >= 0 && index < numValues;
    try {
      if (checking) {
        check(index);
      }
      return doWarm ? warm(index) : doGet(index);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void check(long index) {
    if (counter++ == 0) {
      maxCheckIndex = index + BLOCK_SIZE;
    }
    if (index >= maxCheckIndex) {
      System.out.println(counter);
      if (counter < SAMPLE_THRESHOLD) {
        System.out.println("error!!!" + counter);
        doWarm = false;
      }
      checking = false;
    }
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
}
