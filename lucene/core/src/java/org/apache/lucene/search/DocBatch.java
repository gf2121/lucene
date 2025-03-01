package org.apache.lucene.search;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.IntArrayDocIdSet;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.VectorUtil;

public class DocBatch extends DocIdStream{

  private enum BatchType {
    ARRAY,
    BITSET,
    RANGE
  }

  private final int size;

  private int base;
  private int upTo;
  private BatchType type = BatchType.RANGE;

  // array
  private int[] arrayDocs = IntsRef.EMPTY_INTS;
  private int arrayLength;

  // bitset
  private FixedBitSet bitset;

  //  range
  private int rangeStartInclusive;
  private int rangeEndExclusive;

  public DocBatch(int base, int size) {
    this.base = base;
    this.size = size;
    this.upTo = base + size;
  }

  public DocBatch(int size) {
    this.size = size;
  }

  public int getBase() {
    return base;
  }

  public void setBase(int base) {
    this.base = base;
    this.upTo = base + size;
    clear();
  }

  public int getUpTo() {
    return upTo;
  }

  public int getSize() {
    return size;
  }

  public void appendDISI(DocIdSetIterator disi) throws IOException {
    switch (type) {
      case ARRAY -> {
        int[] docs = arrayDocs;
        int index = arrayLength;
        for (int doc = disi.docID(); doc < upTo; doc = disi.nextDoc()) {
          if (index >= docs.length) {
            docs = ArrayUtil.grow(docs, index);
          }
          docs[index++] = doc;
        }
        arrayDocs = docs;
        arrayLength = index;
      }
      case BITSET -> {
        for (int doc = disi.docID(); doc < upTo; doc = disi.nextDoc()) {
          bitset.set(doc - base);
        }
      }
      case RANGE -> {
        initBitset();
        if (rangeStartInclusive != rangeEndExclusive) {
          bitset.set(rangeStartInclusive - base, rangeEndExclusive - base);
        }
        for (int doc = disi.docID(); doc < upTo; doc = disi.nextDoc()) {
          bitset.set(doc - base);
        }
        asBitset(bitset);
      }
    }
  }

  public void appendArray(int[] docs, int offset, int length) {
    Objects.checkFromIndexSize(offset, length, docs.length);
    assert assertStrictlySorted(docs, offset, length);
    assert docs[offset] >= base;
    assert docs[offset + length - 1] <= upTo;

    switch (type) {
      case ARRAY -> {
        if (dense(arrayLength + length)) {
          initBitset();
          for (int i = 0, to = arrayLength; i < to; i++) {
            bitset.set(arrayDocs[i] - base);
          }
          for (int i = offset, to = offset + length; i < to; i++) {
            bitset.set(docs[i] - base);
          }
          asBitset(bitset);
        } else {
          int targetLen = arrayLength + length;
          arrayDocs = ArrayUtil.grow(arrayDocs, targetLen);
          System.arraycopy(docs, offset, arrayDocs, arrayLength, length);
          arrayLength = targetLen;
        }
      }
      case BITSET -> {
        for (int i = offset, to = offset + length; i < to; i++) {
          bitset.set(docs[i] - base);
        }
      }
      case RANGE -> {
        if (rangeStartInclusive == rangeEndExclusive) {
          int[] array = ArrayUtil.growNoCopy(arrayDocs, length);
          System.arraycopy(docs, offset, array, 0, length);
          asArray(array, length);
          return;
        }
        if (docs[offset] == rangeEndExclusive
            && docs[offset] + length - 1 == docs[offset + length - 1]) {
          rangeEndExclusive += length;
          return;
        }
        int valueCount = rangeEndExclusive - rangeStartInclusive + length;
        if (dense(valueCount)) {
          initBitset();
          bitset.set(rangeStartInclusive - base, rangeEndExclusive - base);
          for (int i = offset, to = offset + length; i < to; i++) {
            bitset.set(docs[i] - base);
          }
          assert bitset.cardinality() == valueCount;
          asBitset(bitset);
        } else {
          int[] array = ArrayUtil.growNoCopy(arrayDocs, valueCount);
          int i = 0;
          for (int doc = rangeStartInclusive; doc < rangeEndExclusive; doc++) {
            array[i++] = doc;
          }
          System.arraycopy(docs, offset, array, i, length);
          assert i + length == valueCount;
          asArray(array, valueCount);
        }
      }
    }
  }

