package org.ingrahamrobotics.robot2015.state;

public class DriveState {
    private static boolean driveStrafeDisabled;
    private static boolean squaredInputs;

    public static boolean isDriveStrafeDisabled() {
        return driveStrafeDisabled;
    }

    public static void setDriveStrafeDisabled(boolean driveStrafeDisabled) {
        DriveState.driveStrafeDisabled = driveStrafeDisabled;
    }

    public static boolean isSquaredInputs() {
        return squaredInputs;
    }

    public static void setSquaredInputs(boolean squaredInputs) {
        DriveState.squaredInputs = squaredInputs;
    }
}
