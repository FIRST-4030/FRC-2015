package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.commands.RunPIDDrive;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.output.Settings;

/**
 * Oversees the PIDDrive and PIDSteer Subsystems.
 * <p>
 * Commands should point to this class rather than PIDDrive or PIDSteer.
 */
public class DriveBase extends Subsystem {

    private boolean wasStillLast;
    public final PIDSteer[] steerSystem;
    public final SpeedDrive[] driveSystem;

    private final int frontTrackWidth = 37;
    private final int backTrackWidth = 37;
    private final int frontWheelBase = 22;
    private final int backWheelBase = 22;
    private final double frontRadius = Math.sqrt(frontTrackWidth ^ 2 + frontWheelBase ^ 2);
    private final double backRadius = Math.sqrt(backTrackWidth ^ 2 + backWheelBase ^ 2);

    public DriveBase() {
        driveSystem = new SpeedDrive[]{
                Subsystems.pidDrive1,
                Subsystems.pidDrive2,
                Subsystems.pidDrive3,
                Subsystems.pidDrive4,
        };
        steerSystem = new PIDSteer[]{
                Subsystems.pidSteer1,
                Subsystems.pidSteer2,
                Subsystems.pidSteer3,
                Subsystems.pidSteer4,
        };
        Output.initialized("DriveBase");
    }

    /**
     * @param movementDirection Direction to move in in radians
     * @param speed             Speed to move at
     * @param turn              Turn speed to move at
     */
    public void driveWithAngle(double movementDirection, double speed, double turn) {
        double fwd = speed * Math.sin(movementDirection);
        double str = speed * Math.cos(movementDirection);
        drive(fwd, str, turn);
    }

    /**
     * Note: there's no `speed` paramater like there is in driveWithAngle because the speed doesn't matter for getting
     * turning directions!
     *
     * @param movementDirection Direction to move in in radians
     * @param turn              Turn speed to move at
     */
    public void prepareWheelAnglesForWithAngle(double movementDirection, double turn) {
        double fwd = Math.sin(movementDirection);
        double str = Math.cos(movementDirection);
        prepareWheelAnglesFor(fwd, str, turn);
    }

    /**
     * @param fwd Forward movement, -1 to 1
     * @param str Strafing movement, -1 to 1
     * @param rcw Rotating movement, -1 to 1
     */
    public void drive(double fwd, double str, double rcw) {
        // Flipping FWD/STR.
        double fwdTemp = str;
        str = fwd;
        fwd = fwdTemp;
        Output.output(OutputLevel.SWERVE_DEBUG, "drive-input-fwd", fwd);
        Output.output(OutputLevel.SWERVE_DEBUG, "drive-input-str", str);
        Output.output(OutputLevel.SWERVE_DEBUG, "drive-input-rcw", rcw);
        prepareWheelAnglesFor(str, fwd, rcw); // flip fwd/str again for this because they also flip fwd/str
        double[] frontTanQuad = {
                (str - rcw * (frontWheelBase / frontRadius)),
                (str + rcw * (frontWheelBase / frontRadius)),
                (fwd - rcw * (frontTrackWidth / frontRadius)),
                (fwd + rcw * (frontTrackWidth / frontRadius)),
        };
        double[] backTanQuad = {
                (str - rcw * (backWheelBase / backRadius)),
                (str + rcw * (backWheelBase / backRadius)),
                (fwd - rcw * (backTrackWidth / backRadius)),
                (fwd + rcw * (backTrackWidth / backRadius)),
        };

        double[] wheelSpeeds = getWheelSpeeds(frontTanQuad, backTanQuad);

        for (int i = 0; i < 4; i++) {
            driveSystem[i].setSetpoint(wheelSpeeds[i]);
        }
    }

    /**
     * This stops drive wheels from driving, without affecting the wheel turn positions *at all*. Wheels will continue
     * to turn to the last target angles.
     */
    public void stopDriving() {
        for (int i = 0; i < 4; i++) {
            driveSystem[i].setSetpoint(0);
        }
    }

