# Database Setup Instructions

## Prerequisites

You need a database server installed. Supported databases:
- MySQL 5.7+ / MariaDB 10.3+
- PostgreSQL 9.6+
- SQLite 3+

## MySQL Setup

### 1. Create Database

```bash
# Login to MySQL
mysql -u root -p

# Run the initialization script
source sql/init.sql
```

Or directly:

```bash
mysql -u root -p < sql/init.sql
```

### 2. Configure Connection

Edit `resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/gameengine?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_password
db.driver=com.mysql.cj.jdbc.Driver
```

### 3. Add MySQL JDBC Driver

Download MySQL Connector/J from:
https://dev.mysql.com/downloads/connector/j/

Add the JAR file to your classpath when compiling:

```bash
javac -cp ".:mysql-connector-java-8.0.xx.jar" -encoding UTF-8 -d bin -sourcepath src src/com/textadventure/game/DemoGame.java
```

Run with:

```bash
java -cp "bin:mysql-connector-java-8.0.xx.jar" -Dfile.encoding=UTF-8 com.textadventure.game.DemoGame
```

## PostgreSQL Setup

### 1. Create Database and Table

```bash
psql -U postgres

CREATE DATABASE gameengine;
\c gameengine

-- Then run the init.sql (modify AUTO_INCREMENT to SERIAL)
```

### 2. Configure Connection

```properties
db.url=jdbc:postgresql://localhost:5432/gameengine
db.username=postgres
db.password=your_password
db.driver=org.postgresql.Driver
```

## SQLite Setup (Simplest for Testing)

### 1. Create Database

```bash
sqlite3 gameengine.db < sql/init_sqlite.sql
```

### 2. Configure Connection

```properties
db.url=jdbc:sqlite:gameengine.db
db.username=
db.password=
db.driver=org.sqlite.JDBC
```

## Default Test Accounts

| Username | Password | Nickname |
|----------|----------|----------|
| admin | admin123 | Administrator |
| player1 | pass123 | Brave Adventurer |
| player2 | pass123 | Mysterious Explorer |
| test | test | Test User |

## Security Notes

**⚠️ IMPORTANT:** The sample users have plain text passwords for demonstration only.

In production, you should:
1. Use password hashing (BCrypt, SHA-256, etc.)
2. Never store plain text passwords
3. Use strong, unique passwords
4. Implement password strength requirements
5. Add salt to password hashes

## Troubleshooting

### Connection Failed

1. Check if database server is running
2. Verify credentials in `db.properties`
3. Ensure JDBC driver is in classpath
4. Check firewall settings

### Driver Not Found

Make sure the JDBC driver JAR is in your classpath during both compilation and runtime.
