package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;

import edu.wpi.first.wpilibj.command.Command;

public class ManualClawControl extends Command {
	
	private double threshold = 0.1;

	public ManualClawControl() {
		requires(Subsystems.verticalClawShifter);
	}

	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		double y = IAxis.manualControl.get();
		if(Math.abs(y) > threshold)
			Subsystems.verticalClawShifter.setSpeed(y);
	}

	//No purpose for this command, will return false
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Subsystems.verticalClawShifter.setSpeed(0);
	}

	//Should always be called, but will redirect to end for form
	@Override
	protected void interrupted() {
		this.end();
	}

}
