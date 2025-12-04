@echo off
rem compile.bat - compile sources using a specific JDK and JavaFX SDK and create app.jar
rem Adjust JDK_PATH below if your JDK is installed elsewhere.

set JDK_PATH="C:\Program Files\Java\jdk-17"
set JAVAC=%JDK_PATH%\bin\javac.exe"
set JAR=%JDK_PATH%\bin\jar.exe"

rem JavaFX relative path inside repo (adjust if needed)
set "JAVAFX_REL=javafx-backup\javafx-sdk-17.0.17\lib"

echo Using javac: %JAVAC%
echo Using jar: %JAR%
echo JavaFX lib: %JAVAFX_REL%

if not exist "%JAVAC%" (
  echo javac not found at %JAVAC%
  echo Please edit compile.bat and set JDK_PATH to your JDK installation.
  pause
  exit /b 1
)

if exist out rd /s /q out
mkdir out

rem create sources list
if exist sources.txt del /f /q sources.txt
for /r %%F in (*.java) do echo %%~fF>>sources.txt

if not exist sources.txt (
  echo No Java source files found.
  pause
  exit /b 1
)

rem compile with module-path to JavaFX
"%JAVAC%" --module-path "%CD%\%JAVAFX_REL%" --add-modules javafx.controls,javafx.fxml,javafx.media -d out @sources.txt
if errorlevel 1 (
  echo Compilation failed.
  pause
  exit /b 1
)

rem create manifest
echo Main-Class: Main>manifest.txt
echo.>>manifest.txt

rem create jar
"%JAR%" cfm app.jar manifest.txt -C out .
if errorlevel 1 (
  echo jar creation failed.
  pause
  exit /b 1
)

echo Created app.jar successfully.
pause
