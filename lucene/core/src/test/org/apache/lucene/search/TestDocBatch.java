package org.apache.lucene.search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.lucene.util.FixedBitSet;

public class TestDocBatch extends LuceneTestCase {

  public void testWiki() throws Exception {
    Directory directory = new NIOFSDirectory(Paths.get("/Users/bytedance/Documents/projects/lucenebench/indices/wikimediumall.lucene_baseline.Lucene101.dvfields.nd33.3326M/index"));
    IndexReader reader = DirectoryReader.open(directory);
    IndexSearcher searcher = new IndexSearcher(reader);
    BooleanQuery.Builder builder = new BooleanQuery.Builder();
    builder.add(new TermQuery(new Term("body", "but")), BooleanClause.Occur.FILTER);
    builder.add(new TermQuery(new Term("body", "year")), BooleanClause.Occur.FILTER);
    System.out.println(searcher.count(builder.build()));
  }

  public void testArrayAndArray() throws Exception {
    testAnd(DataProvider.ARRAY, DataProvider.ARRAY);
  }

  public void testArrayAndBitset() throws Exception {
    testAnd(DataProvider.ARRAY, DataProvider.BITSET);
  }

  public void testArrayAndRange() throws Exception {
    testAnd(DataProvider.ARRAY, DataProvider.RANGE);
  }

  public void testBitsetAndArray() throws Exception {
    testAnd(DataProvider.BITSET, DataProvider.ARRAY);
  }

  public void testBitsetAndBitset() throws Exception {
    testAnd(DataProvider.BITSET, DataProvider.BITSET);
  }

  public void testBitsetAndRange() throws Exception {
    testAnd(DataProvider.BITSET, DataProvider.RANGE);
  }

  public void testRangeAndArray() throws Exception {
    testAnd(DataProvider.RANGE, DataProvider.ARRAY);
  }

  public void testRangeAndBitset() throws Exception {
    testAnd(DataProvider.RANGE, DataProvider.BITSET);
  }

  public void testRangeAndRange() throws Exception {
    testAnd(DataProvider.RANGE, DataProvider.RANGE);
  }

  private void testAnd(DataProvider provider1, DataProvider provider2) throws IOException {
    final int round = atLeast(100);
    for (int i = 0; i < round; i++) {
      int maxBits = 1 + random().nextInt(15);
      int minBits = random().nextInt(maxBits);
      DocIdSet set1 = provider1.set(minBits, maxBits);
      DocIdSet set2 = provider2.set(minBits, maxBits);
      DocBatch batch1 = provider1.batch(set1.iterator(), minBits, maxBits);
      DocBatch batch2 = provider2.batch(set2.iterator(), minBits, maxBits);

      batch1.and(batch2);
      assertDocsEqual(
          ConjunctionUtils.intersectIterators(List.of(set1.iterator(), set2.iterator())), batch1);
    }
  }

  private void testAppend(DataProvider provider1, DataProvider provider2) throws IOException {
    final int round = atLeast(100);
    for (int i = 0; i < round; i++) {
      int maxBits = 1 + random().nextInt(15);
      int minBits = random().nextInt(maxBits - 1);
      int initialDocsBits = minBits + random().nextInt(maxBits - minBits - 1);

      DocIdSet set = provider1.set(minBits, initialDocsBits);
      DocBatch batch = provider1.batch(set.iterator(), minBits, maxBits);

      int bits = initialDocsBits;
      while (true) {
        int bitsUpto = random().nextInt(maxBits + 1 - bits);
        DocIdSetIterator iterator = provider2.append(batch, bits, bitsUpto);
      }


    }
  }

