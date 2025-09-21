package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class ThreadingBenchmark {

    // Analogue of the .NET WhenAllAlloc using two tasks from a range (Enumerable.Range(0, 2)).
    // We construct the combined future first (to measure allocations/overheads), then complete the
    // component futures to avoid blocking and to keep semantics close to the C# snippet.
    @Benchmark
    public CompletableFuture<Void> WhenAllAlloc_Two() {
        CompletableFuture<Void> a = new CompletableFuture<>();
        CompletableFuture<Void> b = new CompletableFuture<>();
        CompletableFuture<Void> whenAll = CompletableFuture.allOf(a, b);
        a.complete(null);
        b.complete(null);
        return whenAll;
    }

    // Analogue of the .NET WhenAllAlloc using an array with a single task (Task.WhenAll([t.Task])).
    @Benchmark
    public CompletableFuture<Void> WhenAllAlloc_One() {
        CompletableFuture<Void> a = new CompletableFuture<>();
        CompletableFuture<Void> whenAll = CompletableFuture.allOf(a);
        a.complete(null);
        return whenAll;
    }
}
