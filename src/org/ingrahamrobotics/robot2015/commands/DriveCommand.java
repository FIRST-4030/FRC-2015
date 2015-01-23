package org.ingrahamrobotics.robot2015.commands;

import static org.ingrahamrobotics.robot2015.Robot.drive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;

public class DriveCommand extends Command {

    private Joystick leftJoystick = new Joystick(0);
    private Joystick rightJoystick = new Joystick(1);

    public DriveCommand() {
        requires(drive);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        double left = leftJoystick.getAxis(AxisType.kY);
        double right = rightJoystick.getAxis(AxisType.kY);
        drive.setSpeed(left, -right);
    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void end() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void interrupted() {
        // TODO Auto-generated method stub
    }

}
