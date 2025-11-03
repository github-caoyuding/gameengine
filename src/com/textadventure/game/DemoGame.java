package com.textadventure.game;

import com.textadventure.core.*;
import com.textadventure.engine.GameEngine;
import com.textadventure.engine.WinCondition;

/**
 * 示例游戏 - 神秘洞穴探险
 */
public class DemoGame {

    public static void main(String[] args) {
        // 创建游戏世界
        Room entrance = createGameWorld();

        // 创建玩家
        Player player = new Player("冒险家");
        player.setCurrentRoom(entrance);

        // 创建并设置胜利条件
        WinCondition winCondition = new WinCondition();
        winCondition.setRequiredRoomCount(5);  // 探索所有5个房间
        winCondition.addRequiredItem("王冠");     // 宝藏室的宝物
        winCondition.addRequiredItem("水晶");     // 地下花园的水晶
        winCondition.addRequiredItem("钥匙");     // 黑暗密室的钥匙

        // 启动游戏引擎
        GameEngine engine = new GameEngine(player);
        engine.setWinCondition(winCondition);
        engine.start();
    }

    /**
     * 创建游戏世界
     */
    private static Room createGameWorld() {
        // 创建房间
        Room entrance = new Room("洞穴入口",
            "你站在一个神秘洞穴的入口。阳光从你身后照进来，\n" +
            "洞穴深处传来阵阵回音。石壁上刻着古老的文字。");

        Room hallway = new Room("阴暗走廊",
            "这是一条狭窄的走廊，墙壁潮湿而冰冷。\n" +
            "你可以听到远处滴水的声音。");

        Room treasureRoom = new Room("宝藏室",
            "哇！这是一个宽敞的房间，到处散落着金币和宝石！\n" +
            "看起来你找到了传说中的宝藏！");

        Room darkChamber = new Room("黑暗密室",
            "这里一片漆黑，只有微弱的光线从缝隙中透进来。\n" +
            "空气中弥漫着古老的气息。");

        Room garden = new Room("地下花园",
            "令人惊讶的是，这里居然有一个地下花园！\n" +
            "发光的蘑菇照亮了整个空间，奇异的植物在四处生长。");

        // 设置房间连接
        entrance.setExit(Direction.NORTH, hallway);

        hallway.setExit(Direction.SOUTH, entrance);
        hallway.setExit(Direction.EAST, treasureRoom);
        hallway.setExit(Direction.WEST, darkChamber);

        treasureRoom.setExit(Direction.WEST, hallway);

        darkChamber.setExit(Direction.EAST, hallway);
        darkChamber.setExit(Direction.DOWN, garden);

        garden.setExit(Direction.UP, darkChamber);

        // 添加物品
        entrance.addItem(new Item("火把", "一支可以照明的火把，木柄上缠绕着油布。"));
        entrance.addItem(new Item("石碑", "一块古老的石碑，上面刻着：'勇敢者将获得奖赏'", false));

        hallway.addItem(new Item("绳索", "一条结实的绳索，可能在探险中会用得上。"));

        treasureRoom.addItem(new Item("金币", "闪闪发光的金币，价值不菲！"));
        treasureRoom.addItem(new Item("宝石", "一颗璀璨的蓝宝石，在光线下熠熠生辉。"));
        treasureRoom.addItem(new Item("王冠", "镶嵌着宝石的金色王冠，看起来属于某位国王。"));

        darkChamber.addItem(new Item("蜡烛", "一支老旧的蜡烛，还可以点燃。"));
        darkChamber.addItem(new Item("钥匙", "一把生锈的钥匙，不知道能开什么锁。"));

        garden.addItem(new Item("发光蘑菇", "散发着柔和光芒的蘑菇，可以作为光源。"));
        garden.addItem(new Item("药草", "一株罕见的药草，据说有神奇的治疗效果。"));
        garden.addItem(new Item("水晶", "透明的水晶，内部似乎有能量在流动。"));

        return entrance;
    }
}
