package org.apache.lucene.util.packed;

import org.apache.lucene.store.DataOutput;

import java.io.IOException;

public class BlockWriter {

  public static final int BLOCK_SIZE = ForUtil.BLOCK_SIZE;

  private final int bpv;
  private final long[] buffer;
  private final DataOutput output;
  private final ForUtil forUtil = new ForUtil();
  private int bufferIndex = 0;
  private boolean finished = false;

  public BlockWriter(DataOutput output, int bpv) {
    this.output = output;
    this.bpv = bpv;
    this.buffer = new long[BLOCK_SIZE];
  }

  public void add(long l) throws IOException {
    assert !finished;
    buffer[bufferIndex++] = l;
    if (bufferIndex == BLOCK_SIZE) {
      forUtil.encode(buffer, bpv, output);
      bufferIndex = 0;
    }
  }

  public void finish() throws IOException {
    assert !finished;
    if (bufferIndex > 0) {
      forUtil.encode(buffer, bpv, output);
    }
    bufferIndex = 0;
    finished = true;
  }
}
