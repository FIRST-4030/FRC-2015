package org.ingrahamrobotics.robot2015.output;

public enum OutputLevel {
    SWERVE_DEBUG(0, "Swerve"),
    DEBUG(0, "Debug"),
    POWER(0, "Power"),
    INITIALIZED_SYSTEMS(1, "Initialized"),
    RAW_MOTORS(2, "Raw Motors"),
    RAW_SENSORS(3, "Raw Sensors"),
    AUTO(4, "Autonomous"),
    HIGH(5, "Important");

    public final int level;
    public final String name;
    public final String networkName;

    private OutputLevel(int level, String name) {
        this.level = level;
        this.name = name;
        this.networkName = "o:" + level + "-" + name;
    }

    public String toString() {
        return name;
    }
}
