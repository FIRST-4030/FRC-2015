package org.ingrahamrobotics.robottables;

import java.io.IOException;
import java.util.Map;
import org.ingrahamrobotics.robottables.api.TableType;
import org.ingrahamrobotics.robottables.interfaces.InternalTableHandler;
import org.ingrahamrobotics.robottables.interfaces.RobotProtocol;
import org.ingrahamrobotics.robottables.network.IO;

public class ProtocolHandler implements RobotProtocol {

    private InternalTableHandler handler;
    private final IO io;

    public ProtocolHandler(IO io) throws IOException {
        this.io = io;
    }

    public void sendPublishRequest(final String tableName) {
        sendMessage(new Message(Message.Type.QUERY, tableName, "PUBLISH", "_"));
    }

    public void sendFullUpdate(final String tableName, final Map<String, String> tableValues) {

    }

    public void sendKeyUpdate(final String tableName, final String key, final String value) {
        sendMessage(new Message(Message.Type.PUBLISH_USER, tableName, key, value));
    }

    public void sendKeyDelete(final String tableName, final String keyName) {
        sendMessage(new Message(Message.Type.DELETE_USER, tableName, keyName, "_"));
    }

    public void sendAdminKeyUpdate(final String tableName, final String key, final String value) {
        sendMessage(new Message(Message.Type.PUBLISH_ADMIN, tableName, key, value));
    }

    public void sendAdminKeyDelete(final String tableName, final String key) {
        sendMessage(new Message(Message.Type.DELETE_ADMIN, tableName, key, "_"));
    }

    public void sendMessage(final Message message) {
        System.out.println("[Raw] Sending: " + message.toString().replace("\0", "\\0"));
        System.out.println("Sending:\n" + message.displayStr());
        try {
            io.send(message.toString());
        } catch (IOException e) {
            System.err.println("Error sending message '" + message.displayStr() + "'.");
            e.printStackTrace();
        }
    }

    public void dispatch(final Message msg) {
        // Message received, perform action
        TableType tableType = handler.getTableType(msg.getTable());
        switch (msg.getType()) {
            case Message.Type.QUERY:
                boolean isPublish = msg.getKey().equals("PUBLISH");
                if (!isPublish && !msg.getKey().equals("EXISTS")) {
                    System.err.println("Invalid message received: " + msg.displayStr());
                }
                if (tableType == TableType.LOCAL) {
                    sendMessage(new Message(isPublish ? Message.Type.NAK : Message.Type.ACK, msg.getTable(), msg.getKey(), msg.getValue()));
                } else if (isPublish && tableType == null) {
                    // Currently we won't send externalPublishedTable if we know the table is remote already,
                    // perhaps we should change this? In the current setup, the table is only reset when the new
                    // published sends the first full update.
                    handler.externalPublishedTable(msg.getTable());
                }
                break;
            case Message.Type.ACK:
                if (msg.getKey().equals("GENERATION_COUNT") && tableType == TableType.LOCAL) {
                    // TODO: Something should happen here
                } else if (msg.getKey().equals("EXISTS")) {
                    if (tableType == null) {
                        handler.externalPublishedTable(msg.getTable()); // We didn't know this existed before, now we do.
                    } else if (tableType == TableType.LOCAL) {
                        // TODO: Something should happen here
                    }
                }
                break;
            case Message.Type.NAK:
                if (tableType == TableType.LOCAL || tableType == null) {
                    handler.externalPublishedTable(msg.getTable());
                }
                break;
            case Message.Type.PUBLISH_ADMIN:
                handler.externalAdminKeyUpdated(msg.getTable(), msg.getKey(), msg.getValue());
                break;
            case Message.Type.DELETE_ADMIN:
                handler.externalAdminKeyRemoved(msg.getTable(), msg.getKey());
                break;
            case Message.Type.PUBLISH_USER:
                handler.externalKeyUpdated(msg.getTable(), msg.getKey(), msg.getValue());
                break;
            case Message.Type.DELETE_USER:
                handler.externalKeyRemoved(msg.getTable(), msg.getKey());
                break;
        }
        System.out.println("[Raw] Received: " + msg.toString().replace("\0", "\\0"));
        System.out.println("Received:\n" + msg.displayStr());
    }

    public void setInternalHandler(final InternalTableHandler handler) {
        this.handler = handler;
    }
}
