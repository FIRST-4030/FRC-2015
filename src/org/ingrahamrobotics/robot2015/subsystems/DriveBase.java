package org.ingrahamrobotics.robot2015.subsystems;

import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.DRIVE_ENCODERS_A;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.DRIVE_ENCODERS_B;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.STEER_ENCODERS_A;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.STEER_ENCODERS_B;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts.DRIVE_MOTORS;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts.STEER_MOTORS;

import org.ingrahamrobotics.robot2015.Robot;
import org.ingrahamrobotics.robot2015.commands.RunPIDDrive;
import org.ingrahamrobotics.robot2015.output.Output;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Oversees the PIDDrive and PIDSteer Subsystems.
 * <p>
 * Commands should point to this class rather than PIDDrive or PIDSteer.
 */
public class DriveBase extends Subsystem {

    private final PIDSteer[] steerSystem;
    private final SpeedDrive[] driveSystem;

    private final int trackWidth = 24;
    private final int wheelBase = 43;
    private final double radius = Math.sqrt(trackWidth ^ 2 + wheelBase ^ 2);

    public DriveBase() {
        driveSystem = new SpeedDrive[4];
        steerSystem = new PIDSteer[4];
        for (int i = 0; i < 4; i++) {
            driveSystem[i] = new SpeedDrive(i + 1);
        }
        for (int i = 0; i < 4; i++) {
            steerSystem[i] = new PIDSteer(i + 1);
        }
        Output.initialized("DriveBase");
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

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

        return new double[]{ws1, ws2, ws3, ws4};
    }

    private double[] getWheelAngles(double a, double b, double c, double d) {
        // Wheel angles -180 to 180. 0 is straight forward
        double wa1 = Math.atan2(b, c) * (180/Math.PI);
        double wa2 = Math.atan2(b, d) * (180/Math.PI);
        double wa3 = Math.atan2(a, d) * (180/Math.PI);
        double wa4 = Math.atan2(a, c) * (180/Math.PI);

        return new double[]{wa1, wa2, wa3, wa4};
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new RunPIDDrive());
    }
    
    public void setSteerPID(double p, double i, double d){
    	for(PIDSteer steer: steerSystem){
    		steer.setPID(p, i, d);
    	}
    }
    /**
     * public void setDrivePID(double p, double i, double d){
     * 		for(PIDDrive drive: driveSystem){
     * 			drive.setPID(p, i, d);
     * 		}
     * }
     * */
}

class SpeedDrive {

    Talon driveMotor;

    public SpeedDrive(int wheelNum) {
        driveMotor = new Talon(DRIVE_MOTORS[wheelNum - 1]);
    }

    public void setSetpoint(double speed) {
        driveMotor.set(speed);
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

        driveMotor = new Talon(DRIVE_MOTORS[wheelNum - 1]);
        driveEncoder = new Encoder(DRIVE_ENCODERS_A[wheelNum - 1], DRIVE_ENCODERS_B[wheelNum - 1]);

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
        return rateBasedDrive ? driveEncoder.getRate() : driveEncoder.getDistance();
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

    private static final double degreesPerTick = (497.0 + 66.0 / 56.0) / 360.0;
    Talon steerMotor;
    Encoder steerEncoder;

    // Initialize your subsystem here
    public PIDSteer(int wheelNum) {
        super("PIDSteer" + wheelNum, 1, 0, 0);
        // Use these to get going:
        // setSetpoint() - Sets where the PID controller should move the system
        // to
        // enable() - Enables the PID controller.
        steerMotor = new Talon(STEER_MOTORS[wheelNum - 1]);
        steerEncoder = new Encoder(STEER_ENCODERS_A[wheelNum - 1], STEER_ENCODERS_B[wheelNum - 1]);

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
    public void setPID(double p, double i, double d){
         getPIDController().setPID(p, i, d);
    }

    public double getAngle() {
        return steerEncoder.getDistance() * degreesPerTick;
    }

}
