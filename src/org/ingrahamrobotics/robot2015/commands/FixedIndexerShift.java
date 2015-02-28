package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.utils.TimedCommand;

public class FixedIndexerShift extends TimedCommand {

    private final Settings.Key additionKey;
    private final boolean directionIsUp;
    private long currentTargetEncoderValue;

    public FixedIndexerShift(final Settings.Key additionKey, boolean directionIsUp) {
        this.additionKey = additionKey;
        requires(Subsystems.verticalIndexerControl);
        this.directionIsUp = directionIsUp;
    }

    @Override
    /**
     * Is executed continuously until it returns true or the time runs out.
     */
    protected boolean executeState(final int state) {
        if (directionIsUp) {
            if (Subsystems.toggleSwitches.getIndexerTop()) {
                return true;
            }else if (Subsystems.indexerEncoder.get() > Settings.Key.INDEXER_MAX_HEIGHT.getInt()){
            	return true;
            }
        } else {
            if (Subsystems.toggleSwitches.getIndexerBottom()) {
                return true;
            }
        }
        if (!Settings.Key.INDEXER_LEVEL_USE_ENCODER.getBoolean()) {
            return false;
        }
        int encoderValue = Subsystems.indexerEncoder.get();
        if (directionIsUp) {
            return encoderValue >= currentTargetEncoderValue;
        } else {
            return encoderValue <= currentTargetEncoderValue;
        }
    }

    @Override
    protected boolean startState(final int state) {
        // How much we go up/down each time
        int interval = Math.abs(Settings.Key.INDEXER_LEVEL_ENCODER_TICKS.getInt());
        // How close to a regular interval can we be that we treat our current count as that regular interval
        int allowedShift = interval / 20;
        // Current encoder value
        int currentEncoderValue = Subsystems.indexerEncoder.get();
        int currentEncoderOffsetFromNorm = currentEncoderValue % interval;

        // If we are very close to a normal interval, we don't want to just go back to the interval we're close to.
        // If this is the case, we should treat the current encoder value as that fixed place.
        // We are assuming that the encoder will increase as we go up.
        if (directionIsUp) {
            if (currentEncoderOffsetFromNorm >= interval - allowedShift) {
                // For example, if interval is 5000, and we're at 9998, set the used value to 10000
                currentEncoderValue = currentEncoderValue - currentEncoderOffsetFromNorm + interval + 1;
            }
        } else {
            if (currentEncoderOffsetFromNorm <= allowedShift) {
                // For example, if interval is 5000, and we're at 10001, set the used value to 10000
                currentEncoderValue = currentEncoderValue - currentEncoderOffsetFromNorm - 1;
            }
        }
        // Current encoder interval number
        int currentEncoderInterval = currentEncoderValue / interval;
        // The target encoder interval number
        // Because currentEncoderInterval is rounded down, when we're going down we will
        //  already have the target we want to get to, and we don't need to subtract 1
        int targetEncoderInterval = currentEncoderInterval + (directionIsUp ? 1 : 0);
        // The target encoder raw count
        currentTargetEncoderValue = targetEncoderInterval * interval;
        if (additionKey != null) {
            currentTargetEncoderValue += additionKey.getInt();
        }

        double speed = Settings.Key.INDEXER_FIXED_SPEED.getDouble();
        if (directionIsUp) {
            Subsystems.verticalIndexerControl.setSpeed(speed);
        } else {
            Subsystems.verticalIndexerControl.setSpeed(-speed);
        }
        return false; // we're never done in startState
    }

    @Override
    protected long[] getWaitTimes() {
        return new long[]{
                Settings.Key.INDEXER_LEVEL_MAX_WAIT_TIME.getLong()
        };
    }

    @Override
    protected void end() {
        Subsystems.verticalIndexerControl.setSpeed(0);
    }
}
