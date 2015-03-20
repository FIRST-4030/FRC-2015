package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts.DRIVE_MOTORS;

/**
 *
 */
public class SpeedDrive extends Subsystem {

    private final int wheelNum;
    Talon driveMotor;

    public SpeedDrive(int wheelNum) {
        this.wheelNum = wheelNum;
        driveMotor = new Talon(DRIVE_MOTORS[wheelNum - 1]);
    }

    public void setSetpoint(double speed) {
        Output.output(OutputLevel.RAW_MOTORS, "OutputSpeed-" + wheelNum, speed);
        driveMotor.set(speed);
    }

    @Override
    protected void initDefaultCommand() {
    }
}
