package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class RegexAndStringBenchmark {

    @Param({"128", "1024", "8192"})
    public int len;

    private String s_input;
    private Pattern s_regex;

    @Setup(Level.Trial)
    public void setup() {
        Random rnd = new Random(123);
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = (char) ('a' + rnd.nextInt(26));
            sb.append(c);
        }
        // add a few word-like tokens to exercise the regex
        sb.append(' ').append("hello world this is a test");
        s_input = sb.toString();
        // Use a simple blog-like pattern; many Regex examples use Count() as the method name
        s_regex = Pattern.compile("\\s+\\S+");
    }

    // Blog-aligned name from multiple Regex snippets
    @Benchmark
    public int Count() {
        var m = s_regex.matcher(s_input);
        int count = 0;
        while (m.find()) count++;
        return count;
    }
}
