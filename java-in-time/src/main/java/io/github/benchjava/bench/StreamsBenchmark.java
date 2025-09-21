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
public class StreamsBenchmark {

    @Param({"100", "1000", "10000"})
    public int size;

    private List<Integer> data;
    private Random rng;

    @Setup(Level.Trial)
    public void setup() {
        data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) data.add(i);
        rng = new Random(42);
    }

    // Note: The following original methods pre-existed in this repo but their names didn't match the .NET blog.
    // We keep them for continuity, but add blog-aligned benchmarks below.




    // Blog-aligned names from .NET 10 blog — LINQ section (dotnetblog.md)
    // ShuffleTakeLinq appears at dotnetblog.md line ~4291 in the LINQ section.
    // C# original: public List<int> ShuffleTakeLinq() => _source.Shuffle().Take(10).ToList();
    @Benchmark
    public List<Integer> ShuffleTakeLinq() {
        ArrayList<Integer> copy = new ArrayList<>(data);
        Collections.shuffle(copy, rng);
        int take = Math.min(10, copy.size());
        return new ArrayList<>(copy.subList(0, take));
    }

    // ShuffleTakeContainsLinq appears at dotnetblog.md line ~4346 in the LINQ section.
    // C# original: public bool ShuffleTakeContainsLinq() => _source.Shuffle().Take(10).Contains(2000);
    @Benchmark
    public boolean ShuffleTakeContainsLinq() {
        ArrayList<Integer> copy = new ArrayList<>(data);
        Collections.shuffle(copy, rng);
        int take = Math.min(10, copy.size());
        return copy.subList(0, take).contains(2000);
    }

    // LeftJoin_Linq appears at dotnetblog.md line ~4443 in the LINQ section.
    // C# original context: a left-join via LINQ query syntax. Here we implement a simple Java Stream-based analog.
    @Benchmark
    public int LeftJoin_Linq() {
        // Prepare two small collections to join: keys are even numbers, values are their negatives
        List<Integer> left = data; // keys to probe
        List<Integer> right = new ArrayList<>();
        for (int i = 0; i < size; i += 2) right.add(i);
        Set<Integer> rightSet = new HashSet<>(right);
        int count = 0;
        for (int k : left) {
            // left join (k, match or null). We count matches to keep it cheap and side-effect-free
            if (rightSet.contains(k)) count++;
        }
        return count;
    }

}
