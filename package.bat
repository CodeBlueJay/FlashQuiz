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

rem Prefer Gradle fat jar if present; fallback to app.jar
set "APP_JAR=app.jar"
if exist "build\libs\FlashcardQuiz.jar" set "APP_JAR=build\libs\FlashcardQuiz.jar"
if not exist "%APP_JAR%" (
	echo Application JAR not found. Build it first.
	echo If using Gradle: .\gradlew shadowJar
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
rem bundle JavaFX native DLLs for software pipeline fallback
if exist "%JAVAFX_BIN%" xcopy "%JAVAFX_BIN%" "%RES_DIR%\bin" /E /I /Y >nul 2>&1


rem Create a minimal input folder for jpackage containing only the application jar
set "INPUT_DIR=%TEMP%\fc-input"
if exist "%INPUT_DIR%" rmdir /s /q "%INPUT_DIR%"
mkdir "%INPUT_DIR%"
copy /y "%APP_JAR%" "%INPUT_DIR%\app.jar" >nul 2>&1

rem Run jpackage with JavaFX modules; do NOT use --runtime-image here so we can pass module-path/add-modules
echo Running jpackage (this may take a while) - logging to %TEMP%\jpackage.log
"%JPACKAGE%" --type %JPACKAGE_TYPE% --name Flashcards --app-version 1.0.0 --dest "%DEST%" --input "%INPUT_DIR%" --resource-dir "%CD%\%RES_DIR%" --main-jar app.jar --main-class Main --module-path "%CD%\%JAVAFX_REL%" --add-modules javafx.controls,javafx.fxml,javafx.media --java-options "-Dprism.order=sw" --java-options "-Dprism.forceGPU=false" --java-options "-Dprism.verbose=true" --java-options "-Djava.library.path=%APPDIR%\bin" --win-console --verbose > "%TEMP%\jpackage.log" 2>&1
rem "%JPACKAGE%" --type %JPACKAGE_TYPE% --name Flashcards --app-version 1.0.0 --dest "%DEST%" --input "%INPUT_DIR%" --resource-dir "%CD%\%RES_DIR%" --main-jar app.jar --main-class Main --module-path "%CD%\%JAVAFX_REL%" --add-modules javafx.controls,javafx.fxml,javafx.media --java-options "--enable-native-access=javafx.graphics" --java-options "--enable-native-access=javafx.media" --java-options "-Dprism.order=sw" --java-options "-Dprism.forceGPU=false" --java-options "-Dprism.verbose=true" --java-options "-Djava.library.path=%CD%\%JAVAFX_BIN%" --verbose > "%TEMP%\jpackage.log" 2>&1
rem Clean up temporary input and resource dirs
if exist "%INPUT_DIR%" rmdir /s /q "%INPUT_DIR%"
if exist "%RES_DIR%" rmdir /s /q "%RES_DIR%"

if errorlevel 1 (
	echo jpackage failed -- see %TEMP%\jpackage.log for details
	type "%TEMP%\jpackage.log"
	pause
	exit /b 1
)

if "%JPACKAGE_TYPE%"=="app-image" (
	echo Built app image in %DEST%\Flashcards. Run %DEST%\Flashcards\Flashcards.exe
) else (
	echo Built installer in %DEST%. Run the installer to install Flashcards.
)
rem Determine actual image directory created by jpackage (robust lookup)
set "IMAGE_DIR="
if exist "%DEST%\Flashcards" (
	set "IMAGE_DIR=%DEST%\Flashcards"
) else (
	for /f "delims=" %%I in ('dir "%DEST%\Flashcards*" /b /ad 2^>nul') do (
		set "IMAGE_DIR=%DEST%\%%I"
		goto :_found_image_dir
	)
)
:_found_image_dir

if not defined IMAGE_DIR (
	echo WARNING: Could not find app image directory under %DEST%. Skipping resource copy.
) else (
	echo Found image directory: %IMAGE_DIR%
	rem Prefer robocopy if available (more reliable). Use quiet output switches.
	where robocopy >nul 2>&1
	if errorlevel 1 (
		set "USE_ROBOCOPY=0"
	) else (
		set "USE_ROBOCOPY=1"
	)

	rem Copy styles.css
	if exist "styles.css" (
		if "%USE_ROBOCOPY%"=="1" (
			robocopy . "%IMAGE_DIR%" "styles.css" /NFL /NDL /NJH /NJS >nul
		) else (
			copy /y "styles.css" "%IMAGE_DIR%\styles.css" >nul 2>&1
		)
	)

	rem Copy sounds dir
	if exist "sounds" (
		if "%USE_ROBOCOPY%"=="1" (
			robocopy "sounds" "%IMAGE_DIR%\sounds" * /E /NFL /NDL /NJH /NJS >nul
		) else (
			xcopy "sounds" "%IMAGE_DIR%\sounds" /E /I /Y >nul 2>&1
		)
	)

	rem Copy fonts dir
	if exist "fonts" (
		if "%USE_ROBOCOPY%"=="1" (
			robocopy "fonts" "%IMAGE_DIR%\fonts" * /E /NFL /NDL /NJH /NJS >nul
		) else (
			xcopy "fonts" "%IMAGE_DIR%\fonts" /E /I /Y >nul 2>&1
		)
	)

	rem Copy JavaFX native bin DLLs into image root
	if exist "%RES_DIR%\bin" (
		if "%USE_ROBOCOPY%"=="1" (
			robocopy "%RES_DIR%\bin" "%IMAGE_DIR%\bin" * /E /NFL /NDL /NJH /NJS >nul
		) else (
			xcopy "%RES_DIR%\bin" "%IMAGE_DIR%\bin" /E /I /Y >nul 2>&1
		)
	)

	echo Copied styles/sounds/fonts into %IMAGE_DIR%

	rem Also copy into the app subfolder if present
	if exist "%IMAGE_DIR%\app" (
		if exist "styles.css" (
			if "%USE_ROBOCOPY%"=="1" (
				robocopy . "%IMAGE_DIR%\app" "styles.css" /NFL /NDL /NJH /NJS >nul
			) else (
				copy /y "styles.css" "%IMAGE_DIR%\app\styles.css" >nul 2>&1
			)
		)
		if exist "sounds" (
			if "%USE_ROBOCOPY%"=="1" (
				robocopy "sounds" "%IMAGE_DIR%\app\sounds" * /E /NFL /NDL /NJH /NJS >nul
			) else (
				xcopy "sounds" "%IMAGE_DIR%\app\sounds" /E /I /Y >nul 2>&1
			)
		)
		if exist "fonts" (
			if "%USE_ROBOCOPY%"=="1" (
				robocopy "fonts" "%IMAGE_DIR%\app\fonts" * /E /NFL /NDL /NJH /NJS >nul
			) else (
				xcopy "fonts" "%IMAGE_DIR%\app\fonts" /E /I /Y >nul 2>&1
			)
		)

		rem Copy JavaFX native bin DLLs into app subfolder as well
		if exist "%RES_DIR%\bin" (
			if "%USE_ROBOCOPY%"=="1" (
				robocopy "%RES_DIR%\bin" "%IMAGE_DIR%\app\bin" * /E /NFL /NDL /NJH /NJS >nul
			) else (
				xcopy "%RES_DIR%\bin" "%IMAGE_DIR%\app\bin" /E /I /Y >nul 2>&1
			)
		)
		echo Also copied resources into %IMAGE_DIR%\app
	)

	rem Copy project-sources snapshot into image and app subfolder
	echo Creating clean project-sources snapshot
	rem Create a temporary snapshot folder in %TEMP% to avoid accidentally copying dist/out or other generated folders
	set "SNAP=%TEMP%\fc-snapshot"
	if exist "%SNAP%" rmdir /s /q "%SNAP%"
	mkdir "%SNAP%"

	rem Copy top-level source files and docs into snapshot
	if exist "*.java" copy /y "*.java" "%SNAP%\" >nul 2>&1
	if exist "*.md" copy /y "*.md" "%SNAP%\" >nul 2>&1

	rem Copy src tree if present (use robocopy for reliability)
	if exist "src" (
		if "%USE_ROBOCOPY%"=="1" (
			robocopy "src" "%SNAP%\src" * /E /NFL /NDL /NJH /NJS >nul
		) else (
			xcopy "src" "%SNAP%\src" /E /I /Y >nul 2>&1
		)
	)

	rem Also include README and other helpful files
	if exist "README.md" copy /y "README.md" "%SNAP%\" >nul 2>&1

	rem Finally copy the snapshot into the image (and app subfolder)
	if exist "%SNAP%" (
		if "%USE_ROBOCOPY%"=="1" (
			robocopy "%SNAP%" "%IMAGE_DIR%\project-sources" * /E /NFL /NDL /NJH /NJS >nul
		) else (
			xcopy "%SNAP%" "%IMAGE_DIR%\project-sources" /E /I /Y >nul 2>&1
		)
		echo Copied project-sources to %IMAGE_DIR%\project-sources
		if exist "%IMAGE_DIR%\app" (
			if "%USE_ROBOCOPY%"=="1" (
				robocopy "%SNAP%" "%IMAGE_DIR%\app\project-sources" * /E /NFL /NDL /NJH /NJS >nul
			) else (
				xcopy "%SNAP%" "%IMAGE_DIR%\app\project-sources" /E /I /Y >nul 2>&1
			)
			echo Also copied project-sources into %IMAGE_DIR%\app\project-sources
		)
		rem Remove temporary snapshot
		if exist "%SNAP%" rmdir /s /q "%SNAP%"
	) else (
		echo WARNING: snapshot folder not created, skipping project-sources copy
	)
	rem Previously the script attempted to copy project sources again from the working directory
	rem which could inadvertently include the just-created image (dist) and cause recursive copies.
	rem That behavior is dangerous; we intentionally avoid copying from the repository root here.
	rem If additional files need to be added into '%IMAGE_DIR%\app\project-sources', create
	rem a safe snapshot first (see above) or place files into 'project-sources' before packaging.
)
endlocal