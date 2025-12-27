# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 9817.914 ops/s | 9461.231 ops/s | 0.964x | **G1** | +3.770% |
| allocateLargeObjects (listSize:10000) | 9987.585 ops/s | 9719.426 ops/s | 0.973x | **G1** | +2.759% |
| allocateShortLived (listSize:1000) | 95660.123 ops/s | 95228.884 ops/s | 0.995x | **G1** | +0.453% |
| allocateShortLived (listSize:10000) | 8837.170 ops/s | 9309.619 ops/s | 1.053x | **ZGC** | +5.346% |

## Summary
Overall, **G1** performed better in this suite, winning 3 out of 4 tests.

### Key Differences
- **G1 GC**: Traditional generational collector, balanced throughput and latency. Default in most JDKs.
- **ZGC**: Low-latency scalable collector, designed for sub-millisecond pauses even with large heaps.

## Benchmarks Description

### `allocateShortLived` (Throughput)
Allocates many short-lived `String` objects to a `List`. This benchmark tests how efficiently the GC handles high allocation rates of small objects. High throughput here means the GC can keep up with rapid allocations without excessive stalling.

### `allocateLargeObjects` (Throughput)
Allocates 1MB byte arrays repeatedly. This benchmark tests how the GC handles large object allocations (Humongous objects in G1). G1 might struggle more with these as they require contiguous regions.
