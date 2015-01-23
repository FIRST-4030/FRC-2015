package org.ingrahamrobotics.robottables.api;

public interface TableCallback {

    /**
     * Run the callback with the given parameter
     *
     * @param table Table parameter to this callback
     */
    public void run(RobotTable table);
}
