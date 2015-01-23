package org.ingrahamrobotics.robottables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
    private final Timer timer = new Timer();

    public TablesInterfaceHandler(final RobotProtocol handler) {
        protocolHandler = handler;
    }

    public void externalPublishedTable(final String tableName) {
        InternalTable airTable = tableMap.get(tableName);

        if (airTable != null) {
            if (airTable.getType() == TableType.LOCAL) {
                // If we are already publishing the table, clear all values, and change the type
                airTable.internalClear();
                TableType oldType = airTable.getType();
                airTable.setType(TableType.REMOTE);
                fireTableTypeChangeEvent(airTable, oldType, TableType.REMOTE);
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
        InternalTable table = (InternalTable) tableMap.get(tableName);
        if (table == null) {
            externalPublishedTable(tableName);
            table = (InternalTable) tableMap.get(tableName);
        }
        table.internalSet(key, newValue);
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
        if (table.isReadyToPublish()) {
            protocolHandler.sendKeyUpdate(table.getName(), key, newValue);
        }
    }

    public void internalAdminKeyUpdated(InternalTable table, String key, String newValue) {
        if (table.isReadyToPublish()) {
            protocolHandler.sendAdminKeyUpdate(table.getName(), key, newValue);
        }
    }

    public void internalKeyRemoved(InternalTable table, String key) {
        if (table.isReadyToPublish()) {
            protocolHandler.sendKeyDelete(table.getName(), key);
        }
    }

    public void internalAdminKeyRemoved(InternalTable table, String key) {
        if (table.isReadyToPublish()) {
            protocolHandler.sendAdminKeyDelete(table.getName(), key);
        }
    }

    public void internalTableCleared(InternalTable table) {
        if (table.isReadyToPublish()) {
            // Just trigger a full update - to show that all values have been removed - the values will have already been cleared
            protocolHandler.sendFullUpdate(table.getName(), table.getInternalValues());
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

    public RobotTable getTable(final String tableName) {
        return tableMap.get(tableName);
    }

    public boolean exists(final String tableName) {
        return tableMap.containsKey(tableName);
    }

    public TableType getTableType(final String tableName) {
        InternalTable table = tableMap.get(tableName);
        return table == null ? null : table.getType();
    }

    public RobotTable publishTable(final String tableName) {
        InternalTable table = tableMap.get(tableName);
        if (table == null) {
            // If we don't know about this table yet, publish it
            protocolHandler.sendPublishRequest(tableName);
            table = new InternalTable(this, tableName, TableType.LOCAL);
            tableMap.put(tableName, table);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    InternalTable table = tableMap.get(tableName);
                    if (table == null) {
                        System.err.println("Warning: Table '" + tableName + "' used to exist, but doesn't anymore.");
                    } else {
                        table.setReadyToPublish(true);
                        protocolHandler.sendFullUpdate(table.getName(), table.getInternalValues());
                    }
                }
            }, TimeConstants.PUBLISH_WAIT_TIME);
        }
        // Return the table we had before, or published
        return table;
    }

    public void addClientListener(final ClientUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeClientListener(final ClientUpdateListener listener) {
        listeners.remove(listener);
    }
}
