# Java Version Benchmarks (JMH)

This project runs a broad JMH microbenchmark suite across multiple Java versions and distributions (e.g., 8/11/17/24/25; Temurin, Oracle, GraalVM, etc.). It mirrors many categories from the .NET 10 performance blog in Java.

Project goal and blog reference
- Goal: port (as faithfully as practical) the C# microbenchmarks from the .NET 10 performance blog to Java, and run the same style of tests on the JVM using JMH. This lets you compare improvements across Java versions similar to how the blog compares .NET versions.
- Reference: https://devblogs.microsoft.com/dotnet/performance-improvements-in-net-10/
- Naming: wherever possible, benchmark method names in this repo match the blog’s names so you can search for them directly (for example, Int32_Add rather than int_add).

Quick links:
- Run benchmarks across JDKs: scripts/run-benchmarks.sh
- Compare two result files: scripts/compare-results.sh (Java tool wrapper)
- Reference blog (local copy): Performance Improvements in .NET 10 - .NET Blog.html

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
   scripts/compare-results.sh results/<baseline>.json results/<candidate>.json > results/compare.md

   Or directly via the Java tool:
   java -cp target/benchmarks.jar io.github.benchjava.tools.CompareResults results/A.json results/B.json > results/compare.md

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
- Performance Improvements in .NET 10 - .NET Blog.html — local reference copy of the blog

these are the categories covered in C# blog

```
Bounds Checking
Cloning
Inlining
Constant Folding
Code Layout
GC Write Barriers
Instruction Sets
Miscellaneous
Native AOT
VM
Threading
Reflection
Primitives and Numerics
Collections
Enumeration
LINQ
Frozen Collections
BitArray
Other Collections
I/O
Networking
Searching
Regex
SearchValues
MemoryExtensions
JSON
Diagnostics
Cryptography
Peanut Butter
```


Mapping to the .NET 10 performance blog
- Numerics (blog) — src/main/java/io/github/benchjava/bench/NumericsBenchmark.java
  - Benchmark method names mirror the blog snippet names:
    - Divide
    - TryWriteBytes
    - ParseInt32Min
    - DecrementManual
    - DecrementTP
  - These map to the Primitives and Numerics section (UInt128/BigInteger/TensorPrimitives items). Search for these identifiers in results to correlate with the blog tables.
- BitArray improvements — src/main/java/io/github/benchjava/bench/BitArrayBenchmark.java
  - Benchmark method names mirror the blog snippet names:
    - HammingDistanceManual
    - HammingDistanceTensorPrimitives
    - ByteCtor
- Cloning — src/main/java/io/github/benchjava/bench/CloningBenchmark.java
- Code layout — src/main/java/io/github/benchjava/bench/CodeLayoutBenchmark.java
- Collections (general) — src/main/java/io/github/benchjava/bench/CollectionsBenchmark.java
  - Methods: InsertRange
- Collections (other/edge cases) — src/main/java/io/github/benchjava/bench/OtherCollectionsBenchmark.java
- Concurrency — src/main/java/io/github/benchjava/bench/ConcurrencyBenchmark.java
- Threading — src/main/java/io/github/benchjava/bench/ThreadingBenchmark.java
- Cryptography — src/main/java/io/github/benchjava/bench/CryptoBenchmark.java
  - Methods: Hash
- Devirtualization/Deabstraction — src/main/java/io/github/benchjava/bench/DeabstractionBenchmark.java
- Diagnostics — src/main/java/io/github/benchjava/bench/DiagnosticsBenchmark.java
- Enumeration — src/main/java/io/github/benchjava/bench/EnumerationBenchmark.java
- GC write barriers — src/main/java/io/github/benchjava/bench/GCWriteBarriersBenchmark.java
- I/O — src/main/java/io/github/benchjava/bench/IOBenchmark.java
  - Methods: WriteByte
