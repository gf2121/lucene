package org.apache.lucene.index;

import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.LongsRef;

public final class NullableLongBuffer {

  // null means dense, dense not guaranteed to be null.
  public FixedBitSet bitSet = null;
  public long[] values = LongsRef.EMPTY_LONGS;

  public void growNoCopy(int size) {
    if (values.length < size) {
      values = ArrayUtil.growNoCopy(values, size);
      if (bitSet != null) {
        bitSet = new FixedBitSet(values.length);
      }
    }
  }
}
