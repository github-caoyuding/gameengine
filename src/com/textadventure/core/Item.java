package com.textadventure.core;

/**
 * 物品类，表示游戏中的物品
 */
public class Item {
    private String name;
    private String description;
    private boolean canTake;

    public Item(String name, String description) {
        this(name, description, true);
    }

    public Item(String name, String description, boolean canTake) {
        this.name = name;
        this.description = description;
        this.canTake = canTake;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTake() {
        return canTake;
    }

    @Override
    public String toString() {
        return name;
    }
}
