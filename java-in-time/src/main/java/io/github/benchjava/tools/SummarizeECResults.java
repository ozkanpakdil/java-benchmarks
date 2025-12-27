package io.github.benchjava.tools;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

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
        ObjectMapper om = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .build();
        List<JsonNode> resultsList;
        try {
            resultsList = om.readValue(file, new TypeReference<>() {
            });
        } catch (Exception e) {
            System.err.println("Warning: Failed to read " + file.getName() + " as a single JSON array: " + e.getMessage());
            resultsList = new ArrayList<>();
            List<JsonNode> finalResults = resultsList;
            try {
                om.readValues(om.createParser(file), JsonNode.class).forEachRemaining(node -> {
                    if (node.isArray()) {
                        node.forEach(finalResults::add);
                    } else {
                        finalResults.add(node);
                    }
                });
                if (!resultsList.isEmpty()) {
                    System.err.println("Successfully recovered " + resultsList.size() + " entries from " + file.getName());
                }
            } catch (Exception e2) {
                System.err.println("Failed to recover " + file.getName() + ": " + e2.getMessage());
                throw e;
            }
        }

        Map<String, Double> scores = new HashMap<>();
        Map<String, String> units = new HashMap<>();

        for (JsonNode node : resultsList) {
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
        System.out.println("📊 **[View Benchmark Source Code](https://github.com/ozkanpakdil/java-benchmarks/blob/develop/eclipse-collections/src/main/java/com/mascix/DataStructureBenchmark.java)**");
        System.out.println();
        
        System.out.println("### Quick Comparison");
        System.out.println("```text");
        System.out.println("HashMap vs TreeMap vs ArrayList vs LinkedList");
        System.out.println("HashMap.get()      → " + format10M(scores.get("hashMapGet"), units.get("hashMapGet")));
        System.out.println("TreeMap.get()      → " + format10M(scores.get("treeMapGet"), units.get("treeMapGet")));
        System.out.println("ArrayList.get(i)   → " + format10M(scores.get("arrayListGet"), units.get("arrayListGet")));
        System.out.println("LinkedList.get(i)  → " + format10M(scores.get("linkedListGet"), units.get("linkedListGet")));
        System.out.println("Insertion (10M elements):");
        System.out.println("ArrayList.add()    → " + format(scores.get("arrayListAdd"), units.get("arrayListAdd")));
        System.out.println("HashMap.put()      → " + format(scores.get("hashMapPut"), units.get("hashMapPut")));
        System.out.println("LinkedList.add()   → " + format(scores.get("linkedListAdd"), units.get("linkedListAdd")));
        System.out.println("```");
        System.out.println();

        System.out.println("### Detailed Comparison Table");
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
        System.out.println("- 'Get' operations are scaled to 10M operations for consistency with 'Insertion'.");
    }

    private static String format10M(Double scorePerOp, String unit) {
        if (scorePerOp == null) return "-";
        // scorePerOp is the time for ONE operation.
        // We want time for 10,000,000 operations.
        double totalScore = scorePerOp * 10_000_000;
        return format(totalScore, unit);
    }

    private static void printRow(String name, String type, String addBench, String getBench, Map<String, Double> scores, Map<String, String> units) {
        String addVal = format(scores.get(addBench), units.get(addBench));
        String getVal = format10M(scores.get(getBench), units.get(getBench));
        System.out.printf("| **%s** | %s | %s | %s |\n", name, type, addVal, getVal);
    }

    private static String format(Double score, String unit) {
        if (score == null) return "-";
        double scoreInMs;
        if ("ms/op".equals(unit)) {
            scoreInMs = score;
        } else if ("ns/op".equals(unit)) {
            scoreInMs = score / 1_000_000.0;
        } else if ("s/op".equals(unit)) {
            scoreInMs = score * 1000.0;
        } else {
            return "~" + DF3.format(score) + " " + unit;
        }

        // Always output in milliseconds for consistency
        if (scoreInMs < 1.0) {
            return "~" + DF3.format(scoreInMs) + " ms";
        } else if (scoreInMs < 1000) {
            return "~" + (int) Math.round(scoreInMs) + " ms";
        } else {
            // For large values, format with commas for readability
            return "~" + String.format("%,.0f", scoreInMs) + " ms";
        }
    }
}
