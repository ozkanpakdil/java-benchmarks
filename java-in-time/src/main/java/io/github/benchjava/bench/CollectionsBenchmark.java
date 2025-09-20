package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class CollectionsBenchmark {

    @Param({"100", "1000", "10000"})
    public int size;

    private List<Integer> list;
    private Map<Integer, Integer> map;
    private int[] array;

    @Setup(Level.Trial)
    public void setup() {
        Random rnd = new Random(42);
        list = new ArrayList<>(size);
        map = new HashMap<>(size * 2);
        array = new int[size];
        for (int i = 0; i < size; i++) {
            int v = rnd.nextInt();
            list.add(v);
            map.put(i, v);
            array[i] = v;
        }
    }

    @Benchmark
    public int array_sum() {
        int s = 0;
        int[] a = array;
        for (int i = 0; i < a.length; i++) {
            s += a[i];
        }
        return s;
    }

    @Benchmark
    public int list_sum_forEach() {
        int s = 0;
        for (int v : list) {
            s += v;
        }
        return s;
    }

    @Benchmark
    public long list_sum_stream() {
        return list.stream().mapToLong(Integer::intValue).sum();
    }

    @Benchmark
    public int hashmap_get_hit() {
        int s = 0;
        for (int i = 0; i < size; i++) {
            s += map.get(i);
        }
        return s;
    }

    @Benchmark
    public int hashmap_get_miss() {
        int s = 0;
        for (int i = 0; i < size; i++) {
            Integer v = map.get(i + size);
            s += (v == null ? 0 : v);
        }
        return s;
    }

    @Benchmark
    public List<Integer> list_sort_copy() {
        List<Integer> copy = new ArrayList<>(list);
        Collections.sort(copy);
        return copy;
    }
}
