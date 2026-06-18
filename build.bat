@echo off
REM Compiles BillCraft into the bin\ directory.

if not exist bin mkdir bin
dir /s /b src\main\java\*.java > sources.txt
javac -d bin -encoding UTF-8 @sources.txt
del sources.txt

echo Build successful. Run with: run.bat
