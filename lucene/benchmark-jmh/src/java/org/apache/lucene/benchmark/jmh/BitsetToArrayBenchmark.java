package org.apache.lucene.benchmark.jmh;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.SplittableRandom;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.IntsRef;
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

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 1,
    jvmArgsAppend = {
        "-Xmx1g",
        "-Xms1g",
        "-XX:+AlwaysPreTouch",
        "--add-modules",
        "jdk.incubator.vector"
    })
public class BitsetToArrayBenchmark {

  private final SplittableRandom R = new SplittableRandom(4314123142L);

  @Param({"128", "256", "512", "768"})
  int bitSetSize;

  private static final int FIXED_SET_BITS = 128;

  private FixedBitSet bitSet;
  private int[] resultArray;
  private int base;

  @Setup(Level.Trial)
  public void setup() {
    base = R.nextInt(1000);
    bitSet = new FixedBitSet(bitSetSize);
    resultArray = new int[FIXED_SET_BITS + Long.SIZE];
    setBits();
  }

  @Setup(Level.Invocation)
  public void setupInvocation() {
    if (bitSetSize != 128) {
      bitSet.clear();
      setBits();
    }
  }

  private void setBits() {
    for (int i = 0; i < FIXED_SET_BITS; i++) {
      int doc;
      do {
        doc = R.nextInt(bitSetSize);
      } while (bitSet.get(doc));
      bitSet.set(doc);
    }
  }

  @Benchmark
  public int baseline() {
    final int[] docs = resultArray;
    int index = 0;
    long[] bits = bitSet.getBits();
    int numLongs = bits.length;

    for (int i = 0; i < numLongs; i++) {
      long word = bits[i];
      int base = this.base + (i << 6);
      while (word != 0L) {
        int ntz = Long.numberOfTrailingZeros(word);
        docs[index++] = base + ntz;
        word ^= 1L << ntz;
      }
    }

    return index;
  }

  @Benchmark
  @Fork(jvmArgsPrepend = {"-XX:UseAVX=3"})
  public int vectorized512() {
    final int[] docs = resultArray;

    long[] bits = bitSet.getBits();
    int numLongs = bits.length;
    int size = 0;

    for (int i = 0; i < numLongs; i++) {
      long word = bits[i];
      if (word != 0) {
        int bitCount = Long.bitCount(word);
        word2Array_512(word, base + (i << 6), docs, size, bitCount);
        size += bitCount;
      }
    }

    return size;
  }

  @SuppressWarnings("fallthrough")
  private static void word2Array_512(long word, int base, int[] docs, int offset, int bitCount) {
    VectorMask<Byte> mask = VectorMask.fromLong(ByteVector.SPECIES_512, word);
    ByteVector indices = ByteVector.fromArray(ByteVector.SPECIES_512, IDENTITY_BYTES, 0)
        .compress(mask);

    switch ((bitCount - 1) >>> 4) {
      case 3:
        indices.convert(VectorOperators.B2I, 3).reinterpretAsInts().add(base).intoArray(docs, offset + 48);
      case 2:
        indices.convert(VectorOperators.B2I, 2).reinterpretAsInts().add(base).intoArray(docs, offset + 32);
      case 1:
        indices.convert(VectorOperators.B2I, 1).reinterpretAsInts().add(base).intoArray(docs, offset + 16);
      case 0:
        indices.convert(VectorOperators.B2I, 0).reinterpretAsInts().add(base).intoArray(docs, offset);
        break;
      default:
        throw new IllegalStateException(bitCount + "");
    }
  }

  @Benchmark
  @Fork(jvmArgsPrepend = {"-XX:UseAVX=2"})
  public int vectorized256() {
    final int[] docs = resultArray;

    long[] bits = bitSet.getBits();
    int numLongs = bits.length;
    int size = 0;

    for (int i = 0; i < numLongs; i++) {
      long word = bits[i];
      int base = this.base + (i << 6);

      long lWord = word & 0xFFFFFFFFL;
      if (lWord != 0) {
        int bitCount = Long.bitCount(lWord);
        word2Array_256(lWord, base, docs, size, bitCount);
        size += bitCount;
      }

      long hWord = word >>> 32;
      if (hWord != 0) {
        int bitCount = Long.bitCount(hWord);
        word2Array_256(hWord, base + 32, docs, size, bitCount);
        size += bitCount;
      }
    }

    return size;
  }

  @SuppressWarnings("fallthrough")
  private static void word2Array_256(long word, int base, int[] docs, int offset, int bitCount) {
    VectorMask<Byte> mask = VectorMask.fromLong(ByteVector.SPECIES_256, word);
    ByteVector indices = ByteVector.fromArray(ByteVector.SPECIES_256, IDENTITY_BYTES, 0).compress(mask);

    switch ((bitCount - 1) >>> 3) {
      case 3:
        indices.convert(VectorOperators.B2I, 3).reinterpretAsInts().add(base).intoArray(docs, offset + 24);
      case 2:
        indices.convert(VectorOperators.B2I, 2).reinterpretAsInts().add(base).intoArray(docs, offset + 16);
      case 1:
        indices.convert(VectorOperators.B2I, 1).reinterpretAsInts().add(base).intoArray(docs, offset + 8);
      case 0:
        indices.convert(VectorOperators.B2I, 0).reinterpretAsInts().add(base).intoArray(docs, offset);
        break;
      default:
        throw new IllegalStateException(bitCount + "");
    }
  }

  private static final byte[] IDENTITY_BYTES = new byte[64];

