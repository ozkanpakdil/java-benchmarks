# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 17423.416 ops/s | 19967.122 ops/s | 1.146x | **ZGC** | +14.599% |
| allocateLargeObjects (listSize:10000) | 17674.332 ops/s | 20227.091 ops/s | 1.144x | **ZGC** | +14.443% |
| allocateShortLived (listSize:1000) | 100433.241 ops/s | 103023.318 ops/s | 1.026x | **ZGC** | +2.579% |
| allocateShortLived (listSize:10000) | 9466.229 ops/s | 9763.601 ops/s | 1.031x | **ZGC** | +3.141% |

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
