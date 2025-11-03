@echo off
REM Windows 运行脚本

REM 设置控制台为UTF-8编码
chcp 65001 >nul

REM 检查是否已编译
if not exist bin (
    echo 项目尚未编译，正在编译...
    call compile.bat
    if errorlevel 1 (
        pause
        exit /b 1
    )
)

echo 启动游戏...
echo.

REM 运行游戏，指定UTF-8编码
java -Dfile.encoding=UTF-8 -cp bin com.textadventure.game.DemoGame

REM 游戏结束后暂停，防止窗口关闭
echo.
pause
