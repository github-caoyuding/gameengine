package com.textadventure.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房间类，表示游戏中的一个地点
 */
public class Room {
    private String name;
    private String description;
    private Map<Direction, Room> exits;
    private List<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 设置某个方向的出口
     */
    public void setExit(Direction direction, Room room) {
        exits.put(direction, room);
    }

    /**
     * 获取某个方向的房间
     */
    public Room getExit(Direction direction) {
        return exits.get(direction);
    }

    /**
     * 获取所有可用的出口
     */
    public Map<Direction, Room> getExits() {
        return exits;
    }

    /**
     * 添加物品到房间
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * 从房间移除物品
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * 根据名称查找物品
     */
    public Item findItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 获取房间中的所有物品
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * 获取房间的完整描述信息
     */
    public String getFullDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(description).append("\n\n");

        // 显示出口
        if (!exits.isEmpty()) {
            sb.append("出口: ");
            for (Direction dir : exits.keySet()) {
                sb.append(dir.getChineseName()).append(" ");
            }
            sb.append("\n");
        }

        // 显示物品
        if (!items.isEmpty()) {
            sb.append("物品: ");
            for (Item item : items) {
                sb.append(item.getName()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
