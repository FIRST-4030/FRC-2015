package org.ingrahamrobotics.robottables.api;

public enum UpdateAction {
    /**
     * Used when a key is assigned a value for the first time in this client
     */
    NEW,
    /**
     * Used when a key is already known to the client, and is updated
     */
    UPDATE,
    /**
     * Used when a key is deleted
     */
    DELETE;
}
