# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 16861.068 ops/s | 19507.174 ops/s | 1.157x | **ZGC** | +15.694% |
| allocateLargeObjects (listSize:10000) | 16763.248 ops/s | 19232.437 ops/s | 1.147x | **ZGC** | +14.730% |
| allocateShortLived (listSize:1000) | 100857.625 ops/s | 103406.647 ops/s | 1.025x | **ZGC** | +2.527% |
| allocateShortLived (listSize:10000) | 9479.208 ops/s | 9786.631 ops/s | 1.032x | **ZGC** | +3.243% |

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
