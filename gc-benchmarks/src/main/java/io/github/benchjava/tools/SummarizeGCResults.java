package io.github.benchjava.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class SummarizeGCResults {
    private static final DecimalFormat DF = new DecimalFormat("0.000");

    private static List<JsonNode> readJson(File f, ObjectMapper om) throws IOException {
        try {
            return om.readValue(f, new TypeReference<List<JsonNode>>() {});
        } catch (Exception e) {
            System.err.println("Warning: Failed to read " + f.getName() + " as a single JSON array: " + e.getMessage());
            List<JsonNode> allNodes = new ArrayList<>();
            List<JsonNode> finalNodes = allNodes;
            try {
                om.readValues(om.createParser(f), JsonNode.class).forEachRemaining(node -> {
                    if (node.isArray()) {
                        node.forEach(finalNodes::add);
                    } else {
                        finalNodes.add(node);
                    }
                });
                if (!allNodes.isEmpty()) {
                    System.err.println("Successfully recovered " + allNodes.size() + " entries from " + f.getName());
                    return allNodes;
                }
            } catch (Exception e2) {
                System.err.println("Failed to recover " + f.getName() + ": " + e2.getMessage());
            }
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: SummarizeGCResults <g1gc-result.json> <zgc-result.json>");
            System.exit(1);
        }

        ObjectMapper om = new ObjectMapper();
        List<JsonNode> g1Results = readJson(new File(args[0]), om);
        List<JsonNode> zgcResults = readJson(new File(args[1]), om);

        Map<String, JsonNode> g1Map = index(g1Results);
        Map<String, JsonNode> zgcMap = index(zgcResults);

        System.out.println("# GC Comparison: G1 vs ZGC");
        System.out.println("Comparison of throughput between G1 and ZGC collectors. Higher scores (ops/s) are better.");
        System.out.println();
        System.out.println("| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) | Winner | Improvement |");
        System.out.println("|---|---|---|---|---|---|");

        Set<String> allBenchmarks = new TreeSet<>(g1Map.keySet());
        allBenchmarks.addAll(zgcMap.keySet());

        int g1Wins = 0;
        int zgcWins = 0;

        for (String benchmark : allBenchmarks) {
            JsonNode g1 = g1Map.get(benchmark);
            JsonNode zgc = zgcMap.get(benchmark);

            String g1ScoreStr = formatScore(g1);
            String zgcScoreStr = formatScore(zgc);
            String ratio = formatRatio(g1, zgc);

            double g1Score = g1 != null ? g1.get("primaryMetric").get("score").asDouble() : 0;
            double zgcScore = zgc != null ? zgc.get("primaryMetric").get("score").asDouble() : 0;

            String winner = "-";
            String improvement = "-";
            if (g1Score > 0 && zgcScore > 0) {
                if (zgcScore > g1Score) {
                    winner = "**ZGC**";
                    improvement = "+" + DF.format((zgcScore / g1Score - 1) * 100) + "%";
                    zgcWins++;
                } else if (g1Score > zgcScore) {
                    winner = "**G1**";
                    improvement = "+" + DF.format((g1Score / zgcScore - 1) * 100) + "%";
                    g1Wins++;
                } else {
                    winner = "Tie";
                    improvement = "0%";
                }
            }

            System.out.printf("| %s | %s | %s | %s | %s | %s |\n", benchmark, g1ScoreStr, zgcScoreStr, ratio, winner, improvement);
        }

        System.out.println();
        System.out.println("## Summary");
        if (zgcWins > g1Wins) {
            System.out.println("Overall, **ZGC** performed better in this suite, winning " + zgcWins + " out of " + (g1Wins + zgcWins) + " tests.");
        } else if (g1Wins > zgcWins) {
            System.out.println("Overall, **G1** performed better in this suite, winning " + g1Wins + " out of " + (g1Wins + zgcWins) + " tests.");
        } else {
            System.out.println("Overall, it's a tie between G1 and ZGC in this suite.");
        }
        System.out.println();
        System.out.println("### Key Differences");
        System.out.println("- **G1 GC**: Traditional generational collector, balanced throughput and latency. Default in most JDKs.");
        System.out.println("- **ZGC**: Low-latency scalable collector, designed for sub-millisecond pauses even with large heaps.");
        System.out.println();
        System.out.println("## Benchmarks Description");
        System.out.println();
        System.out.println("### `allocateShortLived` (Throughput)");
        System.out.println("Allocates many short-lived `String` objects to a `List`. This benchmark tests how efficiently the GC handles high allocation rates of small objects. High throughput here means the GC can keep up with rapid allocations without excessive stalling.");
        System.out.println();
        System.out.println("### `allocateLargeObjects` (Throughput)");
        System.out.println("Allocates 1MB byte arrays repeatedly. This benchmark tests how the GC handles large object allocations (Humongous objects in G1). G1 might struggle more with these as they require contiguous regions.");
    }

    private static Map<String, JsonNode> index(List<JsonNode> results) {
        Map<String, JsonNode> map = new LinkedHashMap<>();
        for (JsonNode node : results) {
            String benchmark = node.get("benchmark").asText();
            String name = benchmark.substring(benchmark.lastIndexOf('.') + 1);
            String params = "";
            if (node.has("params")) {
                JsonNode p = node.get("params");
                params = " (" + p.toString().replace("\"", "").replace("{", "").replace("}", "") + ")";
            }
            map.put(name + params, node);
        }
        return map;
    }

    private static String formatScore(JsonNode node) {
        if (node == null) return "-";
        double score = node.get("primaryMetric").get("score").asDouble();
        String unit = node.get("primaryMetric").get("scoreUnit").asText();
        return DF.format(score) + " " + unit;
    }

    private static String formatRatio(JsonNode g1, JsonNode zgc) {
        if (g1 == null || zgc == null) return "-";
        double g1Score = g1.get("primaryMetric").get("score").asDouble();
        double zgcScore = zgc.get("primaryMetric").get("score").asDouble();
        if (g1Score == 0) return "-";
        double ratio = zgcScore / g1Score;
        return DF.format(ratio) + "x";
    }
}
