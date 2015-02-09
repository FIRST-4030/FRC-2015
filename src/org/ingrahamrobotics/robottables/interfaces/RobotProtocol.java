package org.ingrahamrobotics.robottables.interfaces;

import static org.ingrahamrobotics.robottables.Dispatch.DistpachEvents;

public interface RobotProtocol extends DistpachEvents {

    /**
     * Sends a request to publish this local table.
     *
     * @param tableName The local table
     */
    public void sendPublishRequest(String tableName);

    /**
     * Sends a full update for this local table
     *
     * @param table The local table
     */
    public void sendFullUpdate(ProtocolTable table);

    /**
     * Asks the remote publisher of this table to send a full update
     *
     * @param tableName The remote table name
     */
    public void sendFullUpdateRequest(String tableName);

    public void sendKeyUpdate(String tableName, String key, String value);

    public void sendKeyDelete(String tableName, String keyName);

    public void setInternalHandler(InternalTableHandler handler);

    public void sendAdminKeyUpdate(String tableName, String key, String value);

    public void sendAdminKeyDelete(String tableName, String key);
}
