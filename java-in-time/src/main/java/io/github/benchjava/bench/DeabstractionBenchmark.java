package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class DeabstractionBenchmark {

    interface Op { int apply(int a, int b); }
    static final class Add implements Op { public int apply(int a, int b) { return a + b; } }

    private final Op iface = new Add();
    private final Add concrete = new Add();

    private int a = 3, b = 4;

    @Benchmark
    public int via_interface() { return iface.apply(a, b); }

    @Benchmark
    public int via_concrete() { return concrete.apply(a, b); }
}
