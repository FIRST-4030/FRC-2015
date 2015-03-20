package org.ingrahamrobotics.robot2015.utils;

import edu.wpi.first.wpilibj.command.Command;

public abstract class TimedCommand extends Command {

    protected int nextStartState;
    protected long startTime;
    protected long lastSwitch;
    protected int currentState;
    protected long[] stateTimeouts;

    public void setNextStartState(final int nextStartState) {
        this.nextStartState = nextStartState;
    }

    @Override
    protected void initialize() {
        startTime = lastSwitch = System.currentTimeMillis();
        currentState = nextStartState;
        nextStartState = 0;
        stateTimeouts = getWaitTimes();
        startState(currentState);
    }

    @Override
    protected void execute() {
        boolean continueToNextState = executeState(currentState) == ExecuteResult.DONE
                // We should also continue to the next state if the current state has a timeout, and the timeout has passed
                || (stateTimeouts[currentState] > 0 && System.currentTimeMillis() > lastSwitch + stateTimeouts[currentState]);
        while (!continueToNextState) {
            lastSwitch = System.currentTimeMillis();
            currentState += 1;
            if (currentState < stateTimeouts.length) {
                continueToNextState = startState(currentState) == ExecuteResult.DONE;
            } else {
                break;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return currentState >= stateTimeouts.length;
    }

    @Override
    protected void interrupted() {
        end();
    }

    protected abstract ExecuteResult executeState(int state);

    protected abstract ExecuteResult startState(int state);

    protected abstract long[] getWaitTimes();
}
