package org.ingrahamrobotics.robottables;

import java.io.IOException;

import org.ingrahamrobotics.robottables.api.RobotTablesClient;
import org.ingrahamrobotics.robottables.network.IO;
import org.ingrahamrobotics.robottables.network.Queue;
import org.ingrahamrobotics.robottables.network.Queue.QueueEvents;

public class RobotTables implements QueueEvents {

    // Make IO a package-level variable (instead of a local variable) so that it
    // is accessible from the outside
    IO io;
    private Dispatch dispatch;
    private ProtocolHandler protocolHandler;
    private TablesInterfaceHandler tablesInterfaceHandler;

    public void run(String targetAddress) throws IOException {
        // Message queue between listener and dispatch
        Queue queue = new Queue(this);

        io = new IO(targetAddress);

        // Listen for and queue incoming messages
        io.listen(queue);

        dispatch = new Dispatch(queue);

        protocolHandler = new ProtocolHandler(io);

        // Dispatch all messages from the queue to the protocol handler
        dispatch.setAllHandlers(protocolHandler);

        tablesInterfaceHandler = new TablesInterfaceHandler(protocolHandler);

        // Set the internal handler on the protocol handler
        protocolHandler.setInternalHandler(tablesInterfaceHandler);

        (new Thread(dispatch)).start();
    }

    public void queueError(int size, boolean draining, int targetSize) {
        if (!draining) {
            System.err.println("Queue Warning: Large message queue size: "
                    + size);
        } else {
            System.err.println("Queue Error: Drained to size: " + targetSize);
        }
        if (dispatch.currentMessage() == null) {
            System.err.println("\tNo dispatch handler running");
        } else {
            long now = System.currentTimeMillis();
            System.err.println("\tDispatch time: "
                    + (now - dispatch.dispatchTime()) + " ms ago");
            System.err.println("\tDispatch message:\n"
                    + dispatch.currentMessage().displayStr());
        }
    }

    public RobotTablesClient getClientInterface() {
        return tablesInterfaceHandler;
    }

    public void close() {
        io.close();
    }
}
