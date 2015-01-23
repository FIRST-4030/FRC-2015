package org.ingrahamrobotics.robot2015.subsystems;

import org.ingrahamrobotics.robot2015.commands.DriveCommand;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem {

    private Jaguar leftMotor;
    private Jaguar rightMotor;

    public DriveSubsystem() {
        leftMotor = new Jaguar(0);
        rightMotor = new Jaguar(1);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }

    public void setSpeed(double left, double right) {
        leftMotor.set(left);
        rightMotor.set(right);
    }
}
