package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;

public class FullIndexerCollapse extends Command {

    public FullIndexerCollapse() {
        requires(Subsystems.verticalIndexerControl);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Subsystems.verticalClawShifter.setSpeed(-Settings.Key.INDEXER_FIXED_SPEED.getDouble());
    }

    @Override
    protected boolean isFinished() {
        return Subsystems.toggleSwitches.getIndexerBottom();
    }

    @Override
    protected void end() {
        if (Subsystems.toggleSwitches.getIndexerBottom()) {
            // If this ended at the bottom, reset the indexer encoder! (as apposed to if it was interrupted)
            Subsystems.indexerEncoder.reset();
        }
        Subsystems.verticalClawShifter.setSpeed(0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
