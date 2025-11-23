@echo off
git config core.ignorecase false
:: echo Updating local branch
:: git pull
echo.
set /p msg=Enter description of changes: 
if "%msg%"=="" (
    echo Commit message cannot be empty.
    goto :eof
) 
echo.
echo Adding all files
git add --all
echo.
echo Committing with message: "%msg%"
git commit -m "%msg%"
echo.
echo Pushing to repository
git push
echo.
echo Synced