package com.textadventure.engine;

/**
 * 命令类，表示玩家输入的命令
 */
public class Command {
    private String verb;
    private String object;

    public Command(String verb, String object) {
        this.verb = verb;
        this.object = object;
    }

    public String getVerb() {
        return verb;
    }

    public String getObject() {
        return object;
    }

    public boolean hasObject() {
        return object != null && !object.isEmpty();
    }

    /**
     * 从输入字符串解析命令
     */
    public static Command parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] parts = input.trim().split("\\s+", 2);
        String verb = parts[0].toLowerCase();
        String object = parts.length > 1 ? parts[1] : null;

        return new Command(verb, object);
    }
}
