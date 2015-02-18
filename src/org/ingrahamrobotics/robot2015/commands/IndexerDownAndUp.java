package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.utils.TimedCommand;

public class IndexerDownAndUp extends TimedCommand {

    private boolean buttonReleased;
    private boolean firstFinished;
    private long currentTargetEncoderValue;

    public IndexerDownAndUp() {
        requires(Subsystems.verticalIndexerControl);
    }

    public ButtonReleasedTrigger getReleasedCommand() {
        return new ButtonReleasedTrigger();
    }

    @Override
    /**
     * Is executed continuously until it returns true or the time runs out.
     */
    protected boolean executeState(final int state) {
        if (state == 0) {// 0 is down, 1 is up
            if (Subsystems.toggleSwitches.getIndexerBottom()) {
                return true;
            }
        } else {
            if (Subsystems.toggleSwitches.getIndexerTop()) {
                return true;
            }
        }
        if (!Settings.Key.INDEXER_LEVEL_USE_ENCODER.getBoolean()) {
            return false;
        }
        int encoderValue = Subsystems.indexerEncoder.get();
        if (state == 0) {
            return encoderValue <= currentTargetEncoderValue;
        } else {
            return encoderValue >= currentTargetEncoderValue;
        }
    }

    @Override
    protected boolean startState(final int state) {
        if (state == 0) {
            buttonReleased = false;
            firstFinished = false;
        } else {
            firstFinished = true;
            if (!buttonReleased) {
                setNextStartState(1);
                return true;
            }
        }
        boolean goingUp = state != 0;
        // How much above the interval we are aming
        int amountAboveInterval = Settings.Key.TOTE_CLEARANCE_ADDITION.getInt();
        // How much we go up/down each time
        int interval = Math.abs(Settings.Key.INDEXER_LEVEL_ENCODER_TICKS.getInt());
        // How close to a regular interval can we be that we treat our current count as that regular interval
        int allowedShift = interval / 20;
        // Current encoder value
        int currentEncoderValue = Subsystems.indexerEncoder.get();
        if (goingUp) {
            // For example, if interval is 5000, and we're at 5000, this sets us to 4800,
            //  then below the extra 200+ is added again to the final value, to result in us trying to get to 5200, instead of 10200
            // assuming amountAboveInterval ~= 200
            currentEncoderValue -= amountAboveInterval;
        }
        int currentEncoderOffsetFromNorm = (currentEncoderValue) % interval;

        // If we are very close to a normal interval, we don't want to just go back to the interval we're close to.
        // If this is the case, we should treat the current encoder value as that fixed place.
        // We are assuming that the encoder will increase as we go up.
        if (goingUp) {
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
        int targetEncoderInterval = currentEncoderInterval + (state == 0 ? 0 : 1);
        // The target encoder raw count
        currentTargetEncoderValue = targetEncoderInterval * interval;

        if (goingUp) {
            currentTargetEncoderValue += Settings.Key.TOTE_CLEARANCE_ADDITION.getInt();
        }

        double speed = Settings.Key.INDEXER_FIXED_SPEED.getDouble();
        if (state == 0) {
            Subsystems.verticalIndexerControl.setSpeed(-speed);
        } else {
            Subsystems.verticalIndexerControl.setSpeed(speed);
        }
        return false;
    }

    @Override
    protected long[] getWaitTimes() {
        return new long[]{
                Settings.Key.INDEXER_LEVEL_MAX_WAIT_TIME.getLong(),
                Settings.Key.INDEXER_LEVEL_MAX_WAIT_TIME.getLong()
        };
    }

    @Override
    protected void end() {
        Subsystems.verticalIndexerControl.setSpeed(0);
    }

    public class ButtonReleasedTrigger extends Command {

        private boolean executed = false;

        @Override
        protected void initialize() {
            executed = false;
        }

        @Override
        protected void execute() {
            buttonReleased = true;
            if (firstFinished) {
                IndexerDownAndUp.this.start();
            }
            executed = true;
        }

        @Override
        protected boolean isFinished() {
            return executed;
        }

        @Override
        protected void end() {

        }

        @Override
        protected void interrupted() {

        }
    }
}
