# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 16879.082 ops/s | 19409.430 ops/s | 1.150x | **ZGC** | +14.991% |
| allocateLargeObjects (listSize:10000) | 16829.597 ops/s | 19040.771 ops/s | 1.131x | **ZGC** | +13.139% |
| allocateShortLived (listSize:1000) | 100194.482 ops/s | 103092.651 ops/s | 1.029x | **ZGC** | +2.893% |
| allocateShortLived (listSize:10000) | 9454.458 ops/s | 9764.894 ops/s | 1.033x | **ZGC** | +3.283% |

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
