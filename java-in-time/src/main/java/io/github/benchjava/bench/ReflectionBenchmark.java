package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class ReflectionBenchmark {

    public int add(int a, int b) { return a + b; }

    private Method reflect;
    private MethodHandle handle;

    @Setup(Level.Trial)
    public void setup() throws Throwable {
        reflect = ReflectionBenchmark.class.getMethod("add", int.class, int.class);
        handle = MethodHandles.lookup().findVirtual(ReflectionBenchmark.class, "add",
                MethodType.methodType(int.class, int.class, int.class));
    }

    private int a = 3, b = 4;

    @Benchmark
    public int direct_call() { return add(a, b); }

    @Benchmark
    public int reflection_invoke() throws Exception { return (int) reflect.invoke(this, a, b); }

    @Benchmark
    public int methodhandle_invokeExact() throws Throwable { return (int) handle.invokeExact(this, a, b); }
}
