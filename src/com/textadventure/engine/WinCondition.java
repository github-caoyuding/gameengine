package com.textadventure.engine;

import com.textadventure.core.Item;
import com.textadventure.core.Player;
import com.textadventure.core.Room;

import java.util.HashSet;
import java.util.Set;

/**
 * 胜利条件类，检查玩家是否达成胜利条件
 */
public class WinCondition {
    private int requiredRoomCount;
    private Set<String> requiredItems;
    private boolean isActive;

    public WinCondition() {
        this.requiredItems = new HashSet<>();
        this.requiredRoomCount = 0;
        this.isActive = true;
    }

    /**
     * 设置需要访问的房间数量
     */
    public void setRequiredRoomCount(int count) {
        this.requiredRoomCount = count;
    }

    /**
     * 添加需要收集的物品
     */
    public void addRequiredItem(String itemName) {
        requiredItems.add(itemName);
    }

    /**
     * 停用胜利条件（用于沙盒模式）
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * 检查玩家是否满足胜利条件
     */
    public boolean checkWinCondition(Player player) {
        if (!isActive) {
            return false;
        }

        // 检查房间访问数量
        if (requiredRoomCount > 0 && player.getVisitedRoomsCount() < requiredRoomCount) {
            return false;
        }

        // 检查是否收集了所有必需物品
        if (!requiredItems.isEmpty()) {
            Set<String> collectedItems = new HashSet<>();
            for (Item item : player.getInventory()) {
                collectedItems.add(item.getName());
            }

            for (String requiredItem : requiredItems) {
                if (!collectedItems.contains(requiredItem)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 获取胜利条件的描述
     */
    public String getDescription() {
        if (!isActive) {
            return "（沙盒模式：无胜利条件）";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n========== 任务目标 ==========\n");

        if (requiredRoomCount > 0) {
            sb.append("📍 探索至少 ").append(requiredRoomCount).append(" 个房间\n");
        }

        if (!requiredItems.isEmpty()) {
            sb.append("🎒 收集以下物品:\n");
            for (String item : requiredItems) {
                sb.append("   - ").append(item).append("\n");
            }
        }

        sb.append("============================");
        return sb.toString();
    }

    /**
     * 获取当前进度的描述
     */
    public String getProgress(Player player) {
        if (!isActive) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n---------- 任务进度 ----------\n");

        // 房间探索进度
        if (requiredRoomCount > 0) {
            int visited = player.getVisitedRoomsCount();
            sb.append("📍 房间探索: ").append(visited).append("/").append(requiredRoomCount);
            if (visited >= requiredRoomCount) {
                sb.append(" ✓");
            }
            sb.append("\n");
        }

        // 物品收集进度
        if (!requiredItems.isEmpty()) {
            Set<String> collectedItems = new HashSet<>();
            for (Item item : player.getInventory()) {
                collectedItems.add(item.getName());
            }

            sb.append("🎒 物品收集: ").append(collectedItems.size())
              .append("/").append(requiredItems.size()).append("\n");

            for (String required : requiredItems) {
                sb.append("   ");
                if (collectedItems.contains(required)) {
                    sb.append("✓ ");
                } else {
                    sb.append("✗ ");
                }
                sb.append(required).append("\n");
            }
        }

        sb.append("----------------------------");
        return sb.toString();
    }
}
