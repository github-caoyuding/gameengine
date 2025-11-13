package com.textadventure.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database Initializer
 * Automatically creates database, tables, and inserts test data
 */
public class DatabaseInitializer {

    /**
     * Initialize database: create database, tables, and insert default data
     * @return true if initialization successful or already initialized
     */
    public static boolean initialize() {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Get connection (may fail if database doesn't exist)
            conn = DBUtil.getConnection();

            if (conn == null) {
                // Connection failed - check if it's because database doesn't exist
                SQLException error = DBUtil.getLastError();
                if (error != null && (error.getMessage().contains("Unknown database") ||
                                      error.getMessage().contains("不存在"))) {
                    System.out.println("Database 'gameengine' doesn't exist. Attempting to create...");
                    return createDatabaseAndRetry();
                } else {
                    System.err.println("Cannot connect to database. Please check your database configuration.");
                    System.err.println("Make sure MySQL is running and credentials in db.properties are correct.");
                    if (error != null) {
                        System.err.println("Error: " + error.getMessage());
                    }
                    return false;
                }
            }

            stmt = conn.createStatement();

            // Check if users table exists
            if (!tableExists(conn, "users")) {
                System.out.println("========================================");
                System.out.println("Initializing database...");
                System.out.println("========================================");
                createTables(stmt);
                insertDefaultData(stmt);
                System.out.println("========================================");
                System.out.println("Database initialization complete!");
                System.out.println("========================================");
                return true;
            } else {
                // Table exists, check if it has data
                if (isTableEmpty(conn, "users")) {
                    System.out.println("Table exists but empty. Inserting default data...");
                    insertDefaultData(stmt);
                    System.out.println("Default data inserted!");
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("========================================");
            System.err.println("Database initialization error!");
            System.err.println("========================================");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("SQL state: " + e.getSQLState());

            // If database doesn't exist, try to create it
            if (e.getMessage().contains("Unknown database") || e.getMessage().contains("不存在")) {
                System.out.println("");
                return createDatabaseAndRetry();
            }

            System.err.println("");
            System.err.println("Please check:");
            System.err.println("  1. MySQL server is running");
            System.err.println("  2. Database 'gameengine' exists");
            System.err.println("  3. User has CREATE/INSERT permissions");
            System.err.println("========================================");
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

    /**
     * Check if table exists
     */
    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        ResultSet rs = null;
        try {
            rs = conn.getMetaData().getTables(null, null, tableName, new String[]{"TABLE"});
            return rs.next();
        } finally {
            if (rs != null) rs.close();
        }
    }

    /**
     * Check if table is empty
     */
    private static boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    /**
     * Create tables
     */
    private static void createTables(Statement stmt) throws SQLException {
        String createUsersTable =
            "CREATE TABLE IF NOT EXISTS users (" +
            "    id INT PRIMARY KEY AUTO_INCREMENT," +
            "    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Username (unique)'," +
            "    password VARCHAR(255) NOT NULL COMMENT 'Password'," +
            "    nickname VARCHAR(50) COMMENT 'Display nickname'," +
            "    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation time'," +
            "    last_login_time TIMESTAMP NULL COMMENT 'Last login time'," +
            "    INDEX idx_username (username)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table'";

        stmt.execute(createUsersTable);
        System.out.println("  ✓ Created 'users' table");
    }

    /**
     * Insert default test data
     */
    private static void insertDefaultData(Statement stmt) throws SQLException {
        String insertUsers =
            "INSERT INTO users (username, password, nickname) VALUES " +
            "('admin', 'admin123', 'Administrator')," +
            "('player1', 'pass123', 'Brave Adventurer')," +
            "('player2', 'pass123', 'Mysterious Explorer')," +
            "('test', 'test', 'Test User')";

        try {
            stmt.execute(insertUsers);
            System.out.println("  ✓ Inserted 4 test users (admin, player1, player2, test)");
        } catch (SQLException e) {
            // Ignore duplicate entry errors
            if (!e.getMessage().contains("Duplicate entry")) {
                throw e;
            }
        }
    }

    /**
     * Create database if it doesn't exist, then retry initialization
     */
    private static boolean createDatabaseAndRetry() {
        System.out.println("Database 'gameengine' not found. Attempting to create...");

        Connection conn = null;
        Statement stmt = null;

        try {
            // Extract base URL (remove database name)
            String configUrl = DBUtil.getUrl();
            String baseUrl = null;

            if (configUrl != null && !configUrl.isEmpty()) {
                System.out.println("  Parsing URL: " + configUrl);

                // Remove database name from URL
                // Example: jdbc:mysql://localhost:3306/gameengine?... -> jdbc:mysql://localhost:3306?...
                if (configUrl.contains("/gameengine")) {
                    baseUrl = configUrl.replace("/gameengine", "");
                    System.out.println("  Base URL: " + baseUrl);
                } else {
                    // Try to remove the last path component
                    int lastSlash = configUrl.lastIndexOf('/');
                    int questionMark = configUrl.indexOf('?');
                    if (lastSlash > 0) {
                        if (questionMark > lastSlash) {
                            // Has parameters: jdbc:mysql://localhost:3306/gameengine?params
                            baseUrl = configUrl.substring(0, lastSlash) + configUrl.substring(questionMark);
                        } else {
                            // No parameters: jdbc:mysql://localhost:3306/gameengine
                            baseUrl = configUrl.substring(0, lastSlash);
                        }
                        System.out.println("  Base URL: " + baseUrl);
                    }
                }
            }

            if (baseUrl == null || baseUrl.isEmpty()) {
                System.err.println("Cannot determine MySQL server URL from configuration.");
                System.err.println("Current URL: " + configUrl);
                System.err.println("Please create the database manually:");
                System.err.println("  mysql -h your_host -u root -p -e \"CREATE DATABASE gameengine;\"");
                return false;
            }

            // Connect to MySQL server without specifying database
            conn = java.sql.DriverManager.getConnection(baseUrl, DBUtil.getUsername(), DBUtil.getPassword());
            stmt = conn.createStatement();

            // Create database
            stmt.execute("CREATE DATABASE IF NOT EXISTS gameengine DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("  ✓ Created 'gameengine' database");

            DBUtil.close(conn, stmt);

            // Retry initialization
            return initialize();

        } catch (SQLException e) {
            System.err.println("Failed to create database: " + e.getMessage());
            System.err.println("");
            System.err.println("Please ensure MySQL is running and create the database manually:");
            System.err.println("  mysql -u root -p -e \"CREATE DATABASE gameengine;\"");
            System.err.println("");
            return false;
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

}
