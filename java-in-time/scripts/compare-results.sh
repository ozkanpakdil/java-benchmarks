#!/usr/bin/env bash
# Wrapper that uses the Java comparator (no Python required).
# Usage:
#   scripts/compare-results.sh <baseline.json> <challenger.json> > docs/compare.md
# Or use the Java tool directly:
#   java -cp target/benchmarks.jar io.github.benchjava.tools.CompareResults A.json B.json > compare.md

set -euo pipefail

# Resolve script dir and project root so the script can be run from anywhere
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

if [[ $# -ne 2 ]]; then
  echo "Usage: scripts/compare-results.sh <baseline.json> <challenger.json>" >&2
  exit 1
fi

BASE="$1"
CHAL="$2"

if [[ ! -f "$BASE" ]]; then
  echo "Baseline file not found: $BASE" >&2
  exit 2
fi
if [[ ! -f "$CHAL" ]]; then
  echo "Challenger file not found: $CHAL" >&2
  exit 3
fi

JAR_PATH="$PROJECT_ROOT/target/benchmarks.jar"

# Ensure the shaded jar exists
if [[ ! -f "$JAR_PATH" ]]; then
  echo "Building shaded jar ($JAR_PATH) ..." >&2
  (cd "$PROJECT_ROOT" && mvn -q -DskipTests package)
fi

exec java -cp "$JAR_PATH" io.github.benchjava.tools.CompareResults "$BASE" "$CHAL"
