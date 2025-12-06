@echo off
setlocal ENABLEDELAYEDEXPANSION
if not defined JDK_PATH (
	if defined JAVA_HOME (
		set "JDK_PATH=%JAVA_HOME%"
	) else (
		set "JDK_PATH=C:\Program Files\Java\jdk-17"
	)
)

set "JPACKAGE=%JDK_PATH%\bin\jpackage.exe"
if not exist "%JPACKAGE%" (
	pause
	exit /b 1
)

set "VERFILE=%TEMP%\fc_java_ver.txt"
if exist "%VERFILE%" del /f /q "%VERFILE%" >nul 2>&1
cmd /c ""%JDK_PATH%\bin\java.exe" -version 2^>^&1 | findstr /i "version" > "%VERFILE%""
if not exist "%VERFILE%" (
	pause
	exit /b 1
)
set "JAVA_VER="
for /f "tokens=3" %%v in ('type "%VERFILE%" ^| findstr /i "version"') do set "JAVA_VER=%%~v"
for /f "tokens=1 delims=." %%m in ("!JAVA_VER!") do set "JAVA_MAJOR=%%m"
del /f /q "%VERFILE%" >nul 2>&1

set "BEST_SDK="
set "BEST_MAJOR=0"
for /f "delims=" %%D in ('dir /b /ad "javafx-backup" 2^>nul') do (
	echo %%D | findstr /b /c:"javafx-sdk-" >nul || (rem not a javafx-sdk folder, skip)
	echo %%D | findstr /b /c:"javafx-sdk-" >nul && (
		for /f "tokens=3 delims=-" %%P in ("%%D") do (
			for /f "tokens=1 delims=." %%Q in ("%%P") do (
				set "CAND_MAJOR=%%Q"
				if exist "javafx-backup\%%D\lib" (
					if defined JAVA_MAJOR (
						if !CAND_MAJOR! LEQ !JAVA_MAJOR! (
							if !CAND_MAJOR! GTR !BEST_MAJOR! (
								set "BEST_MAJOR=!CAND_MAJOR!"
								set "BEST_SDK=javafx-backup\%%D\lib"
							)
						)
					) else (
						if !CAND_MAJOR! GTR !BEST_MAJOR! (
							set "BEST_MAJOR=!CAND_MAJOR!"
							set "BEST_SDK=javafx-backup\%%D\lib"
						)
					)
				)
			)
		)
	)
)

if defined BEST_SDK (
	set "JAVAFX_REL=!BEST_SDK!"
)
else (
	if exist "javafx-backup\javafx-sdk-17.0.17\lib" (
		set "JAVAFX_REL=javafx-backup\javafx-sdk-17.0.17\lib"
	) else (
		pause
		exit /b 1
	)
)

rem Compute JavaFX native (bin) folder from selected lib path
set "JAVAFX_BIN=%JAVAFX_REL:\lib=\bin%"
echo JavaFX native folder: %JAVAFX_BIN%

if not exist app.jar (
	pause
	exit /b 1
)

set "DEST=dist"
if not exist "%DEST%" mkdir "%DEST%"

set "JPACKAGE_TYPE=app-image"
where light.exe >nul 2>&1 && set "JPACKAGE_TYPE=exe"

rem Remove previous app image folder if it exists to avoid jpackage error
if exist "%DEST%\Flashcards" rmdir /s /q "%DEST%\Flashcards"

rem Diagnostic output
echo.
echo == Packaging diagnostic ==
echo JDK_PATH=%JDK_PATH%
echo JPACKAGE=%JPACKAGE%
echo JAVA_VER=%JAVA_VER%
echo JAVA_MAJOR=%JAVA_MAJOR%
echo JAVAFX_REL=%JAVAFX_REL%
echo JPACKAGE_TYPE=%JPACKAGE_TYPE%
if exist app.jar (echo app.jar exists) else (echo app.jar MISSING)
echo Dest folder: %DEST%
echo ============================
echo Running jpackage...

rem Prepare temporary resource directory to ensure styles, sounds, fonts are included
set "RES_DIR=package-resources"
if exist "%RES_DIR%" rmdir /s /q "%RES_DIR%"
mkdir "%RES_DIR%"
if exist "styles.css" copy /y "styles.css" "%RES_DIR%\styles.css" >nul 2>&1
if exist "sounds" xcopy "sounds" "%RES_DIR%\sounds" /E /I /Y >nul 2>&1
if exist "fonts" xcopy "fonts" "%RES_DIR%\fonts" /E /I /Y >nul 2>&1

cmd /c ""%JPACKAGE%" --type %JPACKAGE_TYPE% --name Flashcards --app-version 1.0.0 --dest "%DEST%" --input . --resource-dir "%CD%\%RES_DIR%" --main-jar app.jar --main-class Main --module-path "%CD%\%JAVAFX_REL%" --add-modules javafx.controls,javafx.fxml,javafx.media --java-options ""--enable-native-access=javafx.graphics"" --java-options ""--enable-native-access=javafx.media"" --java-options ""-Dprism.order=sw"" --java-options ""-Dprism.forceGPU=false"" --java-options ""-Dprism.verbose=true"" --java-options ""-Djava.library.path=%CD%\%JAVAFX_BIN%"" --win-console"
rem Clean up temporary resource dir
if exist "%RES_DIR%" rmdir /s /q "%RES_DIR%"

if errorlevel 1 (
	pause
	exit /b 1
)

if "%JPACKAGE_TYPE%"=="app-image" (
	echo Built app image in %DEST%\Flashcards. Run %DEST%\Flashcards\Flashcards.exe
) else (
	echo Built installer in %DEST%. Run the installer to install Flashcards.
)
rem Ensure resources are present in the produced app image root so runtime can load them
if exist "%DEST%\Flashcards" (
	if exist "styles.css" copy /y "styles.css" "%DEST%\Flashcards\styles.css" >nul 2>&1
	if exist "sounds" xcopy "sounds" "%DEST%\Flashcards\sounds" /E /I /Y >nul 2>&1
	if exist "fonts" xcopy "fonts" "%DEST%\Flashcards\fonts" /E /I /Y >nul 2>&1
	echo Copied styles/sounds/fonts into %DEST%\Flashcards
)
rem Also copy into the app subfolder (some layouts place resources there)
if exist "%DEST%\Flashcards\app" (
	if exist "styles.css" copy /y "styles.css" "%DEST%\Flashcards\app\styles.css" >nul 2>&1
	if exist "sounds" xcopy "sounds" "%DEST%\Flashcards\app\sounds" /E /I /Y >nul 2>&1
	if exist "fonts" xcopy "fonts" "%DEST%\Flashcards\app\fonts" /E /I /Y >nul 2>&1
	echo Also copied resources into %DEST%\Flashcards\app
)
endlocal