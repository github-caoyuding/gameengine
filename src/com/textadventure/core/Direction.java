package com.textadventure.core;

/**
 * 方向枚举，表示房间之间的连接方向
 */
public enum Direction {
    NORTH("北"),
    SOUTH("南"),
    EAST("东"),
    WEST("西"),
    UP("上"),
    DOWN("下");

    private final String chineseName;

    Direction(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }

    /**
     * 根据字符串获取方向
     */
    public static Direction fromString(String str) {
        str = str.toLowerCase().trim();
        switch (str) {
            case "north":
            case "n":
            case "北":
                return NORTH;
            case "south":
            case "s":
            case "南":
                return SOUTH;
            case "east":
            case "e":
            case "东":
                return EAST;
            case "west":
            case "w":
            case "西":
                return WEST;
            case "up":
            case "u":
            case "上":
                return UP;
            case "down":
            case "d":
            case "下":
                return DOWN;
            default:
                return null;
        }
    }
}
