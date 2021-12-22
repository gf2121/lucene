package org.apache.lucene.util.packed;

import com.carrotsearch.randomizedtesting.generators.RandomNumbers;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

import java.io.IOException;
import java.util.Arrays;

public class TestForUtil extends LuceneTestCase {

  public void testEncodeDecode() throws IOException {
    final int iterations = RandomNumbers.randomIntBetween(random(), 50, 1000);
    final long[] values = new long[iterations * ForUtil.BLOCK_SIZE];

    for (int i = 0; i < iterations; ++i) {
      final int bpv = TestUtil.nextInt(random(), 1, 64);
      for (int j = 0; j < ForUtil.BLOCK_SIZE; ++j) {
        values[i * ForUtil.BLOCK_SIZE + j] =
                RandomNumbers.randomLongBetween(random(), 0, PackedInts.maxValue(bpv));
      }
    }

    final Directory d = new ByteBuffersDirectory();
    final long endPointer;

    {
      // encode
      IndexOutput out = d.createOutput("test.bin", IOContext.DEFAULT);
      final ForUtil forUtil = new ForUtil();

      for (int i = 0; i < iterations; ++i) {
        long[] source = new long[ForUtil.BLOCK_SIZE];
        long or = 0;
        for (int j = 0; j < ForUtil.BLOCK_SIZE; ++j) {
          source[j] = values[i * ForUtil.BLOCK_SIZE + j];
          or |= source[j];
        }
        final int bpv = PackedInts.bitsRequired(or);
        out.writeByte((byte) bpv);
        forUtil.encode(source, bpv, out);
      }
      endPointer = out.getFilePointer();
      out.close();
    }

    {
      // decode
      IndexInput in = d.openInput("test.bin", IOContext.READONCE);
      final ForUtil forUtil = new ForUtil();
      for (int i = 0; i < iterations; ++i) {
        final int bitsPerValue = in.readByte();
        final long currentFilePointer = in.getFilePointer();
        final long[] restored = new long[ForUtil.BLOCK_SIZE];
        forUtil.decode(bitsPerValue, in, restored);
        assertArrayEquals(
                Arrays.toString(restored),
                ArrayUtil.copyOfSubArray(values, i * ForUtil.BLOCK_SIZE, (i + 1) * ForUtil.BLOCK_SIZE),
                restored);
        assertEquals(forUtil.numBytes(bitsPerValue), in.getFilePointer() - currentFilePointer);
      }
      assertEquals(endPointer, in.getFilePointer());
      in.close();
    }

    d.close();
  }
}
