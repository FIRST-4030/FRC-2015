package org.ingrahamrobotics.robottables.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Socket {

    private static final int MAX_MESSAGE_SIZE = 65535;

    private final DatagramSocket conn;
    private final InetAddress addr;
    private final int port;

    public Socket(InetAddress addr, int port) throws IOException {
        this.addr = addr;
        this.port = port;
        try {
            conn = new DatagramSocket(null);
            conn.setReuseAddress(true);
            conn.bind(new InetSocketAddress(port));
            conn.setBroadcast(true);
        } catch (SocketException ex) {
            throw new IOException("Unable to initialize: " + ex.toString());
        }
    }

    public int getPort() {
        return port;
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

        str += "\0" + IO.INSTANCE_UNIQUE;

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
        String messageUnique = null;
        String data = null;
        try {
            // Ignore locally-generated packets
            while (in == null || messageUnique.equals(IO.INSTANCE_UNIQUE)) {
                in = new DatagramPacket(buf, buf.length);
                conn.receive(in);
                String raw = new String(buf, 0, in.getLength());
                data = raw.substring(0, raw.lastIndexOf('\0'));
                messageUnique = raw.substring(raw.lastIndexOf('\0') + 1);
            }
            return data;
        } catch (IOException ex) {
            throw new IOException("Unable to recv: " + ex.toString());
        }
    }
}
