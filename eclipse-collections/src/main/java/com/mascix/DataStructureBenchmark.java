package com.mascix;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.sorted.MutableSortedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.SortedMaps;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class DataStructureBenchmark {

    @Param({"10000000"})
    private int size;

    private List<Integer> arrayList;
    private List<Integer> linkedList;
    private MutableList<Integer> ecMutableList;

    private Map<Integer, Integer> hashMap;
    private Map<Integer, Integer> treeMap;
    private MutableMap<Integer, Integer> ecMutableMap;
    private MutableSortedMap<Integer, Integer> ecTreeSortedMap;

    private Integer[] indices;
    private Integer[] keys;

    @Setup
    public void setup() {
        arrayList = new ArrayList<>(size);
        linkedList = new LinkedList<>();
        ecMutableList = Lists.mutable.withInitialCapacity(size);

        hashMap = new HashMap<>(size);
        treeMap = new TreeMap<>();
        ecMutableMap = Maps.mutable.withInitialCapacity(size);
        ecTreeSortedMap = SortedMaps.mutable.empty();

        indices = new Integer[1000];
        keys = new Integer[1000];
        Random random = new Random(1L);
        for (int i = 0; i < 1000; i++) {
            indices[i] = random.nextInt(size);
            keys[i] = random.nextInt(size);
        }

        for (int i = 0; i < size; i++) {
            arrayList.add(i);
            linkedList.add(i);
            ecMutableList.add(i);
            hashMap.put(i, i);
            treeMap.put(i, i);
            ecMutableMap.put(i, i);
            ecTreeSortedMap.put(i, i);
        }
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public void arrayListGet(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(arrayList.get(indices[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public void linkedListGet(Blackhole bh) {
        // LinkedList.get(i) is O(N), so we do it fewer times or differently to avoid extreme delays
        // but here we want to show it's slow.
        for (int i = 0; i < 1000; i++) {
            bh.consume(linkedList.get(indices[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public void ecMutableListGet(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(ecMutableList.get(indices[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public void hashMapGet(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(hashMap.get(keys[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public void ecMutableMapGet(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(ecMutableMap.get(keys[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public void treeMapGet(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(treeMap.get(keys[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public void ecTreeSortedMapGet(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(ecTreeSortedMap.get(keys[i]));
        }
    }

    @Benchmark
    public void arrayListAdd(Blackhole bh) {
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        bh.consume(list);
    }

    @Benchmark
    public void linkedListAdd(Blackhole bh) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        bh.consume(list);
    }

    @Benchmark
    public void ecMutableListAdd(Blackhole bh) {
        MutableList<Integer> list = Lists.mutable.withInitialCapacity(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        bh.consume(list);
    }

    @Benchmark
    public void hashMapPut(Blackhole bh) {
        Map<Integer, Integer> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            map.put(i, i);
        }
        bh.consume(map);
    }

    @Benchmark
    public void ecMutableMapPut(Blackhole bh) {
        MutableMap<Integer, Integer> map = Maps.mutable.withInitialCapacity(size);
        for (int i = 0; i < size; i++) {
            map.put(i, i);
        }
        bh.consume(map);
    }

    @Benchmark
    public void treeMapPut(Blackhole bh) {
        Map<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < size; i++) {
            map.put(i, i);
        }
        bh.consume(map);
    }

    @Benchmark
    public void ecTreeSortedMapPut(Blackhole bh) {
        MutableSortedMap<Integer, Integer> map = TreeSortedMap.newMap();
        for (int i = 0; i < size; i++) {
            map.put(i, i);
        }
        bh.consume(map);
    }
}
