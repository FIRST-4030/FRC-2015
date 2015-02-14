package org.ingrahamrobotics.robottables.interfaces;

import static org.ingrahamrobotics.robottables.Dispatch.DistpachEvents;

public interface RobotProtocol extends DistpachEvents {

    /**
     * Sends a request to publish a local table. Also starts a timer to send a full update.
     *
     * @param table The local table
     */
    public void sendPublishRequestAndStartTimer(ProtocolTable table);

    /**
     * Sends a full update for a local table.
     *
     * @param table The local table
     */
    public void sendFullUpdate(ProtocolTable table);

    /**
     * Asks if any remote publishers are publishing a table.
     *
     * @param tableName The remote table name
     */
    public void sendExistsQuestionRequest(final String tableName);

    /**
     * Asks the remote publisher of a table to send a full update
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
