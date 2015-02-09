package org.ingrahamrobotics.robottables.api;

// Wish we could enum
public class UpdateAction {

    /**
     * Used when a key is assigned a value for the first time in this client
     */
    public static final UpdateAction NEW = new UpdateAction(0);
    /**
     * Used when a key is already known to the client, and is updated
     */
    public static final UpdateAction UPDATE = new UpdateAction(1);
    /**
     * Used when a key is deleted
     */
    public static final UpdateAction DELETE = new UpdateAction(2);
    private final int value;

    private UpdateAction(final int value) {
        this.value = value;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateAction)) return false;

        UpdateAction action = (UpdateAction) o;

        if (value != action.value) return false;

        return true;
    }

    public int hashCode() {
        return (int) value;
    }
}
