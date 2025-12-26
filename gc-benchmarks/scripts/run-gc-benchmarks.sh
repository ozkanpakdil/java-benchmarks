#!/usr/bin/env bash
set -euo pipefail

# This script runs GC benchmarks with G1 and ZGC
# It assumes JAVA_HOME is set to a JDK that supports ZGC (JDK 15+)

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

RESULTS_DIR="$PROJECT_ROOT/results"
mkdir -p "$RESULTS_DIR"

mvn clean package -DskipTests

echo "Running with G1GC..."
java -XX:+UseG1GC -jar target/benchmarks.jar -rf json -rff "$RESULTS_DIR/g1gc.json"

echo "Running with ZGC..."
java -XX:+UseZGC -jar target/benchmarks.jar -rf json -rff "$RESULTS_DIR/zgc.json"

echo "Benchmarks finished. Results saved in $RESULTS_DIR"
