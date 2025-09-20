package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class IOBenchmark {

    private byte[] data;

    @Param({"1024","65536"})
    public int size;

    @Setup(Level.Trial)
    public void setup() {
        data = new byte[size];
        for (int i = 0; i < data.length; i++) data[i] = (byte)(i * 31);
    }

    @Benchmark
    public int bytearray_stream_copy() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) >= 0) {
            out.write(buf, 0, n);
        }
        return out.size();
    }

    @Benchmark
    public int system_arraycopy() {
        byte[] dst = new byte[data.length];
        System.arraycopy(data, 0, dst, 0, data.length);
        return dst.length;
    }

    @Benchmark
    public int string_roundtrip_utf8() throws Exception {
        String s = new String(data, StandardCharsets.ISO_8859_1);
        byte[] back = s.getBytes(StandardCharsets.UTF_8);
        return back.length;
    }
}
