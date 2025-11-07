# 文本冒险引擎

一个使用 Java SE 开发的文本冒险游戏引擎。玩家可以通过输入文本命令来探索游戏世界、收集物品、解决谜题。

## 特性

- 🎮 完整的游戏引擎系统
- 🔐 用户登录系统，支持注册、登录、游客模式
- 💾 JDBC数据库集成，支持MySQL/PostgreSQL/SQLite
- 🗺️ 房间系统，支持多方向连接
- 🎒 物品系统，可拾取和丢弃物品
- 💬 命令解析器，支持中英文命令
- 🏆 胜利条件系统，任务进度追踪
- 🎯 面向对象设计，易于扩展
- 📦 基于 Java SE + JDBC

## 项目结构

```
gameengine/
├── src/
│   └── com/
│       └── textadventure/
│           ├── core/           # 核心游戏类
│           │   ├── Direction.java    # 方向枚举
│           │   ├── Item.java         # 物品类
│           │   ├── Player.java       # 玩家类
│           │   ├── Room.java         # 房间类
│           │   └── User.java         # 用户实体类
│           ├── database/       # 数据库层
│           │   ├── DBUtil.java       # JDBC工具类
│           │   └── UserDAO.java      # 用户数据访问对象
│           ├── engine/         # 游戏引擎
│           │   ├── Command.java      # 命令类
│           │   ├── GameEngine.java   # 游戏引擎
│           │   ├── LoginService.java # 登录服务
│           │   └── WinCondition.java # 胜利条件
│           └── game/           # 游戏实现
│               └── DemoGame.java     # 示例游戏
├── resources/                  # 资源文件
│   └── db.properties           # 数据库配置
├── sql/                        # SQL脚本
│   ├── init.sql                # MySQL初始化脚本
│   ├── init_sqlite.sql         # SQLite初始化脚本
│   └── README.md               # 数据库设置说明
├── bin/                        # 编译输出目录
├── compile.sh                  # Linux/Mac 编译脚本
├── run.sh                      # Linux/Mac 运行脚本
├── compile.bat                 # Windows 编译脚本
├── run.bat                     # Windows 运行脚本
└── README.md                   # 本文件
```

## 快速开始

### 前置要求

- Java JDK 8 或更高版本
- **(推荐) MySQL 5.7+ 数据库服务器**
- **(推荐) MySQL Connector/J 8.0+ JDBC驱动**

### 数据库配置

游戏支持两种运行模式：

#### 模式1：数据库模式（推荐 - 完整功能）

使用 MySQL 数据库，支持用户注册、登录、数据持久化。

**步骤1：安装 MySQL**

确保已安装 MySQL 5.7 或更高版本。

```bash
# 检查 MySQL 是否已安装
mysql --version
```

**步骤2：创建数据库并初始化**

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本（自动创建数据库和表）
source sql/init.sql;

# 或者在命令行直接执行
mysql -u root -p < sql/init.sql
```

**步骤3：配置数据库连接**

编辑 `resources/db.properties` 文件，设置你的 MySQL 密码：

```properties
db.url=jdbc:mysql://localhost:3306/gameengine?useSSL=false&serverTimezone=UTC
db.username=root
db.password=你的MySQL密码    # ← 修改这里！
db.driver=com.mysql.cj.jdbc.Driver
```

**步骤4：下载 MySQL JDBC 驱动**

下载地址：https://dev.mysql.com/downloads/connector/j/

或者使用 Maven：
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

将下载的 `mysql-connector-java-8.0.xx.jar` 放到项目根目录。

**步骤5：运行游戏（带 JDBC 驱动）**

```bash
# Linux/Mac
java -cp "bin:mysql-connector-java-8.0.xx.jar" -Dfile.encoding=UTF-8 com.textadventure.game.DemoGame

# Windows
java -cp "bin;mysql-connector-java-8.0.xx.jar" -Dfile.encoding=UTF-8 com.textadventure.game.DemoGame
```

**默认测试账号：**
- 用户名: `test` / 密码: `test`
- 用户名: `admin` / 密码: `admin123`
- 用户名: `player1` / 密码: `pass123`

#### 模式2：游客模式（快速体验）

无需数据库，直接运行游戏：

```bash
./run.sh
# 选择 "3. Guest Mode (Skip Login)"
```

**注意：** 游客模式下无法保存游戏进度和用户数据。

---

**其他数据库支持：**
如需使用 PostgreSQL 或 SQLite，请查看：[sql/README.md](sql/README.md)

### 编译和运行

**Linux/Mac:**

```bash
# 赋予脚本执行权限
chmod +x compile.sh run.sh

# 编译项目
./compile.sh

# 运行游戏
./run.sh
```

**Windows:**

```cmd
# 编译项目
compile.bat

