# Java Version Benchmarks (JMH)

This project runs a broad JMH microbenchmark suite across multiple Java versions and distributions (e.g., 8/11/17/24/25; Temurin, Oracle, GraalVM, etc.). It mirrors many categories from the .NET 10 performance blog in Java.

Quick links:
- Run benchmarks across JDKs: scripts/run-benchmarks.sh
- Compare two result files: scripts/compare-results.sh (Java tool wrapper)
- Blog-style document: docs/blog.md

Prerequisites
- Bash, Maven 3.8+, Git
- At least two JDKs installed; note their JAVA_HOME paths. sdkman paths are fine.

How to run (typical flow)
1) Collect results (run first):
   scripts/run-benchmarks.sh labelA:/absolute/path/to/jdkA labelB:/absolute/path/to/jdkB

   Examples (sdkman):
   - scripts/run-benchmarks.sh java25:~/.sdkman/candidates/java/25-oracle/ java24:~/.sdkman/candidates/java/24.0.2-oracle/
   - scripts/run-benchmarks.sh graal25:~/.sdkman/candidates/java/25.0.0-graalce temurin17:/usr/lib/jvm/temurin-17

   Notes:
   - This script now expands a leading ~ in paths; both ~/... and /abs/path work.
   - It builds the shaded JAR with the matching profile based on the JDK’s major (e.g., java-24, java-25) unless you override with PROFILES.
   - Results are saved to results/<label>.json and .txt.
   - You can filter which benchmarks run by providing a JMH include regex as a positional argument via BENCH_INCLUDE env (see below).

2) Compare two results (run second):
   scripts/compare-results.sh results/<baseline>.json results/<candidate>.json > docs/compare.md

   Or directly via the Java tool:
   java -cp target/benchmarks.jar io.github.benchjava.tools.CompareResults results/A.json results/B.json > docs/compare.md

Environment variables (optional)
- PROFILES: Maven profile for release level (default inferred from active JDK)
  Example: PROFILES=java-17
- JMH_OPTS: Extra JMH CLI args
  Example: JMH_OPTS="-wi 5 -i 10 -bm avgt -tu ns -f 1"
- BENCH_INCLUDE: JMH include pattern (regex). If set, it’s passed as a positional filter.
  Example: BENCH_INCLUDE="Regex.*|Json.*"
- MAVEN_ARGS: Extra Maven args (e.g., -q -DskipTests)

Troubleshooting
- Error: "JAVA not found at ~/.sdkman/.../bin/java"
  Cause: ~ wasn’t expanded before. Fixed: run-benchmarks.sh now expands leading ~ to $HOME. Ensure the directory contains bin/java and is a full JDK (not just a JRE).
- Different units across benchmarks
  JMH outputs per-benchmark units (ns/op or us/op). The comparator uses the units written by JMH and computes ratio = B/A (lower is better for AverageTime).
- Limiting benchmarks
  Use BENCH_INCLUDE: BENCH_INCLUDE="Collections.*|Streams.*" scripts/run-benchmarks.sh java25:~/... java24:~/...

Files of interest
- scripts/run-benchmarks.sh — builds with each JDK and runs JMH, writing JSON + text logs
- scripts/compare-results.sh — invokes Java comparator to produce Markdown
- src/main/java/io/github/benchjava/tools/CompareResults.java — JSON comparator (Java)
- docs/blog.md — long-form write-up and methodology

Tips for stable numbers
- Close other apps; set CPU governor to performance if possible.
- Increase warmup/measurement iterations and forks (JMH_OPTS) for less variance.
- Keep JVM flags consistent between runs unless intentionally testing them.
