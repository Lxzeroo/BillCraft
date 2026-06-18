#!/bin/bash
# Runs the compiled BillCraft application.
set -e

if [ ! -d "bin" ]; then
  echo "Project not built yet. Running build.sh first..."
  ./build.sh
fi

java -Dfile.encoding=UTF-8 -cp bin com.billcraft.BillCraftApp
