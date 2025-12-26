# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 19274.515 ops/s | 21741.546 ops/s | 1.128x | **ZGC** | +12.799% |
| allocateLargeObjects (listSize:10000) | 19253.681 ops/s | 21632.237 ops/s | 1.124x | **ZGC** | +12.354% |
| allocateShortLived (listSize:1000) | 101374.436 ops/s | 104241.883 ops/s | 1.028x | **ZGC** | +2.829% |
| allocateShortLived (listSize:10000) | 9522.873 ops/s | 9801.061 ops/s | 1.029x | **ZGC** | +2.921% |

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
