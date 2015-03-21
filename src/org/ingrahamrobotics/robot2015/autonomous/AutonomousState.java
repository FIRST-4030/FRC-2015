package org.ingrahamrobotics.robot2015.autonomous;

import org.ingrahamrobotics.robot2015.output.Settings.Key;
import org.ingrahamrobotics.robot2015.utils.ExecuteResult;

public class AutonomousState {

    /**
     * turn radians, -PI to PI
     */
    private final double turnRadians;
    /**
     * Direction to move forward in feet.
     */
    private final double forwardFeet;
    /**
     * Direction to strafe in feet.
     */
    private double strafeFeet;
    private final Key turnRadiansKey, forwardFeetKey, strafeFeetKey;

    /**
     * @param turnRadians Radians to turn - ignored if either forwardFeet or strafeFeet are non-zero
     * @param forwardFeet Feet to move forward
     * @param strafeFeet  Feet to strafe - ignored if forwardFeet is non-zero
     */
    public AutonomousState(final double turnRadians, final double forwardFeet, final double strafeFeet) {
        this.strafeFeet = strafeFeet;
        this.forwardFeet = forwardFeet;
        this.turnRadians = turnRadians;
        this.strafeFeetKey = null;
        this.forwardFeetKey = null;
        this.turnRadiansKey = null;
    }

    /**
     * @param turnRadians Radians to turn - ignored if either forwardFeet or strafeFeet are non-zero
     * @param forwardFeet Feet to move forward
     * @param strafeFeet  Feet to strafe - ignored if forwardFeet is non-zero
     */
    public AutonomousState(final Key turnRadians, final Key forwardFeet, final Key strafeFeet) {
        this.turnRadians = 0;
        this.forwardFeet = 0;
        this.strafeFeet = 0;
        this.strafeFeetKey = strafeFeet;
        this.forwardFeetKey = forwardFeet;
        this.turnRadiansKey = turnRadians;
    }

    public void start() {
    }

    public void end() {
    }

    public ExecuteResult shouldContinue() {
        return ExecuteResult.NOT_DONE;
    }

    public double getTurnRadians() {
        if (turnRadiansKey != null) {
            return turnRadiansKey.getDouble();
        }
        return turnRadians;
    }

    public double getForwardFeet() {
        if (forwardFeetKey != null) {
            return forwardFeetKey.getDouble();
        }
        return forwardFeet;
    }

    public double getStrafeFeet() {
        if (strafeFeetKey != null) {
            return strafeFeetKey.getDouble();
        }
        return strafeFeet;
    }
}
