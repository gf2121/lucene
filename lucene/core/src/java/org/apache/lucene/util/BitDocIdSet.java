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
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;

/**
 * Implementation of the {@link DocIdSet} interface on top of a {@link BitSet}.
 *
 * @lucene.internal
 */
public class BitDocIdSet extends DocIdSet {

  private static final long BASE_RAM_BYTES_USED =
      RamUsageEstimator.shallowSizeOfInstance(BitDocIdSet.class);

  private final BitSet set;
  private final long cost;

  /**
   * Wrap the given {@link BitSet} as a {@link DocIdSet}. The provided {@link BitSet} must not be
   * modified afterwards.
   */
  public BitDocIdSet(BitSet set, long cost) {
    if (cost < 0) {
      throw new IllegalArgumentException("cost must be >= 0, got " + cost);
    }
    this.set = set;
    this.cost = cost;
  }

  /**
   * Same as {@link #BitDocIdSet(BitSet, long)} but uses the set's {@link
   * BitSet#approximateCardinality() approximate cardinality} as a cost.
   */
  public BitDocIdSet(BitSet set) {
    this(set, set.approximateCardinality());
  }

  @Override
  public DocIdSetIterator iterator() {
    return new BitSetIterator(set, cost);
  }

  @Override
  public int count(int min, int max) throws IOException {
    if (set instanceof FixedBitSet fixedBitSet) {
      if (min >= fixedBitSet.length()) {
        return 0;
      }
      if (max > fixedBitSet.length()) {
        max = set.length();
      }

      long[] bits = fixedBitSet.getBits();
      int startWord = min >> 6;
      int endWord = (max - 1) >> 6;
      long startmask = -1L << min;
      long endmask = -1L >>> -max;

      if (startWord == endWord) {
        return Long.bitCount(bits[startWord] & (startmask & endmask));
      }

      int cnt = Long.bitCount(bits[startWord] & startmask) + Long.bitCount(bits[endWord] & endmask);
      for (int i = startWord + 1; i < endWord; i++) {
        cnt += Long.bitCount(bits[i]);
      }
      return cnt;
    } else {
      // TODO impl sparse
      return super.count(min, max);
    }
  }

  @Override
  public int countAll() {
    return set.cardinality();
  }

  @Override
  public BitSet bits() {
    return set;
  }

  @Override
  public long ramBytesUsed() {
    return BASE_RAM_BYTES_USED + set.ramBytesUsed();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(set=" + set + ",cost=" + cost + ")";
  }
}
