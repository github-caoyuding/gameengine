@echo off
REM Windows compile script

REM Set console to UTF-8 encoding
chcp 65001 >nul

echo Compiling Text Adventure Engine...

REM Create bin directory if not exists
if not exist bin mkdir bin

REM Compile all Java files with UTF-8 encoding
javac -encoding UTF-8 -d bin -sourcepath src src\com\textadventure\game\DemoGame.java

if %errorlevel% equ 0 (
    echo.
    echo Compilation successful!
    echo Run: run.bat
) else (
    echo.
    echo Compilation failed!
    pause
    exit /b 1
)

pause
