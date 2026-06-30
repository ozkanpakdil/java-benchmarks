# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 22423.119 ops/s | 26663.288 ops/s | 1.189x | **ZGC** | +18.910% |
| allocateLargeObjects (listSize:10000) | 22466.963 ops/s | 26810.003 ops/s | 1.193x | **ZGC** | +19.331% |
| allocateShortLived (listSize:1000) | 99173.653 ops/s | 101377.728 ops/s | 1.022x | **ZGC** | +2.222% |
| allocateShortLived (listSize:10000) | 9607.703 ops/s | 9923.353 ops/s | 1.033x | **ZGC** | +3.285% |

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
