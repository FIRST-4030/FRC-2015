package org.ingrahamrobotics.robot2015.autonomous;

import org.ingrahamrobotics.robot2015.utils.ExecuteResult;

public class AutonomousState {

    /**
     * turn radians, -PI to PI
     */
    public final double turnRadians;
    /**
     * Direction to move forward in feet.
     */
    public final double forwardFeet;
    /**
     * Direction to strafe in feet.
     */
    public final double strafeFeet;

    /**
     * @param turnRadians Radians to turn - ignored if either forwardFeet or strafeFeet are non-zero
     * @param forwardFeet Feet to move forward
     * @param strafeFeet  Feet to strafe - ignored if forwardFeet is non-zero
     */
    public AutonomousState(final double turnRadians, final double forwardFeet, final double strafeFeet) {
        this.strafeFeet = strafeFeet;
        this.forwardFeet = forwardFeet;
        this.turnRadians = turnRadians;
    }

    public void start() {
    }

    public void end() {
    }

    public ExecuteResult shouldContinue() {
        return ExecuteResult.NOT_DONE;
    }
}
