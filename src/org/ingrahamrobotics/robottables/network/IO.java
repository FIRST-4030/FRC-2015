package org.ingrahamrobotics.robottables.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IO {

    private static final int COMMUNICATIONS_PORT = 5805;
    public static final String INSTANCE_UNIQUE = UUID.randomUUID().toString();

    private ListenEvents eventClass;
    private List<Socket> sockets;

    public IO(List<InetAddress> addresses) throws IOException {
        sockets = new ArrayList<Socket>(addresses.size());
        for (InetAddress address : addresses) {
            sockets.add(new Socket(address, COMMUNICATIONS_PORT));
        }
    }

    public void send(String data) throws IOException {
        // Push on all sockets, so other driver stations can recieve as well as the robot.
        for (Socket socket : sockets) {
            if (socket.isOpen()) {
                socket.send(data);
            }
        }
    }

    public void listen(ListenEvents eventClass) {
        this.eventClass = eventClass;
        for (Socket socket : sockets) {
            (new Thread(new RecvThread(socket))).start();
        }
    }

    public void close() {
        this.eventClass = null;
        for (Socket socket : sockets) {
            socket.close();
        }
    }

    private class RecvThread implements Runnable {

        private final Socket socket;

        public RecvThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            boolean done = false;
            while (!done) {
                try {
                    final String message = socket.receive();
                    if (eventClass != null) {
                        eventClass.recv(message);
                    } else {
                        done = true;
                    }
                } catch (IOException ex) {
                    done = true;
                    if (eventClass != null) {
                        eventClass.error(ex.toString());
                    }
                }
            }
        }
    }

    public interface ListenEvents {

        public void recv(String message);

        public void error(String error);
    }
}
