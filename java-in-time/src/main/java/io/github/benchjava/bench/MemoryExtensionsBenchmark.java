package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class MemoryExtensionsBenchmark {

    private byte[] a, b;

    @Param({"256","4096"})
    public int len;

    @Setup(Level.Trial)
    public void setup() {
        a = new byte[len];
        b = new byte[len];
        for (int i = 0; i < len; i++) a[i] = (byte) i;
        System.arraycopy(a, 0, b, 0, len);
        // introduce a single difference near the end
        if (len > 0) b[len - 1] ^= 1;
    }

    @Benchmark
    public int arrays_mismatch() {
        return Arrays.mismatch(a, b);
    }

    @Benchmark
    public int system_arraycopy_sum() {
        byte[] dst = new byte[a.length];
        System.arraycopy(a, 0, dst, 0, a.length);
        int s = 0;
        for (byte v : dst) s += v;
        return s;
    }

    @Benchmark
    public int bytebuffer_put_get() {
        ByteBuffer buf = ByteBuffer.allocate(len);
        buf.put(a);
        int s = 0;
        for (int i = 0; i < buf.position(); i++) s += buf.get(i);
        return s;
    }
}
