# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 9325.745 ops/s | 8194.523 ops/s | 0.879x | **G1** | +13.805% |
| allocateLargeObjects (listSize:10000) | 9360.419 ops/s | 8131.340 ops/s | 0.869x | **G1** | +15.115% |
| allocateShortLived (listSize:1000) | 140546.882 ops/s | 119722.264 ops/s | 0.852x | **G1** | +17.394% |
| allocateShortLived (listSize:10000) | 11897.845 ops/s | 10490.538 ops/s | 0.882x | **G1** | +13.415% |

## Summary
Overall, **G1** performed better in this suite, winning 4 out of 4 tests.

### Key Differences
- **G1 GC**: Traditional generational collector, balanced throughput and latency. Default in most JDKs.
- **ZGC**: Low-latency scalable collector, designed for sub-millisecond pauses even with large heaps.

## Benchmarks Description

### `allocateShortLived` (Throughput)
Allocates many short-lived `String` objects to a `List`. This benchmark tests how efficiently the GC handles high allocation rates of small objects. High throughput here means the GC can keep up with rapid allocations without excessive stalling.

### `allocateLargeObjects` (Throughput)
Allocates 1MB byte arrays repeatedly. This benchmark tests how the GC handles large object allocations (Humongous objects in G1). G1 might struggle more with these as they require contiguous regions.
