@echo off
REM Windows 编译脚本

echo 正在编译文本冒险引擎...

REM 创建bin目录（如果不存在）
if not exist bin mkdir bin

REM 编译所有Java文件
javac -d bin -sourcepath src src\com\textadventure\game\DemoGame.java

if %errorlevel% equ 0 (
    echo 编译成功！
    echo 运行: run.bat
) else (
    echo 编译失败！
    exit /b 1
)
