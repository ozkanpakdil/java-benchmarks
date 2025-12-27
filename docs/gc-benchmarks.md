# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 18524.514 ops/s | 20572.038 ops/s | 1.111x | **ZGC** | +11.053% |
| allocateLargeObjects (listSize:10000) | 18694.915 ops/s | 20443.876 ops/s | 1.094x | **ZGC** | +9.355% |
| allocateShortLived (listSize:1000) | 101174.245 ops/s | 103928.661 ops/s | 1.027x | **ZGC** | +2.722% |
| allocateShortLived (listSize:10000) | 9503.959 ops/s | 9714.643 ops/s | 1.022x | **ZGC** | +2.217% |

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
