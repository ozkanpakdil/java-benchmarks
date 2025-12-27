# Eclipse Collections Results

This page lists raw JSON outputs produced by the Eclipse Collections benchmark module and provides a combined viewer link.

## Raw JSON files
- 17-oracle.json
- 17-temurin.json
- 17.0.12-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.17-OpenJDK 64-Bit Server VM.json
- 17.0.6-Eclipse OpenJ9 VM-Eclipse OpenJ9.json
- 17.0.6-Eclipse OpenJ9 VM.json
- 17.0.6-OpenJDK 64-Bit Server VM-Azul Systems, Inc..json
- 17.0.6-OpenJDK 64-Bit Server VM-Eclipse Adoptium.json
- 21-oracle.json
- 21-temurin.json
- 21.0.4-Eclipse OpenJ9 VM.json
- 21.0.9-Java HotSpot(TM) 64-Bit Server VM.json
- 21.0.9-OpenJDK 64-Bit Server VM.json
- 24-Java HotSpot(TM) 64-Bit Server VM.json
- 24.0.2-OpenJDK 64-Bit Server VM.json
- 25-datastructure.json
- 25-oracle.json
- 25-temurin.json
- 25.0.1-Java HotSpot(TM) 64-Bit Server VM.json
- 25.0.1-OpenJDK 64-Bit Server VM.json
- jmh-result-1.8.0_362.json
- jmh-result-11.0.18.json
- jmh-result-17.0.6.json
- jmh-result-21.0.5.json

## View in JMH viewer
[Open all in jmh.morethan.io](https://jmh.morethan.io/?sources=https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17-oracle.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17-temurin.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.12-Java%20HotSpot%28TM%29%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.17-OpenJDK%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-Eclipse%20OpenJ9%20VM-Eclipse%20OpenJ9.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-Eclipse%20OpenJ9%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-OpenJDK%2064-Bit%20Server%20VM-Azul%20Systems%2C%20Inc..json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-OpenJDK%2064-Bit%20Server%20VM-Eclipse%20Adoptium.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21-oracle.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21-temurin.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.4-Eclipse%20OpenJ9%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.9-Java%20HotSpot%28TM%29%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.9-OpenJDK%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/24-Java%20HotSpot%28TM%29%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/24.0.2-OpenJDK%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/25-datastructure.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/25-oracle.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/25-temurin.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/25.0.1-Java%20HotSpot%28TM%29%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/25.0.1-OpenJDK%2064-Bit%20Server%20VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_362.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-11.0.18.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-17.0.6.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-21.0.5.json)

## Common Data Structures Comparison (10M Operations)
Comparison between standard JDK and Eclipse Collections equivalents.

📊 **[View Benchmark Source Code](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/eclipse-collections/src/main/java/com/mascix/DataStructureBenchmark.java)**

### Quick Comparison
```text
Get (10M elements):
ArrayList.get()    → ~8 ms total, ~0.833 ns avg/op
HashMap.get()      → ~43 ms total, ~4.324 ns avg/op
TreeMap.get()      → ~2,728 ms total, ~272.823 ns avg/op
LinkedList.get()   → ~60,368,764 ms total, ~6.037 ms avg/op

Insertion (10M elements):
ArrayList.add()    → ~1,334 ms total, ~133.370 ns avg/op
HashMap.put()      → ~3,781 ms total, ~378.101 ns avg/op
TreeMap.put()      → ~4,324 ms total, ~432.432 ns avg/op
LinkedList.add()   → ~4,081 ms total, ~408.091 ns avg/op
```

### Detailed Comparison Table

| Structure | Type | Insertion 10M (Total / Avg) | Get 10M (Total / Avg) |
|---|---|---|---|
| **ArrayList** | JDK | ~1,334 ms / ~133.370 ns | ~8 ms / ~0.833 ns |
| **MutableList (FastList)** | EC | ~1,294 ms / ~129.426 ns | ~8 ms / ~0.831 ns |
| **HashMap** | JDK | ~3,781 ms / ~378.101 ns | ~43 ms / ~4.324 ns |
| **MutableMap (UnifiedMap)** | EC | ~3,712 ms / ~371.230 ns | ~38 ms / ~3.796 ns |
| **TreeMap** | JDK | ~4,324 ms / ~432.432 ns | ~2,728 ms / ~272.823 ns |
| **TreeSortedMap** | EC | ~4,801 ms / ~480.139 ns | ~2,710 ms / ~271.022 ns |
| **LinkedList** | JDK | ~4,081 ms / ~408.091 ns | ~60,368,764 ms / ~6.037 ms |

### Observations:
- Results generated from 25-temurin.json
- All values show: Total time for 10M operations / Average time per single operation.
- Eclipse Collections does not have a direct LinkedList equivalent (EC focuses on optimized array-based structures like FastList).
