package org.apache.lucene.codecs.lucene103;

import java.io.IOException;
import org.apache.lucene.store.DataInput;
import org.apache.lucene.store.DataOutput;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BitUtil;
import org.apache.lucene.util.BytesRef;

public class VNumbers {

  public static void writeVLong(DataOutput out, long i) throws IOException {
    if (i <= 0x0FFFFFFFFFFFFFFFL) {
      i <<= 3;
      int bytes = Long.BYTES - (Long.numberOfLeadingZeros(i | 1) >>> 3);
      i |= 8 - bytes;
      for (int b = 0; b < bytes; b++) {
        out.writeByte((byte) i);
        i >>>= 8;
      }
    } else {
      out.writeLong((i & ~0x07L) | 0x8000000000000000L);
      out.writeByte((byte) (i & 0x07));
    }
  }

  public static void writeVInt(DataOutput out, int i) throws IOException {
    if (i <= 0x1FFFFFFF) {
      i <<= 2;
      int bytes = Integer.BYTES - (Integer.numberOfLeadingZeros(i | 1) >>> 3);
      i |= 4 - bytes;
      for (int b = 0; b < bytes; b++) {
        out.writeByte((byte) i);
        i >>>= 8;
      }
    } else {
      out.writeInt((i & ~0x03) | 0x80000000);
      out.writeByte((byte) (i & 0x03));
    }
  }

  public static class Reader {

    private byte[] bytes = BytesRef.EMPTY_BYTES;
    private int pos;
    private int len;

    public void reset(DataInput in, int len) throws IOException {
      if (bytes.length < len + 7) {
        bytes = ArrayUtil.growNoCopy(bytes, len + 7);
      }
      in.readBytes(bytes, 0, len);
      this.pos = 0;
      this.len = len;
    }

    public int length() {
      return len;
    }

    public int readVInt() {
      int v = (int) BitUtil.VH_LE_INT.get(bytes, pos);
      switch (v & 0x3) {
        case 3:
          pos++;
          return (v >>> 2) & 0x3F;
        case 2:
          pos += 2;
          return (v >>> 2) & 0x3FFF;
        case 1:
          pos += 3;
          return (v >>> 2) & 0x3FFFFF;
        case 0:
          pos += 4;
          if (v >= 0) {
            return (v >>> 2) & 0x3FFFFFFF;
          } else {
            return (v ^ 0x80000000) | bytes[pos++];
          }
        default:
          throw new AssertionError();
      }
    }

    public long readVLong() {
      long v = (long) BitUtil.VH_LE_LONG.get(bytes, pos);
      switch ((int) (v & 0x07)) {
        case 7:
          pos++;
          return (v >>> 3) & 0x1F;
        case 6:
          pos += 2;
          return (v >>> 3) & 0x1FFF;
        case 5:
          pos += 3;
          return (v >>> 3) & 0x1FFFFF;
        case 4:
          pos += 4;
          return (v >>> 3) & 0x1FFFFFFF;
        case 3:
          pos += 5;
          return (v >>> 3) & 0x1FFFFFFFFFL;
        case 2:
          pos += 6;
          return (v >>> 3) & 0x1FFFFFFFFFFFL;
        case 1:
          pos += 7;
          return (v >>> 3) & 0x1FFFFFFFFFFFFFL;
        case 0:
          pos += 8;
          if (v >= 0) {
            return (v >>> 3) & 0x1FFFFFFFFFFFFFFFL;
          } else {
            return (v ^ 0x8000000000000000L) | bytes[pos++];
          }
        default:
          throw new AssertionError();
      }
    }
  }
}
