package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;

import edu.wpi.first.wpilibj.command.Command;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;
import org.ingrahamrobotics.robot2015.output.Settings;

public class MoveIndexer extends Command {
	
	private int targetHeight;
	private boolean atTargetHeight;
	private boolean lockOnTarget = false;
	private final int indexerDeadZone = 15;

	public MoveIndexer() {
		requires(Subsystems.toteIndexer);
        requires(Subsystems.indexerEncoder);
        requires(Subsystems.toggleSwitches);
	}
	
	@Override
	protected void initialize() {
		targetHeight = 0;
		atTargetHeight = false;
		this.reset();
		atTargetHeight = true;
	}
	
	public void reset() {
		double speed = Math.abs(Settings.Key.INDEXER_FIXED_SPEED.getDouble());
		while(!Subsystems.toggleSwitches.getIndexerBottom()) {
			Subsystems.toteIndexer.setSpeed(-speed);
		}
		Subsystems.toteIndexer.setSpeed(0);
	}

	@Override
	protected void execute() {
		//PID Loop here
		atTargetHeight = Math.abs(targetHeight - Subsystems.indexerEncoder.get()) < indexerDeadZone;
		if(lockOnTarget) {
			lockOnTarget = !atTargetHeight;
		}
		
	}
	
	public boolean isAtTarget() {
		return atTargetHeight;
	}
	
	/* Here are replacement commands:
	 * FixedIndexerShift: adjustTarget(indexerShift);
	 * FullIndexerCollapse: reset();
	 */
	
	public void adjustTarget(int adjustment) {
		this.changeTarget(targetHeight + adjustment);
	}
	
	public void changeTarget(int newHeight) {
		int max = Settings.Key.INDEXER_MAX_HEIGHT.getInt();
		if(newHeight < 0) {
			targetHeight = 0;
		} else if(newHeight > max) {
			targetHeight = max;
		} else {
			targetHeight = newHeight;
		}
	}
	
	public void changeTargetLock(int newHeight) {
		this.changeTarget(newHeight);
		lockOnTarget = true;
	}
	
	public int getTarget() {
		return targetHeight;
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
	
	public class ManualControl extends Command {
		
		@Override
		protected void initialize() {
			
		}

		@Override
		protected void execute() {
			
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
	
	public class DownAndUp extends Command {
		
		private boolean firstFinished;
		private boolean commandSent;
		private boolean executed;
		
		@Override
		protected void initialize() {
			firstFinished = false;
			commandSent = false;
			executed = false;
			
		}

		@Override
		protected void execute() {
			if(!firstFinished & !commandSent) {
				//changeTargetLock(GO DOWN);
				commandSent = true;
			}
			if(!firstFinished) {
				firstFinished = atTargetHeight && !lockOnTarget;
			}
			if(firstFinished) {
				//changeTargetLock(GO UP);
				executed = true;
			}
		}

		@Override
		protected boolean isFinished() {
			return executed;
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

}
