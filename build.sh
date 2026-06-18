#!/bin/bash
# Compiles BillCraft into the bin/ directory.
set -e

SRC_DIR="src/main/java"
OUT_DIR="bin"

mkdir -p "$OUT_DIR"
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$OUT_DIR" -encoding UTF-8 @sources.txt
rm sources.txt

echo "Build successful. Run with: ./run.sh"
