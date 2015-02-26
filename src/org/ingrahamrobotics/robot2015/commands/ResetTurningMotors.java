package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.subsystems.PIDSteer;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ResetTurningMotors extends Command {
	private boolean isReset = false;

    public ResetTurningMotors() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Subsystems.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	for (PIDSteer pidSteer : Subsystems.driveBase.steerSystem) {
    		pidSteer.disable();
    		pidSteer.setSpeed(0.5);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	boolean allHaveReset = true;
    	for (PIDSteer pidSteer : Subsystems.driveBase.steerSystem) {
    		if (!pidSteer.getPreviousResetState() && pidSteer.getResetSwitch()) {
    			pidSteer.resetEncoder();
    			pidSteer.setSpeed(0.0);
    		}
    		allHaveReset = false;
    		pidSteer.setPrevResetState(pidSteer.getResetSwitch());
    	}
    	
    	if (allHaveReset)
    		isReset = true;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isReset;
    }

    // Called once after isFinished returns true
    protected void end() {
    	for (PIDSteer pidSteer : Subsystems.driveBase.steerSystem) {
    		pidSteer.enable();
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
