package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class BitArrayBenchmark {

    private BitSet bits1, bits2;
    private byte[] byteData;

    @Setup(Level.Trial)
    public void setup() {
        Random r = new Random(42);
        byte[] bytes = new byte[128];
        r.nextBytes(bytes);
        bits1 = BitSet.valueOf(bytes);
        r.nextBytes(bytes);
        bits2 = BitSet.valueOf(bytes);

        byteData = new byte[512];
        for (int i = 0; i < byteData.length; i++) byteData[i] = (byte) i;
    }

    // Blog-aligned names
    @Benchmark
    public long HammingDistanceManual() {
        long distance = 0;
        int len = Math.max(bits1.length(), bits2.length());
        for (int i = 0; i < len; i++) {
            if (bits1.get(i) != bits2.get(i)) distance++;
        }
        return distance;
    }

    @Benchmark
    public long HammingDistanceTensorPrimitives() {
        // Java analog using XOR over machine words and bitCount
        long[] a = bits1.toLongArray();
        long[] b = bits2.toLongArray();
        long distance = 0L;
        int min = Math.min(a.length, b.length);
        for (int i = 0; i < min; i++) {
            distance += Long.bitCount(a[i] ^ b[i]);
        }
        // Count remaining bits in the longer array
        if (a.length > b.length) {
            for (int i = min; i < a.length; i++) distance += Long.bitCount(a[i]);
        } else if (b.length > a.length) {
            for (int i = min; i < b.length; i++) distance += Long.bitCount(b[i]);
        }
        return distance;
    }

    @Benchmark
    public BitSet ByteCtor() {
        return BitSet.valueOf(byteData);
    }
}
