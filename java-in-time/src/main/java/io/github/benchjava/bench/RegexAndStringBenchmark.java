package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
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

    private String text;
    private Pattern emailPattern;

    @Setup(Level.Trial)
    public void setup() {
        Random rnd = new Random(123);
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = (char) ('a' + rnd.nextInt(26));
            sb.append(c);
        }
        text = sb.toString() + " test@example.com another@example.org";
        emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    @Benchmark
    public int string_concat_plus() {
        String s = "";
        for (int i = 0; i < 10; i++) s = s + i;
        return s.length();
    }

    @Benchmark
    public int string_concat_builder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(i);
        return sb.length();
    }

    @Benchmark
    public int string_bytes_utf8() {
        byte[] b = text.getBytes(StandardCharsets.UTF_8);
        return b.length;
    }

    @Benchmark
    public int regex_find_emails() {
        Matcher m = emailPattern.matcher(text);
        int count = 0;
        while (m.find()) count++;
        return count;
    }
}
