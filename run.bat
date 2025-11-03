@echo off
REM Windows run script

REM Set console to UTF-8 encoding
chcp 65001 >nul

REM Check if compiled
if not exist bin (
    echo Project not compiled yet, compiling...
    call compile.bat
    if errorlevel 1 (
        pause
        exit /b 1
    )
)

echo Starting game...
echo.

REM Run game with UTF-8 encoding
java -Dfile.encoding=UTF-8 -cp bin com.textadventure.game.DemoGame

REM Pause after game ends to prevent window from closing
echo.
pause