- JIT Inlining — src/main/java/io/github/benchjava/bench/JitInliningBenchmark.java
- JSON — src/main/java/io/github/benchjava/bench/JsonBenchmark.java
- Memory extensions/unsafe-like utilities — src/main/java/io/github/benchjava/bench/MemoryExtensionsBenchmark.java
- Networking — src/main/java/io/github/benchjava/bench/NetworkingBenchmark.java
- Peanut Butter (assorted micro-ops) — src/main/java/io/github/benchjava/bench/PeanutButterBenchmark.java
- Reflection — src/main/java/io/github/benchjava/bench/ReflectionBenchmark.java
  - Methods: ParseAndGetName
- Regular expressions and strings — src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java
  - Methods: Count
- SearchValues (vectorized search) — src/main/java/io/github/benchjava/bench/SearchValuesBenchmark.java
- Searching (algorithms and data structures) — src/main/java/io/github/benchjava/bench/SearchingBenchmark.java
- Streams/LINQ-like patterns — src/main/java/io/github/benchjava/bench/StreamsBenchmark.java

Notes about parity with the blog
- These Java benchmarks are analogues inspired by the blog’s test areas, not literal ports of .NET code.
- Names are intentionally similar so you can correlate sections easily.
- If you need to run only a subset, use BENCH_INCLUDE with the names above (e.g., BENCH_INCLUDE="Numerics.*|Json.*").

Tips for stable numbers
- Close other apps; set CPU governor to performance if possible.
- Increase warmup/measurement iterations and forks (JMH_OPTS) for less variance.
- Keep JVM flags consistent between runs unless intentionally testing them.


these are the categories covered in C# blog

```
Bounds Checking
Cloning
Inlining
Constant Folding
Code Layout
GC Write Barriers
Instruction Sets
Miscellaneous
Native AOT
VM
Threading
Reflection
Primitives and Numerics
Collections
Enumeration
LINQ
Frozen Collections
BitArray
Other Collections
I/O
Networking
Searching
Regex
SearchValues
MemoryExtensions
JSON
Diagnostics
Cryptography
Peanut Butter
```


Mapping to the .NET 10 performance blog
- Numerics (blog) — src/main/java/io/github/benchjava/bench/NumericsBenchmark.java
  - Benchmark method names mirror the blog snippet names:
    - Divide
    - TryWriteBytes
    - ParseInt32Min
    - DecrementManual
    - DecrementTP
  - These map to the Primitives and Numerics section (UInt128/BigInteger/TensorPrimitives items). Search for these identifiers in results to correlate with the blog tables.
- BitArray improvements — src/main/java/io/github/benchjava/bench/BitArrayBenchmark.java
  - Benchmark method names mirror the blog snippet names:
    - HammingDistanceManual
    - HammingDistanceTensorPrimitives
    - ByteCtor
- Cloning — src/main/java/io/github/benchjava/bench/CloningBenchmark.java
- Code layout — src/main/java/io/github/benchjava/bench/CodeLayoutBenchmark.java
- Collections (general) — src/main/java/io/github/benchjava/bench/CollectionsBenchmark.java
  - Methods: InsertRange
- Collections (other/edge cases) — src/main/java/io/github/benchjava/bench/OtherCollectionsBenchmark.java
- Concurrency — src/main/java/io/github/benchjava/bench/ConcurrencyBenchmark.java
- Threading — src/main/java/io/github/benchjava/bench/ThreadingBenchmark.java
- Cryptography — src/main/java/io/github/benchjava/bench/CryptoBenchmark.java
  - Methods: Hash
- Devirtualization/Deabstraction — src/main/java/io/github/benchjava/bench/DeabstractionBenchmark.java
- Diagnostics — src/main/java/io/github/benchjava/bench/DiagnosticsBenchmark.java
- Enumeration — src/main/java/io/github/benchjava/bench/EnumerationBenchmark.java
- I/O — src/main/java/io/github/benchjava/bench/IOBenchmark.java
  - Methods: WriteByte
- JIT Inlining — src/main/java/io/github/benchjava/bench/JitInliningBenchmark.java
- JSON — src/main/java/io/github/benchjava/bench/JsonBenchmark.java
- Memory extensions/unsafe-like utilities — src/main/java/io/github/benchjava/bench/MemoryExtensionsBenchmark.java
- Networking — src/main/java/io/github/benchjava/bench/NetworkingBenchmark.java
- Peanut Butter (assorted micro-ops) — src/main/java/io/github/benchjava/bench/PeanutButterBenchmark.java
- Reflection — src/main/java/io/github/benchjava/bench/ReflectionBenchmark.java
  - Methods: ParseAndGetName
