package org.ingrahamrobotics.robot2015.commands;

import static org.ingrahamrobotics.robot2015.Robot.testSolenoid;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunExampleSolenoid extends Command {

    public RunExampleSolenoid() {
        requires(testSolenoid);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        testSolenoid.set(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
