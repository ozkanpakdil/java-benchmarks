package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class PeanutButterBenchmark {

    private byte[] data;

    @Setup(Level.Trial)
    public void setup() {
        data = new byte[2048];
        for (int i = 0; i < data.length; i++) data[i] = (byte)(i * 7);
    }

    @Benchmark
    public byte[] base64_encode() {
        return Base64.getEncoder().encode(data);
    }

    @Benchmark
    public byte[] base64_decode() {
        byte[] enc = Base64.getEncoder().encode(data);
        return Base64.getDecoder().decode(enc);
    }
}