- Regular expressions and strings — src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java
  - Methods: Count
- SearchValues (vectorized search) — src/main/java/io/github/benchjava/bench/SearchValuesBenchmark.java
- Searching (algorithms and data structures) — src/main/java/io/github/benchjava/bench/SearchingBenchmark.java
- Streams/LINQ-like patterns — src/main/java/io/github/benchjava/bench/StreamsBenchmark.java

Notes about parity with the blog
- These Java benchmarks are analogues inspired by the blog’s test areas, not literal ports of .NET code.
- Names are intentionally similar so you can correlate sections easily.
- If you need to run only a subset, use BENCH_INCLUDE with the names above (e.g., BENCH_INCLUDE="Numerics.*|Json.*").

Tips for stable numbers
- Close other apps; set CPU governor to performance if possible.
- Increase warmup/measurement iterations and forks (JMH_OPTS) for less variance.
- Keep JVM flags consistent between runs unless intentionally testing them.


Coverage plan from the .NET 10 performance blog (classes and functions to write)
- Goal: ensure we cover every benchmark snippet (or a faithful Java analogue) from the blog. We will use the same method names when shown in the blog and group them into Java classes by section. Items marked [done] already exist in this repo.

Primitives and Numerics → class NumericsBenchmark [exists]
- Divide [done]
- TryWriteBytes [done]
- ParseInt32Min [done]
- DecrementManual [done]
- DecrementTP [done]
- GetFutureTime (DateTimeOffset micro-optimization snippet in blog) → method GetFutureTime
- Guid parsing from UTF8 bytes vs transcode → methods TranscodeParse, Utf8ParserParse, GuidParse
- Version parsing from UTF8 bytes vs transcode → methods TranscodeParse_Version, VersionParse

BitArray → class BitArrayBenchmark [exists]
- HammingDistanceManual [done]
- HammingDistanceTensorPrimitives [done]
- ByteCtor [done]

Collections → class CollectionsBenchmark [exists]
- InsertRange (List<T>.InsertRange optimized growth) [done]

Other Collections → class OtherCollectionsBenchmark
- FrozenDictionary alternate-lookup example → method Get (blog shows lookup via alternate lookup)

Regex → class RegexAndStringBenchmark [exists]
- Count (count matches) [done]

Reflection → class ReflectionBenchmark [exists]
- ParseAndGetName (TypeName.Parse(t.FullName).FullName analogue) [done]

I/O → class IOBenchmark [exists]
- MMF (MemoryMappedFile create and accessor) → method MMF
- FSW (FileSystemWatcher setup) → method FSW
- WriteByte (BufferedStream.WriteByte fix) [done]

Cryptography → class CryptoBenchmark [exists]
- Hash (SHA256.HashData src→dst) [done]

Diagnostics → class DiagnosticsBenchmark
- Interpolate (string interpolation allocation reduction) → method Interpolate
- AssemblyQualifiedName caching analogue → method AssemblyQualifiedName (for Java, Class.getName() caching analogue; doc-only or simple method)

Peanut Butter (miscellaneous) → class PeanutButterBenchmark
- GCHandle vs PinnedGCHandle analogue → methods Old, New (Java analogue may use ByteBuffer direct vs array pinning; document as analogue)
- Convert hex UTF8 overloads analogue → methods FromHexStringUtf8, ToHexStringLowerUtf8

MemoryExtensions / SearchValues → class SearchValuesBenchmark
- SearchValues.ContainsAny / IndexOfAny style examples → methods ContainsAny, IndexOfAny (analogue using vectorized search where possible)

JSON → class JsonBenchmark
- System.Text.Json specific improvements (e.g., writer/reader) → methods Serialize, WithDeserialize (analogue with Jackson or Gson)

