package org.apache.lucene.util.packed;

import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.LongValues;

import java.io.IOException;

public class ForwardBlockReader extends LongValues {


  public static final int BLOCK_SIZE = ForUtil.BLOCK_SIZE;
  private static final int BLOCK_MASK = ForUtil.BLOCK_SIZE - 1;

  private final int blockBytes;
  private final ForUtil.Decoder decoder;
  private final IndexInput input;
  private final long[] buffer;
  private final long offset;
  private final long numValues;

  private long maxCurrentBlockIndex;

  public ForwardBlockReader(IndexInput input, int bpv, long numValues) {
    this(input, bpv, 0, numValues);
  }

  public ForwardBlockReader(IndexInput input, int bpv, long offset, long numValues) {
    this(input, bpv, offset, new ForUtil(), new long[BLOCK_SIZE], numValues);
  }

  ForwardBlockReader(
          IndexInput input, int bpv, long offset, ForUtil forUtil, long[] buffer, long numValues) {
    this.buffer = buffer;
    this.input = input;
    this.blockBytes = forUtil.numBytes(bpv);
    this.offset = offset;
    this.numValues = numValues;
    this.decoder = forUtil.decoder(bpv);
  }

  public long get(long index) {
    assert index >= 0 && index < numValues;

    if (index < maxCurrentBlockIndex) {
      return buffer[(int) (index & BLOCK_MASK)];
    }

    try {
      long block = index >>> ForUtil.BLOCK_SIZE_LOG2;
      input.seek(offset + block * blockBytes);
      decoder.decode(input, buffer);
      this.maxCurrentBlockIndex = ((block + 1) << ForUtil.BLOCK_SIZE_LOG2);
      return buffer[(int) (index & BLOCK_MASK)];
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
