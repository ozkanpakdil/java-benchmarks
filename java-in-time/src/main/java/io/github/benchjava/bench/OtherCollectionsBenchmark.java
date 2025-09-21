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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Other Collections — analogue for .NET FrozenDictionary alternate-lookup example.
 * <p>
 * The .NET blog shows looking up values in a FrozenDictionary using an alternate key type
 * (e.g., a UTF-8 byte span) without allocating a new string. Java's standard collections do not
 * have a direct equivalent, but we can simulate it by:
 * - Storing keys as String in a HashMap<String, Integer>.
 * - Performing lookups with a special key wrapper (AlternateAsciiKey) whose hashCode matches
 * what String would compute for the underlying ASCII bytes, and whose equals can compare to
 * the stored String keys without allocating a new String.
 * <p>
 * This lets us call map.get(new AlternateAsciiKey(bytes, off, len)) and have it match the
 * existing String key when the content is the same, mirroring the alternate-lookup intent.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class OtherCollectionsBenchmark {

    private HashMap<String, Integer> map;
    private byte[][] utf8Keys; // ASCII for simplicity
    private int[] indices;

    @Param({"16", "128", "1024"})
    public int count;

    @Setup(Level.Trial)
    public void setup() {
        map = new HashMap<>();
        utf8Keys = new byte[count][];
        indices = new int[Math.max(1, count)];
        for (int i = 0; i < count; i++) {
            String key = "key-" + i;
            map.put(key, i);
            utf8Keys[i] = key.getBytes(StandardCharsets.US_ASCII);
            indices[i] = i;
        }
        // Shuffle indices to avoid predictable access patterns
        Random r = new Random(42);
        for (int i = indices.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            int tmp = indices[i];
            indices[i] = indices[j];
            indices[j] = tmp;
        }
    }

    // Blog-aligned name: Get (alternate-lookup)
    // Returns the sum to prevent dead-code elimination.
    @Benchmark
    public int Get() {
        int sum = 0;
        HashMap<String, Integer> m = map;
        byte[][] keys = utf8Keys;
        for (int idx : indices) {
            byte[] k = keys[idx];
            Integer v = m.get(new AlternateAsciiKey(k, 0, k.length));
            sum += (v != null ? v : -1);
        }
        return sum;
    }

    /**
     * An alternate lookup key that wraps ASCII bytes and implements hashCode/equals compatible
     * with String so that HashMap<String, V> can be probed without allocating a String.
     * <p>
     * Notes:
     * - hashCode replicates String's 31-based hash for the ASCII bytes.
     * - equals returns true when compared to an equal String (content match) or to the same wrapper type.
     * - We only support US-ASCII here to keep the benchmark simple and focused on the mechanism.
     */
    static final class AlternateAsciiKey {
        final byte[] bytes;
        final int off;
        final int len;
        private int hash; // cached 0 means uncomputed; nonzero cached value

        AlternateAsciiKey(byte[] bytes, int off, int len) {
            this.bytes = bytes;
            this.off = off;
            this.len = len;
        }

        @Override
        public int hashCode() {
            int h = hash;
            if (h == 0) {
                int end = off + len;
                for (int i = off; i < end; i++) {
                    h = 31 * h + (bytes[i] & 0xFF);
                }
                if (h == 0) h = 1; // avoid 0 so we can distinguish computed vs not
                hash = h;
            }
            return h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (o instanceof String s) {
                if (s.length() != len) return false;
                for (int i = 0; i < len; i++) {
                    if ((bytes[off + i] & 0xFF) != (s.charAt(i) & 0xFF)) return false;
                }
                return true;
            }
            if (o instanceof AlternateAsciiKey other) {
                if (len != other.len) return false;
                for (int i = 0; i < len; i++) {
                    if (bytes[off + i] != other.bytes[other.off + i]) return false;
                }
                return true;
            }
            return false;
        }
    }
}
