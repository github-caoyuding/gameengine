package com.textadventure.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家类，表示游戏中的玩家
 */
public class Player {
    private String name;
    private Room currentRoom;
    private List<Item> inventory;
    private int maxInventorySize;

    public Player(String name) {
        this(name, 10);
    }

    public Player(String name, int maxInventorySize) {
        this.name = name;
        this.inventory = new ArrayList<>();
        this.maxInventorySize = maxInventorySize;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * 添加物品到背包
     */
    public boolean addItem(Item item) {
        if (inventory.size() >= maxInventorySize) {
            return false;
        }
        inventory.add(item);
        return true;
    }

    /**
     * 从背包移除物品
     */
    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }

    /**
     * 根据名称查找背包中的物品
     */
    public Item findItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 获取背包中的所有物品
     */
    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    /**
     * 移动到指定方向
     */
    public boolean move(Direction direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            return true;
        }
        return false;
    }
}
