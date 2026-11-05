# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 14923.697 ops/s | 19713.415 ops/s | 1.321x | **ZGC** | +32.095% |
| allocateLargeObjects (listSize:10000) | 14799.812 ops/s | 19352.888 ops/s | 1.308x | **ZGC** | +30.764% |
| allocateShortLived (listSize:1000) | 99772.020 ops/s | 102929.220 ops/s | 1.032x | **ZGC** | +3.164% |
| allocateShortLived (listSize:10000) | 9410.353 ops/s | 9769.523 ops/s | 1.038x | **ZGC** | +3.817% |

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
