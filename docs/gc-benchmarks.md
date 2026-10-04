# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 12654.395 ops/s | 8454.791 ops/s | 0.668x | **G1** | +49.671% |
| allocateLargeObjects (listSize:10000) | 12204.972 ops/s | 8442.542 ops/s | 0.692x | **G1** | +44.565% |
| allocateShortLived (listSize:1000) | 111144.517 ops/s | 101572.385 ops/s | 0.914x | **G1** | +9.424% |
| allocateShortLived (listSize:10000) | 10324.996 ops/s | 9860.369 ops/s | 0.955x | **G1** | +4.712% |

## Summary
Overall, **G1** performed better in this suite, winning 4 out of 4 tests.

### Key Differences
- **G1 GC**: Traditional generational collector, balanced throughput and latency. Default in most JDKs.
- **ZGC**: Low-latency scalable collector, designed for sub-millisecond pauses even with large heaps.

## Benchmarks Description

### `allocateShortLived` (Throughput)
Allocates many short-lived `String` objects to a `List`. This benchmark tests how efficiently the GC handles high allocation rates of small objects. High throughput here means the GC can keep up with rapid allocations without excessive stalling.

### `allocateLargeObjects` (Throughput)
Allocates 1MB byte arrays repeatedly. This benchmark tests how the GC handles large object allocations (Humongous objects in G1). G1 might struggle more with these as they require contiguous regions.
