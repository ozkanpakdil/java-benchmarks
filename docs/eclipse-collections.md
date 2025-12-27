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
HashMap vs TreeMap vs ArrayList vs LinkedList
HashMap.get()      → ~42 ms
TreeMap.get()      → ~2,818 ms
ArrayList.get(i)   → ~8 ms
LinkedList.get(i)  → ~56,957,333 ms
Insertion (10M elements):
ArrayList.add()    → ~1,308 ms
HashMap.put()      → ~3,410 ms
LinkedList.add()   → ~4,182 ms
```

### Detailed Comparison Table

| Structure | Type | Insertion (10M) | Get (Random) |
|---|---|---|---|
| **ArrayList** | JDK | ~1,308 ms | ~8 ms |
| **MutableList (FastList)** | EC | ~1,332 ms | ~8 ms |
| **HashMap** | JDK | ~3,410 ms | ~42 ms |
| **MutableMap (UnifiedMap)** | EC | ~3,705 ms | ~38 ms |
| **TreeMap** | JDK | ~4,403 ms | ~2,818 ms |
| **TreeSortedMap** | EC | ~4,535 ms | ~2,754 ms |
| **LinkedList** | JDK | ~4,182 ms | ~56,957,333 ms |

### Observations:
- Results generated from 25-temurin.json
- 'Get' operations are scaled to 10M operations for consistency with 'Insertion'.
