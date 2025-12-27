# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |
|---|---|---|---|---|---|
| allocateLargeObjects (listSize:1000) | 16039.346 ops/s | 18612.028 ops/s | 1.160x | **ZGC** | +16.040% |
| allocateLargeObjects (listSize:10000) | 16690.783 ops/s | 18709.691 ops/s | 1.121x | **ZGC** | +12.096% |
| allocateShortLived (listSize:1000) | 100709.164 ops/s | 103649.837 ops/s | 1.029x | **ZGC** | +2.920% |
| allocateShortLived (listSize:10000) | 9474.198 ops/s | 9746.169 ops/s | 1.029x | **ZGC** | +2.871% |

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
