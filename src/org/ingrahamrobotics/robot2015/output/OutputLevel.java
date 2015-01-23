package org.ingrahamrobotics.robot2015.output;

public enum OutputLevel {
    DEBUG(-1, "Debug"),
    INITIALIZED_SYSTEMS(0, "Initialized"),
    RAW_MOTORS(0, "Raw Motors"),
    RAW_SENSORS(0, "Raw Sensors"),
    CMU(0, "CMUcam"),
    AUTO(0, "Autonomous"),
    HIGH(0, "Important");

    public final int level;
    public final String name;

    private OutputLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
