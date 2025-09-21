package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class CollectionsBenchmark {

    @Param({"4", "16", "64"})
    public int size;

    private List<Integer> list;
    private List<Integer> _data;

    @Setup(Level.Trial)
    public void setup() {
        list = new ArrayList<>();
        _data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            _data.add(i + 1);
        }
        // seed list with a few values to force internal growth on insert
        list.add(1000);
        list.add(2000);
        list.add(3000);
        list.add(4000);
    }

    // Blog-aligned name based on the Collections section discussing List<T>.InsertRange
    @Benchmark
    public List<Integer> InsertRange() {
        // Analog for C# list.InsertRange(0, _data)
        list.addAll(0, _data);
        return list;
    }
}
