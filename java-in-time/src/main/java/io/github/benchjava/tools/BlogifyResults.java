package io.github.benchjava.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Generate a blog-style markdown summary comparing two JMH JSON files.
 * <p>
 * Usage:
 * java -cp target/benchmarks.jar io.github.benchjava.tools.BlogifyResults A.json B.json
 * <p>
 * It prints a Markdown document with:
 * - Title and date
 * - Overall geometric-mean summary
 * - Per-benchmark bullets saying which side is faster/slower and by how much
 * <p>
 * Notes: Lower is better for AverageTime benchmarks. Ratio = B / A.
 */
public class BlogifyResults {
    private static final DecimalFormat DF3 = new DecimalFormat("0.000");
    private static final DecimalFormat DF1P = new DecimalFormat("0.0");

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: BlogifyResults <A.json> <B.json>");
            System.exit(2);
        }
        blogify(args[0], args[1]);
    }

    private static void blogify(String aPath, String bPath) throws IOException {
        File aFile = new File(aPath);
        File bFile = new File(bPath);
        String aName = aFile.getName();
        String bName = bFile.getName();

        ObjectMapper om = new ObjectMapper();
        List<JsonNode> a = om.readValue(aFile, new TypeReference<>() {
        });
        List<JsonNode> b = om.readValue(bFile, new TypeReference<>() {
        });

        Map<String, Entry> base = index(a);
        Map<String, Entry> cand = index(b);

        // Common benchmarks only
        Set<String> common = new TreeSet<>(base.keySet());
        common.retainAll(cand.keySet());

        // Parse versions from filenames if possible
        String aVer = parseVersion(aName);
        String bVer = parseVersion(bName);

        // Title
        System.out.println("# Java " + (bVer != null ? bVer : "B") + " vs " + (aVer != null ? aVer : "A") + " performance");
        System.out.println();
        System.out.println("_Generated on " + LocalDate.now() + " from `" + aName + "` (A) and `" + bName + "` (B)._\n");

        // Overall geometric mean
        int n = 0;
        double sumLog = 0.0;
        int aWins = 0;
        int bWins = 0;
        for (String name : common) {
            Entry ea = base.get(name);
            Entry eb = cand.get(name);
            if (ea != null && eb != null && ea.score > 0 && eb.score > 0) {
                double r = eb.score / ea.score; // B/A
                if (Double.isFinite(r) && r > 0) {
                    sumLog += Math.log(r);
                    n++;
                    if (r > 1.0) aWins++;
                    else if (r < 1.0) bWins++;
                }
            }
        }
        if (n > 0) {
            double geo = Math.exp(sumLog / n);
            String faster;
            if (geo < 1.0) {
                faster = sideLabel(bVer, "B") + " is faster overall";
            } else if (geo > 1.0) {
                faster = sideLabel(aVer, "A") + " is faster overall";
            } else {
                faster = "A and B are tied overall";
            }
            System.out.println("**Overall:** " + faster + " (geomean B/A = " + DF3.format(geo) + ", across " + n + " benchmarks; " + aWins + " A faster, " + bWins + " B faster).\n");
        }

        // Per-benchmark bullets: sort by absolute percent difference (descending)
        List<Row> rows = new ArrayList<>();
        for (String name : common) {
            Entry ea = base.get(name);
            Entry eb = cand.get(name);
            if (ea == null || eb == null || ea.score <= 0 || eb.score <= 0) continue;
            double ratio = eb.score / ea.score; // B/A
            if (!Double.isFinite(ratio) || ratio <= 0) continue;
            double pct = (ratio - 1.0) * 100.0; // >0 => A faster (since lower is better)
            rows.add(new Row(name, ea.score, eb.score, ea.unit, pct));
        }
        rows.sort((r1, r2) -> Double.compare(Math.abs(r2.pct), Math.abs(r1.pct)));

        if (rows.isEmpty()) {
            System.out.println("No common benchmarks to compare.");
            return;
        }

        System.out.println("## Benchmark-by-benchmark\n");
        System.out.println("For each benchmark below, lower is better (AverageTime). Values show B vs A with percentage difference.\n");
        for (Row r : rows) {
            String link = linkify(r.name);
            boolean aFaster = r.pct > 0.0; // B/A>1 -> A faster
            boolean bFaster = r.pct < 0.0; // B/A<1 -> B faster
            String who = bFaster ? sideLabel(bVer, "B") : (aFaster ? sideLabel(aVer, "A") : "Tie");
            String color = bFaster ? "#137333" : (aFaster ? "#c5221f" : "#555");
            String pctStr = DF1P.format(Math.abs(r.pct)) + "%";
            String aVal = DF3.format(r.a);
            String bVal = DF3.format(r.b);
            String message;
            if ("Tie".equals(who)) {
                message = "no overall change";
            } else {
                // 'who' is the faster side, so always say 'faster'
                message = pctStr + " faster";
            }
            System.out.println("- " + link + ": <span style=\"color:" + color + ";font-weight:600\">" + who + ("Tie".equals(who) ? "" : " is ") + message + "</span> " +
                    " (A=" + aVal + ", B=" + bVal + " " + r.unit + ")");
        }

        // Additionally include a full table with ALL benchmarks (including those present in only one run)
        System.out.println();
        System.out.println("## All benchmarks (including single-sided)\n");
        System.out.println("| Benchmark | A (" + aName + ") | B (" + bName + ") | Unit |");
        System.out.println("|---|---:|---:|---|");
        // Build union of names
        Set<String> all = new TreeSet<>();
        all.addAll(base.keySet());
        all.addAll(cand.keySet());
        for (String name : all) {
            Entry ea = base.get(name);
            Entry eb = cand.get(name);
            String aVal = (ea == null) ? "-" : DF3.format(ea.score);
            String bVal = (eb == null) ? "-" : DF3.format(eb.score);
            String unit = (eb != null) ? eb.unit : (ea != null ? ea.unit : "-");
            String link = linkify(name);
            System.out.println("| " + link + " | " + aVal + " | " + bVal + " | " + unit + " |");
        }

        System.out.println();
        System.out.println("_Legend: A = " + aName + (aVer != null ? " (Java " + aVer + ")" : "") + ", B = " + bName + (bVer != null ? " (Java " + bVer + ")" : "") + ". _");
    }

    private static String sideLabel(String ver, String fallback) {
        return ver != null ? ("Java " + ver) : fallback;
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

    private static String parseVersion(String fileName) {
        // Expect filenames like temurin-25.json, oracle-24.json
        int dash = fileName.lastIndexOf('-');
        int dot = fileName.lastIndexOf('.');
        if (dash >= 0 && dot > dash) {
            String ver = fileName.substring(dash + 1, dot);
            if (ver.matches("[0-9]+")) return ver;
        }
        return null;
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
            moduleBase = "java-in-time/src/main/java/";
        }
        String path = moduleBase + className.replace('.', '/') + ".java";
        String url = "https://github.com/" + repo + "/blob/" + branch + "/" + path;
        return "[" + label + "](" + url + ")";
    }

    private record Entry(double score, String unit) {
    }

    private record Row(String name, double a, double b, String unit, double pct) {
    }
}
