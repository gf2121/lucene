package org.apache.lucene.util.packed;


import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.LongValues;

public class BlockReader extends LongValues {

  public static final int BLOCK_SIZE = ForUtil.BLOCK_SIZE;
  private static final int BLOCK_MASK = ForUtil.BLOCK_SIZE - 1;

  private final int bpv;
  private final int blockBytes;
  private final ForUtil forUtil;
  private final IndexInput input;
  private final long[] buffer;
  private long currentBlock = -1;
  private final long offset;

  public BlockReader(IndexInput input, int bpv) {
    this(input, bpv, 0);
  }

  public BlockReader(IndexInput input, int bpv, long offset) {
    this(input, bpv, offset, new long[BLOCK_SIZE], new long[BLOCK_SIZE]);
  }

  public BlockReader(IndexInput input, int bpv, long offset, long[] tmp, long[] buffer) {
    this.bpv = bpv;
    this.forUtil = new ForUtil(tmp);
    this.buffer = buffer;
    this.input = input;
    this.blockBytes = forUtil.numBytes(bpv);
    this.offset = offset;
  }

  @Override
  public long get(long index) {
    long block = index >>> ForUtil.BLOCK_SIZE_LOG2;
    if (block != currentBlock) {
      fillBlock(block);
    }
    return buffer[(int) (index & BLOCK_MASK)];
  }

  private void fillBlock(long block) {
    try {
      input.seek(offset + block * blockBytes);
      forUtil.decode(bpv, input, buffer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    this.currentBlock = block;
  }
}
