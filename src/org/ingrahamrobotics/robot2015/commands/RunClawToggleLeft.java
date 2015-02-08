package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;

import edu.wpi.first.wpilibj.command.Command;

public class RunClawToggle extends Command {

	private boolean finished;
	private boolean toggled;
	private boolean isLeft;

	public RunClawToggle(boolean isLeft) {
		this.isLeft = isLeft;
		requires(isLeft ? Subsystems.clawToggleLeft
				: Subsystems.clawToggleRight);
	}

	@Override
	protected void initialize() {
		finished = false;

	}

	@Override
	protected void execute() {
		finished = true;
		toggled = !toggled;
		if (isLeft) {
			
			Subsystems.clawToggleLeft.set(toggled);
		} else {
			Subsystems.clawToggleRight.set(toggled);
			
		}
		
		
	}

	@Override
	protected boolean isFinished() {

		return finished;
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
