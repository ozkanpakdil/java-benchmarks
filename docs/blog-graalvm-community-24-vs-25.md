# Java 25 vs 24 performance

_Generated on 2025-09-20 from `graalvm-community-24.json` (A) and `graalvm-community-25.json` (B)._

**Overall:** Java 25 is faster overall (geomean B/A = 0.818, across 13 benchmarks; 4 A faster, 9 B faster).

## Benchmark-by-benchmark

For each benchmark below, lower is better (AverageTime). Values show B vs A with percentage difference.

- [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_builder](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 86.4% faster</span>  (A=3.105, B=0.422 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_plus](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 22.6% faster</span>  (A=100.960, B=78.191 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_parallel_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 15.0% faster</span>  (A=17282.271, B=14693.447 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.string_bytes_utf8](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 10.1% faster</span>  (A=610.574, B=548.874 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 5.4% faster</span>  (A=9701.661, B=9174.075 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.biginteger_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 3.2% faster</span>  (A=21.032, B=20.357 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.int_add](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.8% slower</span>  (A=0.588, B=0.592 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.bit_ops](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 0.7% faster</span>  (A=0.943, B=0.936 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.regex_find_emails](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.5% slower</span>  (A=210347528.600, B=211307740.600 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.long_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 0.2% faster</span>  (A=0.626, B=0.624 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_loop](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.2% slower</span>  (A=6291.424, B=6305.127 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_for_each](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 0.2% faster</span>  (A=11525.297, B=11501.942 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.double_fma](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.1% slower</span>  (A=0.628, B=0.629 ns/op)

_Legend: A = graalvm-community-24.json (Java 24), B = graalvm-community-25.json (Java 25). _
