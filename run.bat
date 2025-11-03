@echo off
REM Windows 运行脚本

REM 检查是否已编译
if not exist bin (
    echo 项目尚未编译，正在编译...
    call compile.bat
    if errorlevel 1 exit /b 1
)

echo 启动游戏...
echo.

REM 运行游戏
java -cp bin com.textadventure.game.DemoGame
