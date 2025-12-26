# GC Benchmarks: G1 vs ZGC

This page compares the performance of G1 and ZGC garbage collectors using JMH benchmarks.

## Results

No results available yet. Run the benchmarks using `gc-benchmarks/scripts/run-gc-benchmarks.sh`.

## Benchmarks Description

### `allocateShortLived`
Allocates many short-lived `String` objects to a `List`. This benchmark tests how efficiently the GC handles high allocation rates of small objects.

### `allocateLargeObjects`
Allocates 1MB byte arrays. This benchmark tests how the GC handles large object allocations (Humongous objects in G1).
