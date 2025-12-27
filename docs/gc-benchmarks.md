# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 18500.310 ops/s | 20488.774 ops/s | 1.107x | **ZGC** | +10.748% |
| allocateLargeObjects (listSize:10000) | 18487.160 ops/s | 21045.300 ops/s | 1.138x | **ZGC** | +13.837% |
| allocateShortLived (listSize:1000) | 101018.698 ops/s | 103511.179 ops/s | 1.025x | **ZGC** | +2.467% |
| allocateShortLived (listSize:10000) | 9479.099 ops/s | 9658.030 ops/s | 1.019x | **ZGC** | +1.888% |

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
