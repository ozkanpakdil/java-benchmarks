# Java 25 vs 24 performance

_Generated on 2025-09-21 from `zulu-24.json` (A) and `zulu-25.json` (B)._

**Overall:** Java 25 is faster overall (geomean B/A = 0.980, across 13 benchmarks; 5 A faster, 8 B faster).

## Benchmark-by-benchmark

For each benchmark below, lower is better (AverageTime). Values show B vs A with percentage difference.

- [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_builder](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 64.3% slower</span>  (A=0.381, B=0.626 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_parallel_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 37.7% faster</span>  (A=14197.618, B=8841.340 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.bit_ops](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 20.6% faster</span>  (A=0.985, B=0.781 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_plus](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 8.7% slower</span>  (A=112.436, B=122.241 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_loop](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 6.7% faster</span>  (A=4872.835, B=4548.109 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.int_add](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 5.5% slower</span>  (A=0.595, B=0.627 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 4.4% faster</span>  (A=7350.466, B=7026.567 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.string_bytes_utf8](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 3.0% faster</span>  (A=575.173, B=558.010 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_for_each](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 2.4% faster</span>  (A=5041.967, B=4922.781 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.double_fma](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 2.0% faster</span>  (A=0.640, B=0.627 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.biginteger_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 1.6% faster</span>  (A=22.288, B=21.924 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.regex_find_emails](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.5% slower</span>  (A=259371205.500, B=260699931.500 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.long_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.3% slower</span>  (A=0.624, B=0.626 ns/op)

_Legend: A = zulu-24.json (Java 24), B = zulu-25.json (Java 25). _
