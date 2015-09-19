package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;

import edu.wpi.first.wpilibj.command.Command;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;

public class MaintainIndexerHeight extends Command {
	
	private int targetHeight;
	//NOTE: This should probably be put into the Settings Key file, but I'm not sure how to work that.
	private final int indexerDeadZone = 15;

	public MaintainIndexerHeight() {
		requires(Subsystems.toteIndexer);
        requires(Subsystems.indexerEncoder);
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

	}

	//Need some way to disable this when the indexer is receiving a manual or automatic command
	@Override
	protected void execute() {
		int indexerHeight = Subsystems.indexerEncoder.get();
		int heightErrAbs = Math.abs(targetHeight - indexerHeight);
		
		if(heightErrAbs < indexerDeadZone) {
			return;
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
		// TODO Auto-generated method stub

	}

}
