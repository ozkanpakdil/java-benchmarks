# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 18625.627 ops/s | 20803.294 ops/s | 1.117x | **ZGC** | +11.692% |
| allocateLargeObjects (listSize:10000) | 18601.880 ops/s | 20140.914 ops/s | 1.083x | **ZGC** | +8.274% |
| allocateShortLived (listSize:1000) | 101170.460 ops/s | 103763.123 ops/s | 1.026x | **ZGC** | +2.563% |
| allocateShortLived (listSize:10000) | 9513.080 ops/s | 9846.837 ops/s | 1.035x | **ZGC** | +3.508% |

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
