| Benchmark | A (graalvm-community-17.json) | B (graalvm-community-24.json) | Ratio B/A | Unit |
|---|---:|---:|---:|---|
| io.github.benchjava.bench.NumericsBenchmark.biginteger_mul | 21.167 | 21.221 | 1.003 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.bit_ops | 0.791 | 0.945 | 1.194 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.double_fma | 0.634 | 0.638 | 1.005 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.int_add | 0.594 | 0.598 | 1.006 | ns/op |
| io.github.benchjava.bench.NumericsBenchmark.long_mul | 0.629 | 0.631 | 1.004 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.regex_find_emails | 220084207.200 | 212214657.000 | 0.964 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.string_bytes_utf8 | 747.166 | 625.686 | 0.837 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_builder | 44.484 | 3.054 | 0.069 | ns/op |
| io.github.benchjava.bench.RegexAndStringBenchmark.string_concat_plus | 96.598 | 100.259 | 1.038 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_for_each | 10716.554 | 11595.203 | 1.082 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_loop | 7005.883 | 6286.579 | 0.897 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_parallel_stream | 22166.709 | 18735.976 | 0.845 | ns/op |
| io.github.benchjava.bench.StreamsBenchmark.sum_stream | 8725.455 | 9568.367 | 1.097 | ns/op |
