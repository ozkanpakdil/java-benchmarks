package io.github.benchjava.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Compare JMH JSON result files and print Markdown tables.
 *
 * Usage:
 *   - Two files (baseline vs candidate with ratio):
 *       java -cp target/benchmarks.jar io.github.benchjava.tools.CompareResults A.json B.json
 *   - Many files (big table with all JSONs, no ratios):
 *       java -cp target/benchmarks.jar io.github.benchjava.tools.CompareResults A.json B.json C.json ...
 *
 * Notes: Lower is better for AverageTime benchmarks. Ratio = B / A for the two-file mode.
 */
public class CompareResults {
    private static final DecimalFormat DF3 = new DecimalFormat("0.000");

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: CompareResults <A.json> <B.json> [<C.json> ...]");
            System.exit(2);
        }

        if (args.length == 2) {
            compareTwo(args[0], args[1]);
        } else {
            compareMany(args);
        }
    }

    private static void compareTwo(String aPath, String bPath) throws IOException {
        File aFile = new File(aPath);
        File bFile = new File(bPath);

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

    private static void compareMany(String[] paths) throws IOException {
        ObjectMapper om = new ObjectMapper();

        // Read and index each file
        List<File> files = new ArrayList<>();
        List<Map<String, Entry>> indexed = new ArrayList<>();
        Set<String> allNames = new TreeSet<>();
        for (String p : paths) {
            File f = new File(p);
            files.add(f);
            List<JsonNode> nodes = om.readValue(f, new TypeReference<List<JsonNode>>(){});
            Map<String, Entry> m = index(nodes);
            indexed.add(m);
            allNames.addAll(m.keySet());
        }

        // Header
        StringBuilder hdr = new StringBuilder("| Benchmark ");
        for (File f : files) {
            hdr.append("| ").append(f.getName()).append(" ");
        }
        hdr.append("| Unit |");
        System.out.println(hdr);

        // Separator
        StringBuilder sep = new StringBuilder("|---");
        for (int i = 0; i < files.size(); i++) {
            sep.append("|---:");
        }
        sep.append("|---|");
        System.out.println(sep);

        // Rows
        for (String name : allNames) {
            StringBuilder row = new StringBuilder();
            row.append("| ").append(name).append(" ");
            String unit = "-";
            for (Map<String, Entry> m : indexed) {
                Entry e = m.get(name);
                if (e == null) {
                    row.append("| - ");
                } else {
                    row.append("| ").append(DF3.format(e.score)).append(" ");
                    unit = e.unit != null ? e.unit : unit;
                }
            }
            row.append("| ").append(unit).append(" |");
            System.out.println(row);
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
