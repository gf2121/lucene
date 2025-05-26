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

package org.apache.lucene.index;

import java.io.IOException;
import java.util.stream.IntStream;
import org.apache.lucene.util.FixedBitSet;

/** A per-document numeric value. */
public abstract class NumericDocValues extends DocValuesIterator {

  /** Sole constructor. (For invocation by subclass constructors, typically implicit.) */
  protected NumericDocValues() {}

  /**
   * Returns the numeric value for the current document ID. It is illegal to call this method after
   * {@link #advanceExact(int)} returned {@code false}.
   *
   * @return numeric value
   */
  public abstract long longValue() throws IOException;

  public void nextValues(int[] docs, int size, NullableLongBuffer buffer) throws IOException {
    assert IntStream.range(0, size - 1).allMatch(i -> docs[i] < docs[i + 1]);

    buffer.growNoCopy(size);

    int i = 0;

    if (buffer.bitSet == null) {
      for (; i < size; i++) {
        if (advanceExact(docs[i])) {
          buffer.values[i] = longValue();
        } else {
          buffer.bitSet = new FixedBitSet(buffer.values.length);
          buffer.bitSet.set(0, i);
          i++;
          break;
        }
      }
    } else {
      buffer.bitSet.clear();
    }

    if (buffer.bitSet != null) {
      for (; i < size; i++) {
        if (advanceExact(docs[i])) {
          buffer.bitSet.set(i);
          buffer.values[i] = longValue();
        }
      }
    }
  }
}
