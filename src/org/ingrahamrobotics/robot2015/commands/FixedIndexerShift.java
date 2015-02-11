package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;
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
    /**
     * Runs when the command starts with state=0, and when the command ends with state=1.
     */
    protected void startState(final int state) {
        if (state == 0) {
            Subsystems.verticalClawShifter.setSpeed(directionIsUp ? 1 : -1);
        } else {
            Subsystems.verticalClawShifter.setSpeed(0);
        }
    }

    @Override
    protected long[] getWaitTimes() {
        return new long[]{
                5000l // TODO: Configure this, or encoder
        };
    }
}
