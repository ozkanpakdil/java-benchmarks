# GC Comparison: G1 vs ZGC
Comparison of throughput between G1 and ZGC collectors.

| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) |
|---|---|---|---|
| allocateLargeObjects{"listSize":"1000"} | 18847.326 ops/s | 21325.747 ops/s | 1.131x |
| allocateLargeObjects{"listSize":"10000"} | 18959.092 ops/s | 21423.341 ops/s | 1.130x |
| allocateShortLived{"listSize":"1000"} | 101211.506 ops/s | 103907.981 ops/s | 1.027x |
| allocateShortLived{"listSize":"10000"} | 9516.693 ops/s | 9811.960 ops/s | 1.031x |

## Benchmarks Description

### allocateShortLived
Allocates many short-lived String objects to a List. This benchmark tests how efficiently the GC handles high allocation rates of small objects.

### allocateLargeObjects
Allocates 1MB byte arrays. This benchmark tests how the GC handles large object allocations (Humongous objects in G1).
