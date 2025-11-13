@echo off
REM ========================================
REM Text Adventure Game - Auto Run Script
REM ========================================
REM This script will automatically:
REM   1. Check and download MySQL JDBC driver
REM   2. Compile the project
REM   3. Run the game
REM ========================================

chcp 65001 >nul

echo ========================================
echo   Text Adventure Game Launcher
echo ========================================
echo.

REM Function to find MySQL JDBC driver
set JDBC_JAR=
for %%f in (mysql-connector-j-*.jar) do (
    set JDBC_JAR=%%f
    goto :found
)
for %%f in (mysql-connector-java-*.jar) do (
    set JDBC_JAR=%%f
    goto :found
)
:found

if "%JDBC_JAR%"=="" (
    echo MySQL JDBC Driver not found!
    echo.
    echo You have 3 options:
    echo.
    echo   1. Download manually:
    echo      https://dev.mysql.com/downloads/connector/j/
    echo.
    echo   2. Use PowerShell to download:
    echo      powershell -Command "Invoke-WebRequest -Uri 'https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.2.0.zip' -OutFile 'mysql-connector-j-8.2.0.zip'"
    echo      powershell -Command "Expand-Archive -Path mysql-connector-j-8.2.0.zip -DestinationPath ."
    echo      copy mysql-connector-j-8.2.0\mysql-connector-j-8.2.0.jar .
    echo.
    echo   3. Run in Guest Mode ^(no database required^):
    echo      run.bat
    echo.

    set /p DOWNLOAD="Download now using PowerShell? (y/n): "

    if /i "%DOWNLOAD%"=="y" (
        echo.
        echo Downloading MySQL Connector/J...

        powershell -Command "try { Invoke-WebRequest -Uri 'https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.2.0.zip' -OutFile 'mysql-connector-j-8.2.0.zip'; exit 0 } catch { exit 1 }"

        if errorlevel 1 (
            echo Download failed! Please download manually.
            pause
            exit /b 1
        )

        echo Extracting...
        powershell -Command "Expand-Archive -Path mysql-connector-j-8.2.0.zip -DestinationPath . -Force"
        copy mysql-connector-j-8.2.0\mysql-connector-j-8.2.0.jar . >nul

        REM Cleanup
        del mysql-connector-j-8.2.0.zip >nul 2>&1
        rmdir /s /q mysql-connector-j-8.2.0 >nul 2>&1

        set JDBC_JAR=mysql-connector-j-8.2.0.jar
        echo Download complete!
        echo.
    ) else (
        echo.
        echo Exiting. Please download the JDBC driver and run again.
        pause
        exit /b 0
    )
)

echo Found JDBC driver: %JDBC_JAR%
echo.

REM Create bin directory if not exists
if not exist bin mkdir bin

REM Compile
echo Compiling...
javac -encoding UTF-8 -d bin -sourcepath src src\com\textadventure\game\DemoGame.java

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM Run the game
echo Starting game...
echo ========================================
echo.

java -cp "bin;%JDBC_JAR%" -Dfile.encoding=UTF-8 com.textadventure.game.DemoGame

echo.
echo ========================================
echo Game ended. Thank you for playing!
pause
