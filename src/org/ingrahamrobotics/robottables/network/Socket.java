package org.ingrahamrobotics.robottables.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;

public class Socket {

    private static final boolean IGNORE_LOCAL = true;
    private static final int MAX_MESSAGE_SIZE = 65535;

    private final DatagramSocket conn;
    private final InetAddress addr;
    private final int port;
    private final HashSet<String> lAddrs = new HashSet<String>();

    public Socket(InetAddress addr, int port) throws IOException {
        this.addr = addr;
        this.port = port;
        try {
            conn = new DatagramSocket(port);
            conn.setBroadcast(true);
            if (IGNORE_LOCAL) {
                getLocalAddrs();
            }
        } catch (SocketException ex) {
            throw new IOException("Unable to initialize: " + ex.toString());
        }
    }

    public int getPort() {
        return port;
    }

    /*
     * Load our list of all local IP addresses, sans the loopback and broadcast
     */
    private void getLocalAddrs() throws SocketException {
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface iface : Collections.list(ifaces)) {
            Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();
            for (InetAddress inetAddr : Collections.list(inetAddrs)) {
                if (inetAddr.isLoopbackAddress() || inetAddr.isAnyLocalAddress()) {
                    continue;
                }
                lAddrs.add(inetAddr.getHostAddress());
            }
        }
    }

    public boolean isOpen() {
        return (conn != null);
    }

    private void throwIfClosed() throws IOException {
        if (!isOpen()) {
            throw new IOException("Socket not open");
        }
    }

    public void close() {
        if (!isOpen()) {
            return;
        }
        conn.close();
    }

    public void send(String str) throws IOException {
        throwIfClosed();

        try {
            DatagramPacket out = new DatagramPacket(str.getBytes(), str.length(), addr, port);
            conn.send(out);
        } catch (IOException ex) {
            throw new IOException("Unable to send: " + ex.toString());
        }
    }

    public String receive() throws IOException {
        throwIfClosed();

        byte[] buf = new byte[MAX_MESSAGE_SIZE];
        DatagramPacket in = null;
        try {
            // Ignore locally-generated packets
            while (in == null || lAddrs.contains(in.getAddress().getHostAddress())) {
                in = new DatagramPacket(buf, buf.length);
                conn.receive(in);
            }
            return new String(buf, 0, in.getLength());
        } catch (IOException ex) {
            throw new IOException("Unable to recv: " + ex.toString());
        }
    }
}
