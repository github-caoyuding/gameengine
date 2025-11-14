# 文本冒险引擎

一个使用 Java SE 开发的文本冒险游戏引擎。玩家可以通过输入文本命令来探索游戏世界、收集物品、解决谜题。

## 特性

- 🎮 完整的游戏引擎系统
- 🔐 用户登录系统，支持注册、登录、游客模式
- 💾 MySQL数据库集成，自动初始化
- 🗺️ 房间系统，支持多方向连接
- 🎒 物品系统，可拾取和丢弃物品
- 💬 命令解析器，支持中英文命令
- 🏆 胜利条件系统，任务进度追踪
- 🎯 面向对象设计，易于扩展
- ⚡ 一键运行，自动下载JDBC驱动

## 项目结构

```
gameengine/
├── src/
│   └── com/
│       └── textadventure/
│           ├── core/                    # 核心游戏类
│           │   ├── Direction.java       # 方向枚举
│           │   ├── Item.java            # 物品类
│           │   ├── Player.java          # 玩家类
│           │   ├── Room.java            # 房间类
│           │   └── User.java            # 用户实体类
│           ├── database/                # 数据库层
│           │   ├── DBUtil.java          # JDBC工具类
│           │   ├── UserDAO.java         # 用户数据访问对象
│           │   └── DatabaseInitializer.java # 数据库自动初始化
│           ├── engine/                  # 游戏引擎
│           │   ├── Command.java         # 命令类
│           │   ├── GameEngine.java      # 游戏引擎
│           │   ├── LoginService.java    # 登录服务
│           │   └── WinCondition.java    # 胜利条件
│           └── game/                    # 游戏实现
│               └── DemoGame.java        # 示例游戏
├── resources/                           # 资源文件
│   └── db.properties                    # 数据库配置
├── sql/                                 # SQL脚本
│   └── init.sql                         # MySQL初始化脚本（供参考）
├── bin/                                 # 编译输出目录
├── compile.bat                          # 编译脚本
├── run.bat                              # 运行脚本（游客模式）
├── run_game.bat                         # 一键运行脚本（推荐）⭐
└── README.md                            # 本文件
```

## 快速开始

### 前置要求

- **Java JDK 8 或更高版本**
- **MySQL 5.7+ 数据库服务器**

### 第一步：安装 MySQL

确保已安装 MySQL 5.7 或更高版本。

检查是否已安装：
```cmd
mysql --version
```

如果未安装，请从官网下载：https://dev.mysql.com/downloads/mysql/

### 第二步：配置数据库连接

编辑 `resources/db.properties` 文件，设置你的 MySQL 连接信息：

```properties
# MySQL 服务器地址和端口
db.url=jdbc:mysql://localhost:3306/gameengine?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true

# MySQL 用户名
db.username=root

# MySQL 密码（必须设置！）
db.password=你的MySQL密码    # ← 修改这里！

# JDBC 驱动类名
db.driver=com.mysql.cj.jdbc.Driver
```

**重要提示：**
- 如果 MySQL 在其他服务器上，请修改 `localhost` 为服务器 IP 地址
- 如果端口不是 3306，请修改端口号
- **必须设置正确的密码！**

### 第三步：一键运行（推荐 ⭐）

**最简单的方式！** 双击或在命令行运行：

```cmd
run_game.bat
```

**脚本会自动完成：**
- ✅ 检测 MySQL JDBC 驱动
- ✅ 自动下载驱动（如果不存在）
- ✅ 自动编译项目
- ✅ 自动创建数据库 `gameengine`（如果不存在）
- ✅ 自动创建 `users` 表
- ✅ 自动插入 4 个测试用户
- ✅ 启动游戏

**默认测试账号：**
- 用户名: `admin` / 密码: `admin123`
- 用户名: `player1` / 密码: `pass123`
- 用户名: `player2` / 密码: `pass123`
- 用户名: `test` / 密码: `test`

## 游戏模式

### 1. 登录模式（数据库）

使用已注册的账号登录，可以保存游戏进度。

### 2. 注册模式

创建新账号，数据保存在 MySQL 数据库中。

### 3. 游客模式

无需登录，直接游玩。可以使用 `run.bat` 快速进入游客模式。

```cmd
run.bat
```

**注意：** 游客模式下无法保存游戏进度。

## 游戏命令

### 基本命令

- `look` / `观察` / `l` - 查看当前房间
- `help` / `帮助` - 显示帮助信息
- `quit` / `退出` - 退出游戏
- `progress` / `进度` / `任务` - 查看任务进度

### 移动命令

- `go <方向>` / `前往 <方向>` - 移动到指定方向
- `north` / `n` / `北` - 向北移动
- `south` / `s` / `南` - 向南移动
- `east` / `e` / `东` - 向东移动
- `west` / `w` / `西` - 向西移动
- `up` / `u` / `上` - 向上移动
- `down` / `d` / `下` - 向下移动

### 物品命令

- `take <物品>` / `拿取 <物品>` - 拾取物品
- `drop <物品>` / `丢弃 <物品>` - 丢弃物品
- `inventory` / `背包` / `i` - 查看背包
- `examine <物品>` / `检查 <物品>` / `x <物品>` - 检查物品详情

## 示例游戏：神秘洞穴探险

项目包含一个完整的示例游戏，包含：

- **5 个不同的房间**
  - 洞穴入口
  - 阴暗走廊
  - 宝藏室
  - 黑暗密室
  - 地下花园

- **10+ 种可收集的物品**
  - 火把、绳索、金币、宝石等

- **多方向的探索路径**
  - 支持北、南、东、西、上、下六个方向

### 胜利条件

要赢得游戏，你需要完成以下任务：

