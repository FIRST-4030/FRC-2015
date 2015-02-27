package org.ingrahamrobotics.robot2015.subsystems;

import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.DRIVE_ENCODERS_A;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.DRIVE_ENCODERS_B;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts.DRIVE_MOTORS;

import org.ingrahamrobotics.robot2015.Robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDDrive extends PIDSubsystem {

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
