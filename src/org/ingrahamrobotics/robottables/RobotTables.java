package org.ingrahamrobotics.robottables;

import java.io.IOException;
import java.net.InetAddress;
import org.ingrahamrobotics.robottables.api.RobotTablesClient;
import org.ingrahamrobotics.robottables.network.IO;
import org.ingrahamrobotics.robottables.network.Queue;
import org.ingrahamrobotics.robottables.network.Queue.QueueEvents;

public class RobotTables implements QueueEvents {

    // Make IO a package-level variable (instead of a local variable) so that it is accessible from the outside
    private final IO io;
    private final ProtocolHandler protocolHandler;
    private final TablesInterfaceHandler tablesInterfaceHandler;
    private Dispatch dispatch;

    public RobotTables(InetAddress targetAddress) throws IOException {
        io = new IO(targetAddress);
        protocolHandler = new ProtocolHandler(io);
        tablesInterfaceHandler = new TablesInterfaceHandler(protocolHandler);
        // Set the internal handler on the protocol handler
        protocolHandler.setInternalHandler(tablesInterfaceHandler);
    }

    public void run() {
        // Message queue between listener and dispatch

        Queue queue = new Queue(this);
        // Listen for and queue incoming messages
        io.listen(queue);

        dispatch = new Dispatch(queue);

        // Dispatch all messages from the queue to the protocol handler
        dispatch.setAllHandlers(protocolHandler);

        (new Thread(dispatch)).start();
    }

    public void queueError(int size, boolean draining, int targetSize) {
        if (!draining) {
            System.err.println("Queue Warning: Large message queue size: " + size);
        } else {
            System.err.println("Queue Error: Drained to size: " + targetSize);
        }
        if (dispatch.currentMessage() == null) {
            System.err.println("\tNo dispatch handler running");
        } else {
            long now = System.currentTimeMillis();
            System.err.printf("\tDispatch: [time: %d ms ago] [message: %s]%n", now - dispatch.dispatchTime(),
                    dispatch.currentMessage().singleLineDisplayStr());
        }
    }

    public RobotTablesClient getClientInterface() {
        return tablesInterfaceHandler;
    }

    public void close() {
        io.close();
    }
}
