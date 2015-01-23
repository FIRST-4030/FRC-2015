package org.ingrahamrobotics.robottables;

public class Message {

    public class Type {

        public final static int INVALID = 0;
        public final static int QUERY = 1;
        public final static int ACK = 2;
        public final static int NAK = 3;
        public final static int PUBLISH_ADMIN = 4;
        public final static int DELETE_ADMIN = 5;
        public final static int PUBLISH_USER = 6;
        public final static int DELETE_USER = 7;
        public final static int UPDATE = 8;
        public final static int REQUEST = 9;

        public final static int HIGHEST = REQUEST;
        public final static int LOWEST = QUERY;
    }

    public final static char DELIMITER = '\0';

    private int type;
    private String table;
    private String key;
    private String value;

    public Message(int type, String table, String key, String value) {
        if (type < Type.LOWEST || type > Type.HIGHEST) {
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
        return String.valueOf(type) + DELIMITER + table + DELIMITER + key + DELIMITER + value;
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
            type = Integer.parseInt(raw.substring(0, tableOffset - 1));
        } catch (NumberFormatException ex) {
            type = Type.INVALID;
        }
        table = raw.substring(tableOffset, keyOffset - 1);
        key = raw.substring(keyOffset, valueOffset - 1);
        value = raw.substring(valueOffset);

        // Validation
        if (type < Type.LOWEST || type > Type.HIGHEST) {
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

    public int getType() {
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
