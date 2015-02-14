package org.ingrahamrobotics.robottables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.TableType;
import org.ingrahamrobotics.robottables.api.UpdateAction;
import org.ingrahamrobotics.robottables.api.listeners.TableUpdateListener;
import org.ingrahamrobotics.robottables.interfaces.InternalTableHandler;
import org.ingrahamrobotics.robottables.interfaces.ProtocolTable;

public class InternalTable implements RobotTable, ProtocolTable {

    private final InternalTableHandler robotTables;
    private final Map<String, String> valueMap = new HashMap<String, String>(); // Map from String to String
    private final Map<String, String> adminMap = new HashMap<String, String>(); // Map from String to String
    private final List<TableUpdateListener> listeners = new ArrayList<TableUpdateListener>(); // List of TableUpdateListener
    private TableType type;
    private final String name;
    private long lastUpdate = -1; // -1 for not confirmed existing on network. 0 for confirmed existing on network, but never updated.
    /**
     * Last time a subscriber replied to a table update message.
     * <p>
     * TODO: Handle subsriber last generation count for stale as well, not only time.
     */
    private long lastSubscriberReply;
    /**
     * Whether this table is confirmed to be owned by us. This is only used internally.
     */
    private boolean readyToPublish;

    public InternalTable(final InternalTableHandler tables, final String name, final TableType initialType) {
        robotTables = tables;
        this.type = initialType;
        this.name = name;
    }

    public TableType getType() {
        return type;
    }

    void setType(TableType type) {
        this.type = type;
    }

    public long getLastUpdateTime() {
        if (type == TableType.LOCAL) {
            return System.currentTimeMillis();
        } else {
            return lastUpdate;
        }
    }

    public long getLastSubscriberReply() {
        if (type == TableType.LOCAL) {
            return lastSubscriberReply;
        } else {
            return System.currentTimeMillis();
        }
    }

    public String getName() {
        return name;
    }

    public void updatedNow() {
        lastUpdate = System.currentTimeMillis();
        // TODO: Fire stale event here
    }

    public void subscriberRepliedNow() {
        lastSubscriberReply = System.currentTimeMillis();
        // TODO: Fire stale event here
    }

    public void existenceConfirmed() {
        if (lastUpdate == -1) {
            lastUpdate = 0;
        }
    }

    public void setReadyToPublish(final boolean readyToPublish) {
        this.readyToPublish = readyToPublish;
    }

    public boolean isReadyToPublish() {
        return readyToPublish;
    }

    public void addUpdateListener(final TableUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void addUpdateListener(final TableUpdateListener listener, boolean initialUpdate) {
        addUpdateListener(listener);
        if (initialUpdate) {
            for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                listener.onUpdate(this, entry.getKey(), entry.getValue(), UpdateAction.NEW);
            }
            for (Map.Entry<String, String> entry : adminMap.entrySet()) {
                listener.onUpdateAdmin(this, entry.getKey(), entry.getValue(), UpdateAction.NEW);
            }
        }
    }

    public void remoteUpdateListener(final TableUpdateListener listener) {
        listeners.remove(listener);
    }

    public String get(final String key) {
        return valueMap.get(key);
    }

