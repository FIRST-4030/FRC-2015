package org.ingrahamrobotics.robottables.api.listeners;

import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.TableType;

// TODO: This could be called PublisherEventListener as well, as it contains events for table publishers
public interface ClientUpdateListener {

    // TODO: Should table type change & table stale events be in TableUpdateListener or ClientUpdateListener?

    /**
     * Fired when a table's type changes. Typically this happens when a remote client takes over publishing a table this
     * client used to publish.
     *
     * @param table   Table who's type changed
     * @param oldType Type table is changing from
     * @param newType Type table is changing to
     */
    public void onTableChangeType(RobotTable table, TableType oldType, TableType newType);

    /**
     * Fired when no table updates have been given in 2.1x the specified table update time, with nowStale = true. When
     * the table is updated after the table was previously considered stale, this is fired again with nowStale = false.
     *
     * @param table The table that is now stale
     */
    public void onTableStaleChange(RobotTable table, boolean nowStale);

    /**
     * Fired when no subscribers have replied to the last 2 table updates with nowStale = true. When a subscriber
     * replies for the first time after the table was already considered subscriberStale, this event is fired again with
     * nowStale = false.
     *
     * @param table Table who's subscribers are stale
     */
    public void onAllSubscribersStaleChange(RobotTable table, boolean nowStale);

    /**
     * Fired when a new table is created. Note that this is fired for both local and remote tables.
     *
     * @param table New table created
     */
    public void onNewTable(RobotTable table);
}