# 运行游戏
run.bat
```

**手动编译和运行:**

```bash
# 编译
javac -d bin -sourcepath src src/com/textadventure/game/DemoGame.java

# 运行
java -cp bin com.textadventure.game.DemoGame
```

## 游戏命令

### 基本命令

- `look` / `观察` / `l` - 查看当前房间
- `help` / `帮助` - 显示帮助信息
- `quit` / `退出` - 退出游戏

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
- `examine <物品>` / `检查 <物品>` / `x <物品>` - 检查物品

### 任务命令

- `progress` / `进度` / `任务` - 查看任务进度和完成情况

## 示例游戏

项目包含一个示例游戏 "神秘洞穴探险"，包含：

- 5 个不同的房间（洞穴入口、阴暗走廊、宝藏室、黑暗密室、地下花园）
- 10+ 种可收集的物品
- 多方向的探索路径

### 胜利条件

要赢得游戏，你需要完成以下任务：

1. **探索所有房间** - 访问全部 5 个房间
2. **收集关键物品** - 拿到以下 3 个关键物品：
   - 王冠（在宝藏室）
   - 水晶（在地下花园）
   - 钥匙（在黑暗密室）

**提示**：
- 使用 `progress` 命令随时查看任务进度
- 探索所有方向（北、南、东、西、上、下）找到所有房间
- 不是所有物品都是必需的，只需要收集关键物品
- 完成所有任务后会自动显示胜利画面

## 如何创建自己的游戏

1. 创建一个新的类（例如 `MyGame.java`）在 `com.textadventure.game` 包中
2. 创建房间和物品
3. 设置房间之间的连接
4. 创建玩家并设置初始房间
5. 启动游戏引擎

示例代码：

```java
package com.textadventure.game;

import com.textadventure.core.*;
import com.textadventure.engine.GameEngine;

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

        // 启动游戏
        GameEngine engine = new GameEngine(player);
        engine.start();
    }
}
```

## 核心类说明

### Direction (方向枚举)

定义游戏中的六个方向：北、南、东、西、上、下。

### Room (房间类)

- 房间名称和描述
- 连接到其他房间的出口
- 房间中的物品列表

### Item (物品类)

- 物品名称和描述
- 是否可以被拾取的标志

### Player (玩家类)

- 玩家名称
- 当前所在房间
- 背包（物品列表）
- 背包容量限制

### Command (命令类)

解析玩家输入的文本命令，将其分解为动词和对象。

### GameEngine (游戏引擎)

- 处理游戏循环
- 解析和执行命令
- 管理游戏状态

## 扩展建议

你可以通过以下方式扩展这个引擎：

1. **添加 NPC 系统** - 创建可以对话的非玩家角色
2. **添加战斗系统** - 实现敌人和战斗机制
3. **添加谜题系统** - 创建需要特定物品或条件才能解决的谜题
4. **添加保存/加载功能** - 实现游戏进度的保存和加载
5. **添加任务系统** - 创建任务追踪和完成机制
6. **添加物品组合** - 允许物品之间的组合和使用
7. **添加时间系统** - 实现昼夜循环或时间限制
8. **添加成就系统** - 追踪玩家的成就和里程碑

## 技术要点

本项目使用的 Java SE 知识点：

- 面向对象编程（OOP）
- 类和对象
- 继承和多态
- 枚举类型
- 集合框架（List, Map）
- 异常处理
- 输入/输出（Scanner）
- 字符串处理
- 包管理

## Windows 常见问题

### 中文乱码问题

如果在Windows系统上运行游戏时出现中文乱码，请按以下步骤解决：

**方案1：使用提供的脚本（推荐）**

直接运行 `run.bat`，脚本已经自动配置了UTF-8编码。

**方案2：手动设置编码**

如果仍有问题，请在命令行中执行：

```cmd
# 设置控制台为UTF-8编码
chcp 65001

# 编译（指定UTF-8编码）
javac -encoding UTF-8 -d bin -sourcepath src src\com\textadventure\game\DemoGame.java

# 运行（指定UTF-8编码）
java -Dfile.encoding=UTF-8 -cp bin com.textadventure.game.DemoGame
```

**方案3：使用Windows Terminal**

推荐使用 Windows Terminal（Windows 10/11自带），它对UTF-8支持更好：
1. 打开 Windows Terminal
2. 运行 `run.bat`

**方案4：修改控制台字体**

如果使用传统cmd，请：
1. 右键点击cmd窗口标题栏
2. 选择"属性" → "字体"
3. 选择支持中文的字体（如"新宋体"或"Microsoft YaHei Mono"）

### PowerShell 用户

如果使用PowerShell，可以直接运行：

```powershell
# 设置输出编码
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# 运行游戏
.\run.bat
```

## 许可证

本项目为教育目的创建，可以自由使用和修改。

## 贡献

欢迎提出建议和改进！

---

祝你游戏愉快！🎮
