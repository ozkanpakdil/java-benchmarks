#!/bin/bash
#
# Benchmark Runner Script
# This script runs the Eclipse Collections DataStructure benchmarks
# and optionally updates the documentation.
#
# Usage:
#   ./run-benchmarks-local.sh              # Run with default settings
#   ./run-benchmarks-local.sh --quick      # Quick run (fewer iterations)
#   ./run-benchmarks-local.sh --full       # Full run (more iterations, like CI)
#   ./run-benchmarks-local.sh --ci         # CI mode (used by GitHub Actions)
#   ./run-benchmarks-local.sh --help       # Show help
#
# CI Mode (called from GitHub Actions):
#   ./run-benchmarks-local.sh --ci --output "results/25-temurin.json"
#

set -e

# Colors for output (disabled in CI mode for cleaner logs)
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default settings
WARMUP_ITERATIONS=2
MEASUREMENT_ITERATIONS=3
FORKS=1
TIME_UNIT="ns"
BENCHMARK_PATTERN="DataStructure.*"
OUTPUT_FILE=""
CI_MODE=false

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EC_DIR="$SCRIPT_DIR/eclipse-collections"
JIT_DIR="$SCRIPT_DIR/java-in-time"

print_help() {
    echo "Benchmark Runner Script"
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --quick       Quick run with minimal iterations (wi=1, i=2)"
    echo "  --full        Full run with more iterations (wi=3, i=5)"
    echo "  --ci          CI mode for GitHub Actions (no colors, no doc update)"
    echo "  --pattern     JMH benchmark pattern (default: DataStructure.*)"
    echo "  --output      Output JSON file path (default: results/25-datastructure.json)"
    echo "  --no-update   Skip updating the documentation"
    echo "  --help        Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Default run"
    echo "  $0 --quick                            # Quick smoke test"
    echo "  $0 --full                             # Full benchmark run"
    echo "  $0 --pattern 'array*'                 # Run only array benchmarks"
    echo "  $0 --ci --output 'results/25-temurin.json'  # CI mode with custom output"
    echo ""
}

log_info() {
    if [ "$CI_MODE" = true ]; then
        echo "[INFO] $1"
    else
        echo -e "${BLUE}[INFO]${NC} $1"
    fi
}

log_success() {
    if [ "$CI_MODE" = true ]; then
        echo "[SUCCESS] $1"
    else
        echo -e "${GREEN}[SUCCESS]${NC} $1"
    fi
}

log_warn() {
    if [ "$CI_MODE" = true ]; then
        echo "[WARN] $1"
    else
        echo -e "${YELLOW}[WARN]${NC} $1"
    fi
}

log_error() {
    if [ "$CI_MODE" = true ]; then
        echo "[ERROR] $1"
    else
        echo -e "${RED}[ERROR]${NC} $1"
    fi
}

# Parse arguments
UPDATE_DOCS=true
while [[ $# -gt 0 ]]; do
    case $1 in
        --quick)
            WARMUP_ITERATIONS=1
            MEASUREMENT_ITERATIONS=2
            log_info "Quick mode: wi=$WARMUP_ITERATIONS, i=$MEASUREMENT_ITERATIONS"
            shift
            ;;
        --full)
            WARMUP_ITERATIONS=3
            MEASUREMENT_ITERATIONS=5
            log_info "Full mode: wi=$WARMUP_ITERATIONS, i=$MEASUREMENT_ITERATIONS"
            shift
            ;;
        --ci)
            CI_MODE=true
            UPDATE_DOCS=false
            # Disable colors in CI mode
            RED=''
            GREEN=''
            YELLOW=''
            BLUE=''
            NC=''
            shift
            ;;
        --pattern)
            BENCHMARK_PATTERN="$2"
            log_info "Benchmark pattern: $BENCHMARK_PATTERN"
            shift 2
            ;;
        --output)
            OUTPUT_FILE="$2"
            shift 2
            ;;
        --no-update)
            UPDATE_DOCS=false
            log_info "Documentation update disabled"
            shift
            ;;
        --help)
            print_help
            exit 0
            ;;
        *)
            log_error "Unknown option: $1"
            print_help
            exit 1
            ;;
    esac
