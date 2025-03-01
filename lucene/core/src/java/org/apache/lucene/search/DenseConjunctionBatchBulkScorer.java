package org.apache.lucene.search;

import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DenseConjunctionBatchBulkScorer extends BulkScorer {

  // Use a small-ish window size to make sure that we can take advantage of gaps in the postings of
  // clauses that are not leading iteration.
  static final int WINDOW_SIZE = 4096;
  // Only use bit sets to compute the intersection if more than 1/32th of the docs are expected to
  // match. Experiments suggested that values that are a bit higher than this would work better, but
  // we're erring on the conservative side.
  static final int DENSITY_THRESHOLD_INVERSE = Long.SIZE / 2;

  private final DocIdSetIterator lead;
  private final List<DocIdSetIterator> others;

  private final DocBatch windowMatches = new DocBatch(WINDOW_SIZE);
  private final DocBatch clauseWindowMatches = new DocBatch(WINDOW_SIZE);

  DenseConjunctionBatchBulkScorer(List<DocIdSetIterator> iterators) {
    if (iterators.size() <= 1) {
      throw new IllegalArgumentException("Expected 2 or more clauses, got " + iterators.size());
    }
    iterators = new ArrayList<>(iterators);
    iterators.sort(Comparator.comparingLong(DocIdSetIterator::cost));
    lead = iterators.get(0);
    others = List.copyOf(iterators.subList(1, iterators.size()));
  }

  @Override
  public int score(LeafCollector collector, Bits acceptDocs, int min, int max) throws IOException {
    for (DocIdSetIterator it : others) {
      min = Math.max(min, it.docID());
    }

    if (lead.docID() < min) {
      lead.advance(min);
    }

    if (lead.docID() >= max) {
      return lead.docID();
    }

    // This scorer is only used for conjunctions of FILTER clauses, so we set a simple scorer that
    // always returns a score of zero.
    collector.setScorer(new SimpleScorable());
    List<DocIdSetIterator> otherIterators = this.others;
    DocIdSetIterator collectorIterator = collector.competitiveIterator();
    if (collectorIterator != null) {
      otherIterators = new ArrayList<>(otherIterators);
      otherIterators.add(collectorIterator);
    }

    final DocIdSetIterator[] others = otherIterators.toArray(DocIdSetIterator[]::new);

    int windowMax;
    do {
      windowMax = (int) Math.min(max, (long) lead.docID() + WINDOW_SIZE);
      scoreWindowUsingBitSet(collector, acceptDocs, others, windowMax);
    } while (windowMax < max);

    return lead.docID();
  }

  private static int advance(FixedBitSet set, int i) {
    if (i >= WINDOW_SIZE) {
      return DocIdSetIterator.NO_MORE_DOCS;
    } else {
      return set.nextSetBit(i);
    }
  }

  private void scoreWindowUsingBitSet(
      LeafCollector collector, Bits acceptDocs, DocIdSetIterator[] others, int max)
      throws IOException {

    if (lead.docID() >= max) {
      return;
    }

    int offset = lead.docID();
    windowMatches.setBase(offset);
    clauseWindowMatches.setBase(offset);

    lead.intoDocBatch(windowMatches);
    if (acceptDocs != null) {
      // TODO
      throw new UnsupportedOperationException();
    }

    for (DocIdSetIterator other : others) {
      if (other.docID() < offset) {
        other.advance(offset);
      }
      other.intoDocBatch(clauseWindowMatches);
      windowMatches.and(clauseWindowMatches);
      clauseWindowMatches.clear();
    }

    collector.collect(windowMatches);

    // If another clause is more advanced than lead1 then advance lead1, it's important to take
    // advantage of large gaps in the postings lists of other clauses.
    int maxOtherDocID = -1;
    for (DocIdSetIterator other : others) {
      maxOtherDocID = Math.max(maxOtherDocID, other.docID());
    }
    if (lead.docID() < maxOtherDocID) {
      lead.advance(maxOtherDocID);
    }
  }

  @Override
  public long cost() {
    return lead.cost();
  }

  final class DocIdStreamView extends DocIdStream {

    int offset;

    @Override
    public void forEach(CheckedIntConsumer<IOException> consumer) throws IOException {
      windowMatches.forEach(consumer);
    }

    @Override
    public int count() throws IOException {
      return windowMatches.cardinality();
    }
  }
}
