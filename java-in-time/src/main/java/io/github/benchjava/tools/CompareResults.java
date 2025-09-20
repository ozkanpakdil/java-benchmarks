package io.github.benchjava.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Compare two JMH JSON result files and print a Markdown table with per-benchmark ratios.
 * Usage:
 *   java -cp target/benchmarks.jar io.github.benchjava.tools.CompareResults path/to/A.json path/to/B.json
 * Notes: Lower is better for AverageTime benchmarks. Ratio = B / A.
 */
public class CompareResults {
    private static final DecimalFormat DF3 = new DecimalFormat("0.000");

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: CompareResults <baseline.json> <candidate.json>");
            System.exit(2);
        }
        File aFile = new File(args[0]);
        File bFile = new File(args[1]);

        ObjectMapper om = new ObjectMapper();
        List<JsonNode> a = om.readValue(aFile, new TypeReference<List<JsonNode>>(){});
        List<JsonNode> b = om.readValue(bFile, new TypeReference<List<JsonNode>>(){});

        Map<String, Entry> base = index(a);
        Map<String, Entry> cand = index(b);

        Set<String> names = new TreeSet<>();
        names.addAll(base.keySet());
        names.addAll(cand.keySet());

        System.out.println("| Benchmark | A (" + aFile.getName() + ") | B (" + bFile.getName() + ") | Ratio B/A | Unit |");
        System.out.println("|---|---:|---:|---:|---|");

        for (String name : names) {
            Entry ea = base.get(name);
            Entry eb = cand.get(name);
            String aStr = ea == null ? "-" : DF3.format(ea.score);
            String bStr = eb == null ? "-" : DF3.format(eb.score);
            String unit = eb != null ? eb.unit : (ea != null ? ea.unit : "-");
            String ratio;
            if (ea == null || eb == null || ea.score == 0.0) {
                ratio = "-";
            } else {
                ratio = DF3.format(eb.score / ea.score);
            }
            System.out.println("| " + name + " | " + aStr + " | " + bStr + " | " + ratio + " | " + unit + " |");
        }
    }

    private static Map<String, Entry> index(List<JsonNode> items) {
        Map<String, Entry> m = new HashMap<>();
        for (JsonNode n : items) {
            String bench = n.path("benchmark").asText();
            JsonNode pm = n.path("primaryMetric");
            double score = pm.path("score").asDouble(Double.NaN);
            String unit = pm.path("scoreUnit").asText();
            if (bench != null && !bench.isEmpty() && !Double.isNaN(score)) {
                m.put(bench, new Entry(score, unit));
            }
        }
        return m;
    }

    private record Entry(double score, String unit) {}
}
