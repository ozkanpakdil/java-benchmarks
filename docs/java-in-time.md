# Java-in-time Results

This page lists raw JSON outputs produced by the java-in-time JMH suite and provides a combined viewer link.

## Raw JSON files
- graalvm-community-17.json
- graalvm-community-24.json
- graalvm-community-25.json
- microsoft-17.json
- microsoft-25.json
- oracle-17.json
- oracle-24.json
- oracle-25.json
- temurin-17.json
- temurin-24.json
- temurin-25.json
- zulu-17.json
- zulu-24.json
- zulu-25.json

## View in JMH viewer
[Open all in jmh.morethan.io](https://jmh.morethan.io/?sources=https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/graalvm-community-17.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/graalvm-community-24.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/graalvm-community-25.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/microsoft-17.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/microsoft-25.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/oracle-17.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/oracle-24.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/oracle-25.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/temurin-17.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/temurin-24.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/temurin-25.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/zulu-17.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/zulu-24.json,https://raw.githubusercontent.com/ozkanpakdil/java-benchmarks/develop/java-in-time/results/zulu-25.json)

## Big table (all JSONs)
A single Markdown table with all available result files as columns:

[Open the big table](./java-in-time-all.md)

## Auto-comparisons
Automatically generated comparisons across versions (within a distribution) and across distributions (same Java version), only when both JSON files exist.

### Cross-version (within same distribution)
- zulu: 17 vs 25 → [view](./compare-zulu-17-vs-25.md)
- graalvm-community: 17 vs 25 → [view](./compare-graalvm-community-17-vs-25.md)
- microsoft: 17 vs 25 → [view](./compare-microsoft-17-vs-25.md)

### Cross-distribution (same Java version)
- Java 17: zulu vs graalvm-community → [view](./compare-zulu-vs-graalvm-community-17.md)
- Java 17: zulu vs microsoft → [view](./compare-zulu-vs-microsoft-17.md)
- Java 17: graalvm-community vs microsoft → [view](./compare-graalvm-community-vs-microsoft-17.md)
- Java 25: oracle vs zulu → [view](./compare-oracle-vs-zulu-25.md)
- Java 25: oracle vs graalvm-community → [view](./compare-oracle-vs-graalvm-community-25.md)
- Java 25: oracle vs temurin → [view](./compare-oracle-vs-temurin-25.md)
- Java 25: oracle vs microsoft → [view](./compare-oracle-vs-microsoft-25.md)
- Java 25: zulu vs graalvm-community → [view](./compare-zulu-vs-graalvm-community-25.md)
- Java 25: zulu vs temurin → [view](./compare-zulu-vs-temurin-25.md)
- Java 25: zulu vs microsoft → [view](./compare-zulu-vs-microsoft-25.md)
- Java 25: graalvm-community vs temurin → [view](./compare-graalvm-community-vs-temurin-25.md)
- Java 25: graalvm-community vs microsoft → [view](./compare-graalvm-community-vs-microsoft-25.md)
- Java 25: temurin vs microsoft → [view](./compare-temurin-vs-microsoft-25.md)
