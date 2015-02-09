package org.ingrahamrobotics.robot2015.commands;

import static org.ingrahamrobotics.robot2015.Subsystems.simpleDrive;

import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Robot;

public class RunSimpleDrive extends Command {

    public RunSimpleDrive() {
        requires(simpleDrive);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        double left = Robot.oi.leftJoystick.getAxis(AxisType.kY);
        double right = Robot.oi.rightJoystick.getAxis(AxisType.kY);
        simpleDrive.setSpeed(left, -right);
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
