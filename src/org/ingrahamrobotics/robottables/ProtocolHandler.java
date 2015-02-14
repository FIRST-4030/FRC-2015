package org.ingrahamrobotics.robottables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.ingrahamrobotics.robottables.api.TableType;
import org.ingrahamrobotics.robottables.interfaces.InternalTableHandler;
import org.ingrahamrobotics.robottables.interfaces.ProtocolTable;
import org.ingrahamrobotics.robottables.interfaces.RobotProtocol;
import org.ingrahamrobotics.robottables.network.IO;

public class ProtocolHandler implements RobotProtocol {

    private InternalTableHandler handler;
    private final Timer timer = new Timer();
    private final IO io;

    public ProtocolHandler(IO io) throws IOException {
        this.io = io;
    }

    public void sendPublishRequestAndStartTimer(final ProtocolTable table) {
        sendMessage(new Message(Message.Type.QUERY, table.getName(), "PUBLISH", "_"));

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (table.getType() == TableType.LOCAL) {
                    table.setReadyToPublish(true);
                    sendFullUpdate(table);
                }
            }
        }, TimeConstants.PUBLISH_WAIT_TIME);
    }

    public void sendFullUpdate(final ProtocolTable table) {
        int generationCount;
        String generationString = table.getAdmin("GENERATION_COUNT");
        if (generationString == null) {
            generationCount = 0;
        } else {
            try {
                generationCount = Integer.parseInt(generationString);
            } catch (NumberFormatException e) {
                System.err.printf("Warning: GENERATION_COUNT of %s is not an integer! Resetting...%n", table.getName());
                generationCount = 0;
            }
        }
        generationCount += 1;
        table.setAdmin("GENERATION_COUNT", String.valueOf(generationCount));

        // Clone in order to avoid ConcurrentModificationExceptions
        List<Map.Entry<String, String>> userValues = new ArrayList<Map.Entry<String, String>>(table.getUserValues().entrySet());

        sendMessage(new Message(Message.Type.UPDATE, table.getName(), "USER", Integer.toString(userValues.size())));
        for (Map.Entry<String, String> entry : userValues) {
            sendKeyUpdate(table.getName(), entry.getKey(), entry.getValue());
        }

        // Clone in order to avoid ConcurrentModificationExceptions
        List<Map.Entry<String, String>> adminValues = new ArrayList<Map.Entry<String, String>>(table.getAdminValues().entrySet());

        sendMessage(new Message(Message.Type.UPDATE, table.getName(), "ADMIN", Integer.toString(adminValues.size())));
        for (Map.Entry<String, String> entry : adminValues) {
            sendAdminKeyUpdate(table.getName(), entry.getKey(), entry.getValue());
        }

        sendMessage(new Message(Message.Type.UPDATE, table.getName(), "END", Integer.toString(userValues.size() + adminValues.size())));
    }

    public void sendExistsQuestionRequest(final String tableName) {
        sendMessage(new Message(Message.Type.QUERY, tableName, "EXISTS", "_"));
    }

    public void sendFullUpdateRequest(final String tableName) {
        // TODO: Should there be any key/value values in a REQUEST message?
        sendMessage(new Message(Message.Type.REQUEST, tableName, "_", "_"));
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
//        System.out.println("[Raw] Sending: " + message.toString().replace("\0", "\\0"));
//        System.out.println("Sending:\n" + message.displayStr());
        System.out.println("[Sending] " + message.singleLineDisplayStr());
        try {
            io.send(message.toString());
        } catch (IOException e) {
            System.err.println("Error sending message '" + message.displayStr() + "'.");
            e.printStackTrace();
        }
    }

    public void dispatch(final Message msg) {
        // Message received, perform action
        ProtocolTable table = handler.getTable(msg.getTable());
        TableType tableType = table == null ? null : table.getType();
        System.out.println("[Received]" + msg.singleLineDisplayStr());
        switch (msg.getType()) {
            case QUERY:
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
                    newRemoteTableResponse(msg.getTable());
                }
                break;
            case ACK:
                if (msg.getKey().equals("GENERATION_COUNT") && tableType == TableType.LOCAL) {
                    // TODO: We should use the GENERATION_COUNT value in this message, and use generation count for stale as well as time.
                    table.subscriberRepliedNow();
                } else if (msg.getKey().equals("EXISTS")) {
                    if (tableType == null) {
                        newRemoteTableResponse(msg.getTable()); // We didn't know this existed before, now we do.
                    } else if (tableType == TableType.LOCAL) {
                        newRemoteTableResponse(msg.getTable());
                        // TODO: Something should happen here
                    } else { // Table is remote
                        if (table.getLastUpdateTime() == -1) {
                            // If we were unsure if this existed on the network
                            table.existenceConfirmed();
                            sendFullUpdateRequest(table.getName());
                        }
                        // TODO: Do we want to do something else here?
                    }
                }
                break;
            case NAK:
                if (tableType == TableType.LOCAL || tableType == null) {
                    newRemoteTableResponse(msg.getTable());
                }
                break;
            case PUBLISH_ADMIN:
                if (table == null) {
                    newRemoteTableResponse(msg.getTable());
                }
                handler.externalAdminKeyUpdated(msg.getTable(), msg.getKey(), msg.getValue());
                break;
            case DELETE_ADMIN:
                if (table == null) {
                    newRemoteTableResponse(msg.getTable());
                } else {
                    handler.externalAdminKeyRemoved(msg.getTable(), msg.getKey());
                }
                break;
            case PUBLISH_USER:
                if (table == null) {
                    newRemoteTableResponse(msg.getTable());
                }
                handler.externalKeyUpdated(msg.getTable(), msg.getKey(), msg.getValue());
                break;
            case DELETE_USER:
                if (table == null) {
                    newRemoteTableResponse(msg.getTable());
                } else {
                    handler.externalKeyRemoved(msg.getTable(), msg.getKey());
                }
                break;
            case REQUEST:
                // TODO: Should we rate limit this in some way?
                if (tableType == TableType.LOCAL) {
                    sendFullUpdate(table);
                }
                break;
        }
    }

    private void newRemoteTableResponse(String tableName) {
        handler.externalPublishedTable(tableName);
        sendFullUpdateRequest(tableName);
    }

    public void setInternalHandler(final InternalTableHandler handler) {
        this.handler = handler;
    }
}
