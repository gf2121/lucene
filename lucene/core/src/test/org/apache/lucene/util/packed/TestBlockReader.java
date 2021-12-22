package org.apache.lucene.util.packed;

import com.carrotsearch.randomizedtesting.generators.RandomNumbers;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.DataOutput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

import java.io.IOException;

public class TestBlockReader extends LuceneTestCase {

  public void testWriteAndRead() throws IOException {
    for (int iter = 0; iter < 100; iter++) {
      final int valueCount = RandomNumbers.randomIntBetween(random(), 5, 100000);
      final long[] values = new long[valueCount];

      final int bpv = TestUtil.nextInt(random(), 1, 64);
      for (int i = 0; i < valueCount; ++i) {
        values[i] = RandomNumbers.randomLongBetween(random(), 0, PackedInts.maxValue(bpv));
      }

      final Directory d = new ByteBuffersDirectory();
      IndexOutput output = d.createOutput("test.bin", IOContext.DEFAULT);
      BlockWriter blockWriter = new BlockWriter(output, bpv);
      for (long l : values) {
        blockWriter.add(l);
      }
      blockWriter.finish();
      output.close();

      IndexInput input = d.openInput("test.bin", IOContext.READONCE);
      BlockReader blockReader = new BlockReader(input, bpv);
      for (int i = 0; i < 10000; i++) {
        int index = RandomNumbers.randomIntBetween(random(), 0, valueCount - 1);
        assertEquals(values[index], blockReader.get(index));
      }
      input.close();
      d.close();
    }
  }
}
