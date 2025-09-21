package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Peanut Butter (miscellaneous) — Java analogues for assorted .NET blog snippets.
 * <p>
 * GCHandle vs PinnedGCHandle analogue:
 * - .NET compares GCHandle.Alloc(array, Pinned) vs PinnedGCHandle. Java doesn't expose pinning of
 * managed arrays; as an analogue we compare creating a heap ByteBuffer view (wrap) vs allocating a
 * direct (off-heap) ByteBuffer. Direct buffers are often used where pinning/native interop is desired.
 * <p>
 * Convert hex UTF8 overloads analogue:
 * - .NET added overloads to convert to/from hex using UTF8 without allocating strings.
 * - Here we implement byte[] <-> byte[] conversions using ASCII/UTF-8 hex representations without
 * creating Java String objects.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class PeanutButterBenchmark {

    // ---- GCHandle vs PinnedGCHandle analogue ----
    private final byte[] _array = new byte[16];

    @Benchmark
    public ByteBuffer Old() {
        // Analogue to GCHandle.Alloc(array, Pinned).Free(): cheap view over the heap array
        // (no actual pinning in Java; this mirrors the older approach of working with managed memory)
        return ByteBuffer.wrap(_array);
    }

    @Benchmark
    public ByteBuffer New() {
        // Analogue to PinnedGCHandle: use a direct buffer (off-heap) often preferred for interop
        // and to avoid GC movement concerns. We do not attempt to manually free; GC will clean it.
        return ByteBuffer.allocateDirect(_array.length);
    }

    // ---- Convert hex UTF8 overloads analogue ----
    @Param({"16", "32", "64"})
    public int bytesLen;

    private byte[] rawBytes;         // binary input
    private byte[] hexUtf8Lower;     // lowercase hex ASCII/UTF-8 bytes
    private byte[] hexOutBuffer;     // scratch output for ToHexStringLowerUtf8
    private byte[] binOutBuffer;     // scratch output for FromHexStringUtf8

    @Setup(Level.Trial)
    public void setup() {
        rawBytes = new byte[bytesLen];
        for (int i = 0; i < rawBytes.length; i++) rawBytes[i] = (byte) (i * 31 + 7);
        hexUtf8Lower = toHexLowerBytes(rawBytes);
        hexOutBuffer = new byte[rawBytes.length * 2];
        binOutBuffer = new byte[rawBytes.length];
    }

    // Parse lowercase hex from UTF-8 bytes into raw bytes, without creating Strings
    @Benchmark
    public byte[] FromHexStringUtf8() {
        byte[] src = hexUtf8Lower;
        byte[] dst = binOutBuffer;
        int len = src.length;
        int di = 0;
        for (int i = 0; i < len; i += 2) {
            int hi = hexNibble(src[i]);
            int lo = hexNibble(src[i + 1]);
            dst[di++] = (byte) ((hi << 4) | lo);
        }
        return dst;
    }

    // Convert raw bytes to lowercase hex-ASCII UTF-8 bytes, without creating Strings
    @Benchmark
    public byte[] ToHexStringLowerUtf8() {
        byte[] src = rawBytes;
        byte[] dst = hexOutBuffer;
        for (int i = 0, j = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            dst[j++] = HEX_LOWER[(v >>> 4) & 0xF];
            dst[j++] = HEX_LOWER[v & 0xF];
        }
        return dst;
    }

    private static final byte[] HEX_LOWER = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);

    private static int hexNibble(byte b) {
        int c = b & 0xFF;
        // ASCII digits
        if (c >= '0' && c <= '9') return c - '0';
        // Lowercase a-f
        if (c >= 'a' && c <= 'f') return 10 + (c - 'a');
        // Uppercase A-F (tolerate even if our inputs are lower)
        if (c >= 'A' && c <= 'F') return 10 + (c - 'A');
        // For benchmark purposes, assume valid input; fall back to 0
        return 0;
    }

    private static byte[] toHexLowerBytes(byte[] data) {
        byte[] out = new byte[data.length * 2];
        for (int i = 0, j = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;
            out[j++] = HEX_LOWER[(v >>> 4) & 0xF];
            out[j++] = HEX_LOWER[v & 0xF];
        }
        return out;
    }
}
