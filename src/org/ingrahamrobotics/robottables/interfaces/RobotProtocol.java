package org.ingrahamrobotics.robottables.interfaces;

import java.util.Map;
import static org.ingrahamrobotics.robottables.Dispatch.DistpachEvents;

public interface RobotProtocol extends DistpachEvents {

    public void sendPublishRequest(String tableName);

    public void sendFullUpdate(String tableName, Map<String, String> tableValues);

    public void sendKeyUpdate(String tableName, String key, String value);

    public void sendKeyDelete(String tableName, String keyName);

    public void setInternalHandler(InternalTableHandler handler);

    public void sendAdminKeyUpdate(String tableName, String key, String value);

    public void sendAdminKeyDelete(String tableName, String key);
}
