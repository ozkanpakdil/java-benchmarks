package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class IOBenchmark {

    private byte[] bytes;
    private Path tempFile;
    private Path watchDir;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        // 1 MiB input like the blog snippet
        bytes = new byte[1024 * 1024];
        java.util.Random r = new java.util.Random(42);
        r.nextBytes(bytes);
        // Prepare a temp file for MMF
        tempFile = Files.createTempFile("jmh-mmf", ".bin");
        Files.write(tempFile, bytes, StandardOpenOption.TRUNCATE_EXISTING);
        // Prepare a temp directory for watch service
        watchDir = Files.createTempDirectory("jmh-watch");
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException {
        if (tempFile != null) Files.deleteIfExists(tempFile);
        if (watchDir != null) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(watchDir)) {
                for (Path p : ds) Files.deleteIfExists(p);
            }
            Files.deleteIfExists(watchDir);
        }
    }

    // Blog-aligned name from the I/O section (BufferedStream WriteByte)
    @Benchmark
    public void WriteByte() throws IOException {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        try (OutputStream sink = OutputStream.nullOutputStream();
             DeflaterOutputStream def = new DeflaterOutputStream(sink, deflater);
             BufferedOutputStream bos = new BufferedOutputStream(def, 256)) {
            for (byte b : bytes) {
                bos.write(b & 0xFF);
            }
            bos.flush();
        }
    }

    // Blog-aligned names: MMF (MemoryMappedFile) and FSW (FileSystemWatcher) analogs
    @Benchmark
    public long MMF() throws IOException {
        try (FileChannel ch = FileChannel.open(tempFile, StandardOpenOption.READ)) {
            MappedByteBuffer map = ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size());
            long sum = 0;
            for (int i = 0; i < map.limit(); i++) {
                sum += map.get(i) & 0xFF;
            }
            return sum;
        }
    }

    @Benchmark
    public int FSW() throws IOException {
        try (WatchService ws = FileSystems.getDefault().newWatchService()) {
            watchDir.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
            // Trigger an event
            Path f = watchDir.resolve("file.txt");
            Files.writeString(f, "hi");
            Files.delete(f);
            // Poll events (non-blocking)
            int count = 0;
            WatchKey key = ws.poll();
            if (key != null) {
                for (WatchEvent<?> ignored : key.pollEvents()) count++;
                key.reset();
            }
            return count;
        }
    }
}
