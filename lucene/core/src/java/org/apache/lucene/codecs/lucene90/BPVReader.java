package org.apache.lucene.codecs.lucene90;

import java.io.IOException;
import org.apache.lucene.store.RandomAccessInput;

public class BPVReader {

  public static long get1(final RandomAccessInput in, final long offset, final long index) {
    try {
      int shift = (int) (index & 7);
      return (in.readByte(offset + (index >>> 3)) >>> shift) & 0x1;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get2(final RandomAccessInput in, final long offset, final long index) {
    try {
      int shift = ((int) (index & 3)) << 1;
      return (in.readByte(offset + (index >>> 2)) >>> shift) & 0x3;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get4(final RandomAccessInput in, final long offset, final long index) {
    try {
      int shift = (int) (index & 1) << 2;
      return (in.readByte(offset + (index >>> 1)) >>> shift) & 0xF;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get8(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readByte(offset + index) & 0xFF;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get12(final RandomAccessInput in, final long offset, final long index) {
    try {
      long off = (index * 12) >>> 3;
      int shift = (int) (index & 1) << 2;
      return (in.readShort(offset + off) >>> shift) & 0xFFF;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get16(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readShort(offset + (index << 1)) & 0xFFFF;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get20(final RandomAccessInput in, final long offset, final long index) {
    try {
      long off = (index * 20) >>> 3;
      int shift = (int) (index & 1) << 2;
      return (in.readInt(offset + off) >>> shift) & 0xFFFFF;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get24(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readInt(offset + index * 3) & 0xFFFFFF;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get28(final RandomAccessInput in, final long offset, final long index) {
    try {
      long off = (index * 28) >>> 3;
      int shift = (int) (index & 1) << 2;
      return (in.readInt(offset + off) >>> shift) & 0xFFFFFFF;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get32(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readInt(offset + (index << 2)) & 0xFFFFFFFFL;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get40(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readLong(offset + index * 5) & 0xFFFFFFFFFFL;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get48(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readLong(offset + index * 6) & 0xFFFFFFFFFFFFL;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get56(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readLong(offset + index * 7) & 0xFFFFFFFFFFFFFFL;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static long get64(final RandomAccessInput in, final long offset, final long index) {
    try {
      return in.readLong(offset + (index << 3));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
