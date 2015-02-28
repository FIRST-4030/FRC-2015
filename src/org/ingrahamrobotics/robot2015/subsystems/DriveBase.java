package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.commands.RunPIDDrive;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

/**
 * Oversees the PIDDrive and PIDSteer Subsystems.
 * <p>
 * Commands should point to this class rather than PIDDrive or PIDSteer.
 */
public class DriveBase extends Subsystem {

    private boolean wasStillLast;
    public final PIDSteer[] steerSystem;
    public final SpeedDrive[] driveSystem;

    private final int trackWidth = 37;
    private final int wheelBase = 21;
    private final double radius = Math.sqrt(trackWidth ^ 2 + wheelBase ^ 2);

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

    public void setPID(double p, double i, double d) {
        for (PIDSteer steer : steerSystem) {
            steer.setPID(p, i, d);
        }
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

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
        double a = str - rcw * (wheelBase / radius);
        double b = str + rcw * (wheelBase / radius);
        double c = fwd - rcw * (trackWidth / radius);
        double d = fwd + rcw * (trackWidth / radius);

        double[] wheelSpeeds = getWheelSpeeds(a, b, c, d);
        double[] wheelAngles = getWheelAngles(a, b, c, d);

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

    private double[] getWheelSpeeds(double a, double b, double c, double d) {
        double ws1 = Math.sqrt(b * b + c * c);
        double ws2 = Math.sqrt(b * b + d * d);
        double ws3 = Math.sqrt(a * a + d * d);
        double ws4 = Math.sqrt(a * a + c * c);

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

    private double[] getWheelAngles(double a, double b, double c, double d) {
        // Wheel angles -180 to 180. 0 is straight forward
        double wa1 = Math.atan2(b, c);
        double wa2 = Math.atan2(b, d);
        double wa3 = Math.atan2(a, d);
        double wa4 = Math.atan2(a, c);

        return new double[]{wa1, wa2, wa3, wa4};
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new RunPIDDrive());
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

