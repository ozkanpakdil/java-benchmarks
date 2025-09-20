package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class ConcurrencyBenchmark {

    @Param({"100", "1000"})
    public int tasks;

    private ExecutorService pool;

    @Setup(Level.Trial)
    public void setup() {
        pool = Executors.newWorkStealingPool();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        pool.shutdownNow();
    }

    private static long cpuWork(int iters) {
        long x = 1;
        for (int i = 0; i < iters; i++) x = x * 1664525 + 1013904223;
        return x;
    }

    @Benchmark
    public long completableFuture_allOf() throws Exception {
        List<CompletableFuture<Long>> list = new ArrayList<>(tasks);
        for (int i = 0; i < tasks; i++) {
            list.add(CompletableFuture.supplyAsync(() -> cpuWork(2000), pool));
        }
        CompletableFuture<Void> all = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
        all.get();
        long s = 0;
        for (CompletableFuture<Long> f : list) s += f.get();
        return s;
    }

    @Benchmark
    public long forkJoin_tasks() throws Exception {
        List<Callable<Long>> cs = new ArrayList<>(tasks);
        for (int i = 0; i < tasks; i++) cs.add(() -> cpuWork(2000));
        List<Future<Long>> futures = pool.invokeAll(cs);
        long s = 0;
        for (Future<Long> f : futures) s += f.get();
        return s;
    }
}
