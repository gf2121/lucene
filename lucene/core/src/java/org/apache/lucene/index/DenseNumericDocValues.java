package org.apache.lucene.index;

import java.io.IOException;

public abstract class DenseNumericDocValues extends NumericDocValues {

  public final int maxDoc;
  protected int doc = -1;

  public DenseNumericDocValues(int maxDoc) {
    this.maxDoc = maxDoc;
  }

  @Override
  public int docID() {
    return doc;
  }

  @Override
  public int nextDoc() throws IOException {
    return advance(doc + 1);
  }

  @Override
  public int advance(int target) throws IOException {
    if (target >= maxDoc) {
      return doc = NO_MORE_DOCS;
    }
    return doc = target;
  }

  @Override
  public boolean advanceExact(int target) {
    doc = target;
    return true;
  }

  @Override
  public long cost() {
    return maxDoc;
  }

  public final void doc(int doc) {
    this.doc = doc;
  }
}
