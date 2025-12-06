@echo off
setlocal
rem compile.bat - compile sources using a specific JDK and JavaFX SDK and create app.jar
rem You can optionally set JDK_PATH before running this script. If not set, JAVA_HOME will be used.

rem 1) Resolve JDK path
if not defined JDK_PATH (
  if defined JAVA_HOME (
    set "JDK_PATH=%JAVA_HOME%"
  ) else (
    rem Fallback default (edit this if needed)
    set "JDK_PATH=C:\Program Files\Java\jdk-17"
  )
)

rem Normalize tool paths (no embedded quotes in variables)
set "JAVAC=%JDK_PATH%\bin\javac.exe"
set "JAR=%JDK_PATH%\bin\jar.exe"

rem 2) Detect JDK major version and choose compatible JavaFX lib
for /f "tokens=2" %%v in ('"%JAVAC%" -version 2^>^&1') do set "JAVAC_VER=%%v"
for /f "tokens=1 delims=." %%m in ("%JAVAC_VER%") do set "JAVAC_MAJOR=%%m"

set "JAVAFX_REL=javafx-backup\javafx-sdk-17.0.17\lib"
if defined JAVAC_MAJOR (
  rem JavaFX 25.x requires JDK 23+ (class file version 67)
  if %JAVAC_MAJOR% GEQ 23 (
    if exist "javafx-backup\javafx-sdk-25.0.1\lib" set "JAVAFX_REL=javafx-backup\javafx-sdk-25.0.1\lib"
  )
)

echo Using javac: "%JAVAC%" (version %JAVAC_VER%)
echo Using jar: "%JAR%"
echo JavaFX lib: %JAVAFX_REL%

if not exist "%JAVAC%" (
  echo javac not found at "%JAVAC%"
  echo Please set environment variable JAVA_HOME or edit JDK_PATH in compile.bat.
  pause
  exit /b 1
)

if exist out rd /s /q out
mkdir out

rem create sources list
if exist sources.txt del /f /q sources.txt
rem Build sources list but exclude files under dist, out, package-resources, and .git folders
for /f "delims=" %%F in ('dir /s /b *.java ^| findstr /i /v "\\dist\\" ^| findstr /i /v "\\out\\" ^| findstr /i /v "\\package-resources\\" ^| findstr /i /v "\\.git\\"') do echo %%~fF>>sources.txt

if not exist sources.txt (
  echo No Java source files found.
  pause
  exit /b 1
)

rem 3) Compile with module-path to JavaFX
"%JAVAC%" --module-path "%CD%\%JAVAFX_REL%" --add-modules javafx.controls,javafx.fxml,javafx.media -d out @sources.txt
if errorlevel 1 (
  echo Compilation failed.
  pause
  exit /b 1
)

rem 4) Create manifest with Class-Path to JavaFX jars so you can run with: java -jar app.jar
set "JFX_CP=%JAVAFX_REL:\=/%"
>manifest.txt echo Main-Class: Main
>>manifest.txt echo Class-Path: %JFX_CP%/javafx-base.jar %JFX_CP%/javafx-graphics.jar %JFX_CP%/javafx-controls.jar
>>manifest.txt echo  %JFX_CP%/javafx-fxml.jar %JFX_CP%/javafx-media.jar
>>manifest.txt echo.

rem 5) Create jar
"%JAR%" cfm app.jar manifest.txt -C out .
if errorlevel 1 (
  echo jar creation failed.
  pause
  exit /b 1
)

rem 5b) Ensure resources are packaged inside the jar by copying into out before jar creation
rem (If earlier jar creation already succeeded, we still want resources inside app.jar on next run.)
if exist "styles.css" copy /y "styles.css" "out\styles.css" >nul 2>&1
if exist "fonts" (
  xcopy "fonts" "out\fonts" /E /I /Y >nul 2>&1
)
if exist "sounds" (
  xcopy "sounds" "out\sounds" /E /I /Y >nul 2>&1
)

echo Created app.jar successfully.
endlocal
pause