1. **探索所有房间** - 访问全部 5 个房间
2. **收集关键物品** - 拿到以下 3 个关键物品：
   - 👑 王冠（在宝藏室）
   - 💎 水晶（在地下花园）
   - 🔑 钥匙（在黑暗密室）

**游戏提示：**
- 使用 `progress` 命令随时查看任务进度
- 探索所有方向找到隐藏的房间
- 不是所有物品都是必需的，只需要收集关键物品
- 完成所有任务后会自动显示胜利画面

## 手动编译和运行

如果你想手动控制编译和运行过程：

### 编译项目

```cmd
compile.bat
```

或手动编译：
```cmd
javac -encoding UTF-8 -d bin -sourcepath src src\com\textadventure\game\DemoGame.java
```

### 运行游戏

**游客模式（无数据库）：**
```cmd
run.bat
```

**完整模式（带数据库）：**
```cmd
java -cp "bin;mysql-connector-j-8.2.0.jar" -Dfile.encoding=UTF-8 com.textadventure.game.DemoGame
```

## 如何创建自己的游戏

1. 在 `src/com/textadventure/game` 包中创建新类（例如 `MyGame.java`）
2. 创建房间和物品
3. 设置房间之间的连接
4. 创建玩家并设置初始房间
5. 启动游戏引擎

**示例代码：**

```java
package com.textadventure.game;

import com.textadventure.core.*;
import com.textadventure.engine.GameEngine;
import com.textadventure.engine.WinCondition;

public class MyGame {
    public static void main(String[] args) {
        // 创建房间
        Room startRoom = new Room("起始房间", "这是游戏的开始...");
        Room nextRoom = new Room("下一个房间", "你来到了另一个地方...");

        // 连接房间
        startRoom.setExit(Direction.NORTH, nextRoom);
        nextRoom.setExit(Direction.SOUTH, startRoom);

        // 添加物品
        startRoom.addItem(new Item("钥匙", "一把神秘的钥匙"));

        // 创建玩家
        Player player = new Player("勇者");
        player.setCurrentRoom(startRoom);

        // 创建胜利条件
        WinCondition winCondition = new WinCondition();
        winCondition.setRequiredRoomCount(2);
        winCondition.addRequiredItem("钥匙");

        // 启动游戏
        GameEngine engine = new GameEngine(player);
        engine.setWinCondition(winCondition);
        engine.start();
    }
}
```

## 核心类说明

### Direction (方向枚举)
定义游戏中的六个方向：北、南、东、西、上、下。支持中英文解析。

### Room (房间类)
- 房间名称和描述
- 连接到其他房间的出口 (Map<Direction, Room>)
- 房间中的物品列表 (List<Item>)

### Item (物品类)
- 物品名称和描述
- 是否可以被拾取的标志

### Player (玩家类)
- 玩家名称
- 当前所在房间
- 背包（物品列表）
- 背包容量限制
- 已访问房间追踪

### Command (命令类)
解析玩家输入的文本命令，将其分解为动词和对象。支持中英文命令。

### GameEngine (游戏引擎)
- 处理游戏主循环
- 解析和执行命令
- 管理游戏状态
- 检查胜利条件

### WinCondition (胜利条件)
- 设置需要访问的房间数量
- 设置需要收集的关键物品
- 实时追踪任务进度
- 提供进度查询功能

### LoginService (登录服务)
- 用户登录验证
- 新用户注册
- 游客模式支持

### DatabaseInitializer (数据库初始化器)
- 自动创建数据库
- 自动创建表结构
- 自动插入测试数据

## 技术要点

本项目使用的 Java SE 知识点：

- **面向对象编程（OOP）**
- **类和对象**
- **枚举类型** (Direction)
- **集合框架** (List, Map, Set)
- **异常处理** (try-catch-finally)
- **输入/输出** (Scanner)
- **字符串处理**
- **JDBC 数据库连接**
- **PreparedStatement 防SQL注入**
- **包管理和模块化设计**

## 扩展建议

你可以通过以下方式扩展这个引擎：

1. **添加 NPC 系统** - 创建可以对话的非玩家角色
2. **添加战斗系统** - 实现敌人和战斗机制
3. **添加谜题系统** - 创建需要特定物品或条件才能解决的谜题
4. **添加保存/加载功能** - 实现游戏进度的保存和恢复
5. **添加物品组合** - 允许物品之间的组合和使用
6. **添加时间系统** - 实现昼夜循环或时间限制
7. **添加成就系统** - 追踪玩家的成就和里程碑
8. **添加多结局** - 根据玩家选择提供不同的游戏结局

## 常见问题

### 中文乱码问题

如果出现中文乱码，请使用提供的 `run_game.bat` 脚本，它已自动配置 UTF-8 编码。

### 推荐使用 Windows Terminal

Windows Terminal 对 UTF-8 支持更好，推荐使用（Windows 10/11 自带）。

### JDBC 驱动下载失败

如果自动下载失败，可以手动下载：
1. 访问：https://dev.mysql.com/downloads/connector/j/
2. 下载 `mysql-connector-j-8.x.x.jar`
3. 放到项目根目录

### 数据库连接失败

检查以下几点：
1. MySQL 服务是否启动
2. `db.properties` 中的密码是否正确
3. 服务器地址和端口是否正确
4. 用户是否有远程连接权限（如果 MySQL 在其他机器上）

### 无法创建数据库

如果程序无法自动创建数据库，请手动执行：

```cmd
mysql -u root -p -e "CREATE DATABASE gameengine DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

## 许可证

本项目为教育目的创建，可以自由使用和修改。

## 贡献

欢迎提出建议和改进！

---

祝你游戏愉快！🎮
