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
    echo MySQL JDBC Driver not found. Auto-downloading...
    echo.

    set VERSION=8.2.0
    set FILENAME=mysql-connector-j-%VERSION%
    set MAVEN_URL=https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/%VERSION%/%FILENAME%.jar

    echo Downloading MySQL JDBC Driver ^(%VERSION%^)...
    echo Source: Maven Central Repository
    echo.

    REM Try downloading JAR directly from Maven Central
    powershell -Command "try { $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%FILENAME%.jar' -ErrorAction Stop; Write-Host 'Download complete!' -ForegroundColor Green; exit 0 } catch { exit 1 }"

    if not errorlevel 1 (
        set JDBC_JAR=%FILENAME%.jar
        echo.
    ) else (
        echo.
        echo Maven Central download failed. Trying alternative source...
        echo.

        REM Fallback: Try MySQL official site
        set ZIP_URL=https://dev.mysql.com/get/Downloads/Connector-J/%FILENAME%.zip

        powershell -Command "try { $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri '%ZIP_URL%' -OutFile '%FILENAME%.zip' -ErrorAction Stop; exit 0 } catch { exit 1 }"

        if not errorlevel 1 (
            echo Extracting...
            powershell -Command "Expand-Archive -Path %FILENAME%.zip -DestinationPath . -Force"
            copy %FILENAME%\%FILENAME%.jar . >nul

            REM Cleanup
            del %FILENAME%.zip >nul 2>&1
            rmdir /s /q %FILENAME% >nul 2>&1

            set JDBC_JAR=%FILENAME%.jar
            echo Download complete!
            echo.
        ) else (
            echo.
            echo Auto-download failed!
            echo.
            echo Please download manually from:
            echo   https://dev.mysql.com/downloads/connector/j/
            echo.
            echo Or run in Guest Mode ^(no database^):
            echo   run.bat
            echo.
            pause
            exit /b 1
        )
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
