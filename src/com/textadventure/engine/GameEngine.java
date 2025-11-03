package com.textadventure.engine;

import com.textadventure.core.*;

import java.util.Scanner;

/**
 * 游戏引擎，负责处理游戏循环和命令执行
 */
public class GameEngine {
    private Player player;
    private boolean running;
    private Scanner scanner;
    private WinCondition winCondition;

    public GameEngine(Player player) {
        this.player = player;
        this.running = false;
        this.scanner = new Scanner(System.in);
        this.winCondition = new WinCondition();
    }

    /**
     * 设置胜利条件
     */
    public void setWinCondition(WinCondition winCondition) {
        this.winCondition = winCondition;
    }

    /**
     * 获取胜利条件
     */
    public WinCondition getWinCondition() {
        return winCondition;
    }

    /**
     * 启动游戏
     */
    public void start() {
        running = true;
        printWelcome();

        while (running) {
            System.out.print("\n> ");
            String input = scanner.nextLine();
            Command command = Command.parse(input);

            if (command != null) {
                processCommand(command);

                // 检查胜利条件
                if (running && winCondition != null && winCondition.checkWinCondition(player)) {
                    printVictory();
                    running = false;
                }
            }
        }

        scanner.close();
    }

    /**
     * 打印欢迎信息
     */
    private void printWelcome() {
        System.out.println("==========================================");
        System.out.println("    欢迎来到文本冒险游戏引擎！");
        System.out.println("==========================================");

        if (winCondition != null) {
            System.out.println(winCondition.getDescription());
        }

        System.out.println("\n输入 'help' 查看可用命令");
        System.out.println("\n" + player.getCurrentRoom().getFullDescription());
    }

    /**
     * 处理命令
     */
    private void processCommand(Command command) {
        String verb = command.getVerb();

        switch (verb) {
            case "help":
            case "帮助":
                showHelp();
                break;

            case "look":
            case "观察":
            case "l":
                look();
                break;

            case "go":
            case "前往":
                go(command.getObject());
                break;

            case "north":
            case "south":
            case "east":
            case "west":
            case "up":
            case "down":
            case "n":
            case "s":
            case "e":
            case "w":
            case "u":
            case "d":
            case "北":
            case "南":
            case "东":
            case "西":
            case "上":
            case "下":
                go(verb);
                break;

            case "take":
            case "拿取":
            case "get":
                take(command.getObject());
                break;

            case "drop":
            case "丢弃":
                drop(command.getObject());
                break;

            case "inventory":
            case "背包":
            case "i":
                showInventory();
                break;

            case "examine":
            case "检查":
            case "x":
                examine(command.getObject());
                break;

            case "quit":
            case "exit":
            case "退出":
                quit();
                break;

            case "progress":
            case "进度":
            case "任务":
                showProgress();
                break;

            default:
                System.out.println("我不明白这个命令。输入 'help' 查看可用命令。");
        }
    }

    /**
     * 显示帮助信息
     */
    private void showHelp() {
        System.out.println("\n可用命令:");
        System.out.println("  look/观察 (l)        - 查看当前房间");
        System.out.println("  go <方向>            - 移动到指定方向");
        System.out.println("  north/south/east/west - 直接移动 (或使用 n/s/e/w)");
        System.out.println("  take/拿取 <物品>     - 拿取物品");
        System.out.println("  drop/丢弃 <物品>     - 丢弃物品");
        System.out.println("  inventory/背包 (i)   - 查看背包");
        System.out.println("  examine/检查 <物品>  - 检查物品");
        System.out.println("  progress/进度/任务   - 查看任务进度");
        System.out.println("  help/帮助            - 显示此帮助");
        System.out.println("  quit/退出            - 退出游戏");
    }

    /**
     * 查看当前房间
     */
    private void look() {
        System.out.println("\n" + player.getCurrentRoom().getFullDescription());
    }

    /**
     * 移动到指定方向
     */
    private void go(String directionStr) {
        if (directionStr == null || directionStr.isEmpty()) {
            System.out.println("去哪里？请指定方向。");
            return;
        }

        Direction direction = Direction.fromString(directionStr);
        if (direction == null) {
            System.out.println("无效的方向。");
            return;
        }

        if (player.move(direction)) {
            System.out.println("\n你向" + direction.getChineseName() + "走去...\n");
            look();
        } else {
            System.out.println("那个方向没有出口。");
        }
    }

    /**
     * 拿取物品
     */
    private void take(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            System.out.println("拿取什么？");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.findItem(itemName);

        if (item == null) {
            System.out.println("这里没有这个物品。");
            return;
        }

        if (!item.canTake()) {
            System.out.println("你无法拿取这个物品。");
            return;
        }

        if (player.addItem(item)) {
            currentRoom.removeItem(item);
            System.out.println("你拿起了 " + item.getName());
        } else {
            System.out.println("你的背包已满！");
        }
    }

    /**
     * 丢弃物品
     */
    private void drop(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            System.out.println("丢弃什么？");
            return;
        }

        Item item = player.findItem(itemName);
        if (item == null) {
            System.out.println("你没有这个物品。");
            return;
        }

        player.removeItem(item);
        player.getCurrentRoom().addItem(item);
        System.out.println("你丢下了 " + item.getName());
    }

    /**
     * 查看背包
     */
    private void showInventory() {
        System.out.println("\n你的背包:");
        if (player.getInventory().isEmpty()) {
            System.out.println("  (空)");
        } else {
            for (Item item : player.getInventory()) {
                System.out.println("  - " + item.getName());
            }
        }
    }

    /**
     * 检查物品
     */
    private void examine(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            System.out.println("检查什么？");
            return;
        }

        // 先在背包中查找
        Item item = player.findItem(itemName);

        // 如果背包中没有，在房间中查找
        if (item == null) {
            item = player.getCurrentRoom().findItem(itemName);
        }

        if (item == null) {
            System.out.println("你看不到这个物品。");
            return;
        }

        System.out.println("\n" + item.getName());
        System.out.println(item.getDescription());
    }

    /**
     * 退出游戏
     */
    private void quit() {
        System.out.println("\n感谢游玩！再见！");
        running = false;
    }

    /**
     * 显示任务进度
     */
    private void showProgress() {
        if (winCondition != null) {
            System.out.println(winCondition.getProgress(player));
        } else {
            System.out.println("\n当前游戏没有设定任务目标。");
        }
    }

    /**
     * 打印胜利信息
     */
    private void printVictory() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║                                      ║");
        System.out.println("║          🎉 恭喜你获胜了！ 🎉          ║");
        System.out.println("║                                      ║");
        System.out.println("║   你已经完成了所有任务目标！          ║");
        System.out.println("║                                      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("\n感谢游玩！");
    }
}
