# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 17132.916 ops/s | 19523.323 ops/s | 1.140x | **ZGC** | +13.952% |
| allocateLargeObjects (listSize:10000) | 17158.254 ops/s | 18949.244 ops/s | 1.104x | **ZGC** | +10.438% |
| allocateShortLived (listSize:1000) | 100996.207 ops/s | 102915.164 ops/s | 1.019x | **ZGC** | +1.900% |
| allocateShortLived (listSize:10000) | 9510.035 ops/s | 9768.792 ops/s | 1.027x | **ZGC** | +2.721% |

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