    public String get(final String key, final String defaultValue) {
        String value = valueMap.get(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public int getInt(final String key) {
        String str = valueMap.get(key);
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public int getInt(final String key, final int defaultValue) {
        String str = valueMap.get(key);
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public double getDouble(final String key) {
        String str = valueMap.get(key);
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    public double getDouble(final String key, final double defaultValue) {
        String str = valueMap.get(key);
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public boolean getBoolean(final String key) {
        String str = valueMap.get(key);
        return ((str != null) && str.equalsIgnoreCase("true"));
    }

    public boolean getBoolean(final String key, final boolean defaultValue) {
        String str = valueMap.get(key);
        return (str != null) ? str.equalsIgnoreCase("true") : defaultValue;
    }

    public long getLong(final String key) {
        String str = valueMap.get(key);
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            return 0l;
        }
    }

    public long getLong(final String key, final long defaultValue) {
        String str = valueMap.get(key);
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public boolean contains(final String key) {
        return valueMap.containsKey(key);
    }

    public boolean isInt(final String key) {
        String str = valueMap.get(key);
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean isDouble(final String key) {
        String str = valueMap.get(key);
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean isBoolean(final String key) {
        String str = valueMap.get(key);
        return str != null && (str.equals("true") || str.equals("false"));
    }

    public String set(final String key, final String value) {
        ensureLocal();
        String oldValue = valueMap.get(key);
        if (value == null) {
            if (oldValue != null) {
                // If we do have a value, remove it
                valueMap.remove(key);
                sendUpdateEvent(key, null, UpdateAction.DELETE);
                robotTables.internalKeyRemoved(this, key);
            }
        } else {
            if (oldValue == null || !value.equals(oldValue)) {
                // If the value isn't there, or has changed.
                valueMap.put(key, value);
                sendUpdateEvent(key, value, (oldValue == null) ? UpdateAction.NEW : UpdateAction.UPDATE);
                robotTables.internalKeyUpdated(this, key, value);
            }
        }
        return oldValue;
    }

    public String getAdmin(final String key) {
        return adminMap.get(key);
    }

    public String setAdmin(final String key, final String value) {
        ensureLocal();
        String oldValue = adminMap.get(key);
        if (value == null) {
            if (oldValue != null) {
                // If we do have a value, remove it
                adminMap.remove(key);
                sendUpdateAdminEvent(key, null, UpdateAction.DELETE);
                robotTables.internalAdminKeyRemoved(this, key);
            }
        } else {
            if (oldValue == null || !value.equals(oldValue)) {
                // If the value isn't there, or has changed.
                adminMap.put(key, value);
                sendUpdateAdminEvent(key, value, (oldValue == null) ? UpdateAction.NEW : UpdateAction.UPDATE);
                robotTables.internalAdminKeyUpdated(this, key, value);
            }
        }
        return oldValue;
    }

    public boolean containsAdmin(final String key) {
        return adminMap.containsKey(key);
    }

    public List<String> getKeys() {
        if (valueMap.isEmpty()) {
            return Collections.emptyList();
        } else {
            return new ArrayList<String>(valueMap.keySet());
        }
    }

    public void clear() {
        ensureLocal();
        if (!valueMap.isEmpty()) {
            valueMap.clear();
            robotTables.internalTableCleared(this);
            sendClearTableEvent();
        }
    }

    private void ensureLocal() {
        if (type != TableType.LOCAL) {
            throw new IllegalStateException("Table is remote and unmodifiable");
        }
    }

    /**
     * Sets key to value, for internal use only. Doesn't call internal methods on the TablesInterfaceHandler. A null
     * value will result in the key being removed
     */
    public void internalSet(final String key, final String value) {
        String oldValue = valueMap.get(key);
        if (value == null) {
            if (oldValue != null) {
                valueMap.remove(key);
                sendUpdateEvent(key, null, UpdateAction.DELETE);
            }
        } else {
            if (oldValue == null || !value.equals(oldValue)) {
                valueMap.put(key, value);
                sendUpdateEvent(key, value, (oldValue == null) ? UpdateAction.NEW : UpdateAction.UPDATE);
            }
        }
    }

    /**
     * Sets key to value in the admin namespace, for internal use only. Doesn't call internal methods on the
     * TablesInterfaceHandler. A null value will result in the key being removed
     */
    public void internalSetAdmin(final String key, final String value) {
        String oldValue = valueMap.get(key);
        if (value == null) {
            if (oldValue != null) {
                adminMap.remove(key);
                sendUpdateAdminEvent(key, null, UpdateAction.DELETE);
            }
        } else {
            if (oldValue == null || !value.equals(oldValue)) {
                adminMap.put(key, value);
                sendUpdateAdminEvent(key, value, (oldValue == null) ? UpdateAction.NEW : UpdateAction.UPDATE);
            }
        }
    }

    /**
     * Clears the table, for internal use only. Doesn't call internal methods on the TablesInterfaceHandler.
     */
    public void internalClear() {
        if (!valueMap.isEmpty()) {
            valueMap.clear();
            sendClearTableEvent();
        }
    }

    public Map<String, String> getUserValues() {
        return valueMap;
    }

    public Map<String, String> getAdminValues() {
        return adminMap;
    }

    private void sendUpdateEvent(final String key, final String value, final UpdateAction action) {
        for (TableUpdateListener listener : listeners) {
            listener.onUpdate(this, key, value, action);
        }
    }

    private void sendUpdateAdminEvent(final String key, final String value, final UpdateAction action) {
        for (TableUpdateListener listener : listeners) {
            listener.onUpdateAdmin(this, key, value, action);
        }
    }

    private void sendClearTableEvent() {
        for (TableUpdateListener listener : listeners) {
            listener.onTableCleared(this);
        }
    }
}
