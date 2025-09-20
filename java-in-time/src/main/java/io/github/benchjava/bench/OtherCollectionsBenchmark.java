package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class OtherCollectionsBenchmark {

    private final LinkedList<Integer> linked = new LinkedList<>();
    private final ArrayDeque<Integer> deque = new ArrayDeque<>();

    @Setup(Level.Iteration)
    public void fill() {
        linked.clear();
        deque.clear();
        for (int i = 0; i < 1024; i++) {
            linked.add(i);
            deque.add(i);
        }
    }

    @Benchmark
    public int linkedlist_push_pop() {
        LinkedList<Integer> l = new LinkedList<>(linked);
        int s = 0;
        for (int i = 0; i < 256; i++) {
            l.push(i);
            s += l.pop();
        }
        return s;
    }

    @Benchmark
    public int arraydeque_push_pop() {
        ArrayDeque<Integer> d = new ArrayDeque<>(deque);
        int s = 0;
        for (int i = 0; i < 256; i++) {
            d.push(i);
            s += d.pop();
        }
        return s;
    }
}
