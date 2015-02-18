package org.ingrahamrobotics.robot2015.utils;

import edu.wpi.first.wpilibj.command.Command;

public abstract class TimedCommand extends Command {

    protected int nextStartState;
    protected long startTime;
    protected long lastSwitch;
    protected int currentState;
    protected long[] states;

    public void setNextStartState(final int nextStartState) {
        this.nextStartState = nextStartState;
    }

    @Override
    protected void initialize() {
        startTime = lastSwitch = System.currentTimeMillis();
        currentState = nextStartState;
        nextStartState = 0;
        states = getWaitTimes();
        startState(currentState);
    }

    @Override
    protected void execute() {
        boolean next = executeState(currentState) || (states[currentState] > 0 && System.currentTimeMillis() > lastSwitch + states[currentState]);
        while (next) {
            lastSwitch = System.currentTimeMillis();
            currentState += 1;
            if (currentState < states.length) {
                next = startState(currentState);
            } else {
                break;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return currentState >= states.length;
    }

    @Override
    protected void interrupted() {
        end();
    }

    protected abstract boolean executeState(int state);

    protected abstract boolean startState(int state);

    protected abstract long[] getWaitTimes();
}
