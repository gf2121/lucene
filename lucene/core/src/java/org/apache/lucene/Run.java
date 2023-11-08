package org.apache.lucene;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;

public class Run {

  public static void main(String[] args) throws Exception {
    BytesRefHash.STABLE_SORT = false;
    doTest();
    BytesRefHash.STABLE_SORT = true;
    doTest();
  }

  private static void doTest() throws Exception {
    Path path = Paths.get("./test_index");
    Directory directory = FSDirectory.open(path);
    IndexWriterConfig config = new IndexWriterConfig();
    config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    config.setRAMBufferSizeMB(300);
    IndexWriter writer = new IndexWriter(directory, config);
    for (int i = 0; i < 60000000; i++) {
      Document document = new Document();
      byte[] bytes = new byte[16];
      ThreadLocalRandom.current().nextBytes(bytes);
      BytesRef bytesRef = new BytesRef(bytes);
      document.add(new StringField("id", bytesRef, Field.Store.NO));
      writer.addDocument(document);
    }
    writer.flush();
    writer.commit();
    writer.close();
  }
}
