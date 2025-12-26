package io.github.benchjava.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CompareResultsTest {
    @Test
    public void testLinkify() {
        String link = CompareResults.linkify("io.github.benchjava.bench.NetworkingBenchmark.test");
        assertNotNull(link);
    }
}