package org.ingrahamrobotics.robot2015.subsystems;

import static org.ingrahamrobotics.robot2015.output.Output.output;

import org.ingrahamrobotics.robot2015.commands.RunSimpleDrive;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SimpleDriveSubsystem extends Subsystem {

    private Jaguar leftMotor;
    private Jaguar rightMotor;

    public SimpleDriveSubsystem() {
        leftMotor = new Jaguar(MotorPorts.SIMPLE_DRIVE_LEFT);
        rightMotor = new Jaguar(MotorPorts.SIMPLE_DRIVE_RIGHT);
        output(OutputLevel.INITIALIZED_SYSTEMS, "SimpleDrive", true);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RunSimpleDrive());
    }

    public void setSpeed(double left, double right) {
        leftMotor.set(left);
        rightMotor.set(right);
    }
}
