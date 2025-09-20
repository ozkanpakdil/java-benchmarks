package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class GCWriteBarriersBenchmark {

    static final class Node { Node next; int v; }

    private final Node head;
    private final int[] array = new int[1024];

    public GCWriteBarriersBenchmark() {
        head = new Node();
        Node cur = head;
        for (int i = 0; i < 1023; i++) {
            Node n = new Node();
            cur.next = n;
            cur = n;
        }
    }

    @Benchmark
    public int field_writes() {
        Node cur = head;
        int i = 0;
        while (cur != null) {
            cur.v = i++;
            cur = cur.next;
        }
        return i;
    }

    @Benchmark
    public int array_writes() {
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        return array.length;
    }
}
