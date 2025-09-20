package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class StreamsBenchmark {

    @Param({"100", "1000", "10000"})
    public int size;

    private List<Integer> data;

    @Setup(Level.Trial)
    public void setup() {
        data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) data.add(i);
    }

    @Benchmark
    public long sum_loop() {
        long s = 0;
        for (int i = 0; i < data.size(); i++) s += data.get(i);
        return s;
    }

    @Benchmark
    public long sum_for_each() {
        long s = 0;
        for (int v : data) s += v;
        return s;
    }

    @Benchmark
    public long sum_stream() {
        return data.stream().mapToLong(Integer::intValue).sum();
    }

    @Benchmark
    public long sum_parallel_stream() {
        return data.parallelStream().mapToLong(Integer::intValue).sum();
    }
}
