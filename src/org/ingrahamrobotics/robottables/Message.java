package org.ingrahamrobotics.robottables;

import java.util.HashMap;
import java.util.Map;

public class Message {

    public enum Type {
        INVALID(0),
        QUERY(1),
        ACK(2),
        NAK(3),
        PUBLISH_ADMIN(4),
        DELETE_ADMIN(5),
        PUBLISH_USER(6),
        DELETE_USER(7),
        UPDATE(8),
        REQUEST(9);
        public static final Type HIGHEST = REQUEST;
        public static final Type LOWEST = QUERY;
        private static final Map<Integer, Type> intToType = new HashMap<Integer, Type>();

        public final int networkValue;

        private Type(int networkValue) {
            this.networkValue = networkValue;
        }

        public static Type fromInt(int networkValue) {
            Type type = intToType.get(networkValue);
            if (type == null) {
                type = Type.INVALID;
            }
            return type;
        }

        static {
            for (Type type : Type.values()) {
                intToType.put(type.networkValue, type);
            }
        }
    }

    public final static char DELIMITER = '\0';

    private Type type;
    private String table;
    private String key;
    private String value;

    public Message(Type type, String table, String key, String value) {
        if (type == Type.INVALID) {
            throw new IllegalArgumentException("Invalid message type");
        }
        if (table == null || table.length() < 1) {
            throw new IllegalArgumentException("Invalid table name");
        }
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("Invalid key name");
        }
        if (value == null) {
            value = "";
        }

        this.type = type;
        this.table = table;
        this.key = key;
        this.value = value;
    }

    public Message(String raw) {
        parse(raw);
    }

    @Override
    public String toString() {
        return Integer.toString(type.networkValue) + DELIMITER + table + DELIMITER + key + DELIMITER + value;
    }

    private void parse(String raw) {
        // Parsing
        int tableOffset = raw.indexOf(DELIMITER) + 1;
        int keyOffset = raw.indexOf(DELIMITER, tableOffset) + 1;
        int valueOffset = raw.indexOf(DELIMITER, keyOffset) + 1;
        if (tableOffset <= 0 || keyOffset <= tableOffset || valueOffset <= keyOffset) {
            throw new IllegalArgumentException("Invalid message: Bad delimiter count");
        }

        // Extraction
        try {
            type = Type.fromInt(Integer.parseInt(raw.substring(0, tableOffset - 1)));
        } catch (NumberFormatException ex) {
            type = Type.INVALID;
        }
        table = raw.substring(tableOffset, keyOffset - 1);
        key = raw.substring(keyOffset, valueOffset - 1);
        value = raw.substring(valueOffset);

        // Validation
        if (type == Type.INVALID) {
            throw new IllegalArgumentException("Invalid message: Bad type");
        }
        if (table.length() < 1) {
            throw new IllegalArgumentException("Invalid message: Bad table");
        }
        if (key.length() < 1) {
            throw new IllegalArgumentException("Invalid message: Bad key");
        }
    }

    public String displayStr() {
        return "\tTable: " + getTable() + " [Type: " + getType() + "]\n" + "\t" + getKey() + " => " + getValue();
    }

    public String singleLineDisplayStr() {
        return "[Table: " + getTable() + "][Type:" + getType() + "][Key:" + getKey() + "] " + getValue();
    }

    public Type getType() {
        return type;
    }

    public String getTable() {
        return table;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