Networking → class NetworkingBenchmark
- TLS/HTTP client small improvements (not microbenchmarked in detail; optional) → method HttpGetSmall
  - Note: The .NET 10 blog does not include a benchmark named "HttpGetSmall". The Networking section microbenchmarks focus on IPAddress parsing and Uri path compression; TLS/HttpClient improvements are described qualitatively and validated via existing perf tests, not a dedicated microbenchmark.
  - Our HttpGetSmall is a Java analogue to exercise HTTPS/TLS + small HTTP GET overheads. If you want to mirror the blog’s methodology more closely, treat this as optional or run it against a stable local HTTPS endpoint.

Threading / Concurrency → classes ThreadingBenchmark, ConcurrencyBenchmark
- ThreadPool / Task scheduling examples (analogue with Java ForkJoinPool) → methods QueueWork, ScheduleMany
- Task.WhenAll allocation analogues → ThreadingBenchmark.WhenAllAlloc_Two, ThreadingBenchmark.WhenAllAlloc_One

Bounds Checking / Inlining / Constant Folding / Code Layout / Instruction Sets / VM → low-level JIT topics
- JitInliningBenchmark → methods PolymorphicCall, DevirtualizedCall
- CodeLayoutBenchmark → methods HotColdSplit (analogue via predictable branches)

Enumeration / LINQ / Streams → class StreamsBenchmark
- LINQ section methods: ShuffleTakeLinq, ShuffleTakeContainsLinq, LeftJoin_Linq

Diagnostics (logging analyzer CA1873)
- Expensive logging callsite guard → class LoggingBenchmark, method Guarded

Notes
- Some .NET snippets are platform/runtime-specific and don’t have direct Java equivalents. For those we include an "analogue" with a note in code comments and in docs.
- The authoritative source for names and intent is the blog itself: https://devblogs.microsoft.com/dotnet/performance-improvements-in-net-10/

Action plan
- Implement the missing classes/methods above in small PRs per section. Keep names identical to the blog’s snippet names when they exist (e.g., Divide, WriteByte, ParseAndGetName). Where a snippet shows multiple methods (e.g., TranscodeParse vs GuidParse), implement each as a separate @Benchmark.
- For areas with no precise Java equivalent, add an analogue with a clear note linking to the blog paragraph, and keep the method name the same to aid cross-referencing.



Where is the test code (benchmarks)
- All benchmarks live under: src/main/java/io/github/benchjava/bench
- Each class corresponds to a section in the .NET 10 blog and contains @Benchmark methods with blog-aligned names.
- Quick map (file → benchmark methods):
  - BitArrayBenchmark.java — HammingDistanceManual, HammingDistanceTensorPrimitives, ByteCtor
  - CollectionsBenchmark.java — InsertRange
  - CryptoBenchmark.java — Hash
  - DiagnosticsBenchmark.java — Interpolate, AssemblyQualifiedName, WithGetTimestamp, WithStartNew, Oops
  - IOBenchmark.java — WriteByte, MMF, FSW
  - JsonBenchmark.java — Serialize, WithDeserialize
  - NumericsBenchmark.java — Divide, TryWriteBytes, ParseInt32Min, GetFutureTime, GuidParse, TranscodeParse, Utf8ParserParse, VersionParse, TranscodeParse_Version, DecrementManual, DecrementTP
  - ReflectionBenchmark.java — ParseAndGetName
  - RegexAndStringBenchmark.java — Count
  - SearchValuesBenchmark.java — ContainsAny, IndexOfAny
  - StreamsBenchmark.java — ShuffleTakeLinq, ShuffleTakeContainsLinq, LeftJoin_Linq
  - OtherCollectionsBenchmark.java — Get
  - NetworkingBenchmark.java — CtorHost, Ctor, ResponseContentRead_ReadAsByteArrayAsync, ResponseHeadersRead_ReadAsStringAsync, Add, GetValues, HttpGetSmall
  - ThreadingBenchmark.java — WhenAllAlloc_Two, WhenAllAlloc_One
  - PeanutButterBenchmark.java — Old, New, FromHexStringUtf8, ToHexStringLowerUtf8

Blog reference inside repo
- Performance Improvements in .NET 10 - .NET Blog.html — local copy of the reference blog used for naming and rough parity.
