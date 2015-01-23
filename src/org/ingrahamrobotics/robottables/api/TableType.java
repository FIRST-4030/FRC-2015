package org.ingrahamrobotics.robottables.api;

// Wish I could make this an enum
public class TableType {

    /**
     * Local table. A table with this type is local and modifiable.
     */
    public static final TableType LOCAL = new TableType("Local");
    /**
     * Remote table. A table with this type is published by a remote client and unmodifiable.
     */
    public static final TableType REMOTE = new TableType("Remote");
    private final String stringValue;

    private TableType(final String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return stringValue;
    }
}
