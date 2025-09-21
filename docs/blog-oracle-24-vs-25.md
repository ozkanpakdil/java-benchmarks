# Java 25 vs 24 performance

_Generated on 2025-09-21 from `oracle-24.json` (A) and `oracle-25.json` (B)._

**Overall:** Java 24 is faster overall (geomean B/A = 1.009, across 13 benchmarks; 5 A faster, 8 B faster).

## Benchmark-by-benchmark

For each benchmark below, lower is better (AverageTime). Values show B vs A with percentage difference.

- [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_builder](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 61.5% slower</span>  (A=0.388, B=0.626 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.bit_ops](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 17.1% faster</span>  (A=0.941, B=0.780 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_parallel_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 7.8% faster</span>  (A=14064.775, B=12968.794 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_plus](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 7.7% slower</span>  (A=113.338, B=122.084 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_loop](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 7.3% faster</span>  (A=4911.796, B=4552.423 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 5.4% faster</span>  (A=7248.577, B=6858.735 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.string_bytes_utf8](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 5.1% faster</span>  (A=572.275, B=542.868 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.int_add](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 4.9% slower</span>  (A=0.596, B=0.626 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.biginteger_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 2.5% faster</span>  (A=22.460, B=21.893 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.double_fma](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 1.3% faster</span>  (A=0.635, B=0.626 ns/op)
- [io.github.benchjava.bench.RegexAndStringBenchmark.regex_find_emails](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.6% slower</span>  (A=259414042.750, B=260897142.250 ns/op)
- [io.github.benchjava.bench.NumericsBenchmark.long_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java): <span style="color:#c5221f;font-weight:600">Java 24 is 0.3% slower</span>  (A=0.625, B=0.627 ns/op)
- [io.github.benchjava.bench.StreamsBenchmark.sum_for_each](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java): <span style="color:#137333;font-weight:600">Java 25 is 0.2% faster</span>  (A=4976.348, B=4968.625 ns/op)

## All benchmarks (including single-sided)

| Benchmark | A (oracle-24.json) | B (oracle-25.json) | Unit |
|---|---:|---:|---|
| [io.github.benchjava.bench.NumericsBenchmark.biginteger_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java) | 22.460 | 21.893 | ns/op |
| [io.github.benchjava.bench.NumericsBenchmark.bit_ops](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java) | 0.941 | 0.780 | ns/op |
| [io.github.benchjava.bench.NumericsBenchmark.double_fma](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java) | 0.635 | 0.626 | ns/op |
| [io.github.benchjava.bench.NumericsBenchmark.int_add](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java) | 0.596 | 0.626 | ns/op |
| [io.github.benchjava.bench.NumericsBenchmark.long_mul](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/NumericsBenchmark.java) | 0.625 | 0.627 | ns/op |
| [io.github.benchjava.bench.RegexAndStringBenchmark.regex_find_emails](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java) | 259414042.750 | 260897142.250 | ns/op |
| [io.github.benchjava.bench.RegexAndStringBenchmark.string_bytes_utf8](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java) | 572.275 | 542.868 | ns/op |
| [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_builder](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java) | 0.388 | 0.626 | ns/op |
| [io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_plus](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/RegexAndStringBenchmark.java) | 113.338 | 122.084 | ns/op |
| [io.github.benchjava.bench.StreamsBenchmark.sum_for_each](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java) | 4976.348 | 4968.625 | ns/op |
| [io.github.benchjava.bench.StreamsBenchmark.sum_loop](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java) | 4911.796 | 4552.423 | ns/op |
| [io.github.benchjava.bench.StreamsBenchmark.sum_parallel_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java) | 14064.775 | 12968.794 | ns/op |
| [io.github.benchjava.bench.StreamsBenchmark.sum_stream](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/java-in-time/src/main/java/io/github/benchjava/bench/StreamsBenchmark.java) | 7248.577 | 6858.735 | ns/op |

_Legend: A = oracle-24.json (Java 24), B = oracle-25.json (Java 25). _
