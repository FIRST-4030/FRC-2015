package org.ingrahamrobotics.robottables.interfaces;

import org.ingrahamrobotics.robottables.api.RobotTable;

public interface ProtocolTable extends RobotTable {

    public void setReadyToPublish(boolean readyToPublish);

    public boolean isReadyToPublish();

    public void updatedNow();
}
