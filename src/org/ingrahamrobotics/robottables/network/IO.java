package org.ingrahamrobotics.robottables.network;

import java.io.IOException;

public class IO {

    private static final int COMMUNICATIONS_PORT = 5805;

    private ListenEvents eventClass;
    private Socket socket;

    public IO(String address) throws IOException {
        socket = new Socket(address, COMMUNICATIONS_PORT);
    }

    public void send(String data) throws IOException {
        // Push on all sockets, so other driver stations can recieve as well as the robot.
        if (socket.isOpen()) {
            socket.send(data);
        }
    }

    public void listen(ListenEvents eventClass) {
        this.eventClass = eventClass;
        (new Thread(new RecvThread(socket))).start();
    }

    public void close() {
        this.eventClass = null;
        socket.close();
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
