# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 14125.062 ops/s | 16807.862 ops/s | 1.190x | **ZGC** | +18.993% |
| allocateLargeObjects (listSize:10000) | 14535.998 ops/s | 16978.117 ops/s | 1.168x | **ZGC** | +16.800% |
| allocateShortLived (listSize:1000) | 100261.506 ops/s | 102511.314 ops/s | 1.022x | **ZGC** | +2.244% |
| allocateShortLived (listSize:10000) | 7517.832 ops/s | 9616.937 ops/s | 1.279x | **ZGC** | +27.922% |

## Summary
Overall, **ZGC** performed better in this suite, winning 4 out of 4 tests.

### Key Differences
- **G1 GC**: Traditional generational collector, balanced throughput and latency. Default in most JDKs.
- **ZGC**: Low-latency scalable collector, designed for sub-millisecond pauses even with large heaps.

## Benchmarks Description

### `allocateShortLived` (Throughput)
Allocates many short-lived `String` objects to a `List`. This benchmark tests how efficiently the GC handles high allocation rates of small objects. High throughput here means the GC can keep up with rapid allocations without excessive stalling.

### `allocateLargeObjects` (Throughput)
Allocates 1MB byte arrays repeatedly. This benchmark tests how the GC handles large object allocations (Humongous objects in G1). G1 might struggle more with these as they require contiguous regions.