    /**
     * @param fwd Forward movement, -1 to 1
     * @param str Strafing movement, -1 to 1
     * @param rcw Rotating movement, -1 to 1
     */
    public void prepareWheelAnglesFor(double fwd, double str, double rcw) {
        // Flipping FWD/STR.
        double fwdTemp = str;
        str = fwd;
        fwd = fwdTemp;
        boolean isStill = Math.abs(fwd) < 0.05 && Math.abs(str) < 0.05 && Math.abs(rcw) < 0.05;
        double[] frontTanQuad = {
                (str - rcw * (frontWheelBase / frontRadius)),
                (str + rcw * (frontWheelBase / frontRadius)),
                (fwd - rcw * (frontTrackWidth / frontRadius)),
                (fwd + rcw * (frontTrackWidth / frontRadius)),
        };
        double[] backTanQuad = {
                (str - rcw * (backWheelBase / backRadius)),
                (str + rcw * (backWheelBase / backRadius)),
                (fwd - rcw * (backTrackWidth / backRadius)),
                (fwd + rcw * (backTrackWidth / backRadius)),
        };

        double[] wheelAngles = getWheelAngles(frontTanQuad, backTanQuad);

        // this was kind of broken and not doing anything (travel was not used afterwards), so just commenting it out is ok.
//        // This should work...?
//        for (int i = 0; i < wheelAngles.length; i++) {
//            // Angles are -PI to PI
//            double pAngle = steerSystem[i].returnPIDInput();
//            double travel = Math.abs(wheelAngles[i] - pAngle);
//            // Reverse the wheel if the angle is greater than 90, but less than 270
//            // Allows shortest path to still function over the -PI -> PI wrap-around
//            if ((Math.PI * 3) / 2 > travel && travel > Math.PI / 2 * 1.2) {
//                wheelSpeeds[i] *= -1;
//                travel -= Math.PI / 2;
//            }
//        }
//            if (pAngle * wheelAngles[i] < 0) {
//                wheelAngles[i] += Settings.Key.TURNING_SLOP.getDouble();
//            }

        // This logic stops us from turning the wheels at all if we aren't trying to move the robot at all.
        if (isStill != wasStillLast) {
            if (isStill) {
                // we are now still
                for (PIDSteer steer : steerSystem) {
                    steer.disable();
                }
            } else {
                // we are no longer still
                for (PIDSteer steer : steerSystem) {
                    steer.enable();
                }
            }
            wasStillLast = isStill;
        }

        for (int i = 0; i < 4; i++) {
            steerSystem[i].setSetpoint(wheelAngles[i]);
        }


//        for (int i = 0; i < 4; i++) {
//            // Resets the steering encoder when it passes the switch going in the positive direction
//            if (wheelAngles[i] < steerSystem[i].getAngle()) {
//                if (steerSystem[i].getPreviousResetState() && !steerSystem[i].getResetSwitch()) {
//                    steerSystem[i].resetEncoder();
//                }
//            }
//            // Resets the steering encoder when it passes the switch going in the negative direction
//            else if (wheelAngles[i] > steerSystem[i].getAngle()) {
//                if (!steerSystem[i].getPreviousResetState() && steerSystem[i].getResetSwitch()) {
//                    steerSystem[i].resetEncoder();
//                }
//            }
//        }
    }

    /**
     * Returns whether the wheels are turned to the angle last set, allowing allowanceRadians difference
     *
     * @param allowanceRadians Radian difference to allow for each wheel - must be positive
     */
    public boolean areWheelAnglesReady(double allowanceRadians) {
        for (PIDSteer pidSteer : steerSystem) {
            if (Math.abs(pidSteer.getSetpoint() - pidSteer.getPosition()) > allowanceRadians) {
//                System.out.println("Wheels not ready!");
                return false;
            } else {
                System.out.println("Wheel is good! Setpoint: " + pidSteer.getSetpoint() + " Position: " + pidSteer.getPosition() + " Allowance: " + allowanceRadians);
            }
        }
        System.out.println("Wheels ready!");
        return true;
    }

    private double[] getWheelSpeeds(double[] frontQuad, double[] backQuad) {
        double ws1 = Math.sqrt(Math.pow(frontQuad[1], 2) + Math.pow(frontQuad[2], 2));
        double ws2 = Math.sqrt(Math.pow(frontQuad[1], 2) + Math.pow(frontQuad[3], 2));
        double ws3 = Math.sqrt(Math.pow(backQuad[0], 2) + Math.pow(backQuad[3], 2));
        double ws4 = Math.sqrt(Math.pow(backQuad[0], 2) + Math.pow(backQuad[2], 2));

        // Binds the wheel speeds to [0, +1]
        double max = ws1;
        if (ws2 > max)
            max = ws2;
        if (ws3 > max)
            max = ws3;
        if (ws4 > max)
            max = ws4;
        if (max > 1) {
            ws1 /= max;
            ws2 /= max;
            ws3 /= max;
            ws4 /= max;
        }

        return new double[]{ws1, ws2, ws3, ws4};
    }

    private double[] getWheelAngles(double[] frontQuad, double[] backQuad) {
        // Wheel angles -180 to 180. 0 is straight forward
        double wa1 = Math.atan2(frontQuad[1], frontQuad[2]);
        double wa2 = Math.atan2(frontQuad[1], frontQuad[3]);
        double wa3 = Math.atan2(backQuad[0], backQuad[3]);
        double wa4 = Math.atan2(backQuad[0], backQuad[2]);

        return new double[]{wa1, wa2, wa3, wa4};
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new RunPIDDrive());
    }

    /**
     * Sets the steer pid settings from Settings.
     */
    public void updateSteerPID() {
        double p = Settings.Key.STEER_PID_P.getDouble();
        double i = Settings.Key.STEER_PID_I.getDouble();
        double d = Settings.Key.STEER_PID_D.getDouble();
        setSteerPID(p, i, d);
    }

    public void setSteerPID(double p, double i, double d) {
        for (PIDSteer steer : steerSystem) {
            steer.setPID(p, i, d);
        }
    }

    /**
     * public void setDrivePID(double p, double i, double d){ for(PIDDrive drive: driveSystem){ drive.setPID(p, i, d); }
     * }
     */

    public void resetEncoders() {
        for (PIDSteer steer : steerSystem) {
            steer.steerEncoder.reset();
            steer.getPIDController().enable();
        }
    }
}

