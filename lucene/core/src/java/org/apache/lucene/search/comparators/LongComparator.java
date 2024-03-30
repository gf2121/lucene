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

package org.apache.lucene.search.comparators;

import org.apache.lucene.search.Pruning;
import org.apache.lucene.util.NumericUtils;

/**
 * Comparator based on {@link Long#compare} for {@code numHits}. This comparator provides a skipping
 * functionality – an iterator that can skip over non-competitive documents.
 */
public class LongComparator extends NumericComparator<Long> {

  public LongComparator(
      int numHits, String field, Long missingValue, boolean reverse, Pruning pruning) {
    super(numHits, field, missingValue != null ? missingValue : 0L, reverse, pruning, Long.BYTES);
  }

  @Override
  protected long sortableBytesToLong(byte[] bytes) {
    return NumericUtils.sortableBytesToLong(bytes, 0);
  }

  @Override
  protected long valueToComparableLong(Long value) {
    return value;
  }

  @Override
  protected Long comparableLongToValue(long comparableLong) {
    return comparableLong;
  }
}
