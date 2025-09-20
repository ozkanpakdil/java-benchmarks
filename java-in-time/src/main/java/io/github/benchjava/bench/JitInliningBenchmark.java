package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class JitInliningBenchmark {

    private int x = 42;

    @Benchmark
    public int direct_call() {
        return add1(x);
    }

    @Benchmark
    public int virtual_call() {
        return adder.add(x);
    }

    @Benchmark
    public int constant_folding_math() {
        // The JIT should fold these constants aggressively
        return (int) (10 * 20 + 30 - 5 + 2 * 8);
    }

    @Benchmark
    public int bounds_checked_loop() {
        int[] a = arr;
        int s = 0;
        for (int i = 0; i < a.length; i++) {
            s += a[i];
        }
        return s;
    }

    // Helper state
    private final int[] arr = init();
    private static int[] init() {
        int[] a = new int[256];
        for (int i = 0; i < a.length; i++) a[i] = i;
        return a;
    }

    private int add1(int v) { return v + 1; }

    interface Adder { int add(int v); }
    private final Adder adder = new Adder() {
        @Override public int add(int v) { return v + 1; }
    };
}
