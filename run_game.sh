#!/bin/bash

# ========================================
# Text Adventure Game - Auto Run Script
# ========================================
# This script will automatically:
#   1. Auto-download MySQL JDBC driver if missing
#   2. Compile the project
#   3. Run the game
# ========================================

YELLOW='\033[1;33m'
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "========================================"
echo "  Text Adventure Game Launcher"
echo "========================================"
echo ""

# Function to find MySQL JDBC driver
find_jdbc_driver() {
    local jar=$(ls mysql-connector-j-*.jar 2>/dev/null | head -n 1)
    if [ -z "$jar" ]; then
        jar=$(ls mysql-connector-java-*.jar 2>/dev/null | head -n 1)
    fi
    echo "$jar"
}

# Function to download JDBC driver
download_jdbc_driver() {
    local version="8.2.0"
    local filename="mysql-connector-j-${version}"
    local url="https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${version}/${filename}.jar"

    echo -e "${BLUE}Downloading MySQL JDBC Driver (${version})...${NC}"
    echo "Source: Maven Central Repository"
    echo ""

    # Try wget first
    if command -v wget &> /dev/null; then
        wget -q --show-progress -O "${filename}.jar" "$url"
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ Download complete!${NC}"
            echo "$filename.jar"
            return 0
        fi
    fi

    # Try curl if wget failed or not available
    if command -v curl &> /dev/null; then
        echo "Trying curl..."
        curl -L -o "${filename}.jar" --progress-bar "$url"
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ Download complete!${NC}"
            echo "$filename.jar"
            return 0
        fi
    fi

    # If both failed, try alternative source
    echo -e "${YELLOW}Trying alternative source...${NC}"
    local alt_url="https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-${version}.tar.gz"

    if command -v wget &> /dev/null; then
        wget -q --show-progress "$alt_url"
        if [ $? -eq 0 ]; then
            echo "Extracting..."
            tar -xzf "mysql-connector-j-${version}.tar.gz"
            cp "mysql-connector-j-${version}/mysql-connector-j-${version}.jar" .
            rm -rf "mysql-connector-j-${version}.tar.gz" "mysql-connector-j-${version}/"
            echo -e "${GREEN}✓ Download and extract complete!${NC}"
            echo "mysql-connector-j-${version}.jar"
            return 0
        fi
    fi

    return 1
}

# Check for JDBC driver
JDBC_JAR=$(find_jdbc_driver)

if [ -z "$JDBC_JAR" ]; then
    echo -e "${YELLOW}MySQL JDBC Driver not found. Auto-downloading...${NC}"
    echo ""

    # Auto download
    JDBC_JAR=$(download_jdbc_driver)

    if [ $? -ne 0 ] || [ -z "$JDBC_JAR" ]; then
        echo ""
        echo -e "${RED}✗ Auto-download failed!${NC}"
        echo ""
        echo "Please download manually from:"
        echo "  https://dev.mysql.com/downloads/connector/j/"
        echo ""
        echo "Or run in Guest Mode (no database):"
        echo -e "  ${GREEN}./run.sh${NC}"
        echo ""
        exit 1
    fi
    echo ""
fi

echo -e "${GREEN}✓ Found JDBC driver: $JDBC_JAR${NC}"
echo ""

# Create bin directory if not exists
mkdir -p bin

# Compile
echo "Compiling..."
javac -encoding UTF-8 -d bin -sourcepath src src/com/textadventure/game/DemoGame.java

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation successful!${NC}"
    echo ""
else
    echo -e "${RED}✗ Compilation failed!${NC}"
    exit 1
fi

# Run the game
echo "Starting game..."
echo "========================================"
echo ""

java -cp "bin:$JDBC_JAR" \
     -Dfile.encoding=UTF-8 \
     com.textadventure.game.DemoGame

echo ""
echo "========================================"
echo "Game ended. Thank you for playing!"