  public void appendBitset(
      int docBase, FixedBitSet bits, int bitsStartInclusive, int bitsEndExclusive) {
    Objects.checkFromIndexSize(bitsStartInclusive, bitsEndExclusive, bits.length());
    assert docBase + bitsStartInclusive >= base;
    assert docBase + bitsEndExclusive <= upTo;

    switch (type) {
      case ARRAY -> {
        initBitset();
        for (int i = 0, to = arrayLength; i < to; i++) {
          bitset.set(arrayDocs[i] - base);
        }
        asBitset(bitset);
      }
      case RANGE -> {
        initBitset();
        bitset.set(rangeStartInclusive - base, rangeEndExclusive - base);
        asBitset(bitset);
      }
    }

    FixedBitSet.orRange(
        bits,
        bitsStartInclusive,
        bitset,
        bitsStartInclusive + docBase - base,
        bitsEndExclusive - bitsStartInclusive);
  }

  public void appendRange(int startInclusive, int endExclusive) {
    assert startInclusive >= base;
    assert endExclusive <= upTo;
    switch (type) {
      case ARRAY -> {
        initBitset();
        for (int i = 0; i < arrayLength; i++) {
          bitset.set(arrayDocs[i] - base);
        }
        bitset.set(startInclusive - base, endExclusive - base);
        asBitset(bitset);
      }
      case BITSET -> {
        bitset.set(startInclusive - base, endExclusive - base);
      }
      case RANGE -> {
        if (rangeStartInclusive == rangeEndExclusive) {
          rangeStartInclusive = startInclusive;
          rangeEndExclusive = endExclusive;
        } else if (rangeEndExclusive == startInclusive) {
          rangeEndExclusive = endExclusive;
        } else {
          initBitset();
          bitset.set(rangeStartInclusive - base, rangeEndExclusive - base);
          bitset.set(startInclusive - base, endExclusive - base);
          asBitset(bitset);
        }
      }
    }
  }

  @Override
  public void forEach(CheckedIntConsumer<IOException> consumer) throws IOException {
    switch (type) {
      case ARRAY -> {
        int[] docs = arrayDocs;
        for (int i = 0, to = arrayLength; i < to; i++) {
          consumer.accept(docs[i]);
        }
      }
      case BITSET -> {
        int base = this.base;
        long[] words = bitset.getBits();
        for (int idx = 0; idx < words.length; idx++) {
          long bits = words[idx];
          while (bits != 0L) {
            int ntz = Long.numberOfTrailingZeros(bits);
            consumer.accept(base + ((idx << 6) | ntz));
            bits ^= 1L << ntz;
          }
        }
      }
      case RANGE -> {
        for (int i = rangeStartInclusive; i < rangeEndExclusive; i++) {
          consumer.accept(i);
        }
      }
    }
  }

  public void and(DocBatch batch) {
    if (size != batch.size || base != batch.base) {
      throw new IllegalArgumentException();
    }
    switch (type) {
      case ARRAY -> {
        switch (batch.type) {
          case ARRAY -> andArrayArray(batch.arrayDocs, batch.arrayLength);
          case BITSET -> andArrayBitset(arrayDocs, arrayLength, batch.bitset);
          case RANGE -> andArrayRange(arrayDocs, arrayLength, batch.rangeStartInclusive, batch.rangeEndExclusive);
        }
      }
      case BITSET -> {
        switch (batch.type) {
          case BITSET -> andBitsetBitset(batch.bitset);
          case RANGE -> andBitsetRange(bitset, batch.rangeStartInclusive, batch.rangeEndExclusive);
          case ARRAY -> andArrayBitset(batch.arrayDocs, batch.arrayLength, bitset);
        }
      }
      case RANGE -> {
        switch (batch.type) {
          case ARRAY -> andArrayRange(batch.arrayDocs, batch.arrayLength, rangeStartInclusive, rangeEndExclusive);
          case BITSET -> andBitsetRange(batch.bitset, rangeStartInclusive, rangeEndExclusive);
          case RANGE -> andRangeRange(batch.rangeStartInclusive, batch.rangeEndExclusive);
        }
      }
    }
  }

  public void clear() {
    arrayLength = 0;
    asRange(base, base);
  }

  private void andArrayArray(int[] docs, int length) {
    assert type == BatchType.ARRAY;

    if (arrayLength == 0 || length == 0) {
      clear();
      return;
    }

    final int toI = arrayLength, toJ = length;
    final int[] docsI = arrayDocs, docsJ = docs;

    if (docsI[0] > docsJ[toJ - 1] || docsJ[0] > docsI[toI - 1]) {
      clear();
      return;
    }

    int i = 0, j = 0, k = 0;
    while (i < toI && j < toJ) {
      int docI = docsI[i], docJ = docsJ[j];
      if (docJ == docI) {
        docsI[k++] = docI;
        ++i;
        ++j;
      } else if (docI < docJ) {
        i = VectorUtil.findNextGEQ(docsI, docJ, i + 1, toI);
      } else {
        j = VectorUtil.findNextGEQ(docsJ, docI, j + 1, toJ);
      }
    }

    asArray(docsI, k);
  }

