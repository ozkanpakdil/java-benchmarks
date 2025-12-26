#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   scripts/run-benchmarks.sh label1:/path/to/jdk1 label2:/path/to/jdk2 ...
# Optional env:
#   PROFILES="java-17"   # Maven profile to use for --release (defaults to matching the JDK major)
#   BENCH_INCLUDE="Regex.*|Json.*"  # Regex of benchmarks to include (JMH include pattern)
#   JMH_OPTS="-wi 5 -i 10 -bm avgt -tu ns"  # Extra JMH options
#   MAVEN_ARGS="-q -DskipTests"  # Extra Maven args
#
# Results are stored under results/<label>.json and results/<label>.txt (under project root)

# Default JMH options (overridable via env). Keep CI fast but results less stable.
JMH_OPTS="${JMH_OPTS:-"-wi 2 -i 2 -f 2 -r 1s -w 1s -bm avgt -tu ns"}"


# Resolve script dir and project root so the script can be run from anywhere
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

if [[ $# -lt 1 ]]; then
  echo "Provide at least one argument as label:JAVA_HOME path"
  echo "Example: scripts/run-benchmarks.sh java25:/usr/lib/jvm/jdk-25 javaF24:/usr/lib/jvm/jdk-24"
  exit 1
fi

RESULTS_DIR="$PROJECT_ROOT/results"
mkdir -p "$RESULTS_DIR"

for pair in "$@"; do
  label="${pair%%:*}"
  jhome_raw="${pair#*:}"
  if [[ -z "$label" || -z "$jhome_raw" ]]; then
    echo "Bad arg: $pair (expected label:JAVA_HOME)" >&2
    exit 2
  fi
  # Expand leading ~ to $HOME and strip any trailing slash
  jhome="${jhome_raw/#\~/$HOME}"
  jhome="${jhome%/}"

  if [[ ! -x "$jhome/bin/java" ]]; then
    echo "JAVA not found at $jhome/bin/java" >&2
    exit 3
  fi

  echo -e "\n=== Building with $label ($jhome) ==="
  export JAVA_HOME="$jhome"
  export PATH="$JAVA_HOME/bin:$PATH"

  # Determine major version for profile default
  major=$("$JAVA_HOME/bin/java" -Xms16m -Xmx16m -version 2>&1 | awk -F'[ \"\+]+' '/version/{print $3}' | sed -E 's/^1\.([0-9]+)$/\1/')
  profile_from_jdk="java-$major"

  mvn clean package ${MAVEN_ARGS:-} -P"${PROFILES:-$profile_from_jdk}"

  echo -e "\n=== Running JMH for $label ==="
  out_json="$RESULTS_DIR/${label}.json"
  out_txt="$RESULTS_DIR/${label}.txt"

  # Build runtime classpath and run JMH via classpath to avoid shaded-jar META-INF issues
  mvn -q -DskipTests dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
  CP="target/classes:$(cat target/classpath.txt)"
  # shellcheck disable=SC2086
  java -cp "$CP" org.openjdk.jmh.Main -rf json -rff "$out_json" ${JMH_OPTS:-} ${BENCH_EXTRA_OPTS:-} ${BENCH_INCLUDE:-} | tee "$out_txt"
  echo "Saved: $out_json and $out_txt"

done

echo "All runs finished."
