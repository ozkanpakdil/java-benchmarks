package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class CryptoBenchmark {

    private byte[] data;
    private MessageDigest sha256;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        data = new byte[1024];
        for (int i = 0; i < data.length; i++) data[i] = (byte) (i * 13);
        sha256 = MessageDigest.getInstance("SHA-256");
    }

    // Blog-aligned name from the Cryptography section
    @Benchmark
    public void Hash() {
        // Analog to SHA256.HashData(src, dst) in the blog; we compute the hash into a new array
        sha256.digest(data);
    }

}
