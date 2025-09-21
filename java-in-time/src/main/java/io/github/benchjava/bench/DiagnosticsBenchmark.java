package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class DiagnosticsBenchmark {

    private String cachedName;
    private Logger logger;
    private Handler handler;

    @Setup(Level.Trial)
    public void setup() {
        cachedName = DiagnosticsBenchmark.class.getName();
        // Set up a logger to simulate EventSource + listener (C# analogue)
        logger = Logger.getLogger("MyTestEventSource");
        logger.setUseParentHandlers(false);
        logger.setLevel(java.util.logging.Level.SEVERE);
        handler = new MyListener();
        handler.setLevel(java.util.logging.Level.SEVERE);
        // Avoid adding the same handler multiple times if reused across trials
        boolean already = false;
        for (Handler h : logger.getHandlers()) {
            if (h.getClass() == MyListener.class) { already = true; break; }
        }
        if (!already) logger.addHandler(handler);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        if (logger != null && handler != null) {
            logger.removeHandler(handler);
            handler.flush();
            handler.close();
        }
    }

    // Blog-aligned names (Diagnostics section)
    // Interpolate: string interpolation allocation reduction example
    @Benchmark
    public int Interpolate() {
        int a = 42; String b = "world";
        String s = "hello " + b + " " + a; // interpolation analogue
        return s.length();
    }

    // AssemblyQualifiedName: caching of computed name (analogue)
    @Benchmark
    public String AssemblyQualifiedName() {
        // Java analogue: fully qualified class name; simulate caching
        return cachedName;
    }

    // Stopwatch-style timing analogues
    @Benchmark
    public long WithGetTimestamp() {
        long start = System.nanoTime();
        Nop();
        long end = System.nanoTime();
        return end - start;
    }

    @Benchmark
    public long WithStartNew() {
        long start = System.nanoTime();
        Nop();
        return System.nanoTime() - start;
    }

    // EventSource analogue: log a single error event to our listener
    @Benchmark
    public void Oops() {
        logger.severe("Oops");
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private static void Nop() { /* intentionally empty */ }

    // Simple listener that collects events; minimal processing to keep benchmark focused on emission cost
    private static final class MyListener extends Handler {
        @Override
        public void publish(LogRecord record) { /* no-op to keep cost minimal */ }
        @Override
        public void flush() { }
        @Override
        public void close() throws SecurityException { }
    }
}
