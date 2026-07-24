# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 18152.263 ops/s | 20178.145 ops/s | 1.112x | **ZGC** | +11.160% |
| allocateLargeObjects (listSize:10000) | 18189.971 ops/s | 19928.194 ops/s | 1.096x | **ZGC** | +9.556% |
| allocateShortLived (listSize:1000) | 100741.387 ops/s | 103031.993 ops/s | 1.023x | **ZGC** | +2.274% |
| allocateShortLived (listSize:10000) | 9495.832 ops/s | 9743.469 ops/s | 1.026x | **ZGC** | +2.608% |

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
