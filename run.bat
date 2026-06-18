@echo off
REM Runs the compiled BillCraft application.

if not exist bin (
  echo Project not built yet. Running build.bat first...
  call build.bat
)

java -Dfile.encoding=UTF-8 -cp bin com.billcraft.BillCraftApp
