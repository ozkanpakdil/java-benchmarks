package com.mascix;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BenchmarkSmokeTest {
    @Test
    public void testIntegerListSumSetup() {
        IntegerListSum benchmark = new IntegerListSum();
        assertDoesNotThrow(benchmark::setup);
    }

    @Test
    public void testIntegerListFilterSetup() {
        IntegerListFilter benchmark = new IntegerListFilter();
        assertDoesNotThrow(benchmark::setup);
    }
}