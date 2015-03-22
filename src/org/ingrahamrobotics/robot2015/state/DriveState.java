package org.ingrahamrobotics.robot2015.state;

public class DriveState {

    private static boolean waitForWheelsEnabled;
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

    public static boolean isWaitForWheelsEnabled() {
        return waitForWheelsEnabled;
    }

    public static void setWaitForWheelsEnabled(final boolean waitForWheelsEnabled) {
        DriveState.waitForWheelsEnabled = waitForWheelsEnabled;
    }
}
