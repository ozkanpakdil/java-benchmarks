| Benchmark | A (graalvm-community-24.json) | B (graalvm-community-25.json) | Ratio B/A | Unit |
|---|---:|---:|---:|---|
| io.github.benchjava.bench.NumericsBenchmark.biginteger_mul | 21.221 | 20.357 | 0.959 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.bit_ops | 0.945 | 0.936 | 0.991 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.double_fma | 0.638 | 0.629 | 0.986 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.int_add | 0.598 | 0.592 | 0.991 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.long_mul | 0.631 | 0.624 | 0.989 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.regex_find_emails | 212214657.000 | 211307740.600 | 0.996 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.string_bytes_utf8 | 625.686 | 548.874 | 0.877 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_builder | 3.054 | 0.422 | 0.138 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_plus | 100.259 | 78.191 | 0.780 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_for_each | 11595.203 | 11501.942 | 0.992 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_loop | 6286.579 | 6305.127 | 1.003 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_parallel_stream | 18735.976 | 14693.447 | 0.784 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_stream | 9568.367 | 9174.075 | 0.959 | ns/op |
