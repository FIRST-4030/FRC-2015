package org.ingrahamrobotics.robot2015.output;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import org.ingrahamrobotics.robottables.RobotTables;
import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.RobotTablesClient;

public class Output {

    private static Output instance;
    private RobotTablesClient client;
    private EnumMap<OutputLevel, RobotTable> levelMap;
    private boolean successfullyInitialized;

    public Output() {
        List<InetAddress> addresses;
        try {
            addresses = findValidBroadcastAddresses();
        } catch (SocketException e) {
            e.printStackTrace();
            successfullyInitialized = false;
            return;
        }
        if (addresses == null || addresses.isEmpty()) {
            System.err.println("Failed to find valid broadcast address!");
            successfullyInitialized = false;
            return;
        }
        System.out.printf("Using broadcast addresses: %s%n", addresses);
        RobotTables robotTables;
        try {
            robotTables = new RobotTables(addresses);
        } catch (IOException e) {
            e.printStackTrace();
            successfullyInitialized = false;
            return;
        }
        client = robotTables.getClientInterface();
        RobotTable levelTable = client.publishTable("__output_display_names");

        levelMap = new EnumMap<>(OutputLevel.class);
        for (OutputLevel level : OutputLevel.values()) {
            levelMap.put(level, client.publishTable(level.networkName));
            levelTable.set(level.networkName, level.name);
        }
        robotTables.run();
        successfullyInitialized = true;
    }

    private List<InetAddress> findValidBroadcastAddresses() throws SocketException {
        List<InetAddress> result = new ArrayList<>();
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

    public void log(OutputLevel level, String key, String value) {
        if (!level.enabled) {
            return;
        }
        if (successfullyInitialized) {
            levelMap.get(level).set(key, value);
        } else {
            System.out.printf("Backup logging: [%s][%s] %s%n", level, key, value);
        }
    }

    public static void initInstance() {
        instance = new Output();
    }

    public static void output(OutputLevel level, String key, String value) {
        if (instance == null) {
            throw new IllegalStateException("RobotTables not initialized");
        }
        instance.log(level, key, value);
    }

    public static void output(OutputLevel level, String key, int value) {
        output(level, key, Integer.toString(value));
    }

    public static void output(OutputLevel level, String key, double value) {
        output(level, key, Double.toString(((int) (value * 100)) / 100.0));
    }

    public static void output(OutputLevel level, String key, long value) {
        output(level, key, Long.toString(value));
    }

    public static void output(OutputLevel level, String key, boolean value) {
        output(level, key, value ? "true" : "false");
    }

    public static void output(OutputLevel level, String key, Object value) {
        output(level, key, String.valueOf(value));
    }

    public static void initialized(String system) {
        output(OutputLevel.INITIALIZED_SYSTEMS, system, true);
    }

    public static RobotTablesClient getRobotTables() {
        if (instance == null) {
            throw new IllegalStateException("RobotTables not initialized");
        }
        return instance.client;
    }
}
