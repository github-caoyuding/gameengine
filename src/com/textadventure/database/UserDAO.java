package com.textadventure.database;

import com.textadventure.core.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 用户数据访问对象（DAO）
 * 负责用户相关的数据库操作
 */
public class UserDAO {

    /**
     * 根据用户名和密码查询用户（用于登录验证）
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，如果不存在或密码错误则返回null
     */
    public User getUserByUsernameAndPassword(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return null;
            }

            String sql = "SELECT id, username, password, nickname, create_time, last_login_time " +
                        "FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                user.setLastLoginTime(rs.getTimestamp("last_login_time"));
            }
        } catch (SQLException e) {
            System.err.println("Database query error: " + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return user;
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象，如果不存在返回null
     */
    public User getUserByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return null;
            }

            String sql = "SELECT id, username, password, nickname, create_time, last_login_time " +
                        "FROM users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                user.setLastLoginTime(rs.getTimestamp("last_login_time"));
            }
        } catch (SQLException e) {
            System.err.println("Database query error: " + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return user;
    }

    /**
     * 注册新用户
     * @param user 用户对象
     * @return 是否注册成功
     */
    public boolean registerUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return false;
            }

            String sql = "INSERT INTO users (username, password, nickname, create_time) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getNickname());
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Failed to register user: " + e.getMessage());
            return false;
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     * @return 是否更新成功
     */
    public boolean updateLastLoginTime(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return false;
            }

            String sql = "UPDATE users SET last_login_time = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(2, userId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update login time: " + e.getMessage());
            return false;
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    public boolean isUsernameExists(String username) {
        return getUserByUsername(username) != null;
    }
}
