@echo off
rem create-exe.bat - build a Windows installer (EXE) using jpackage
rem Prerequisites:
rem  - JDK 17+ with jpackage (set JDK_PATH below if not detected)
rem  - JavaFX SDK 17 in repo at javafx-backup\javafx-sdk-17.0.17\lib (or adjust JAVAFX_REL)

setlocal

rem Adjust if your JDK is installed elsewhere
set "JDK_PATH=C:\Program Files\Java\jdk-17"
set "JPACKAGE=%JDK_PATH%\bin\jpackage.exe"

if not exist "%JPACKAGE%" (
  echo jpackage not found at %JPACKAGE%
  echo Please install a JDK with jpackage or set JDK_PATH in this script.
  pause
  exit /b 1
)

rem JavaFX library folder relative to repo root (adjust if needed)
set "JAVAFX_REL=javafx-backup\javafx-sdk-17.0.17\lib"
set "JAVAFX_ABS=%CD%\%JAVAFX_REL%"

if not exist "%JAVAFX_ABS%" (
  echo JavaFX SDK not found at %JAVAFX_ABS%
  echo Place JavaFX 17 SDK under the repo (e.g. javafx-backup\javafx-sdk-17.0.17\lib) or edit this script to point to it.
  pause
  exit /b 1
)

rem Output installer directory
set "OUT_DIR=%CD%\installer"
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

echo Running jpackage to create installer. This may take a minute...

rem Prepare package-input folder with app.jar and resources (fonts, sounds, styles)
set "PACKAGE_INPUT=%CD%\package-input"
if exist "%PACKAGE_INPUT%" rd /s /q "%PACKAGE_INPUT%"
mkdir "%PACKAGE_INPUT%"

rem copy app.jar
copy /y "%CD%\app.jar" "%PACKAGE_INPUT%\" >nul 2>&1

rem copy styles.css if present
if exist "%CD%\styles.css" copy /y "%CD%\styles.css" "%PACKAGE_INPUT%\" >nul 2>&1

rem copy fonts and sounds folders if present
if exist "%CD%\fonts" (
  xcopy "%CD%\fonts" "%PACKAGE_INPUT%\fonts" /e /i /y >nul 2>&1
)
if exist "%CD%\sounds" (
  xcopy "%CD%\sounds" "%PACKAGE_INPUT%\sounds" /e /i /y >nul 2>&1
)

rem detect icon if present (handle icon arg and path separately to avoid parsing issues)
set "ICON_ARG="
set "ICON_PATH="
if exist "%CD%\app.ico" (
  set "ICON_ARG=--icon"
  set "ICON_PATH=%CD%\app.ico"
)

echo Packaging input folder: %PACKAGE_INPUT%

rem Use jpackage to create an app-image (plain executable) rather than an installer
echo Running jpackage to create an app-image (plain exe) ...
if defined ICON_ARG (
  "%JPACKAGE%" --type app-image --input "%PACKAGE_INPUT%" --main-jar app.jar --main-class Main --name FlashcardQuiz --module-path "%JAVAFX_ABS%" --add-modules javafx.controls,javafx.fxml,javafx.media --app-version 1.0 --dest "%OUT_DIR%" --icon "%ICON_PATH%"
) else (
  "%JPACKAGE%" --type app-image --input "%PACKAGE_INPUT%" --main-jar app.jar --main-class Main --name FlashcardQuiz --module-path "%JAVAFX_ABS%" --add-modules javafx.controls,javafx.fxml,javafx.media --app-version 1.0 --dest "%OUT_DIR%"
)

if errorlevel 1 (
  echo jpackage failed. See output above for details.
  pause
  exit /b 1
)

echo App image created in %OUT_DIR%
pause
endlocal
