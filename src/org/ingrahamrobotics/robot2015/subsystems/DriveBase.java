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
    private final int backWheelBase = 21;
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
     * @param fwd Forward movement, -1 to 1
     * @param str Strafing movement, -1 to 1
     * @param rcw Rotating movement, -1 to 1
     */
    public void drive(double fwd, double str, double rcw) {
        Output.output(OutputLevel.SWERVE_DEBUG, "fwd", fwd);
        Output.output(OutputLevel.SWERVE_DEBUG, "str", str);
        Output.output(OutputLevel.SWERVE_DEBUG, "rcw", rcw);
        boolean isStill = fwd == 0 && str == 0 && rcw == 0;
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
        double[] wheelAngles = getWheelAngles(frontTanQuad, backTanQuad);

        // This should work...?
        for (int i = 0; i < wheelAngles.length; i++) {
            // Angles are -PI to PI
            double pAngle = steerSystem[i].returnPIDInput();
            double travel = Math.abs(wheelAngles[i] - pAngle);
            // Reverse the wheel if the angle is greater than 90, but less than 270
            // Allows shortest path to still function over the -PI -> PI wrap-around 
            if ((Math.PI * 3) / 2 > travel && travel > Math.PI / 2 * 1.2) {
                wheelSpeeds[i] *= -1;
                travel -= Math.PI / 2;
            }
        }
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
            driveSystem[i].setSetpoint(wheelSpeeds[i]);
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

    private double[] getWheelSpeeds(double[] frontQuad, double[] backQuad) {
        double ws1 = Math.sqrt(Math.pow(frontQuad[1], 2) + Math.pow(frontQuad[2], 2));
        double ws2 = Math.sqrt(Math.pow(frontQuad[1], 2) + Math.pow(frontQuad[3], 2));
        double ws3 = Math.sqrt(Math.pow(backQuad[0], 2) + backQuad[3]);
        double ws4 = Math.sqrt(Math.pow(backQuad[0], 2) + backQuad[2]);

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

