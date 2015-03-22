package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.state.DriveState;

public class ToggleWaitForWheels extends Command {

    private boolean finished;
    private final boolean stateToSet;

    public ToggleWaitForWheels(final boolean stateToSet) {
        this.stateToSet = stateToSet;
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        finished = false;
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        DriveState.setWaitForWheelsEnabled(stateToSet);
        finished = true;
    }

    /**
     * Returns whether this command is finished. If it is, then the command will be removed and {@link
     * edu.wpi.first.wpilibj.command.Command#end() end()} will be called. <p> <p>It may be useful for a team to
     * reference the {@link edu.wpi.first.wpilibj.command.Command#isTimedOut() isTimedOut()} method for time-sensitive
     * commands.</p>
     *
     * @return whether this command is finished.
     * @see edu.wpi.first.wpilibj.command.Command#isTimedOut() isTimedOut()
     */
    @Override
    protected boolean isFinished() {
        return finished;
    }

    /**
     * Called when the command ended peacefully.  This is where you may want to wrap up loose ends, like shutting off a
     * motor that was being used in the command.
     */
    @Override
    protected void end() {

    }

    /**
     * Called when the command ends because somebody called {@link edu.wpi.first.wpilibj.command.Command#cancel()
     * cancel()} or another command shared the same requirements as this one, and booted it out. <p> <p>This is where
     * you may want to wrap up loose ends, like shutting off a motor that was being used in the command.</p> <p>
     * <p>Generally, it is useful to simply call the {@link edu.wpi.first.wpilibj.command.Command#end() end()} method
     * within this method</p>
     */
    @Override
    protected void interrupted() {

    }
}
