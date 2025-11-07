package com.textadventure.engine;

import com.textadventure.core.User;
import com.textadventure.database.UserDAO;

import java.util.Scanner;

/**
 * 登录服务类
 * 处理用户登录和注册逻辑
 */
public class LoginService {
    private UserDAO userDAO;
    private Scanner scanner;

    public LoginService() {
        this.userDAO = new UserDAO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * 显示登录菜单并处理用户选择
     * @return 登录成功的用户对象，如果用户退出则返回null
     */
    public User showLoginMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("    Welcome to Text Adventure Game!");
            System.out.println("========================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Guest Mode (Skip Login)");
            System.out.println("4. Exit");
            System.out.println("========================================");
            System.out.print("Please select (1-4): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    User user = handleLogin();
                    if (user != null) {
                        return user;
                    }
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    return createGuestUser();
                case "4":
                    System.out.println("\nGoodbye!");
                    return null;
                default:
                    System.out.println("\nInvalid choice, please try again.");
            }
        }
    }

    /**
     * 处理用户登录
     * @return 登录成功返回用户对象，失败返回null
     */
    private User handleLogin() {
        System.out.println("\n========== User Login ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!");
            return null;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty!");
            return null;
        }

        User user = userDAO.getUserByUsernameAndPassword(username, password);

        if (user != null) {
            // 更新最后登录时间
            userDAO.updateLastLoginTime(user.getId());
            System.out.println("\nLogin successful! Welcome, " +
                (user.getNickname() != null ? user.getNickname() : user.getUsername()) + "!");
            return user;
        } else {
            System.out.println("\nLogin failed! Invalid username or password.");
            return null;
        }
    }

    /**
     * 处理用户注册
     */
    private void handleRegister() {
        System.out.println("\n========== User Registration ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!");
            return;
        }

        // 检查用户名是否已存在
        if (userDAO.isUsernameExists(username)) {
            System.out.println("Username already exists! Please choose another one.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty!");
            return;
        }

        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        System.out.print("Nickname (optional, press Enter to skip): ");
        String nickname = scanner.nextLine().trim();

        if (nickname.isEmpty()) {
            nickname = username;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);

        if (userDAO.registerUser(user)) {
            System.out.println("\nRegistration successful! You can now login.");
        } else {
            System.out.println("\nRegistration failed! Please try again later.");
        }
    }

    /**
     * 创建游客用户
     * @return 游客用户对象
     */
    private User createGuestUser() {
        System.out.println("\nEntering as guest...");
        User guest = new User();
        guest.setUsername("guest");
        guest.setNickname("Guest Player");
        return guest;
    }

    /**
     * 关闭Scanner资源
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
