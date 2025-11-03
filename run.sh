#!/bin/bash

# 运行脚本

# 检查是否已编译
if [ ! -d "bin" ] || [ -z "$(ls -A bin)" ]; then
    echo "项目尚未编译，正在编译..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        exit 1
    fi
fi

echo "启动游戏..."
echo ""

# 运行游戏
java -cp bin com.textadventure.game.DemoGame
