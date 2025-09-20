package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.BitSet;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class BitArrayBenchmark {

    private BitSet bs;

    @Param({"1024","16384"})
    public int size;

    @Setup(Level.Trial)
    public void setup() {
        bs = new BitSet(size);
        for (int i = 0; i < size; i += 3) bs.set(i);
    }

    @Benchmark
    public int count_bits() { return bs.cardinality(); }

    @Benchmark
    public boolean get_flip() {
        int idx = (size / 3);
        boolean before = bs.get(idx);
        bs.flip(idx);
        return before ^ bs.get(idx);
    }
}
