package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class CloningBenchmark {

    private int[] data;

    @Param({"128","4096"})
    public int len;

    @Setup(Level.Trial)
    public void setup() {
        data = new int[len];
        for (int i = 0; i < len; i++) data[i] = i;
    }

    @Benchmark
    public int[] array_clone() { return data.clone(); }

    @Benchmark
    public int[] array_copyOf() { return Arrays.copyOf(data, data.length); }

    @Benchmark
    public int[] array_manual_copy() {
        int[] out = new int[data.length];
        System.arraycopy(data, 0, out, 0, data.length);
        return out;
    }
}
