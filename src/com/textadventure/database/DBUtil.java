package com.textadventure.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC数据库工具类
 * 负责数据库连接的创建和资源的关闭
 */
public class DBUtil {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;
    private static SQLException lastError = null;

    // 静态代码块，加载配置文件
    static {
        loadConfig();
    }

    /**
     * 加载数据库配置文件
     */
    private static void loadConfig() {
        Properties props = new Properties();
        InputStream input = null;

        try {
            // 尝试从resources目录加载配置文件
            input = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");

            // 如果类路径找不到，尝试从文件系统加载
            if (input == null) {
                input = new FileInputStream("resources/db.properties");
            }

            if (input != null) {
                props.load(input);
                url = props.getProperty("db.url");
                username = props.getProperty("db.username");
                password = props.getProperty("db.password");
                driver = props.getProperty("db.driver");

                // 加载JDBC驱动
                if (driver != null && !driver.isEmpty()) {
                    try {
                        Class.forName(driver);
                    } catch (ClassNotFoundException e) {
                        System.err.println("JDBC Driver not found: " + driver);
                        System.err.println("Please add the JDBC driver to your classpath");
                    }
                }
            } else {
                System.err.println("Database configuration file not found!");
                System.err.println("Please create resources/db.properties file");
            }
        } catch (IOException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象，如果连接失败返回null
     */
    public static Connection getConnection() {
        if (url == null || url.isEmpty()) {
            System.err.println("Database URL is not configured!");
            System.err.println("Please configure db.url in resources/db.properties");
            return null;
        }

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // Store the error message for DatabaseInitializer to check
            lastError = e;
            System.err.println("Failed to connect to database: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取最后的连接错误
     * @return 最后的 SQLException，如果没有错误返回 null
     */
    public static SQLException getLastError() {
        return lastError;
    }

    /**
     * 关闭数据库资源
     * @param conn 数据库连接
     * @param stmt PreparedStatement对象
     * @param rs ResultSet对象
     */
    public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭数据库资源 (Statement版本)
     * @param conn 数据库连接
     * @param stmt Statement对象
     * @param rs ResultSet对象
     */
    public static void close(Connection conn, java.sql.Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接和PreparedStatement
     */
    public static void close(Connection conn, PreparedStatement stmt) {
        close(conn, stmt, null);
    }

    /**
     * 关闭连接和Statement
     */
    public static void close(Connection conn, java.sql.Statement stmt) {
        close(conn, stmt, null);
    }

    /**
     * 关闭连接
     */
    public static void close(Connection conn) {
        close(conn, null, null);
    }

    /**
     * 测试数据库连接
     * @return 连接是否成功
     */
    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            close(conn);
            return true;
        }
        return false;
    }
}
