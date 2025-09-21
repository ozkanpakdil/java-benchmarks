package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class NumericsBenchmark {

    // ---- UInt128 Divide analog (using BigInteger to simulate 128-bit) ----
    private static final int DATA_SIZE = 1024;
    private int idx;
    private BigInteger[] nData;
    private BigInteger[] dData;
    private BigInteger[] writeValues;
    private String[] uuidStrings;
    private byte[][] uuidUtf8;
    private String[] versionStrings;
    private byte[][] versionUtf8;

    @Setup(Level.Trial)
    public void setupData() {
        Random rnd = new Random(12345);
        nData = new BigInteger[DATA_SIZE];
        dData = new BigInteger[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; i++) {
            byte[] nb = new byte[16];
            rnd.nextBytes(nb);
            nb[0] &= 0x7F; // ensure positive
            nData[i] = new BigInteger(nb);
            long dl;
            do {
                dl = rnd.nextLong();
            } while (dl == 0L);
            dData[i] = BigInteger.valueOf(dl).abs();
        }
        // Pre-generate values for TryWriteBytes
        writeValues = new BigInteger[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; i++) {
            int digits = 10 + rnd.nextInt(40); // 10..49 digits
            StringBuilder sb = new StringBuilder(digits);
            for (int d = 0; d < digits; d++) {
                int digit = rnd.nextInt(10);
                if (d == 0 && digit == 0) digit = 1; // avoid leading zero shrinking
                sb.append((char) ('0' + digit));
            }
            writeValues[i] = new BigInteger(sb.toString());
        }
        // UUID data
        uuidStrings = new String[DATA_SIZE];
        uuidUtf8 = new byte[DATA_SIZE][];
        for (int i = 0; i < DATA_SIZE; i++) {
            UUID u = new UUID(rnd.nextLong(), rnd.nextLong());
            String s = u.toString();
            uuidStrings[i] = s;
            uuidUtf8[i] = s.getBytes(StandardCharsets.US_ASCII);
        }
        // Version data
        versionStrings = new String[DATA_SIZE];
        versionUtf8 = new byte[DATA_SIZE][];
        for (int i = 0; i < DATA_SIZE; i++) {
            int a = 1 + rnd.nextInt(10);
            int b = rnd.nextInt(20);
            int c = rnd.nextInt(50);
            // Java Runtime.Version supports major[.minor][.security], not 4-part versions.
            // Randomly choose 1–3 components.
            int parts = 1 + rnd.nextInt(3);
            String vs;
            if (parts == 1) {
                vs = Integer.toString(a);
            } else if (parts == 2) {
                vs = a + "." + b;
            } else {
                vs = a + "." + b + "." + c;
            }
            versionStrings[i] = vs;
            versionUtf8[i] = vs.getBytes(StandardCharsets.US_ASCII);
        }
    }

    @Benchmark
    public BigInteger Divide() {
        int i = (idx++) & (DATA_SIZE - 1);
        return nData[i].divide(dData[i]);
    }

    // ---- BigInteger.TryWriteBytes analog ----
    private final byte[] _bytes = new byte[256];

    @Benchmark
    public boolean TryWriteBytes() {
        int i = (idx++) & (DATA_SIZE - 1);
        byte[] src = writeValues[i].toByteArray();
        if (src.length > _bytes.length) return false;
        System.arraycopy(src, 0, _bytes, 0, src.length);
        return true;
    }

    // ---- BigInteger.Parse(int.MinValue) analog ----
    private final String _int32min = Integer.toString(Integer.MIN_VALUE);

    @Benchmark
    public BigInteger ParseInt32Min() {
        // Construct a fresh String instance to avoid potential constant folding/interning biases
        return new BigInteger(new String(_int32min));
    }

    // ---- DateTimeOffset GetFutureTime analog ----
    @Benchmark
    public long GetFutureTime() {
        // Java analog: compute a future time instant 5 seconds from now
        return Instant.now().plusSeconds(5).toEpochMilli();
    }

    // ---- Guid parsing from UTF8 bytes vs transcode (Java UUID analog) ----

    @Benchmark
    public UUID GuidParse() {
        int i = (idx++) & (DATA_SIZE - 1);
        return UUID.fromString(uuidStrings[i]);
    }

    @Benchmark
    public UUID TranscodeParse() {
        int i = (idx++) & (DATA_SIZE - 1);
        String s = new String(uuidUtf8[i], StandardCharsets.US_ASCII);
        return UUID.fromString(s);
    }

    @Benchmark
    public UUID Utf8ParserParse() {
        int i = (idx++) & (DATA_SIZE - 1);
        // Java has no built-in UUID parser from UTF8 bytes without creating a String;
        // we mirror the name and perform the same work as TranscodeParse for parity.
        return UUID.fromString(new String(uuidUtf8[i], StandardCharsets.US_ASCII));
    }

    // ---- Version parsing: bytes -> transcode vs direct parse (Java Runtime.Version analog) ----

    @Benchmark
    public Runtime.Version VersionParse() {
        int i = (idx++) & (DATA_SIZE - 1);
        return Runtime.Version.parse(versionStrings[i]);
    }

    @Benchmark
    public Runtime.Version TranscodeParse_Version() {
        int i = (idx++) & (DATA_SIZE - 1);
        String s = new String(versionUtf8[i], StandardCharsets.US_ASCII);
        return Runtime.Version.parse(s);
    }

    // ---- TensorPrimitives.Decrement analogs (names preserved from blog) ----
    private final float[] _src = IntStream.range(0, 1000).mapToObj(i -> (float) i).collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
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
