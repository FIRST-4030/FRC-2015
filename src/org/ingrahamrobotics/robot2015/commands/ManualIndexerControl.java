package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.state.ManualControlState;

public class ManualIndexerControl extends Command {

    public ManualIndexerControl() {
        requires(Subsystems.toteIndexer);
        ManualControlState.setManualIndexerRunning(false);
        Subsystems.toteIndexer.setSpeed(0);
    }

    @Override
    protected void initialize() {
        ManualControlState.setManualIndexerRunning(true);
    }

    @Override
    protected void execute() {
        double y = -IAxis.manualControl.get();

        if (y > 0 && (Subsystems.toggleSwitches.getIndexerTop()
                || Subsystems.indexerEncoder.get() > Settings.Key.INDEXER_MAX_HEIGHT.getInt())) {
            Subsystems.toteIndexer.setSpeed(0);
            return;
        }
        if (y < 0 && Subsystems.toggleSwitches.getIndexerBottom()) {
            Subsystems.toteIndexer.setSpeed(0);
            return;
        }

        Subsystems.toteIndexer.setSpeed(y);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Subsystems.toteIndexer.setSpeed(0);
        ManualControlState.setManualIndexerRunning(false);
    }

    //Should always be called, but will redirect to end for form
    @Override
    protected void interrupted() {
        this.end();
    }
}
