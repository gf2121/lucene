package org.apache.lucene.search;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.util.Bits;

public abstract class DocIdSetWeight extends ConstantScoreWeight {

  protected DocIdSetWeight(Query query, float score) {
    super(query, score);
  }

  @Override
  public BulkScorer bulkScorer(LeafReaderContext context) throws IOException {
    DocIdSetScorerSupplier scorerSupplier = docIdSetScorerSupplier(context);
    if (scorerSupplier == null) {
      // No docs match
      return null;
    }

    scorerSupplier.setTopLevelScoringClause();
    return new DocIdSetBulkScorer(scorerSupplier.get(Long.MAX_VALUE), scorerSupplier.getDocIdSet());
  }

  @Override
  public final ScorerSupplier scorerSupplier(LeafReaderContext context) throws IOException {
    return docIdSetScorerSupplier(context);
  }

  protected abstract DocIdSetScorerSupplier docIdSetScorerSupplier(LeafReaderContext context)
      throws IOException;

  protected abstract static class DocIdSetScorerSupplier extends ScorerSupplier {

    private DocIdSet docIdSet;

    protected abstract DocIdSet computeDocIdSet() throws IOException;

    final DocIdSet getDocIdSet() {
      if (docIdSet == null) {
        try {
          this.docIdSet = computeDocIdSet();
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }
      return docIdSet;
    }
  }

  protected static class DocIdSetBulkScorer extends DefaultBulkScorer {

    private final DocIdSet docIdSet;

    public DocIdSetBulkScorer(Scorer scorer, DocIdSet docIdSet) {
      super(scorer);
      this.docIdSet = docIdSet;
    }

    @Override
    public int score(LeafCollector collector, Bits acceptDocs, int min, int max)
        throws IOException {

      if (twoPhase != null || acceptDocs != null) {
        return super.score(collector, acceptDocs, min, max);
      }

      DocIdSetIterator competitiveIterator = collector.competitiveIterator();
      if (competitiveIterator != null) {
        return super.score(collector, acceptDocs, min, max);
      }

      collector.setScorer(scorer);
      if (iterator.docID() == -1 && min == 0 && max == DocIdSetIterator.NO_MORE_DOCS) {
        collector.collect(allStream(iterator, docIdSet));
        return DocIdSetIterator.NO_MORE_DOCS;
      } else {
        int[] doc = new int[1];
        collector.collect(rangeStream(doc, iterator, min, max, docIdSet));
        return doc[0];
      }
    }

    static DocIdStream rangeStream(
        int[] doc, DocIdSetIterator iterator, int min, int max, DocIdSet docIdSet) {
      return new DocIdStream() {
        @Override
        public void forEach(CheckedIntConsumer<IOException> consumer) throws IOException {
          doc[0] =
              scoreRange(
                  new LeafCollector() {
                    @Override
                    public void setScorer(Scorable scorer) {
                      assert false : "scoreRange should not set scorer";
                    }

                    @Override
                    public void collect(int doc) throws IOException {
                      consumer.accept(doc);
                    }
                  },
                  iterator,
                  null,
                  null,
                  null,
                  min,
                  max);
        }

        @Override
        public int count() throws IOException {
          doc[0] = iterator.docID() >= max ? iterator.docID() : iterator.advance(max);
          return docIdSet.count(min, max);
        }
      };
    }

    static DocIdStream allStream(DocIdSetIterator iterator, DocIdSet docIdSet) {
      return new DocIdStream() {
        @Override
        public void forEach(CheckedIntConsumer<IOException> consumer) throws IOException {
          scoreAll(
              new LeafCollector() {

                @Override
                public void setScorer(Scorable scorer) {
                  assert false : "scoreAll should not set scorer";
                }

                @Override
                public void collect(int doc) throws IOException {
                  consumer.accept(doc);
                }
              },
              iterator,
              null,
              null);
        }

        @Override
        public int count() throws IOException {
          return docIdSet.countAll();
        }
      };
    }
  }
}
