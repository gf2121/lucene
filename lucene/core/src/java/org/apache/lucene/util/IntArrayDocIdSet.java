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
import java.util.Arrays;
import java.util.stream.IntStream;
import org.apache.lucene.search.AbstractDocIdSetIterator;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.packed.PackedInts;

final class IntArrayDocIdSet extends DocIdSet {

  private static final long BASE_RAM_BYTES_USED =
      RamUsageEstimator.shallowSizeOfInstance(IntArrayDocIdSet.class);

  private final int[] docs;
  private final int length;
  private final int maxDoc;

  IntArrayDocIdSet(int[] docs, int length, int maxDoc) {
    if (docs[length] != DocIdSetIterator.NO_MORE_DOCS) {
      throw new IllegalArgumentException();
    }
    this.docs = docs;
    this.length = length;
    this.maxDoc = maxDoc;
  }

  @Override
  public long ramBytesUsed() {
    return BASE_RAM_BYTES_USED + RamUsageEstimator.sizeOf(docs);
  }

  @Override
  public DocIdSetIterator iterator() {
    if (length == 0) {
      return DocIdSetIterator.empty();
    }
    return new ArrayDocIdSetIterator(docs, length, maxDoc);
  }

  /**
   * A DocIdSetIterator implementation that sorts documents on-demand. Documents are partitioned
   * into 256 buckets by their Most Significant Byte. Each bucket is sorted only when needed during
   * iteration.
   */
  private static class ArrayDocIdSetIterator extends AbstractDocIdSetIterator {

    private static final int INSERTION_SORT_THRESHOLD = 30;
    private static final int HISTOGRAM_SIZE = 256;

    private final int[] buckets = new int[HISTOGRAM_SIZE];
    private final int[] histogram = new int[HISTOGRAM_SIZE];
    private final int bucketShift;
    private final int[] docs;
    private final int length;
    private final int maxDoc;
    private final int[] buffer;

    private int nextBucket = -1;
    private int i = 0;
    private int bucketFrom = 0;
    private int bucketTo = 0;

    ArrayDocIdSetIterator(int[] docs, int length, int maxDoc) {
      if (docs[length] != DocIdSetIterator.NO_MORE_DOCS) {
        throw new IllegalArgumentException();
      }

      this.docs = docs;
      this.length = length;
      this.maxDoc = maxDoc;
      this.bucketShift = PackedInts.bitsRequired(maxDoc - 1) - 8;
      this.buffer = new int[length];

      buildHistogram(docs, 0, length, buckets, bucketShift);
      sumHistogram(0, buckets);
      reorder(docs, 0, length, buckets, bucketShift, buffer);
      assert buckets[HISTOGRAM_SIZE - 1] == length;
    }

    @Override
    public int nextDoc() {
      return advance(doc + 1);
    }

    @Override
    public int advance(int target) {
      if (moveTo(target) == false) {
        return doc = DocIdSetIterator.NO_MORE_DOCS;
      }

      i = VectorUtil.findNextGEQ(docs, target, i, bucketTo);

      if (i == bucketTo) {
        return advance(nextBucket);
      }

      return doc = docs[i++];
    }

    @Override
    public void intoBitSet(int upTo, FixedBitSet bitSet, int offset) throws IOException {
      // TODO we can skip sort if whole block into bitset, but could that really happen?
      super.intoBitSet(upTo, bitSet, offset);
    }

    @Override
    public long cost() {
      return length;
    }

    private boolean moveTo(int target) {
      if (target >= nextBucket) {
        int bucket = target >>> bucketShift;
        while (bucketEmpty(bucket)) {
          bucket++;
        }

        if (bucket >= HISTOGRAM_SIZE) {
          return false;
        }

        i = bucketFrom = bucket == 0 ? 0 : buckets[bucket - 1];
        bucketTo = buckets[bucket];
        sort();
        nextBucket = (bucket + 1) << bucketShift;
      }
      return true;
    }

    private boolean bucketEmpty(int bucket) {
      if (bucket == 0) {
        return buckets[0] == 0;
      } else {
        return bucket < HISTOGRAM_SIZE && buckets[bucket] == buckets[bucket - 1];
      }
    }

    private void sort() {
      if (bucketTo - bucketFrom < INSERTION_SORT_THRESHOLD) {
        insertionSort(buffer, bucketFrom, bucketTo);
        System.arraycopy(buffer, bucketFrom, docs, bucketFrom, bucketTo - bucketFrom);
      } else {

        // LSB Radix Sort
        int[] arr = buffer;
        int[] buf = docs;

        for (int shift = 0; shift < bucketShift; shift += 8) {
          if (sort(arr, bucketFrom, bucketTo, histogram, shift, buf)) {
            int[] tmp = arr;
            arr = buf;
            buf = tmp;
          }
        }

        if (docs == buf) {
          System.arraycopy(arr, bucketFrom, docs, bucketFrom, bucketTo - bucketFrom);
        }
      }
    }

    private static boolean sort(
        int[] array, int from, int to, int[] histogram, int shift, int[] dest) {
      Arrays.fill(histogram, 0);
      buildHistogram(array, from, to, histogram, shift);
      if (histogram[0] == to - from) {
        return false;
      }
      sumHistogram(from, histogram);
      reorder(array, from, to, histogram, shift, dest);
      return true;
    }

    private static void buildHistogram(int[] array, int from, int to, int[] histogram, int shift) {
      for (int i = from; i < to; ++i) {
        final int b = (array[i] >>> shift) & 0xFF;
        histogram[b] += 1;
      }
    }

    private static void sumHistogram(int base, int[] histogram) {
      int accum = base;
      for (int i = 0; i < HISTOGRAM_SIZE; ++i) {
        final int count = histogram[i];
        histogram[i] = accum;
        accum += count;
      }
    }

    private static void reorder(
        int[] array, int from, int to, int[] histogram, int shift, int[] dest) {
      for (int i = from; i < to; ++i) {
        final int v = array[i];
        final int b = (v >>> shift) & 0xFF;
        dest[histogram[b]++] = v;
      }
    }

    private static void insertionSort(int[] array, int from, int to) {
      for (int i = from + 1; i < to; ++i) {
        for (int j = i; j > from; --j) {
          if (array[j - 1] > array[j]) {
            int tmp = array[j - 1];
            array[j - 1] = array[j];
            array[j] = tmp;
          } else {
            break;
          }
        }
      }
    }
  }
}
