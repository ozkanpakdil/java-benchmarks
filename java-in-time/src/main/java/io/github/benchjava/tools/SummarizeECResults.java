package io.github.benchjava.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class SummarizeECResults {
    private static final DecimalFormat DF1 = new DecimalFormat("0.0");
    private static final DecimalFormat DF3 = new DecimalFormat("0.000");

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: SummarizeECResults <result.json>");
            System.exit(1);
        }

        File file = new File(args[0]);
        ObjectMapper om = new ObjectMapper();
        List<JsonNode> results = om.readValue(file, new TypeReference<List<JsonNode>>() {});

        Map<String, Double> scores = new HashMap<>();
        Map<String, String> units = new HashMap<>();

        for (JsonNode node : results) {
            String benchmark = node.get("benchmark").asText();
            String shortName = benchmark.substring(benchmark.lastIndexOf('.') + 1);
            double score = node.get("primaryMetric").get("score").asDouble();
            String unit = node.get("primaryMetric").get("scoreUnit").asText();
            scores.put(shortName, score);
            units.put(shortName, unit);
        }

        System.out.println("## Common Data Structures Comparison (10M Operations)");
        System.out.println("Comparison between standard JDK and Eclipse Collections equivalents.");
        System.out.println();
        System.out.println("| Structure | Type | Insertion (10M) | Get (Random) |");
        System.out.println("|---|---|---|---|");

        printRow("ArrayList", "JDK", "arrayListAdd", "arrayListGet", scores, units);
        printRow("MutableList (FastList)", "EC", "ecMutableListAdd", "ecMutableListGet", scores, units);
        printRow("HashMap", "JDK", "hashMapPut", "hashMapGet", scores, units);
        printRow("MutableMap (UnifiedMap)", "EC", "ecMutableMapPut", "ecMutableMapGet", scores, units);
        printRow("TreeMap", "JDK", "treeMapPut", "treeMapGet", scores, units);
        printRow("TreeSortedMap", "EC", "ecTreeSortedMapPut", "ecTreeSortedMapGet", scores, units);
        printRow("LinkedList", "JDK", "linkedListAdd", "linkedListGet", scores, units);
        
        System.out.println();
        System.out.println("### Observations:");
        System.out.println("- Results generated from " + file.getName());
    }

    private static void printRow(String name, String type, String addBench, String getBench, Map<String, Double> scores, Map<String, String> units) {
        String addVal = format(scores.get(addBench), units.get(addBench));
        String getVal = format(scores.get(getBench), units.get(getBench));
        System.out.printf("| **%s** | %s | %s | %s |\n", name, type, addVal, getVal);
    }

    private static String format(Double score, String unit) {
        if (score == null) return "-";
        if ("ms/op".equals(unit)) {
            if (score < 0.1) {
                return "~" + DF1.format(score * 1_000_000) + " ns";
            }
            return "~" + (int) Math.round(score) + " ms";
        } else if ("ns/op".equals(unit)) {
            return "~" + DF1.format(score) + " ns";
        }
        return "~" + DF3.format(score) + " " + unit;
    }
}
