package org.ingrahamrobotics.robottables.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class IO {

    private static final int COMMUNICATIONS_PORT = 5805;
    public static final String INSTANCE_UNIQUE = UUID.randomUUID().toString();

    private ListenEvents eventClass;
    private List<Socket> sockets;
    private List<RecvThread> threads;
    private List<InetAddress> lastUsedAddresses;

    public IO() throws IOException {
        List<InetAddress> addresses = findValidBroadcastAddresses();
        System.out.printf("Using broadcast addresses: %s%n", addresses);
        sockets = new ArrayList<Socket>(addresses.size());
        for (InetAddress address : addresses) {
            sockets.add(new Socket(address, COMMUNICATIONS_PORT));
        }
        lastUsedAddresses = addresses;
    }

    private List<InetAddress> findValidBroadcastAddresses() throws SocketException {
        List<InetAddress> result = new ArrayList<InetAddress>();
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface iface : Collections.list(ifaces)) {
            for (InterfaceAddress interfaceAddress : iface.getInterfaceAddresses()) {
                InetAddress inetAddress = interfaceAddress.getAddress();
                if (inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress()) {
                    continue;
                }
                InetAddress broadcastAddress = interfaceAddress.getBroadcast();
                // this might be null (only for IPv6 addresses?)
                if (broadcastAddress != null) {
                    result.add(interfaceAddress.getBroadcast());
                }
            }
        }
        return result;
    }

    public void reconnect(boolean forceRestart) throws IOException {
        List<InetAddress> addresses = findValidBroadcastAddresses();
        if (lastUsedAddresses.containsAll(addresses) && !forceRestart) {
            return;
        }
        System.out.printf("Reconnecting to broadcast addresses: %s%n", addresses);
        List<Socket> newSockets = new ArrayList<Socket>(addresses.size());
        for (InetAddress address : addresses) {
            newSockets.add(new Socket(address, COMMUNICATIONS_PORT));
        }
        List<RecvThread> newThreads = new ArrayList<RecvThread>(sockets.size());

        if (eventClass != null) {
            for (Socket socket : newSockets) {
                RecvThread thread = new RecvThread(socket);
                newThreads.add(thread);
                new Thread(thread).start();
            }
        }
        List<RecvThread> oldThreads = threads;
        List<Socket> oldSockets = sockets;
        threads = newThreads;
        sockets = newSockets;
        for (Socket socket : oldSockets) {
            socket.close();
        }
        for (RecvThread thread : oldThreads) {
            thread.done = true;
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
        threads = new ArrayList<RecvThread>(sockets.size());
        for (Socket socket : sockets) {
            RecvThread thread = new RecvThread(socket);
            threads.add(thread);
            new Thread(thread).start();
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
        private boolean done = false;

        public RecvThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
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
