package io.github.benchjava.bench;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class JsonBenchmark {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public record OrderItem(String sku, int qty, double price) {}

    public record Order(String id, String customer, List<OrderItem> items, long createdAt) {}

    @Param({"5", "20", "100"})
    public int itemsCount;

    private Order order;
    private String json;

    @Setup(Level.Trial)
    public void setup() throws JsonProcessingException {
        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < itemsCount; i++) items.add(new OrderItem("SKU-" + i, i + 1, i * 1.25));
        order = new Order("ORD-123", "Alice", items, System.currentTimeMillis());
        json = MAPPER.writeValueAsString(order);
    }

    // Blog JSON section — names aligned with blog snippet methods
    // Blog HTML: Serialize method around line ~8876
    @Benchmark
    public String Serialize() throws JsonProcessingException {
        return MAPPER.writeValueAsString(order);
    }

    // Blog HTML: WithDeserialize method around line ~9146 (uses JsonSerializer.Deserialize<JsonElement> in C#)
    // Java analogue: deserialize into Order using Jackson
    @Benchmark
    public Order WithDeserialize() throws JsonProcessingException {
        return MAPPER.readValue(json, Order.class);
    }
}
