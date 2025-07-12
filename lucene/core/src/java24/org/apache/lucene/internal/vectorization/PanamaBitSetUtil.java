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
package org.apache.lucene.internal.vectorization;

import java.util.stream.IntStream;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;

public class PanamaBitSetUtil extends BitSetUtil {

  static final PanamaBitSetUtil INSTANCE = new PanamaBitSetUtil();

  private static final int[] IDENTITY = IntStream.range(0, Long.SIZE).toArray();
  private static final int[] IDENTITY_MASK = IntStream.range(0, 16).map(i -> 1 << i).toArray();
  private static final int MASK = (1 << IntVector.SPECIES_PREFERRED.length()) - 1;

  PanamaBitSetUtil() {}

  @Override
  int word2Array(long word, int base, int[] docs, int offset) {
    int lWord = (int) word;
    int hWord = (int) (word >>> 32);

    IntVector identityMask = IntVector.fromArray(IntVector.SPECIES_PREFERRED, IDENTITY_MASK, 0);
    for (int i = 0; i < Integer.SIZE; i += IntVector.SPECIES_PREFERRED.length()) {
      VectorMask<Integer> mask = IntVector.broadcast(IntVector.SPECIES_PREFERRED, lWord)
          .and(identityMask)
          .compare(VectorOperators.NE, 0);
      IntVector.fromArray(IntVector.SPECIES_PREFERRED, IDENTITY, i)
          .add(base)
          .compress(mask)
          .reinterpretAsInts()
          .intoArray(docs, offset);
      offset += Integer.bitCount(lWord & MASK); // faster than mask.trueCount()
      lWord >>>= IntVector.SPECIES_PREFERRED.length();
    }
    for (int i = 0; i < Integer.SIZE; i += IntVector.SPECIES_PREFERRED.length()) {
      VectorMask<Integer> mask = IntVector.broadcast(IntVector.SPECIES_PREFERRED, hWord)
          .and(identityMask)
          .compare(VectorOperators.NE, 0);
      IntVector.fromArray(IntVector.SPECIES_PREFERRED, IDENTITY, i)
          .add(base)
          .compress(mask)
          .reinterpretAsInts()
          .intoArray(docs, offset);
      offset += Integer.bitCount(hWord & MASK); // faster than mask.trueCount()
      hWord >>>= IntVector.SPECIES_PREFERRED.length();
    }

    return offset;
  }
}
