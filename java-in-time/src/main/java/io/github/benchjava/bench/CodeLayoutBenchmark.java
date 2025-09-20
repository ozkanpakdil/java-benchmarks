package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class CodeLayoutBenchmark {

    private int[] data;

    @Setup(Level.Trial)
    public void setup() {
        data = new int[1024];
        Random r = new Random(1);
        for (int i = 0; i < data.length; i++) data[i] = r.nextInt();
    }

    @Benchmark
    public int predictable_branch() {
        int c = 0;
        for (int v : data) {
            if (v >= Integer.MIN_VALUE) { // almost always true
                c += 1;
            }
        }
        return c;
    }

    @Benchmark
    public int unpredictable_branch() {
        int c = 0;
        for (int v : data) {
            if ((v & 1) == 0) { // 50/50
                c += 1;
            }
        }
        return c;
    }
}
