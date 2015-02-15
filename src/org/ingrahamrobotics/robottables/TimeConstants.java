package org.ingrahamrobotics.robottables;

public class TimeConstants {

    /**
     * Time to wait from when publishing a table to sending updates for it.
     */
    public static final long PUBLISH_WAIT_TIME = 200l;

    /**
     * Time between full updates sent.
     */
    public static final long UPDATE_INTERVAL = 5000l;

    /**
     * How long we should wait between update messages during a table update before we assume the update is finished. If
     * this many ms have passed after the last update during a table update, the update is considered finished.
     */
    public static final long MAX_INTERVAL_DURING_UPDATE = 100l;
}
