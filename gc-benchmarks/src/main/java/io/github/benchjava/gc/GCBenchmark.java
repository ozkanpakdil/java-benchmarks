package io.github.benchjava.gc;

import org.openjdk.jmh.annotations.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class GCBenchmark {

    @Param({"1000", "10000"})
    private int listSize;

    @Benchmark
    public List<String> allocateShortLived() {
        List<String> list = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            list.add(new String("Garbage " + i));
        }
        return list;
    }

    @Benchmark
    public Object allocateLargeObjects() {
        return new byte[1024 * 1024]; // 1MB allocation
    }
}
