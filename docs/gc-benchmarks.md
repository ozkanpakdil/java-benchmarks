# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 11091.194 ops/s | 10004.622 ops/s | 0.902x | **G1** | +10.861% |
| allocateLargeObjects (listSize:10000) | 11090.254 ops/s | 10501.186 ops/s | 0.947x | **G1** | +5.610% |
| allocateShortLived (listSize:1000) | 102438.699 ops/s | 99698.980 ops/s | 0.973x | **G1** | +2.748% |
| allocateShortLived (listSize:10000) | 9310.387 ops/s | 9431.920 ops/s | 1.013x | **ZGC** | +1.305% |

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
