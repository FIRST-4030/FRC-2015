package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;

public class FullIndexerCollapse extends Command {

    private boolean initialUpDone;
    private final int encoderUpInitial;

    public FullIndexerCollapse(final int encoderUpInitial) {
        this.encoderUpInitial = encoderUpInitial;
        requires(Subsystems.toteIndexer);
    }

    @Override
    protected void initialize() {
        if (encoderUpInitial <= 0) {
            initialUpDone = true;
        }
    }

    @Override
    protected void execute() {
        double speed = Math.abs(Settings.Key.INDEXER_FIXED_SPEED.getDouble());
        if (initialUpDone) {
            Subsystems.toteIndexer.setSpeed(-speed);
            if (initialUpDone && Subsystems.toggleSwitches.getIndexerBottom()) {
                Subsystems.indexerEncoder.reset();
            }
        } else {
            int encoderValue = Subsystems.indexerEncoder.get();
            if (encoderValue >= encoderUpInitial) {
                initialUpDone = true;
                return;
            }
            if (Subsystems.toggleSwitches.getIndexerTop() || encoderValue > Settings.Key.INDEXER_MAX_HEIGHT.getInt()) {
                initialUpDone = true;
                return;
            }
            Subsystems.toteIndexer.setSpeed(speed);
        }
    }

    @Override
    protected boolean isFinished() {
        return initialUpDone && Subsystems.toggleSwitches.getIndexerBottom();
    }

    @Override
    protected void end() {
        Subsystems.toteIndexer.setSpeed(0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
