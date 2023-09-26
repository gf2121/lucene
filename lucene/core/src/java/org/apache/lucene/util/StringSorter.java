package org.apache.lucene.util;

import java.util.Comparator;

abstract class StringSorter extends Sorter {

  private final Comparator<BytesRef> cmp;
  protected final BytesRefBuilder scratch1 = new BytesRefBuilder();
  protected final BytesRefBuilder scratch2 = new BytesRefBuilder();
  protected final BytesRefBuilder pivotBuilder = new BytesRefBuilder();
  protected final BytesRef scratchBytes1 = new BytesRef();
  protected final BytesRef scratchBytes2 = new BytesRef();
  protected final BytesRef pivot = new BytesRef();

  StringSorter(Comparator<BytesRef> cmp) {
    this.cmp = cmp;
  }

  protected abstract void get(BytesRefBuilder builder, BytesRef result, int i);

  @Override
  protected int compare(int i, int j) {
    get(scratch1, scratchBytes1, i);
    get(scratch2, scratchBytes2, j);
    return cmp.compare(scratchBytes1, scratchBytes2);
  }

  @Override
  public void sort(int from, int to) {
    if (cmp instanceof BytesRefComparator bCmp) {
      radixSorter(bCmp).sort(from, to);
    } else {
      fallbackSorter(cmp).sort(from, to);
    }
  }

  protected Sorter radixSorter(BytesRefComparator cmp) {
    return new MSBRadixSorter(cmp.comparedBytesCount) {
      @Override
      protected void swap(int i, int j) {
        StringSorter.this.swap(i, j);
      }

      @Override
      protected int byteAt(int i, int k) {
        get(scratch1, scratchBytes1, i);
        return cmp.byteAt(scratchBytes1, k);
      }
    };
  }

  protected Sorter fallbackSorter(Comparator<BytesRef> cmp) {
    return new IntroSorter() {
      @Override
      protected void swap(int i, int j) {
        StringSorter.this.swap(i, j);
      }

      @Override
      protected int compare(int i, int j) {
        return StringSorter.this.compare(i, j);
      }

      @Override
      protected void setPivot(int i) {
        get(pivotBuilder, pivot, i);
      }

      @Override
      protected int comparePivot(int j) {
        get(scratch1, scratchBytes1, j);
        return cmp.compare(pivot, scratchBytes1);
      }
    };
  }
}
