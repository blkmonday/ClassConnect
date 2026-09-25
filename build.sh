#!/usr/bin/env bash
set -euo pipefail

# Build helper: prefers Maven, falls back to javac with bundled Gson jar.
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR"

if command -v mvn >/dev/null 2>&1; then
  echo "Building with Maven..."
  mvn -f backend/pom.xml dependency:copy-dependencies package -DskipTests
  echo "Running..."
  java -cp "backend/target/classes:backend/target/dependency/*" backend.Main
else
  echo "Maven not found; compiling backend/src/main/java with javac and using bundled gson jar..."
  mkdir -p backend/out
  javac -cp "backend/lib/*" -d backend/out $(find backend/src/main/java -name "*.java")
  java -cp 'backend/out:backend/lib/*' backend.Main
fi
