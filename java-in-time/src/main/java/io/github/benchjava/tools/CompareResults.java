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

        // Determine common and union of benchmarks
        Set<String> common = new TreeSet<>(base.keySet());
        common.retainAll(cand.keySet());
        Set<String> all = new TreeSet<>();
        all.addAll(base.keySet());
        all.addAll(cand.keySet());

        // Compute overall summary using geometric mean of ratios over common benchmarks
        int n = 0;
        double sumLog = 0.0;
        int aWinCount = 0;
        int bWinCount = 0;
        for (String name : common) {
            Entry ea = base.get(name);
            Entry eb = cand.get(name);
            if (ea != null && eb != null && ea.score > 0.0 && eb.score > 0.0) {
                double ratio = eb.score / ea.score; // B/A
                if (ratio > 0.0 && Double.isFinite(ratio)) {
                    sumLog += Math.log(ratio);
                    n++;
                    if (ratio > 1.0) aWinCount++; // lower is better
                    else if (ratio < 1.0) bWinCount++;
                }
            }
        }
        if (n > 0) {
            double geo = Math.exp(sumLog / n); // geometric mean of B/A
            String faster;
            String percent;
            if (geo < 1.0) {
                faster = "B (" + bFile.getName() + ") is faster overall";
                percent = DF3.format((1.0 - geo) * 100.0) + "% faster";
            } else if (geo > 1.0) {
                faster = "A (" + aFile.getName() + ") is faster overall";
                percent = DF3.format((geo - 1.0) * 100.0) + "% faster";
            } else {
                faster = "A and B are tied overall";
                percent = "0.000%";
            }
            System.out.println("**Overall result:** " + faster + " (geometric mean ratio B/A = " + DF3.format(geo) + ", " + percent + ", " + n + " benchmark(s)).");
            System.out.println();
        }

        System.out.println("| Benchmark | A (" + aFile.getName() + ") | B (" + bFile.getName() + ") | Ratio B/A | Winner | Unit |");
        System.out.println("|---|---:|---:|---:|:---:|---|");

        for (String name : all) {
            Entry ea = base.get(name);
            Entry eb = cand.get(name);
            String unit = eb != null ? eb.unit : (ea != null ? ea.unit : "-");

            Double aScore = (ea != null) ? ea.score : null;
            Double bScore = (eb != null) ? eb.score : null;
            double ratioVal = (aScore == null || bScore == null || aScore == 0.0) ? Double.NaN : (bScore / aScore);
            boolean aWins = (!Double.isNaN(ratioVal) && ratioVal > 1.0); // lower is better → B/A > 1 means A is faster
            boolean bWins = (!Double.isNaN(ratioVal) && ratioVal < 1.0);

            String aStr = (aScore == null) ? "-" : formatCell(aScore, aWins, bWins);
            String bStr = (bScore == null) ? "-" : formatCell(bScore, bWins, aWins);
            String ratio = Double.isNaN(ratioVal) ? "-" : formatRatio(ratioVal, aWins || bWins);
            String winner = (aScore == null || bScore == null) ? "-" : (aWins ? "A" : (bWins ? "B" : "-"));

            String link = linkify(name);
            System.out.println("| " + link + " | " + aStr + " | " + bStr + " | " + ratio + " | " + winner + " | " + unit + " |");
        }

        System.out.println();
        System.out.println("_Note: Benchmarks present in only one file are included with '-' on the missing side._");

        // Show counts and lists of benchmarks present only in A or only in B to make mismatches obvious
        Set<String> onlyA = new TreeSet<>(base.keySet());
        onlyA.removeAll(cand.keySet());
        Set<String> onlyB = new TreeSet<>(cand.keySet());
        onlyB.removeAll(base.keySet());

        if (!onlyA.isEmpty() || !onlyB.isEmpty()) {
            System.out.println();
            System.out.println("Only in A (" + aFile.getName() + "): " + onlyA.size());
            for (String name : onlyA) {
                System.out.println("- " + name);
            }
            System.out.println();
            System.out.println("Only in B (" + bFile.getName() + "): " + onlyB.size());
            for (String name : onlyB) {
                System.out.println("- " + name);
            }
            // Enforce strict matching: if any benchmark exists only on one side, fail the process
            System.out.println();
            System.err.println("ERROR: Benchmark set mismatch between files. All benchmarks should be present in both A and B.");
            System.err.println("Failing the build. Re-run benchmarks ensuring both JSONs are produced from the same commit and include filters.");
            System.exit(5);
        } else {
            // No mismatches → show who is faster counts instead of confusing Only-in=0 lines
            int ties = n - aWinCount - bWinCount;
            System.out.println();
            System.out.println("Summary: A faster: " + aWinCount + ", B faster: " + bWinCount + ", Ties: " + Math.max(0, ties) + ".");
        }
    }

    private static String formatCell(double value, boolean highlightWin, boolean highlightLose) {
        String v = DF3.format(value);
        if (highlightWin) {
            return "<span style=\"color:#137333;font-weight:600\">" + v + "</span>"; // green
        }
        if (highlightLose) {
            return "<span style=\"color:#c5221f\">" + v + "</span>"; // red
        }
        return v;
    }

    private static String formatRatio(double ratio, boolean highlight) {
        String v = DF3.format(ratio);
        if (highlight) {
            // Green if <1 (B faster), Red if >1 (A faster) – make it bold for emphasis
            String color = ratio < 1.0 ? "#137333" : "#c5221f";
            return "<span style=\"color:" + color + ";font-weight:600\">" + v + "</span>";
        }
        return v;
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
            row.append("| ").append(linkify(name)).append(" ");
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

    private static String linkify(String benchmarkName) {
        // Build URL to the declaring class, but use a short label "Class.Method"
        String className = benchmarkName;
        int lastDot = benchmarkName.lastIndexOf('.');
        if (lastDot > 0) {
            className = benchmarkName.substring(0, lastDot);
        }
        String method = (lastDot > 0) ? benchmarkName.substring(lastDot + 1) : benchmarkName;
        String classSimple;
        int prevDot = className.lastIndexOf('.');
        classSimple = (prevDot >= 0) ? className.substring(prevDot + 1) : className;
        String label = classSimple + "." + method;

        String repo = Optional.ofNullable(System.getenv("GITHUB_REPOSITORY")).orElse("ozkanpakdil/java-benchmarks");
        String branch = Optional.ofNullable(System.getenv("GITHUB_REF_NAME")).orElse("main");

        String moduleBase;
        if (className.startsWith("io.github.benchjava.bench")) {
            moduleBase = "java-in-time/src/main/java/";
        } else if (className.startsWith("com.mascix")) {
            moduleBase = "eclipse-collections/src/main/java/";
        } else {
            // Fallback to java-in-time module
            moduleBase = "java-in-time/src/main/java/";
        }
        String path = moduleBase + className.replace('.', '/') + ".java";
        String url = "https://github.com/" + repo + "/blob/" + branch + "/" + path;
        return "[" + label + "](" + url + ")";
    }

    private record Entry(double score, String unit) {}
}
