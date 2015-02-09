package org.ingrahamrobotics.robottables.api;

public enum TableType {
    /**
     * Local table. A table with this type is local and modifiable.
     */
    LOCAL("Local"),
    /**
     * Remote table. A table with this type is published by a remote client and unmodifiable.
     */
    REMOTE("Remote");
    private final String stringValue;

    private TableType(final String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return stringValue;
    }
}
