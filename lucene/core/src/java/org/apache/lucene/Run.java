package org.apache.lucene;

import org.apache.lucene.util.FixedBitSet;

public class Run {

  public static void main(String[] args) {
    FixedBitSet bitSet = new FixedBitSet(4096);
    for (int i = 0; i < 4096; i++) {
      for (int x = 0; x < i; x++) {
        bitSet.set(x);
      }
      for (int j=0; j < 64; j++) {
        int f = nextUnsetBit(bitSet, j);
        if (f != Math.max(i, j)) {
          throw new RuntimeException(f + " vs " + i + " vs " + j);
        }
      }
      bitSet.clear();
    }
  }

  private static int nextUnsetBit(FixedBitSet bitSet, int index) {
    assert index >= 0 && index < bitSet.length();
    int i = index >> 6;
    long[] bits = bitSet.getBits();
    long word = (~bits[i]) >>> index;

    if (word != 0) {
      return index + Long.numberOfTrailingZeros(word);
    }

    while (++i < FixedBitSet.bits2words(4096)) {
      word = bits[i];
      if (word != -1L) {
        return (i << 6) + Long.numberOfTrailingZeros(~word);
      }
    }
    return 4096;
  }
}
