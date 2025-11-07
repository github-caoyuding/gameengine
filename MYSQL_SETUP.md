# MySQL 数据库配置指南

完整的 MySQL 数据库配置步骤，适用于文本冒险游戏引擎。

---

## 📋 目录

1. [安装 MySQL](#安装-mysql)
2. [创建数据库](#创建数据库)
3. [配置连接](#配置连接)
4. [安装 JDBC 驱动](#安装-jdbc-驱动)
5. [运行游戏](#运行游戏)
6. [常见问题](#常见问题)

---

## 1. 安装 MySQL

### Windows

1. 下载 MySQL 安装器：https://dev.mysql.com/downloads/installer/
2. 选择 "MySQL Installer for Windows"
3. 运行安装器，选择 "Developer Default" 或 "Server only"
4. 设置 root 密码（记住这个密码！）
5. 完成安装

### macOS

```bash
# 使用 Homebrew 安装
brew install mysql

# 启动 MySQL 服务
brew services start mysql

# 设置 root 密码
mysql_secure_installation
```

### Linux (Ubuntu/Debian)

```bash
# 更新包列表
sudo apt update

# 安装 MySQL
sudo apt install mysql-server

# 启动 MySQL 服务
sudo systemctl start mysql

# 设置 root 密码
sudo mysql_secure_installation
```

### Linux (CentOS/RHEL)

```bash
# 安装 MySQL
sudo yum install mysql-server

# 启动 MySQL 服务
sudo systemctl start mysqld

# 查看临时密码
sudo grep 'temporary password' /var/log/mysqld.log

# 修改 root 密码
mysql_secure_installation
```

### 验证安装

```bash
# 检查 MySQL 版本
mysql --version

# 应该显示类似：mysql  Ver 8.0.33 for Linux on x86_64
```

---

## 2. 创建数据库

### 方法1：使用 SQL 脚本（推荐）

```bash
# 在项目根目录执行
mysql -u root -p < sql/init.sql

# 输入你设置的 root 密码
```

这个命令会自动：
- 创建 `gameengine` 数据库
- 创建 `users` 表
- 插入 4 个测试用户

### 方法2：手动创建

```bash
# 登录 MySQL
mysql -u root -p

# 输入密码后，在 MySQL 命令行中执行：
```

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS gameengine DEFAULT CHARACTER SET utf8mb4;

-- 使用数据库
USE gameengine;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Username (unique)',
    password VARCHAR(255) NOT NULL COMMENT 'Password',
    nickname VARCHAR(50) COMMENT 'Display nickname',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation time',
    last_login_time TIMESTAMP NULL COMMENT 'Last login time',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

-- 插入测试用户
INSERT INTO users (username, password, nickname) VALUES
('admin', 'admin123', 'Administrator'),
('player1', 'pass123', 'Brave Adventurer'),
('player2', 'pass123', 'Mysterious Explorer'),
('test', 'test', 'Test User');

-- 查看结果
SELECT * FROM users;

-- 退出
exit;
```

### 验证数据库创建

```bash
# 登录并查看
mysql -u root -p

mysql> SHOW DATABASES;
mysql> USE gameengine;
mysql> SHOW TABLES;
mysql> SELECT * FROM users;
mysql> exit;
```

---

## 3. 配置连接

编辑项目中的 `resources/db.properties` 文件：

```properties
# MySQL 数据库连接配置
db.url=jdbc:mysql://localhost:3306/gameengine?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
db.username=root
db.password=你的MySQL密码    # ← 修改这里！填入你的 MySQL root 密码
db.driver=com.mysql.cj.jdbc.Driver
```

**配置说明：**

| 参数 | 说明 | 示例 |
|------|------|------|
| `localhost` | MySQL 服务器地址 | 本地用 `localhost`，远程用 IP 地址 |
| `3306` | MySQL 端口号 | 默认是 3306 |
| `gameengine` | 数据库名称 | 必须与创建的数据库名一致 |
| `useSSL=false` | 关闭 SSL（开发环境） | 生产环境建议设为 `true` |
| `serverTimezone=UTC` | 时区设置 | 避免时区错误 |
| `characterEncoding=utf8` | 字符编码 | 支持中文 |

**其他配置示例：**

```properties
# 远程 MySQL 服务器
db.url=jdbc:mysql://192.168.1.100:3306/gameengine?useSSL=false&serverTimezone=UTC

# 使用非 root 用户
db.username=gameuser
db.password=game123

# MySQL 5.x 使用旧驱动
db.driver=com.mysql.jdbc.Driver
```

---

## 4. 安装 JDBC 驱动

### 方法1：手动下载（推荐）

1. **下载驱动包**

   访问：https://dev.mysql.com/downloads/connector/j/

   - 选择 "Platform Independent"
   - 下载 ZIP 或 TAR 格式

2. **解压并复制 JAR 文件**

   ```bash
   # 解压下载的文件
   unzip mysql-connector-java-8.0.33.zip

   # 复制 JAR 文件到项目根目录
   cp mysql-connector-java-8.0.33/mysql-connector-java-8.0.33.jar .
   ```

3. **验证文件**

   ```bash
   # 检查 JAR 文件
   ls -lh mysql-connector-java-*.jar
   ```

### 方法2：使用 Maven（如果使用 Maven 项目）

在 `pom.xml` 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

### 驱动版本对应关系

| MySQL 版本 | 推荐 JDBC 驱动版本 | 驱动类名 |
|-----------|------------------|---------|
| MySQL 8.x | 8.0.x | `com.mysql.cj.jdbc.Driver` |
| MySQL 5.7 | 5.1.x 或 8.0.x | `com.mysql.cj.jdbc.Driver` |
| MySQL 5.6 | 5.1.x | `com.mysql.jdbc.Driver` |

---

## 5. 运行游戏

### Linux / macOS

```bash
# 方式1：编译并运行（带 JDBC 驱动）
javac -encoding UTF-8 -d bin -sourcepath src src/com/textadventure/game/DemoGame.java

java -cp "bin:mysql-connector-java-8.0.33.jar" \
     -Dfile.encoding=UTF-8 \
     com.textadventure.game.DemoGame

# 方式2：创建运行脚本
cat > run_with_mysql.sh << 'EOF'
#!/bin/bash
java -cp "bin:mysql-connector-java-8.0.33.jar" \
     -Dfile.encoding=UTF-8 \
     com.textadventure.game.DemoGame
EOF

chmod +x run_with_mysql.sh
./run_with_mysql.sh
```

### Windows

```cmd
REM 方式1：直接运行
javac -encoding UTF-8 -d bin -sourcepath src src\com\textadventure\game\DemoGame.java

java -cp "bin;mysql-connector-java-8.0.33.jar" ^
     -Dfile.encoding=UTF-8 ^
     com.textadventure.game.DemoGame

REM 方式2：创建批处理文件 run_with_mysql.bat
@echo off
java -cp "bin;mysql-connector-java-8.0.33.jar" ^
     -Dfile.encoding=UTF-8 ^
     com.textadventure.game.DemoGame
pause
```

### 测试登录

运行游戏后，选择 "1. Login"，使用测试账号：

```
Username: test
Password: test
```

如果登录成功，说明数据库配置正确！

---

## 6. 常见问题

### 问题1：无法连接数据库

**错误信息：**
```
Failed to connect to database: Communications link failure
```

**解决方法：**
1. 检查 MySQL 服务是否启动
   ```bash
   # Linux
   sudo systemctl status mysql

   # macOS
   brew services list

   # Windows
   services.msc（查看 MySQL 服务）
   ```

2. 检查端口是否正确（默认 3306）
   ```bash
   netstat -an | grep 3306
   ```

3. 检查防火墙设置

---

### 问题2：密码错误

**错误信息：**
```
Access denied for user 'root'@'localhost'
```

**解决方法：**
1. 确认 `db.properties` 中的密码正确
2. 尝试重置 MySQL root 密码
   ```bash
   mysql -u root -p
   # 输入旧密码

   ALTER USER 'root'@'localhost' IDENTIFIED BY '新密码';
   FLUSH PRIVILEGES;
   ```

---

### 问题3：找不到 JDBC 驱动

**错误信息：**
```
java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
```

**解决方法：**
1. 确认 JAR 文件在项目根目录
2. 运行时使用 `-cp` 参数包含 JAR 文件
3. 检查 JAR 文件名是否正确

---

### 问题4：时区错误

**错误信息：**
```
The server time zone value 'XXX' is unrecognized
```

**解决方法：**

在 `db.url` 中添加时区参数：
```properties
db.url=jdbc:mysql://localhost:3306/gameengine?serverTimezone=UTC
```

或设置 MySQL 时区：
```sql
SET GLOBAL time_zone = '+8:00';
```

---

### 问题5：中文乱码

**解决方法：**

1. 确保数据库使用 UTF-8 编码
   ```sql
   ALTER DATABASE gameengine CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 在连接 URL 中添加编码参数
   ```properties
   db.url=jdbc:mysql://localhost:3306/gameengine?characterEncoding=utf8
   ```

---

### 问题6：无法创建数据库

**错误信息：**
```
ERROR 1044 (42000): Access denied for user 'root'@'localhost' to database 'gameengine'
```

**解决方法：**

授予 root 用户完整权限：
```sql
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

---

## 📚 额外资源

- MySQL 官方文档：https://dev.mysql.com/doc/
- JDBC 教程：https://docs.oracle.com/javase/tutorial/jdbc/
- MySQL Workbench (图形化工具)：https://www.mysql.com/products/workbench/

---

## 🔒 安全建议

**⚠️ 生产环境注意事项：**

1. **不要使用 root 用户**
   ```sql
   -- 创建专用数据库用户
   CREATE USER 'gameuser'@'localhost' IDENTIFIED BY '强密码';
   GRANT ALL PRIVILEGES ON gameengine.* TO 'gameuser'@'localhost';
   FLUSH PRIVILEGES;
   ```

2. **使用密码哈希**
   - 当前版本使用明文密码仅用于演示
   - 生产环境必须使用 BCrypt 或 SHA-256 加密

3. **启用 SSL 连接**
   ```properties
   db.url=jdbc:mysql://localhost:3306/gameengine?useSSL=true&requireSSL=true
   ```

4. **限制远程访问**
   - 仅允许必要的 IP 地址连接

5. **定期备份数据库**
   ```bash
   mysqldump -u root -p gameengine > backup.sql
   ```

---

配置完成后，享受游戏吧！🎮

如有问题，请查看项目 [README.md](README.md) 或 [sql/README.md](sql/README.md)。
