# Eclipse Collections Results

This page lists raw JSON outputs produced by the Eclipse Collections benchmark module and provides a combined viewer link.

## Common Data Structures Comparison (10M Operations)

Reproducing the benchmark from the [Substack post](https://substack.com/@skilledcoder/note/c-190793397) and adding Eclipse Collections.

### Insertion (10M elements)
| Structure | Time (~ms) |
|---|---|
| **ArrayList.add()** | ~70 |
| **MutableList.add() (EC)** | ~63 |
| **HashMap.put()** | ~241 |
| **MutableMap.put() (EC)** | ~95 |
| **TreeMap.put()** | ~806 |
| **TreeSortedMap.put() (EC)** | ~904 |
| **LinkedList.add()** | ~337 |

### Get (10M operations total)
Baseline comparison on a 10M sized structure. Values are normalized for comparison.

| Structure | Time |
|---|---|
| **ArrayList.get(i)** | ~40 ms |
| **MutableList.get(i) (EC)** | ~50 ms |
| **HashMap.get(key)** | ~140 ms |
| **MutableMap.get(key) (EC)** | ~150 ms |
| **TreeMap.get(key)** | ~420 ms |
| **TreeSortedMap.get(key) (EC)** | ~450 ms |
| **LinkedList.get(i)** | ~2.5 s |

*Note: The get() results for Eclipse Collections and standard JDK are very close. In this environment, `MutableList` and `MutableMap` showed significant performance improvements during insertion compared to standard JDK `ArrayList` and `HashMap` respectively. `LinkedList` and Tree-based structures show significantly higher costs as expected.*

## Raw JSON files
- 17-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.10-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.10-OpenJDK 64-Bit Server VM.json
- 17.0.11-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.12-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.12-OpenJDK 64-Bit Server VM.json
- 17.0.13-OpenJDK 64-Bit Server VM.json
- 17.0.16-OpenJDK 64-Bit Server VM.json
- 17.0.17-OpenJDK 64-Bit Server VM.json
- 17.0.6-Eclipse OpenJ9 VM-Eclipse OpenJ9.json
- 17.0.6-Eclipse OpenJ9 VM.json
- 17.0.6-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.6-OpenJDK 64-Bit Server VM-Azul Systems, Inc..json
- 17.0.6-OpenJDK 64-Bit Server VM-Eclipse Adoptium.json
- 17.0.6-OpenJDK 64-Bit Server VM.json
- 17.0.7-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.8-Java HotSpot(TM) 64-Bit Server VM.json
- 17.0.9-Java HotSpot(TM) 64-Bit Server VM.json
- 21.0.4-Eclipse OpenJ9 VM.json
- 21.0.5-Java HotSpot(TM) 64-Bit Server VM.json
- 21.0.8-Java HotSpot(TM) 64-Bit Server VM.json
- 21.0.8-OpenJDK 64-Bit Server VM.json
- 21.0.9-Java HotSpot(TM) 64-Bit Server VM.json
- 21.0.9-OpenJDK 64-Bit Server VM.json
- 24-Java HotSpot(TM) 64-Bit Server VM.json
- 24.0.2-OpenJDK 64-Bit Server VM.json
- 25.0.1-Java HotSpot(TM) 64-Bit Server VM.json
- 25.0.1-OpenJDK 64-Bit Server VM.json
- jmh-result-1.8.0_312.json
- jmh-result-1.8.0_322.json
- jmh-result-1.8.0_332.json
- jmh-result-1.8.0_342.json
- jmh-result-1.8.0_345.json
- jmh-result-1.8.0_352.json
- jmh-result-1.8.0_362.json
- jmh-result-11.0.13.json
- jmh-result-11.0.14.json
- jmh-result-11.0.15.json
- jmh-result-11.0.16.json
- jmh-result-11.0.17.json
- jmh-result-11.0.18.json
- jmh-result-17.0.1.json
- jmh-result-17.0.2.json
- jmh-result-17.0.3.json
- jmh-result-17.0.4.json
- jmh-result-17.0.5.json
- jmh-result-17.0.6.json
- jmh-result-21.0.5.json

## View in JMH viewer
[Open all in jmh.morethan.io](https://jmh.morethan.io/?sources=https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.10-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.10-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.11-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.12-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.12-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.13-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.16-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.17-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-Eclipse OpenJ9 VM-Eclipse OpenJ9.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-Eclipse OpenJ9 VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-OpenJDK 64-Bit Server VM-Azul Systems, Inc..json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-OpenJDK 64-Bit Server VM-Eclipse Adoptium.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.6-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.7-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.8-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/17.0.9-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.4-Eclipse OpenJ9 VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.5-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.8-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.8-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.9-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/21.0.9-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/24-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/24.0.2-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/25.0.1-Java HotSpot(TM) 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/25.0.1-OpenJDK 64-Bit Server VM.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_312.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_322.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_332.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_342.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_345.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_352.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-1.8.0_362.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-11.0.13.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-11.0.14.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-11.0.15.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-11.0.16.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-11.0.17.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-11.0.18.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-17.0.1.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-17.0.2.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-17.0.3.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-17.0.4.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-17.0.5.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-17.0.6.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/eclipse-collections/results/jmh-result-21.0.5.json)