done

# Set default output file if not specified
if [ -z "$OUTPUT_FILE" ]; then
    OUTPUT_FILE="results/25-datastructure.json"
fi

# Check Java version
log_info "Checking Java version..."
java -version 2>&1 | head -3
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
log_info "Detected Java major version: $JAVA_VERSION"

# Step 1: Build eclipse-collections benchmarks
log_info "Building eclipse-collections benchmarks..."
cd "$EC_DIR"
mvn -ntp clean package -DskipTests
if [ $? -ne 0 ]; then
    log_error "Failed to build eclipse-collections"
    exit 1
fi
log_success "eclipse-collections built successfully"

# Step 2: Create results directory
mkdir -p results

# Step 3: Run the benchmarks
log_info "Running benchmarks with pattern: $BENCHMARK_PATTERN"
log_info "Settings: warmup=$WARMUP_ITERATIONS, measurement=$MEASUREMENT_ITERATIONS, forks=$FORKS"
log_info "Output file: $OUTPUT_FILE"
echo ""

java -jar target/benchmarks.jar "$BENCHMARK_PATTERN" \
    -rf json \
    -rff "$OUTPUT_FILE" \
    -wi $WARMUP_ITERATIONS \
    -i $MEASUREMENT_ITERATIONS \
    -f $FORKS \
    -bm avgt \
    -tu $TIME_UNIT

if [ $? -ne 0 ]; then
    log_error "Benchmark run failed"
    exit 1
fi
log_success "Benchmarks completed. Results saved to: $EC_DIR/$OUTPUT_FILE"

# Step 4: Update documentation (optional, skipped in CI mode)
if [ "$UPDATE_DOCS" = true ]; then
    log_info "Building java-in-time tools..."
    cd "$JIT_DIR"
    mvn -ntp clean package -DskipTests
    if [ $? -ne 0 ]; then
        log_error "Failed to build java-in-time"
        exit 1
    fi
    
    log_info "Generating updated documentation..."
    java -cp target/benchmarks.jar io.github.benchjava.tools.SummarizeECResults \
        "$EC_DIR/$OUTPUT_FILE" > "$SCRIPT_DIR/docs/eclipse-collections.md.new"
    
    # Preserve the header from the original file
    if [ -f "$SCRIPT_DIR/docs/eclipse-collections.md" ]; then
        # Extract header (everything before "## Common Data Structures")
        head -n 32 "$SCRIPT_DIR/docs/eclipse-collections.md" > "$SCRIPT_DIR/docs/eclipse-collections.md.tmp"
        echo "" >> "$SCRIPT_DIR/docs/eclipse-collections.md.tmp"
        cat "$SCRIPT_DIR/docs/eclipse-collections.md.new" >> "$SCRIPT_DIR/docs/eclipse-collections.md.tmp"
        mv "$SCRIPT_DIR/docs/eclipse-collections.md.tmp" "$SCRIPT_DIR/docs/eclipse-collections.md"
        rm -f "$SCRIPT_DIR/docs/eclipse-collections.md.new"
    else
        mv "$SCRIPT_DIR/docs/eclipse-collections.md.new" "$SCRIPT_DIR/docs/eclipse-collections.md"
    fi
    
    log_success "Documentation updated: $SCRIPT_DIR/docs/eclipse-collections.md"
fi

echo ""
log_success "All done!"
echo ""
echo "Results file: $EC_DIR/$OUTPUT_FILE"
echo ""
if [ "$CI_MODE" = false ]; then
    echo "To view the results in JMH viewer, upload the JSON file to:"
    echo "  https://jmh.morethan.io/"
    echo ""
fi
