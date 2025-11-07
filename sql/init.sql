-- ========================================
-- Text Adventure Game Database Initialization Script
-- MySQL Version
-- ========================================
-- This script will:
--   1. Create 'gameengine' database
--   2. Create 'users' table
--   3. Insert sample test users
-- ========================================

-- Create database
CREATE DATABASE IF NOT EXISTS gameengine DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE gameengine;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Username (unique)',
    password VARCHAR(255) NOT NULL COMMENT 'Password',
    nickname VARCHAR(50) COMMENT 'Display nickname',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation time',
    last_login_time TIMESTAMP NULL COMMENT 'Last login time',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

-- Insert sample users (password is plain text for demo, should use encryption in production)
INSERT INTO users (username, password, nickname) VALUES
('admin', 'admin123', 'Administrator'),
('player1', 'pass123', 'Brave Adventurer'),
('player2', 'pass123', 'Mysterious Explorer'),
('test', 'test', 'Test User');

-- Display created data
SELECT * FROM users;
