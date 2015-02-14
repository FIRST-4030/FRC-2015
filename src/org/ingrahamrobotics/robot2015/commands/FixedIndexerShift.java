package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.utils.TimedCommand;

public class FixedIndexerShift extends TimedCommand {

    private final boolean directionIsUp;

    public FixedIndexerShift(boolean directionIsUp) {
        requires(Subsystems.verticalClawShifter);
        this.directionIsUp = directionIsUp;
    }

    @Override
    /**
     * Is executed continuously until it returns true or the time runs out.
     */
    protected boolean executeState(final int state) {
        return false;
    }

    @Override
    protected void startState(final int state) {
        Subsystems.verticalClawShifter.setSpeed(directionIsUp ? 1 : -1);
    }

    @Override
    protected long[] getWaitTimes() {
        return new long[]{
                Settings.Key.INDEXER_SHIFT_WAIT.getLong() // TODO: encoder?
        };
    }

    @Override
    protected void end() {
        Subsystems.verticalClawShifter.setSpeed(0);
    }
}
