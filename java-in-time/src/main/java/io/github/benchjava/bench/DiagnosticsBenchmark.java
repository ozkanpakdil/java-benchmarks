package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class DiagnosticsBenchmark {

    @Benchmark
    public int throwable_fillInStackTrace() {
        Throwable t = new Throwable();
        t.fillInStackTrace();
        return t.hashCode();
    }

    @Benchmark
    public int capture_stacktrace_elements() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        int s = 0;
        for (StackTraceElement e : st) s += e.getClassName().length();
        return s;
    }
}
