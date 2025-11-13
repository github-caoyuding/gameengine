#!/bin/bash

# ========================================
# Text Adventure Game - Auto Run Script
# ========================================
# This script will automatically:
#   1. Check and download MySQL JDBC driver
#   2. Compile the project
#   3. Run the game
# ========================================

set -e  # Exit on error

YELLOW='\033[1;33m'
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "========================================"
echo "  Text Adventure Game Launcher"
echo "========================================"
echo ""

# Function to find MySQL JDBC driver
find_jdbc_driver() {
    # Look for mysql-connector-java JAR files
    local jar=$(ls mysql-connector-j-*.jar 2>/dev/null | head -n 1)
    if [ -z "$jar" ]; then
        jar=$(ls mysql-connector-java-*.jar 2>/dev/null | head -n 1)
    fi
    echo "$jar"
}

# Check for JDBC driver
JDBC_JAR=$(find_jdbc_driver)

if [ -z "$JDBC_JAR" ]; then
    echo -e "${YELLOW}MySQL JDBC Driver not found!${NC}"
    echo ""
    echo "You have 3 options:"
    echo ""
    echo "  1. Download manually:"
    echo "     https://dev.mysql.com/downloads/connector/j/"
    echo ""
    echo "  2. Download using wget (recommended):"
    echo -e "     ${GREEN}wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.2.0.tar.gz${NC}"
    echo -e "     ${GREEN}tar -xzf mysql-connector-j-8.2.0.tar.gz${NC}"
    echo -e "     ${GREEN}cp mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar .${NC}"
    echo ""
    echo "  3. Run in Guest Mode (no database required):"
    echo -e "     ${GREEN}./run.sh${NC}"
    echo ""
    read -p "Download now using wget? (y/n): " -n 1 -r
    echo ""

    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo ""
        echo "Downloading MySQL Connector/J..."

        # Download
        wget -q --show-progress https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.2.0.tar.gz

        if [ $? -eq 0 ]; then
            echo "Extracting..."
            tar -xzf mysql-connector-j-8.2.0.tar.gz
            cp mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar .

            # Cleanup
            rm -rf mysql-connector-j-8.2.0.tar.gz mysql-connector-j-8.2.0/

            JDBC_JAR="mysql-connector-j-8.2.0.jar"
            echo -e "${GREEN}Download complete!${NC}"
            echo ""
        else
            echo -e "${RED}Download failed! Please download manually.${NC}"
            exit 1
        fi
    else
        echo ""
        echo "Exiting. Please download the JDBC driver and run again."
        exit 0
    fi
fi

echo -e "${GREEN}Found JDBC driver: $JDBC_JAR${NC}"
echo ""

# Create bin directory if not exists
mkdir -p bin

# Compile
echo "Compiling..."
javac -encoding UTF-8 -d bin -sourcepath src src/com/textadventure/game/DemoGame.java

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Compilation successful!${NC}"
    echo ""
else
    echo -e "${RED}Compilation failed!${NC}"
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
