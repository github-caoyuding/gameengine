#!/bin/bash

# 编译脚本

echo "正在编译文本冒险引擎..."

# 创建bin目录（如果不存在）
mkdir -p bin

# 编译所有Java文件
javac -d bin -sourcepath src src/com/textadventure/game/DemoGame.java

if [ $? -eq 0 ]; then
    echo "编译成功！"
    echo "运行: ./run.sh"
else
    echo "编译失败！"
    exit 1
fi
