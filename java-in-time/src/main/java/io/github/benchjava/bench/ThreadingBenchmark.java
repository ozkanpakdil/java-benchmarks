package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class ThreadingBenchmark {

    private final ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 0);
    private int x;
    private final Object monitor = new Object();
    private final ReentrantLock lock = new ReentrantLock();

    @Benchmark
    public int thread_local_get_set() {
        int v = tl.get();
        tl.set(v + 1);
        return v;
    }

    @Benchmark
    public int synchronized_inc() {
        synchronized (monitor) {
            return ++x;
        }
    }

    @Benchmark
    public int reentrantlock_inc() {
        lock.lock();
        try { return ++x; } finally { lock.unlock(); }
    }
}
