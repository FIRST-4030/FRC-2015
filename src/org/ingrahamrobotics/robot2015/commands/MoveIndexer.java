package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;

import edu.wpi.first.wpilibj.command.Command;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;
import org.ingrahamrobotics.robot2015.output.Settings;

public class MoveIndexer extends Command {
	
	private boolean atSetpoint;
	private boolean lockOn = false;

	public MoveIndexer() {
		requires(Subsystems.toteIndexer);
        requires(Subsystems.indexerEncoder);
        requires(Subsystems.toggleSwitches);
	}
	
	@Override
	protected void initialize() {
		atSetpoint = false;
		this.reset();
		atSetpoint = true;
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
		atSetpoint = Subsystems.toteIndexer.isAtSetpoint();
		if(lockOn) {
			lockOn = !atSetpoint;
		}
		
	}
	
	public boolean isAtSetpoint() {
		return atSetpoint;
	}
	
	/* Here are replacement commands:
	 * FixedIndexerShift: adjustTarget(indexerShift);
	 * FullIndexerCollapse: reset();
	 */
	
	public void adjustSetpoint(int adjustment) {
		this.changeSetpoint(Subsystems.toteIndexer.getSetpoint() + adjustment);
	}
	
	public void changeSetpoint(double newHeight) {
		int max = Settings.Key.INDEXER_MAX_HEIGHT.getInt();
		if(newHeight < 0) {
			Subsystems.toteIndexer.setSetpoint(0);
		} else if(newHeight > max) {
			Subsystems.toteIndexer.setSetpoint(max);
		} else {
			Subsystems.toteIndexer.setSetpoint(newHeight);
		}
	}
	
	public void changeSetpointLock(int newHeight) {
		this.changeSetpoint(newHeight);
		lockOn = true;
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
		
		private final int per50MSAdjust = 2;
		
		@Override
		protected void initialize() {
			
		}

		//TODO: See if negative value for y and INDEXER_SPEED makes sense.
		@Override
		protected void execute() {
			double y = -IAxis.manualControl.get();
			adjustSetpoint((int) y * per50MSAdjust);
			//Subsystems.toteIndexer.setSpeed(y * Settings.Key.INDEXER_SPEED.getDouble());
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
		//20 ms
		@Override
		protected void execute() {
			if(!firstFinished & !commandSent) {
				//changeSetpointLock(GO DOWN);
				commandSent = true;
			}
			if(!firstFinished) {
				firstFinished = atSetpoint && !lockOn;
			}
			if(firstFinished) {
				//changeSetpointLock(GO UP);
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
