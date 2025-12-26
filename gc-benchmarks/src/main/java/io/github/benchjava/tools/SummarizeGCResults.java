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

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: SummarizeGCResults <g1gc-result.json> <zgc-result.json>");
            System.exit(1);
        }

        ObjectMapper om = new ObjectMapper();
        List<JsonNode> g1Results = om.readValue(new File(args[0]), new TypeReference<List<JsonNode>>() {});
        List<JsonNode> zgcResults = om.readValue(new File(args[1]), new TypeReference<List<JsonNode>>() {});

        Map<String, JsonNode> g1Map = index(g1Results);
        Map<String, JsonNode> zgcMap = index(zgcResults);

        System.out.println("# GC Comparison: G1 vs ZGC");
        System.out.println("Comparison of throughput between G1 and ZGC collectors.");
        System.out.println();
        System.out.println("| Benchmark | G1 Score | ZGC Score | Ratio (ZGC/G1) |");
        System.out.println("|---|---|---|---|");

        Set<String> allBenchmarks = new TreeSet<>(g1Map.keySet());
        allBenchmarks.addAll(zgcMap.keySet());

        for (String benchmark : allBenchmarks) {
            JsonNode g1 = g1Map.get(benchmark);
            JsonNode zgc = zgcMap.get(benchmark);

            String g1Score = formatScore(g1);
            String zgcScore = formatScore(zgc);
            String ratio = formatRatio(g1, zgc);

            System.out.printf("| %s | %s | %s | %s |\n", benchmark, g1Score, zgcScore, ratio);
        }
    }

    private static Map<String, JsonNode> index(List<JsonNode> results) {
        Map<String, JsonNode> map = new HashMap<>();
        for (JsonNode node : results) {
            String benchmark = node.get("benchmark").asText();
            String params = "";
            if (node.has("params")) {
                params = node.get("params").toString();
            }
            map.put(benchmark.substring(benchmark.lastIndexOf('.') + 1) + params, node);
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