  enum DataProvider {
    ARRAY {
      @Override
      DocIdSet set(int minBits, int maxBits) throws IOException {
        assert maxBits < 31;
        Set<Integer> set = new HashSet<>();
        int maxSize = 1 << (maxBits + 1);
        int minSize = 1 << minBits;
        int round = Math.min(atLeast(1000), maxSize - minSize);
        for (int i = 0; i < round; i++) {
          set.add(minSize + random().nextInt(maxSize - minSize));
        }
        set.add(DocIdSetIterator.NO_MORE_DOCS);
        int[] docs = set.stream().mapToInt(i -> i).sorted().toArray();
        return new IntArrayDocIdSet(docs, docs.length - 1);
      }

      @Override
      DocBatch batch(DocIdSetIterator iterator, int minBits, int maxBits) throws IOException {
        List<Integer> docs = new ArrayList<>();
        for (int doc = iterator.nextDoc();
            doc != DocIdSetIterator.NO_MORE_DOCS;
            doc = iterator.nextDoc()) {
          docs.add(doc);
        }
        int maxSize = 1 << (maxBits + 1);
        int minSize = 1 << minBits;
        DocBatch batch = new DocBatch(minSize, maxSize - minSize);
        int[] docsArray = docs.stream().mapToInt(i -> i).toArray();
        batch.asArray(docsArray, docsArray.length);
        return batch;
      }
    },
    BITSET {
      @Override
      DocIdSet set(int minBits, int maxBits) throws IOException {
        return ARRAY.set(minBits, maxBits);
      }

      @Override
      DocBatch batch(DocIdSetIterator iterator, int minBits, int maxBits) throws IOException {
        assert maxBits < 31;

        int maxSize = 1 << (maxBits + 1);
        int minSize = 1 << minBits;
        FixedBitSet bitSet = new FixedBitSet(maxSize - minSize);

        for (int doc = iterator.nextDoc();
            doc != DocIdSetIterator.NO_MORE_DOCS;
            doc = iterator.nextDoc()) {
          bitSet.set(doc - minSize);
        }

        DocBatch batch = new DocBatch(minSize, maxSize - minSize);
        batch.asBitset(bitSet);
        return batch;
      }
    },
    RANGE {
      @Override
      DocIdSet set(int minBits, int maxBits) throws IOException {
        assert maxBits < 31;
        int maxSize = 1 << (maxBits + 1);
        int minSize = 1 << minBits;
        int start = minSize + random().nextInt(maxSize - minSize);
        int end = start + random().nextInt(maxSize - start);
        return new DocIdSet() {
          @Override
          public DocIdSetIterator iterator() throws IOException {
            return end > start ? DocIdSetIterator.range(start, end) : DocIdSetIterator.empty();
          }

          @Override
          public long ramBytesUsed() {
            return 0;
          }
        };
      }

      @Override
      DocBatch batch(DocIdSetIterator iterator, int minBits, int maxBits) throws IOException {
        assert maxBits < 31;
        int maxSize = 1 << (maxBits + 1);
        int minSize = 1 << minBits;

        int minDoc = iterator.nextDoc();
        int maxDoc = minDoc + 1;
        if (minDoc != DocIdSetIterator.NO_MORE_DOCS) {
          for (int doc = iterator.nextDoc();
              doc != DocIdSetIterator.NO_MORE_DOCS;
              doc = iterator.nextDoc()) {
            assertEquals(maxDoc, doc);
            maxDoc = doc + 1; //exclusive bound
          }
        } else {
          minDoc = maxDoc = minSize;
        }

        DocBatch batch = new DocBatch(minSize, maxSize - minSize);
        batch.asRange(minDoc, maxDoc);
        return batch;
      }
    };

    abstract DocIdSet set(int minBits, int maxBits) throws IOException;

    abstract DocBatch batch(DocIdSetIterator iterator, int minBits, int maxBits) throws IOException;

    DocIdSetIterator append(DocBatch docBatch, int minBits, int maxBits) {return null;}
  }

  private void assertDocsEqual(DocIdSetIterator docIdSetIterator, DocBatch batch)
      throws IOException {
    batch.forEach(doc -> assertEquals(docIdSetIterator.nextDoc(), doc));
  }
}
