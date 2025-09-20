package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class NumericsBenchmark {

    private int ia = 123456789, ib = 987654321;
    private long la = 1234567890123456789L, lb = 987654321098765432L;
    private double da = 12345.6789, db = 0.0001234;

    @Benchmark public int int_add() { return ia + ib; }
    @Benchmark public long long_mul() { return la * 31L; }
    @Benchmark public double double_fma() { return Math.fma(da, db, 1.0); }

    @Benchmark
    public BigInteger biginteger_mul() {
        BigInteger A = BigInteger.valueOf(la).shiftLeft(32).add(BigInteger.valueOf(ia));
        return A.multiply(BigInteger.valueOf(1234567));
    }

    @Benchmark
    public int bit_ops() {
        int x = ia;
        x ^= (x << 13);
        x ^= (x >>> 17);
        x ^= (x << 5);
        return x;
    }
}
