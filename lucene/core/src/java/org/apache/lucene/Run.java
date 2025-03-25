package org.apache.lucene;

import org.apache.lucene.util.FixedBitSet;

public class Run {

  public static void main(String[] args) {
    FixedBitSet bitSet = new FixedBitSet(4096);
    for (int i=0;i<4096;i++) {
      for (int x = 0; x < i; x++) {
        bitSet.set(x);
      }
      int f = firstUnsetMatchingBit(bitSet);
      if (i != f) {
        System.out.println(i + " vs " + f);
        throw new RuntimeException();
      }
      bitSet.clear();
    }
  }

  private static int firstUnsetMatchingBit(FixedBitSet matching) {
    long[] words = matching.getBits();
    for (int i = 0, len = words.length; i < len; i++) {
      long word = words[i];
      if (word != -1) {
        return (i << 6) + Long.numberOfTrailingZeros(~word);
      }
    }
    return matching.length();
  }
}
