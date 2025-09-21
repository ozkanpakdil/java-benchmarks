package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class ReflectionBenchmark {

    private Class<?> t;

    @Setup(Level.Trial)
    public void setup() {
        // Use a complex nested/array type to simulate blog's TypeName.Parse workload
        t = java.util.Map.Entry[].class;
    }

    // Blog-aligned name from the Reflection section
    @Benchmark
    public String ParseAndGetName() {
        // Java analog: return the full type name string
        return t.getTypeName();
    }
}
