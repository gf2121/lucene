package org.apache.lucene.benchmark.jmh;

import org.apache.lucene.codecs.lucene90.IndexedDISI;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1)
public class IndexedDISIBenchmark {

  private static final int DOC_COUNT_POW = 20;

  @Param({"2", "4", "8", "16", "32", "64", "128", "256", "512", "1024"})
  private int interval;

  private IndexInput input;
  private IndexedDISI disi;

  private short jumpTableEntryCont;

  @Setup(Level.Trial)
  public void init() throws Exception {
    Directory dir = FSDirectory.open(Files.createTempDirectory("IndexedDISIBenchmark"));
    try (IndexOutput out = dir.createOutput("disi", IOContext.DEFAULT)) {
      jumpTableEntryCont = IndexedDISI.writeBitSet(
          new IntervalIterator(1 << DOC_COUNT_POW, interval),
          out,
          IndexedDISI.DEFAULT_DENSE_RANK_POWER);
    }
    input = dir.openInput("disi", IOContext.DEFAULT);
  }

  @Setup(Level.Invocation)
  public void reset() throws Exception {
    input.seek(0);
    disi = new IndexedDISI(input, 0, input.length(), jumpTableEntryCont, IndexedDISI.DEFAULT_DENSE_RANK_POWER, 0);
  }

  @Benchmark
  public void nextDoc(Blackhole bh) throws IOException {
    int res = 0, doc;
    while ((doc = disi.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
      res ^= doc;
    }
    bh.consume(res);
  }

  @Benchmark
  public void advanceDocPlusOne(Blackhole bh) throws IOException {
    int res = 0, doc;
    while ((doc = disi.advance(disi.docID() + 1)) != DocIdSetIterator.NO_MORE_DOCS) {
      res ^= doc;
    }
    bh.consume(res);

  }

  private static class IntervalIterator extends DocIdSetIterator {

    private final int interval;
    private final int docCount;
    private int doc = -1;
    private int index = -1;

    IntervalIterator(int docCount, int interval) {
      this.docCount = docCount;
      this.interval = interval;
    }

    @Override
    public int docID() {
      return doc;
    }

    @Override
    public int nextDoc() throws IOException {
      if (++index >= docCount) {
        return doc = DocIdSetIterator.NO_MORE_DOCS;
      }
      doc += interval;
      return doc;
    }

    @Override
    public int advance(int target) throws IOException {
      return slowAdvance(target);
    }

    @Override
    public long cost() {
      return docCount;
    }
  }
}
