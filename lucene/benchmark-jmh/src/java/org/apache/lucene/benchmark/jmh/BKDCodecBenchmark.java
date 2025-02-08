package org.apache.lucene.benchmark.jmh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.bkd.BKDWriter;
import org.apache.lucene.util.bkd.DocIdsWriter;
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
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
@Fork(value = 1)
public class BKDCodecBenchmark {

  private static final int SIZE = 512;

  @Param({"16", "24"})
  public int bpv;

  private Directory dir;
  private DocIdsWriter legacy;
  private IndexInput legacyIn;
  private DocIdsWriter current;
  private IndexInput currentIn;
  private int[] docs;

  @Setup(Level.Trial)
  public void setupTrial() throws IOException {
    Path path = Files.createTempDirectory("bkd");
    dir = MMapDirectory.open(path);
    docs = new int[SIZE];
    legacy = new DocIdsWriter(SIZE, BKDWriter.VERSION_META_FILE);
    legacyIn = writeDocIds("legacy", docs, legacy);
    current = new DocIdsWriter(SIZE, BKDWriter.VERSION_CURRENT);
    currentIn = writeDocIds("current", docs, current);
  }

  private IndexInput writeDocIds(String file, int[] docs, DocIdsWriter writer) throws IOException {
    try (IndexOutput out = dir.createOutput(file, IOContext.DEFAULT)) {
      Random r = new Random(0);
      // avoid cluster encoding
      docs[0] = 1;
      docs[1] = (1 << bpv) - 1;
      for (int i = 2; i < SIZE; ++i) {
        docs[i] = r.nextInt(1 << bpv);
      }
      writer.writeDocIds(docs, 0, SIZE, out);
    }
    return dir.openInput(file, IOContext.DEFAULT);
  }

  @Setup(Level.Invocation)
  public void setupInvocation() throws IOException {
    legacyIn.seek(0);
    currentIn.seek(0);
  }

  @TearDown(Level.Trial)
  public void tearDownTrial() throws IOException {
    IOUtils.close(legacyIn, currentIn, dir);
  }

  private static int count(int iter) {
    // to make benchmark more realistic
    return iter % 20 == 0 ? 511 : SIZE;
  }

  @Benchmark
  public void legacy(Blackhole bh) throws IOException {
    for (int i = 0; i <= 100; i++) {
      int count = count(i);
      legacy.readInts(legacyIn, count, docs);
      bh.consume(docs);
      setupInvocation();
    }
  }

  @Benchmark
  public void current(Blackhole bh) throws IOException {
    for (int i = 0; i <= 100; i++) {
      int count = count(i);
      current.readInts(currentIn, count, docs);
      bh.consume(docs);
      setupInvocation();
    }
  }

  @Benchmark
  @Fork(jvmArgsPrepend = {"--add-modules=jdk.incubator.vector"})
  public void currentVector(Blackhole bh) throws IOException {
    for (int i = 0; i <= 100; i++) {
      int count = count(i);
      current.readInts(currentIn, count, docs);
      bh.consume(docs);
      setupInvocation();
    }
  }
}
