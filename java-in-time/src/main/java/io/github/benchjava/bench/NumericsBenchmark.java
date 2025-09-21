package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class NumericsBenchmark {

    // ---- UInt128 Divide analog (using BigInteger to simulate 128-bit) ----
    private final BigInteger _n = (BigInteger.ONE.shiftLeft(64).multiply(BigInteger.valueOf(123L))).add(BigInteger.valueOf(456L));
    private final BigInteger _d = BigInteger.valueOf(789L);

    @Benchmark
    public BigInteger Divide() {
        return _n.divide(_d);
    }

    // ---- BigInteger.TryWriteBytes analog ----
    private final BigInteger _value = new BigInteger(String.join("", java.util.Collections.nCopies(20, "1234567890")));
    private final byte[] _bytes = new byte[256];

    @Benchmark
    public boolean TryWriteBytes() {
        byte[] src = _value.toByteArray();
        if (src.length > _bytes.length) return false;
        System.arraycopy(src, 0, _bytes, 0, src.length);
        return true;
    }

    // ---- BigInteger.Parse(int.MinValue) analog ----
    private final String _int32min = Integer.toString(Integer.MIN_VALUE);

    @Benchmark
    public BigInteger ParseInt32Min() {
        return new BigInteger(_int32min);
    }

    // ---- DateTimeOffset GetFutureTime analog ----
    @Benchmark
    public long GetFutureTime() {
        // Java analog: compute a future time instant 5 seconds from now
        return Instant.now().plusSeconds(5).toEpochMilli();
    }

    // ---- Guid parsing from UTF8 bytes vs transcode (Java UUID analog) ----
    private final String _uuidStr = "123e4567-e89b-12d3-a456-426655440000";
    private final byte[] _uuidUtf8 = _uuidStr.getBytes(StandardCharsets.US_ASCII);

    @Benchmark
    public UUID GuidParse() {
        return UUID.fromString(_uuidStr);
    }

    @Benchmark
    public UUID TranscodeParse() {
        String s = new String(_uuidUtf8, StandardCharsets.US_ASCII);
        return UUID.fromString(s);
    }

    @Benchmark
    public UUID Utf8ParserParse() {
        // Java has no built-in UUID parser from UTF8 bytes without creating a String;
        // we mirror the name and perform the same work as TranscodeParse for parity.
        return UUID.fromString(new String(_uuidUtf8, StandardCharsets.US_ASCII));
    }

    // ---- Version parsing: bytes -> transcode vs direct parse (Java Runtime.Version analog) ----
    private final String _versionStr = "1.2.3.4";
    private final byte[] _versionUtf8 = _versionStr.getBytes(StandardCharsets.US_ASCII);

    @Benchmark
    public Runtime.Version VersionParse() {
        return Runtime.Version.parse(_versionStr);
    }

    @Benchmark
    public Runtime.Version TranscodeParse_Version() {
        String s = new String(_versionUtf8, StandardCharsets.US_ASCII);
        return Runtime.Version.parse(s);
    }

    // ---- TensorPrimitives.Decrement analogs (names preserved from blog) ----
    private final float[] _src = java.util.stream.IntStream.range(0, 1000).mapToObj(i -> (float) i).collect(java.util.stream.Collectors.collectingAndThen(java.util.stream.Collectors.toList(), l -> {
        float[] a = new float[l.size()];
        for (int i = 0; i < a.length; i++) a[i] = l.get(i);
        return a;
    }));
    private final float[] _dest = new float[1000];

    @Benchmark
    public void DecrementManual() {
        float[] src = _src;
        for (int i = 0; i < src.length; i++) {
            _dest[i] = src[i] - 1f;
        }
    }

    @Benchmark
    public void DecrementTP() {
        // No direct TensorPrimitives in Java stdlib; keep the same name and perform the same operation.
        float[] src = _src;
        for (int i = 0; i < src.length; i++) {
            _dest[i] = src[i] - 1f;
        }
    }
}
