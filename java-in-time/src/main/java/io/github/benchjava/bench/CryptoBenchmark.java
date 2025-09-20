package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
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
    private Cipher aes;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        data = new byte[1024];
        for (int i = 0; i < data.length; i++) data[i] = (byte) (i * 13);
        sha256 = MessageDigest.getInstance("SHA-256");
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        SecretKey key = kg.generateKey();
        aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(Cipher.ENCRYPT_MODE, key);
    }

    @Benchmark
    public byte[] sha256_digest() {
        return sha256.digest(data);
    }

    @Benchmark
    public byte[] aes_encrypt() throws Exception {
        return aes.doFinal(data);
    }
}
