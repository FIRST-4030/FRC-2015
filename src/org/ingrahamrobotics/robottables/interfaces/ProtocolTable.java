package org.ingrahamrobotics.robottables.interfaces;

import java.util.Map;
import org.ingrahamrobotics.robottables.api.RobotTable;

public interface ProtocolTable extends RobotTable {

    public void setReadyToPublish(boolean readyToPublish);

    public boolean isReadyToPublish();

    /**
     * This remote table has just been updated.
     */
    public void updatedNow();

    /**
     * A subscriber of this local table has just replied
     */
    public void subscriberRepliedNow();

    /**
     * The existence of this remote table on a network has been confirmed.
     */
    public void existenceConfirmed();

    /**
     * Gets the internal HashMap of user key->value entries.
     * <p>
     * This returns the actual internal map, code that iterates over values should clone/copy the map first.
     */
    public Map<String, String> getUserValues();

    /**
     * Gets the internal HashMap of admin key->value entries.
     * <p>
     * This returns the actual internal map, code that iterates over values should clone/copy the map first.
     */
    public Map<String, String> getAdminValues();
}