  static {
    for (int i = 0; i < IDENTITY_BYTES.length; i++) {
      IDENTITY_BYTES[i] = (byte) i;
    }
  }

  public int denseBitsetToArray(FixedBitSet bitSet, int from, int to, int base, int[] array) {
    Objects.checkFromToIndex(from, to, bitSet.length());

    int offset = 0;
    long[] bits = bitSet.getBits();
    // First, align `from` with a word start, ie. a multiple of Long.SIZE (64)
    if ((from & 0x3F) != 0) {
      long word = bits[from >> 6] >>> from;
      int numBitsTilNextWord = -from & 0x3F;
      if (to - from < numBitsTilNextWord) {
        // All bits are in a single word
        word &= (1L << (to - from)) - 1L;
        return word2Array(word, from + base, array, offset);
      }
      offset = word2Array(word, from + base, array, offset);
      from += numBitsTilNextWord;
      assert (from & 0x3F) == 0;
    }

    for (int i = from >> 6, end = to >> 6; i < end; ++i) {
      long word = bits[i];
      offset = word2Array(word, base + (i << 6), array, offset);
    }

    // Now handle remaining bits in the last partial word
    if ((to & 0x3F) != 0) {
      long word = bits[to >> 6] & ((1L << to) - 1);
      offset = word2Array(word, base + (to & ~0x3F), array, offset);
    }

    return offset;
  }

  private static final int PREFERRED_VECTOR_BITSIZE = 256;

  private static int word2Array(long word, int base, int[] docs, int offset) {
    if (PREFERRED_VECTOR_BITSIZE == 512) {
      return word2Array(word, base, docs, offset, ByteVector.SPECIES_512);
    } else if (PREFERRED_VECTOR_BITSIZE == 256) {
      int start = offset;
      offset = word2Array(word & 0xFFFFFFFFL, base, docs, offset, ByteVector.SPECIES_256);
      return word2Array(word >>> 32, base + offset - start, docs, offset, ByteVector.SPECIES_256);
    } else {
      throw new IllegalStateException("Unsupported vector size: " + PREFERRED_VECTOR_BITSIZE);
    }
  }

  @SuppressWarnings("fallthrough")
  private static int word2Array(long word, int base, int[] docs, int offset, VectorSpecies<Byte> species) {
    if (word == 0L) {
      return offset;
    }

    int bitCount = Long.bitCount(word);

    VectorMask<Byte> mask = VectorMask.fromLong(species, word);
    ByteVector indices = ByteVector.fromArray(species, IDENTITY_BYTES, 0)
        .compress(mask);

    switch ((bitCount - 1) / species.length()) {
      case 3:
        indices.convert(VectorOperators.B2I, 3).reinterpretAsInts().add(base).intoArray(docs, offset + species.length() * 3);
      case 2:
        indices.convert(VectorOperators.B2I, 2).reinterpretAsInts().add(base).intoArray(docs, offset + species.length() * 2);
      case 1:
        indices.convert(VectorOperators.B2I, 1).reinterpretAsInts().add(base).intoArray(docs, offset + species.length());
      case 0:
        indices.convert(VectorOperators.B2I, 0).reinterpretAsInts().add(base).intoArray(docs, offset);
        break;
      default:
        throw new IllegalStateException(bitCount + "");
    }

    return offset + bitCount;
  }

  public static void main(String[] args) {
    for (int bitSetSize : new int[]{128, 256, 512, 1024}) {
      BitsetToArrayBenchmark baseline = new BitsetToArrayBenchmark();
      baseline.bitSetSize = bitSetSize;
      baseline.setup();
      int size = baseline.baseline();
      if (size != FIXED_SET_BITS) {
        throw new IllegalArgumentException("incorrect size: " + size);
      }

      {
        BitsetToArrayBenchmark candidate = new BitsetToArrayBenchmark();
        candidate.bitSetSize = bitSetSize;
        candidate.setup();
        size = candidate.vectorized512();
        if (size != FIXED_SET_BITS) {
          throw new IllegalArgumentException("incorrect size: " + size);
        }

        if (Arrays.equals(baseline.resultArray, 0, FIXED_SET_BITS, candidate.resultArray, 0, FIXED_SET_BITS)
            == false) {
          throw new IllegalArgumentException(
              "incorrect docs,"
                  + "\nbaseline: "
                  + Arrays.toString(ArrayUtil.copyOfSubArray(baseline.resultArray, 0, FIXED_SET_BITS))
                  + "\ncandidate: "
                  + Arrays.toString(ArrayUtil.copyOfSubArray(candidate.resultArray, 0, FIXED_SET_BITS)));
        }
      }

      {
        BitsetToArrayBenchmark candidate = new BitsetToArrayBenchmark();
        candidate.bitSetSize = bitSetSize;
        candidate.setup();
        size = candidate.vectorized256();
        if (size != FIXED_SET_BITS) {
          throw new IllegalArgumentException("incorrect size: " + size);
        }

        if (Arrays.equals(baseline.resultArray, 0, FIXED_SET_BITS, candidate.resultArray, 0, FIXED_SET_BITS)
            == false) {
          throw new IllegalArgumentException(
              "incorrect docs,"
                  + "\nbaseline: "
                  + Arrays.toString(ArrayUtil.copyOfSubArray(baseline.resultArray, 0, FIXED_SET_BITS))
                  + "\ncandidate: "
                  + Arrays.toString(ArrayUtil.copyOfSubArray(candidate.resultArray, 0, FIXED_SET_BITS)));
        }
      }

    }
  }
}
