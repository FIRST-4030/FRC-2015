package org.ingrahamrobotics.robot2015.subsystems;

import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts.DRIVE_MOTORS;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SpeedDrive extends Subsystem {

    Talon driveMotor;

    public SpeedDrive(int wheelNum) {
        driveMotor = new Talon(DRIVE_MOTORS[wheelNum - 1]);
    }

    public void setSetpoint(double speed) {
        driveMotor.set(speed);
    }

    @Override
    protected void initDefaultCommand() {
    }
}
