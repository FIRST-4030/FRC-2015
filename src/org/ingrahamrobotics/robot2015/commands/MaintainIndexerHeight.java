package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;

import edu.wpi.first.wpilibj.command.Command;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;

public class MaintainIndexerHeight extends Command {
	
	private int targetHeight;
	private boolean atTargetHeight;
	private boolean initialized = false;
	private final int indexerDeadZone = 15;

	public MaintainIndexerHeight() {
		requires(Subsystems.toteIndexer);
        requires(Subsystems.indexerEncoder);
	}

	@Override
	protected void initialize() {
		targetHeight = 0;
		atTargetHeight = false;
	}

	@Override
	protected void execute() {
		int indexerHeight = Subsystems.indexerEncoder.get();
		int heightErrAbs = Math.abs(targetHeight - indexerHeight);
		
		if(heightErrAbs < indexerDeadZone) {
			return;
		}
		
		
	}
	
	public void changeTarget(int newHeight) {
		if(newHeight < 0) {
			targetHeight = 0;
		} else {
			targetHeight = newHeight;
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		this.end();
	}

}
