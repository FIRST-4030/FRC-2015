package org.ingrahamrobotics.robottables.api;

import java.util.List;
import java.util.Set;
import org.ingrahamrobotics.robottables.api.listeners.TableUpdateListener;

public interface RobotTable {

    /**
     * Gets the type of this table. A LOCAL table will be modifiable, and is published locally. A REMOTE table is
     * published by a remote client, as in read-only.
     *
     * @return The type of this table
     */
    public TableType getType();

    /**
     * Gets the time of the last remote update of this table in milliseconds since epoch. Note that if this table is
     * LOCAL, the returned value is always equal to System.currentTimeMillis(). If this is a remote table which hasn't
     * been confirmed existing on the network, this will return -1.
     * <p>
     * TODO: Is having a state for not knowing if the table exists (-1) separate from not being every updated (0)
     * useful?
     *
     * @return System.currentTimeMillis() if {@code getType() == LOCAL}. Last update time if {@code getType() ==
     * REMOTE}.
     */
    public long getLastUpdateTime();

    /**
     * Gets the time of the last reply from a subscriber to the table update in milliseconds since epoch. If this table
     * is REMOTE, the returned value is always equal to System.currentTimeMillis().
     *
     * @return System.currentTimeMillis() if {@code getType() == REMOTE}. Last update time if {@code getType() ==
     * LOCAL}.
     */
    public long getLastSubscriberReply();

    /**
     * Adds a TableUpdateListener to this table. The listener will continue to recieve updates until {@code
     * removeUpdateListener()} is called with the listener.
     *
     * @param listener The listener to add
     */
    public void addUpdateListener(TableUpdateListener listener);

    /**
     * Adds a TableUpdateListener to this table. The listener will continue to recieve updates until {@code
     * removeUpdateListener()} is called with the listener. If {@code initialUpdate == true} the listener will
     * immidietly recieve a new key event for each of the keys which is already known.
     *
     * @param listener The listener to add
     */
    public void addUpdateListener(TableUpdateListener listener, boolean initialUpdate);

    /**
     * Removes a TableUpdateListener from this table. This will do nothing if the listener hasn't been added with {@code
     * addUpdateListener()}. The listener will not recieve updates from this table unless {@code addUpdateListener()} is
     * called again with the listener.
     *
     * @param listener The listener to remove
     */
    public void remoteUpdateListener(TableUpdateListener listener);

    /**
     * Gets the String value stored in this table under a given key
     *
     * @param key The key for the value to retrieve
     * @return The value stored in this table, or null if there is no value stored under the given key.
     */
    public String get(String key);

    /**
     * Gets the string value stored in this table under a given key, defaulting to a given defaultValue.
     *
     * @param key          The key for the value to retrieve
     * @param defaultValue The default value to return if the given key does not exist in this table
     * @return The value stored in this table, or the defaultValue if the key does not exist. key.
     */
    public String get(String key, String defaultValue);

    /**
     * Gets the integer value stored in this table under a given key, defaulting to 0
     *
     * @param key The key for the value to retrieve
     * @return The value stored in this table, or 0 if the given key does not exist, or the value stored is not parsable
     * as an integer
     */
    public int getInt(String key);

    /**
     * Gets the integer value stored in this table under a given key, defaulting to a given defaultValue.
     *
     * @param key          The key for the value to retrieve
     * @param defaultValue The default value to return if the given key does not exist, or the value is not parsable as
     *                     an integer.
     * @return The value stored in this table, or the defaultValue if the given key does not exist, or the value stored
     * is not parsable as an integer.
     */
    public int getInt(String key, int defaultValue);

    /**
     * Gets the double value stored in this table under a given key, defaulting to 0.0
     *
     * @param key The key for the value to retrieve
     * @return The value stored in this table, or 0.0 if the given key does not exist, or the value stored is not
     * parsable as a double
     */
    public double getDouble(String key);

    /**
     * Gets the double value stored in this table under a given key, defaulting to a given defaultValue.
     *
     * @param key          The key for the value to retrieve
     * @param defaultValue The default value to return if the given key does not exist, or the value is not parsable as
     *                     a double.
     * @return The value stored in this table, or the defaultValue if the given key does not exist, or the value stored
     * is not parsable as a double.
     */
    public double getDouble(String key, double defaultValue);

