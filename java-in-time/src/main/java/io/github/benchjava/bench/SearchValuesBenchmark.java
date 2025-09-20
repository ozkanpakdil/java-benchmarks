package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class SearchValuesBenchmark {

    private String text;
    private boolean[] table = new boolean[256];

    @Setup(Level.Trial)
    public void setup() {
        text = "the quick brown fox jumps over the lazy dog 1234567890";
        String needles = "aeiou0123456789";
        for (int i = 0; i < needles.length(); i++) table[needles.charAt(i) & 0xFF] = true;
    }

    @Benchmark
    public int index_of_any_table() {
        String s = text;
        boolean[] t = table;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 256 && t[c]) return i;
        }
        return -1;
    }

    @Benchmark
    public int index_of_any_chain() {
        // naive chain of indexOf on multiple chars
        int pos = Integer.MAX_VALUE;
        for (char c : new char[]{'a','e','i','o','u'}) {
            int p = text.indexOf(c);
            if (p >= 0 && p < pos) pos = p;
        }
        return pos == Integer.MAX_VALUE ? -1 : pos;
    }
}
