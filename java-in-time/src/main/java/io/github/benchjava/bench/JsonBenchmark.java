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

    public static class OrderItem {
        public String sku;
        public int qty;
        public double price;

        public OrderItem() {}
        public OrderItem(String sku, int qty, double price) {
            this.sku = sku; this.qty = qty; this.price = price;
        }
    }

    public static class Order {
        public String id;
        public String customer;
        public List<OrderItem> items;
        public long createdAt;

        public Order() {}
        public Order(String id, String customer, List<OrderItem> items, long createdAt) {
            this.id = id; this.customer = customer; this.items = items; this.createdAt = createdAt;
        }
    }

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

    @Benchmark
    public String serialize_jackson() throws JsonProcessingException {
        return MAPPER.writeValueAsString(order);
    }

    @Benchmark
    public Order deserialize_jackson() throws JsonProcessingException {
        return MAPPER.readValue(json, Order.class);
    }
}
