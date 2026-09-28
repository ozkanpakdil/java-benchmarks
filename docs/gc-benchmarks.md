# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 17394.451 ops/s | 19008.765 ops/s | 1.093x | **ZGC** | +9.281% |
| allocateLargeObjects (listSize:10000) | 17310.046 ops/s | 19117.148 ops/s | 1.104x | **ZGC** | +10.440% |
| allocateShortLived (listSize:1000) | 100238.616 ops/s | 102561.849 ops/s | 1.023x | **ZGC** | +2.318% |
| allocateShortLived (listSize:10000) | 9422.978 ops/s | 9750.200 ops/s | 1.035x | **ZGC** | +3.473% |

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
