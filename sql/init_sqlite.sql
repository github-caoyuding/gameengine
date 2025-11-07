-- SQLite version of initialization script

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nickname TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_time DATETIME
);

-- Create index
CREATE INDEX IF NOT EXISTS idx_username ON users(username);

-- Insert sample users
INSERT INTO users (username, password, nickname) VALUES
('admin', 'admin123', 'Administrator'),
('player1', 'pass123', 'Brave Adventurer'),
('player2', 'pass123', 'Mysterious Explorer'),
('test', 'test', 'Test User');

-- Display created data
SELECT * FROM users;
