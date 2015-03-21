package org.ingrahamrobotics.robot2015.output;

public enum OutputLevel {
    SWERVE_DEBUG(0, "Swerve", true),
    DEBUG(0, "Debug", false),
    POWER(0, "Power", false),
    INITIALIZED_SYSTEMS(1, "Initialized", false),
    RAW_MOTORS(2, "Raw Motors", true),
    RAW_SENSORS(3, "Raw Sensors", true),
    AUTO(4, "Autonomous", false),
    HIGH(5, "Important", true);

    public final boolean enabled;
    public final int level;
    public final String name;
    public final String networkName;

    private OutputLevel(int level, String name, boolean enabled) {
        this.level = level;
        this.name = name;
        this.enabled = enabled;
        this.networkName = "o:" + level + "-" + name;
    }

    public String toString() {
        return name;
    }
}