    /**
     * Gets the boolean value stored in this table under a given key, defaulting to false
     *
     * @param key The key for the value to retrieve
     * @return The value stored in this table, or false if the given key does not exist, or the value stored is not
     * parsable as a boolean
     */
    public boolean getBoolean(String key);

    /**
     * Gets the boolean value stored in this table under a given key, defaulting to a given defaultValue.
     *
     * @param key          The key for the value to retrieve
     * @param defaultValue The default value to return if the given key does not exist, or the value is not parsable as
     *                     a boolean.
     * @return The value stored in this table, or the defaultValue if the given key does not exist, or the value stored
     * is not parsable as a boolean.
     */
    public boolean getBoolean(String key, boolean defaultValue);

    /**
     * Gets the long value stored in this table under a given key, defaulting to 0l
     *
     * @param key The key for the value to retrieve
     * @return The value stored in this table, or 0l if the given key does not exist, or the value stored is not
     * parsable as a long
     */
    public long getLong(String key);

    /**
     * Gets the lonng value stored in this table under a given key, defaulting to a given defaultValue.
     *
     * @param key          The key for the value to retrieve
     * @param defaultValue The default value to return if the given key does not exist, or the value is not parsable as
     *                     a long.
     * @return The value stored in this table, or the defaultValue if the given key does not exist, or the value stored
     * is not parsable as a long.
     */
    public long getLong(String key, long defaultValue);

    /**
     * Checks whether or not the given key exists in this table.
     *
     * @param key The key to check
     * @return True if the key exists in this table, false otherwise
     */
    public boolean contains(String key);

    /**
     * Checks whether or not the given key exists in this table, and is parsable as an integer.
     *
     * @param key The key to check
     * @return True if the key exists in this table and is parsable as an integer, false otherwise
     */
    public boolean isInt(String key);

    /**
     * Checks whether or not the given key exists in this table, and is parsable as a double.
     *
     * @param key The key to check
     * @return True if the key exists in this table and is parsable as a double, false otherwise
     */
    public boolean isDouble(String key);

    /**
     * Checks whether or not the given key exists in this table, and is parsable as a boolean.
     *
     * @param key The key to check
     * @return True if the key exists in this table and is parsable as a boolean, false otherwise.
     */
    public boolean isBoolean(String key);

    /**
     * Gets the name for this table.
     *
     * @return this tables 'name'. Case sensitive.
     */
    public String getName();

    /**
     * Sets the given key to the given value
     *
     * @param key   The key to set
     * @param value The value to set the key to
     * @return The old value for this key, if any
     * @throws java.lang.IllegalStateException if {@code getType() != TableType.LOCAL}
     */
    public String set(String key, String value);

    /**
     * Gets the String value stored in the admin namespace in this table under a given key
     *
     * @param key The key for the value to retrieve
     * @return The value stored in the admin space in this table, or null if there is no value stored under the given
     * admin key.
     */
    public String getAdmin(String key);

    /**
     * Sets the given key to the given value in the admin namespace
     *
     * @param key   The key to set
     * @param value The value to set the key to
     * @return The old value for this key, if any
     * @throws java.lang.IllegalStateException if {@code getType() != TableType.LOCAL}
     */
    public String setAdmin(String key, String value);

    /**
     * Checks whether or not the given key exists in this table.
     *
     * @param key The key to check
     * @return True if the key exists in this table, false otherwise
     */
    public boolean containsAdmin(String key);

    /**
     * Gets a copy of all keys
     *
     * @return A new list, or Collections.EMPTY_LIST if empty
     */
    public List<String> getKeys();

    /**
     * Gets an unmodifiable view on the current key set.
     *
     * @return The internal set (unmodifiable), or Collections.EMPTY_SET if empty
     */
    public Set<String> getKeySet();

    /**
     * Gets a copy of all admin keys
     *
     * @return A new list, or Collections.EMPTY_LIST if empty
     */
    public List<String> getAdminKeys();

    /**
     * Gets an unmoifiable view on the current admin key set.
     *
     * @return The internal set (unmodifiable), or Collections.EMPTY_SET if empty
     */
    public Set<String> getAdminKeySet();

    /**
     * Clears all values from this RobotTable.
     */
    public void clear();
}
