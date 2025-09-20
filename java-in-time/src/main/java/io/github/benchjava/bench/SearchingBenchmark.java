package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class SearchingBenchmark {

    private int[] sorted;
    private int needle;
    private String haystack;

    @Setup(Level.Trial)
    public void setup() {
        sorted = new int[2048];
        for (int i = 0; i < sorted.length; i++) sorted[i] = i * 2;
        needle = 1984;
        StringBuilder sb = new StringBuilder(4096);
        Random r = new Random(1);
        for (int i = 0; i < 4096; i++) sb.append((char)('a' + r.nextInt(26)));
        haystack = sb.toString();
    }

    @Benchmark
    public int arrays_binary_search() {
        return Arrays.binarySearch(sorted, needle);
    }

    @Benchmark
    public int string_indexOf_hit() {
        return haystack.indexOf('z');
    }

    @Benchmark
    public int string_indexOf_miss() {
        return haystack.indexOf("notpresent");
    }
}
