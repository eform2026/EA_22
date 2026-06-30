@echo off
cd /d "%~dp0"
echo CurrentDir=%cd%
if not exist gradlew.bat (
  echo gradlew.bat missing
  exit /b 1
)
call gradlew.bat assembleDebug --console=plain
exit /b %ERRORLEVEL%
