package org.ingrahamrobotics.robottables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.RobotTablesClient;
import org.ingrahamrobotics.robottables.api.TableType;
import org.ingrahamrobotics.robottables.api.listeners.ClientUpdateListener;
import org.ingrahamrobotics.robottables.interfaces.InternalTableHandler;
import org.ingrahamrobotics.robottables.interfaces.RobotProtocol;

public class TablesInterfaceHandler implements RobotTablesClient, InternalTableHandler {

    private final Map<String, InternalTable> tableMap = new HashMap<String, InternalTable>(); // Map from String to InternalTable
    private final List<ClientUpdateListener> listeners = new ArrayList<ClientUpdateListener>(); // List of ClientUpdateListener
    private final RobotProtocol protocolHandler;

    public TablesInterfaceHandler(final RobotProtocol handler) {
        protocolHandler = handler;
    }

    public void externalPublishedTable(final String tableName) {
        InternalTable airTable = tableMap.get(tableName);

        if (airTable != null) {
            if (airTable.getType() == TableType.LOCAL) {
                // If we are already publishing the table, clear all values, and change the type
                airTable.internalClear();
                airTable.setType(TableType.REMOTE);
                fireTableTypeChangeEvent(airTable, TableType.LOCAL, TableType.REMOTE);
            } else {
                throw new IllegalStateException("externalPublishedTable called when external table already known");
                // TODO: Should we clear all values when a already remote table is re-published?
                // In fact, what does it even mean that an external table has been published when the table we know about is remote?
                // Is this even a valid usage of externalPublishedTable?
                // Should we just ignore this?
            }
        } else {
            // If we don't know about this table, create a new one
            airTable = new InternalTable(this, tableName, TableType.REMOTE);
            tableMap.put(tableName, airTable);
            fireNewTableEvent(airTable);
        }
    }

    public void externalKeyUpdated(final String tableName, final String key, final String newValue) {
        // TODO:
        // Assuming that the other client is publishing when we recieve a message from them could lead
        // to a condition where both clients end up thinking that the other is publishing
        // this will also need to be fixed in externalAdminKeyUpdated
        InternalTable table = tableMap.get(tableName);
        if (table == null) {
            externalPublishedTable(tableName);
            table = tableMap.get(tableName);
        }
        table.internalSet(key, newValue);
        table.getProtocolData().internalUserUpdated(key);
    }

    public void externalKeyRemoved(final String tableName, final String key) {
        InternalTable table = tableMap.get(tableName);
        if (table == null) {
            externalPublishedTable(tableName);
        } else { // We don't care about a key being removed for a table that we have no data on, so put it in an else statement
            table.internalSet(key, null);
        }
    }

    public void externalAdminKeyUpdated(final String tableName, final String key, final String newValue) {
        InternalTable table = tableMap.get(tableName);
        if (table == null) {
            externalPublishedTable(tableName);
            table = tableMap.get(tableName);
        }
        table.internalSetAdmin(key, newValue);
        table.getProtocolData().internalAdminUpdated(key);
    }

    public void externalAdminKeyRemoved(final String tableName, final String key) {
        InternalTable table = tableMap.get(tableName);
        if (table == null) {
            externalPublishedTable(tableName);
        } else { // We don't care about a key being removed for a table that we have no data on, so put it in an else statement
            table.internalSetAdmin(key, null);
        }
    }

    public void internalKeyUpdated(InternalTable table, String key, String newValue) {
        if (table.getProtocolData().isReadyToPublish()) {
            protocolHandler.sendKeyUpdate(table.getName(), key, newValue);
        }
    }

    public void internalAdminKeyUpdated(InternalTable table, String key, String newValue) {
        if (table.getProtocolData().isReadyToPublish()) {
            protocolHandler.sendAdminKeyUpdate(table.getName(), key, newValue);
        }
    }

    public void internalKeyRemoved(InternalTable table, String key) {
        if (table.getProtocolData().isReadyToPublish()) {
            protocolHandler.sendKeyDelete(table.getName(), key);
        }
    }

    public void internalAdminKeyRemoved(InternalTable table, String key) {
        if (table.getProtocolData().isReadyToPublish()) {
            protocolHandler.sendAdminKeyDelete(table.getName(), key);
        }
    }

    public void internalTableCleared(InternalTable table) {
        if (table.getProtocolData().isReadyToPublish()) {
            // Just trigger a full update - to show that all values have been removed - the values will have already been cleared
            protocolHandler.sendFullUpdate(table);
        }
    }

    void fireTableTypeChangeEvent(final RobotTable table, final TableType oldType, final TableType newType) {
        for (final ClientUpdateListener listener : listeners) {
            listener.onTableChangeType(table, oldType, newType);
        }
    }

    void fireNewTableEvent(final RobotTable table) {
        for (final ClientUpdateListener listener : listeners) {
            listener.onNewTable(table);
        }
    }

    public void fireStaleEvent(final RobotTable table, final boolean nowStale) {
        for (final ClientUpdateListener listener : listeners) {
            listener.onTableStaleChange(table, nowStale);
        }
    }

    public void fireSubscriberStaleEvent(final RobotTable table, final boolean nowStale) {
        for (final ClientUpdateListener listener : listeners) {
            listener.onAllSubscribersStaleChange(table, nowStale);
        }
    }

    public InternalTable getTable(final String tableName) {
        return tableMap.get(tableName);
    }

    public RobotProtocol getProtocolHandler() {
        return protocolHandler;
    }

    public boolean exists(final String tableName) {
        return tableMap.containsKey(tableName);
    }

    public RobotTable publishTable(final String tableName) {
        InternalTable table = tableMap.get(tableName);
        if (table == null) {
            // If we don't know about this table yet, publish it
            table = new InternalTable(this, tableName, TableType.LOCAL);
            tableMap.put(tableName, table);

            protocolHandler.sendPublishRequestAndStartTimer(table);
        }
        // Return the table we had before, or published
        return table;
    }

    public RobotTable subscribeToTable(final String tableName) {
        InternalTable table = tableMap.get(tableName);
        if (table == null) {
            table = new InternalTable(this, tableName, TableType.REMOTE);
            tableMap.put(tableName, table);
            protocolHandler.sendExistsQuestionRequest(tableName);
        }
        return table;
    }

    public void addClientListener(final ClientUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void addClientListener(final ClientUpdateListener listener, boolean initialUpdate) {
        addClientListener(listener);
        if (initialUpdate) {
            for (RobotTable table : tableMap.values()) {
                listener.onNewTable(table);
            }
        }
    }

    public void removeClientListener(final ClientUpdateListener listener) {
        listeners.remove(listener);
    }
}