  private void andArrayBitset(int[] docs, int length, FixedBitSet bitset) {
    int[] result = ArrayUtil.growNoCopy(arrayDocs, length);
    int k = 0;
    for (int i = 0, offset = base; i < length; i++) {
      int doc = docs[i];
      if (bitset.get(doc - offset)) {
        result[k++] = doc;
      }
    }
    asArray(result, k);
  }

  private void andArrayRange(int[] docs, int length, int startInclusive, int endExclusive) {
    assert endExclusive >= startInclusive;

    if (endExclusive == startInclusive || docs[0] >= endExclusive || docs[length - 1] < startInclusive) {
      clear();
      return;
    }

    int start = VectorUtil.findNextGEQ(docs, startInclusive, 0, length);
    int end = VectorUtil.findNextGEQ(docs, endExclusive, start, length);

    if (start == 0 && arrayDocs == docs) {
      asArray(arrayDocs, end);
    } else {
      int[] result = ArrayUtil.growNoCopy(arrayDocs, end);
      System.arraycopy(docs, start, result, 0, end - start);
      asArray(result, end - start);
    }
  }

  private void andBitsetBitset(FixedBitSet bitset) {
    assert type == BatchType.BITSET;
    this.bitset.and(bitset);
  }

  private void andRangeRange(int startInclusive, int endExclusive) {
    assert type == BatchType.RANGE;
    rangeStartInclusive = Math.max(rangeStartInclusive, startInclusive);
    rangeEndExclusive = Math.min(rangeEndExclusive, endExclusive);
    if (rangeStartInclusive >= rangeEndExclusive) {
      clear();
    }
  }

  public void maybeTrim() {
    switch (type) {
      case ARRAY -> {
        if (arrayLength == 0) {
          clear();
        } else if (arrayDocs[0] + arrayLength - 1 == arrayDocs[arrayLength - 1]) {
          asRange(arrayDocs[0], arrayDocs[arrayLength - 1] + 1);
        }
      }
      case BITSET -> {
        int cardinality = cardinality();
        if (cardinality == size) {
          asRange(base, upTo);
        }
        else if (dense(cardinality) == false) {
          arrayLength = cardinality;
          int[] docs = ArrayUtil.growNoCopy(arrayDocs, cardinality);
          int base = this.base;
          long[] words = bitset.getBits();
          for (int idx = 0, i = 0; idx < words.length; idx++) {
            long bits = words[idx];
            while (bits != 0L) {
              int ntz = Long.numberOfTrailingZeros(bits);
              docs[i++] = base + ((idx << 6) | ntz);
              bits ^= 1L << ntz;
            }
          }
          asArray(docs, cardinality);
        }
      }
    }
  }

  @Override
  public int count() throws IOException {
    return cardinality();
  }

  public int cardinality() {
    switch (type) {
      case ARRAY -> {
        return arrayLength;
      }
      case BITSET -> {
        return bitset.cardinality();
      }
      case RANGE -> {
        return rangeEndExclusive - rangeStartInclusive;
      }
      default -> {
        throw new AssertionError();
      }
    }
  }

  private void andBitsetRange(FixedBitSet bitset, int startInclusive, int endExclusive) {
    if (startInclusive == endExclusive) {
      clear();
      return;
    }
    if (bitset != this.bitset) {
      initBitset();
    }
    FixedBitSet result = this.bitset;
    if (bitset != result) {
      System.arraycopy(bitset.getBits(), 0, result.getBits(), 0, bitset.getBits().length);
    }
    result.clear(endExclusive - base, bitset.length());
    if (startInclusive > base) {
      result.clear(0, startInclusive - base);
    }
    asBitset(result);
  }

  void asArray(int[] docs, int length) {
    assert assertStrictlySorted(docs, 0, length);
    assert length == 0 || docs[0] >= base;
    assert length == 0 || docs[length - 1] < upTo;
    type = BatchType.ARRAY;
    arrayDocs = docs;
    arrayLength = length;
  }

  void asBitset(FixedBitSet bitSet) {
    assert bitSet.length() == size;
    type = BatchType.BITSET;
    bitset = bitSet;
  }

  void asRange(int startInclusive, int endExclusive) {
    assert endExclusive >= startInclusive;
    assert startInclusive >= base;
    assert endExclusive <= upTo;
    type = BatchType.RANGE;
    rangeStartInclusive = startInclusive;
    rangeEndExclusive = endExclusive;
  }

  private void initBitset() {
    if (bitset == null) {
      bitset = new FixedBitSet(size);
    } else {
      bitset.clear();
    }
  }

  private boolean dense(int valueCount) {
    return valueCount > (size >> 5);
  }

  private static boolean assertStrictlySorted(int[] docs, int offset, int length) {
    assert IntStream.range(offset, offset + length - 1).allMatch(i -> docs[i + 1] > docs[i])
        : "not strictly sorted "
            + Arrays.toString(ArrayUtil.copyOfSubArray(docs, offset, offset + length));
    return true;
  }
}
