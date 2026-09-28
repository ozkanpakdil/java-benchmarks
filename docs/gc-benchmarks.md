# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 22126.048 ops/s | 26646.773 ops/s | 1.204x | **ZGC** | +20.432% |
| allocateLargeObjects (listSize:10000) | 22701.902 ops/s | 25602.338 ops/s | 1.128x | **ZGC** | +12.776% |
| allocateShortLived (listSize:1000) | 127721.442 ops/s | 133773.611 ops/s | 1.047x | **ZGC** | +4.739% |
| allocateShortLived (listSize:10000) | 12514.570 ops/s | 12463.160 ops/s | 0.996x | **G1** | +0.412% |

## Summary
Overall, **ZGC** performed better in this suite, winning 3 out of 4 tests.

### Key Differences
- **G1 GC**: Traditional generational collector, balanced throughput and latency. Default in most JDKs.
- **ZGC**: Low-latency scalable collector, designed for sub-millisecond pauses even with large heaps.

## Benchmarks Description

### `allocateShortLived` (Throughput)
Allocates many short-lived `String` objects to a `List`. This benchmark tests how efficiently the GC handles high allocation rates of small objects. High throughput here means the GC can keep up with rapid allocations without excessive stalling.

### `allocateLargeObjects` (Throughput)
Allocates 1MB byte arrays repeatedly. This benchmark tests how the GC handles large object allocations (Humongous objects in G1). G1 might struggle more with these as they require contiguous regions.
