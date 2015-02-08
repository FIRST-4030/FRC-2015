package org.ingrahamrobotics.robot2015.subsystems;

import org.ingrahamrobotics.robot2015.Robot;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.SwerveSpecific;
import org.ingrahamrobotics.robot2015.output.Output;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Oversees the PIDDrive and PIDSteer Subsystems.
 * 
 * Commands should point to this class rather than PIDDrive or PIDSteer.
 */
public class DriveBase extends Subsystem {

    private PIDSteer[] steerSystem;
    private PIDDrive[] driveSystem;

    private final int trackWidth = 24;
    private final int wheelBase = 43;
    private final double radius = Math.sqrt(trackWidth ^ 2 + wheelBase ^ 2);

    public DriveBase() {
        Output.initialized("DriveBase");
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void stop() {
        driveSystem = new PIDDrive[] { new PIDDrive(1), new PIDDrive(2),
                new PIDDrive(3), new PIDDrive(4) };
        steerSystem = new PIDSteer[] { new PIDSteer(1), new PIDSteer(2),
                new PIDSteer(3), new PIDSteer(4) };
    }

    public void drive(double fwd, double str, double rcw) {
        double a = str - rcw * (wheelBase / radius);
        double b = str + rcw * (wheelBase / radius);
        double c = fwd - rcw * (trackWidth / radius);
        double d = fwd + rcw * (trackWidth / radius);

        double[] wheelSpeeds = getWheelSpeeds(a, b, c, d);
        double[] wheelAngles = getWheelAngles(a, b, c, d);

        // This should work...?
        for (int i = 0; i < wheelAngles.length; i++) {
            double pAngle = steerSystem[i].getAngle();
            double travel = Math.abs(wheelAngles[i] - pAngle);
            if (travel > 90 && travel < 270)
                wheelSpeeds[i] *= -1;
        }

        for (int i = 0; i < 4; i++) {
            driveSystem[i].setSetpoint(wheelSpeeds[i]);
            steerSystem[i].setSetpoint(wheelAngles[i]);
        }
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

        return new double[] { ws1, ws2, ws3, ws4 };
    }

    private double[] getWheelAngles(double a, double b, double c, double d) {
        // Wheel angles -180 to 180. 0 is straight forward
        double wa1 = Math.atan2(b, c);
        double wa2 = Math.atan2(b, d);
        double wa3 = Math.atan2(a, d);
        double wa4 = Math.atan2(a, c);

        return new double[] { wa1, wa2, wa3, wa4 };
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}

class PIDDrive extends PIDSubsystem {

    Talon driveMotor;
    Encoder driveEncoder;

    boolean rateBasedDrive;

    // Initialize your subsystem here
    public PIDDrive(int wheelNum) {
        super("PIDDrive" + wheelNum, 1.0, 0.0, 0.0);
        // Use these to get going:
        // setSetpoint() - Sets where the PID controller should move the system
        // to
        // enable() - Enables the PID controller.

        driveMotor = new Talon(SwerveSpecific.driveMotors[wheelNum - 1]);
        driveEncoder = new Encoder(SwerveSpecific.driveEncoders[wheelNum * 2],
                SwerveSpecific.driveEncoders[wheelNum * 2 + 1]);

        setSetpoint(0.0);
        enable();
        rateBasedDrive = Robot.rateBasedDrive;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return rateBasedDrive ? driveEncoder.getRate() : driveEncoder
                .getDistance();
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        driveMotor.set(output);
    }

    public double getSpeed() {
        return driveMotor.get();
    }
}

class PIDSteer extends PIDSubsystem {

    Talon steerMotor;
    Encoder steerEncoder;

    // Initialize your subsystem here
    public PIDSteer(int wheelNum) {
        super("PIDSteer" + wheelNum, 1, 0, 0);
        // Use these to get going:
        // setSetpoint() - Sets where the PID controller should move the system
        // to
        // enable() - Enables the PID controller.
        steerMotor = new Talon(SwerveSpecific.steerMotors[wheelNum - 1]);
        steerEncoder = new Encoder(SwerveSpecific.steerEncoders[wheelNum * 2],
                SwerveSpecific.steerEncoders[wheelNum * 2 + 1]);

        setSetpoint(0.0);
        enable();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return steerEncoder.getDistance();
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        steerMotor.set(output);
    }

    public double getAngle() {
        return steerEncoder.getDistance();
    }
}
