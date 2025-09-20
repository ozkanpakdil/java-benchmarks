package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class EnumerationBenchmark {

    enum Color { RED, GREEN, BLUE, YELLOW, ORANGE, PURPLE }

    private EnumSet<Color> set = EnumSet.of(Color.RED, Color.BLUE, Color.GREEN);

    @Benchmark
    public int enumset_iterate_ordinals() {
        int s = 0;
        for (Color c : set) s += c.ordinal();
        return s;
    }

    @Benchmark
    public int switch_on_enum() {
        int s = 0;
        for (Color c : set) {
            switch (c) {
                case RED: s += 1; break;
                case GREEN: s += 2; break;
                case BLUE: s += 3; break;
                default: s += 4; break;
            }
        }
        return s;
    }
}
