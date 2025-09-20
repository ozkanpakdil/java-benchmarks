package com.mascix;

import org.junit.jupiter.api.Test;

import java.util.Map;

public class PrintSome {
    @Test
    public void printEnv(){
        System.out.println("ENV VARS-----------------------------------------------");
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
    }
}
