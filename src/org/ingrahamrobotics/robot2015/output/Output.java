package org.ingrahamrobotics.robot2015.output;

import java.io.IOException;
import java.util.EnumMap;

import org.ingrahamrobotics.robottables.RobotTables;
import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.RobotTablesClient;

public class Output {

    // this address is temporary, at best. We need to find a more portable
    // address that still works with the robot router.
    private static final String ADDRESS = "10.40.30.255";
    private static final Object instanceLock = new Object();
    private static Output instance;
    private RobotTablesClient client;
    private EnumMap<OutputLevel, RobotTable> levelMap;
    private boolean successfullyInitialized;

    public Output(String address) {
        RobotTables robotTables = new RobotTables();
        try {
            robotTables.run(address);
        } catch (IOException e) {
            e.printStackTrace();
            successfullyInitialized = false;
            return;
        }
        client = robotTables.getClientInterface();

        levelMap = new EnumMap<OutputLevel, RobotTable>(OutputLevel.class);
        for (OutputLevel level : OutputLevel.values()) {
            levelMap.put(level, client.publishTable(level.name));
        }
        successfullyInitialized = true;
    }

    public void log(OutputLevel level, String key, String value) {
        if (successfullyInitialized) {
            levelMap.get(level).set(key, value);
        } else {
            System.out.printf("Backup logging: [%s][%s] %s%n", level, key,
                    value);
        }
    }

    public static void initInstance(String address) {
        instance = new Output(address);
    }

    public static void output(OutputLevel level, String key, String value) {
        synchronized (instanceLock) {
            if (instance == null) {
                initInstance(ADDRESS);
            }
        }
        instance.log(level, key, value);
    }

    public static void output(OutputLevel level, String key, int value) {
        output(level, key, Integer.toString(value));
    }

    public static void output(OutputLevel level, String key, double value) {
        output(level, key, Double.toString(value));
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
}
