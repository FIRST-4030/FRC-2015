package org.ingrahamrobotics.robottables.api;

import java.util.Collection;
import org.ingrahamrobotics.robottables.api.listeners.ClientUpdateListener;

public interface RobotTablesClient {

    /**
     * Gets a table currently known to this client. The table is not guaranteed to be local or remote. If there are no
     * local or remote tables on this client under the given name, this will return null.
     *
     * @param tableName Table to retrieve
     * @return The table, or null if unknown
     */
    public RobotTable getTable(String tableName);

    /**
     * Gets an unmodifiable version of the internal list of all tables known to this client.
     */
    public Collection<? extends RobotTable> getTableSet();

    /**
     * Checks whether or not this client knows about a given table known to this client.
     *
     * @param tableName Table to check if exists
     * @return True if the table is currently known to this client as a local or remote table, false otherwise.
     */
    public boolean exists(String tableName);

    /**
     * Publishes a table on the network, or retrieves the table we know about (published or subscribed). If another
     * client replies that they are publishing the table, this table will change type from LOCAL to REMOTE within
     * 200ms.
     *
     * @param tableName Table to publish
     * @return The table published / retrieved. You should check that {@code table.getType() == TableType.LOCAL} before
     * modifying.
     */
    public RobotTable publishTable(String tableName);

    /**
     * Subscribes to a given table on the network. If we already know about this table name (remote or local), this will
     * return that table. Otherwise this will return a REMOTE table *which is stale*. The table returned isn't
     * guaranteed to exist on the network, until the table is declared no longer stale.
     * <p>
     * TODO: Is the stale timer how we do want to handle tables we don't know exist on the network yet?
     *
     * @param tableName Table
     */
    public RobotTable subscribeToTable(String tableName);

    /**
     * Adds a ClientUpdateListener to this client. The listener will continue to recieve updates until {@code
     * removeClientListener()} is called with the listener.
     *
     * @param listener The listener to add
     */
    public void addClientListener(ClientUpdateListener listener);

    /**
     * Adds a ClientUpdateListener to this client. The listener will continue to recieve updates until {@code
     * removeClientListener()} is called with the listener. If {@code initialUpdate == true} the listener will
     * immidietly recieve a new table event for each of the tables which is already known.
     *
     * @param listener      The listener to add
     * @param initialUpdate Whether or not to send an initial update
     */
    public void addClientListener(ClientUpdateListener listener, boolean initialUpdate);

    /**
     * Removes a ClientUpdateListener from this client. This will do nothing if the listener hasn't been added with
     * {@code addClientListener()}. The listener will not recieve updates from this client unless {@code
     * addClientListener()} is called again with the listener.
     *
     * @param listener The listener to remove
     */
    public void removeClientListener(ClientUpdateListener listener);
}
