@echo off
REM Windows 编译脚本

REM 设置控制台为UTF-8编码
chcp 65001 >nul

echo 正在编译文本冒险引擎...

REM 创建bin目录（如果不存在）
if not exist bin mkdir bin

REM 编译所有Java文件，指定UTF-8编码
javac -encoding UTF-8 -d bin -sourcepath src src\com\textadventure\game\DemoGame.java

if %errorlevel% equ 0 (
    echo 编译成功！
    echo 运行: run.bat
) else (
    echo 编译失败！
    exit /b 1
)
