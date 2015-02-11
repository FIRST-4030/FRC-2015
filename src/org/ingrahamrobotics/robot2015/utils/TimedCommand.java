package org.ingrahamrobotics.robot2015.utils;

import edu.wpi.first.wpilibj.command.Command;

public abstract class TimedCommand extends Command {

    protected long startTime;
    protected long lastSwitch;
    protected int currentState;
    protected long[] states;

    @Override
    protected void initialize() {
        startTime = lastSwitch = System.currentTimeMillis();
        currentState = 0;
        states = getWaitTimes();
        startState(currentState);
    }

    @Override
    protected void execute() {
        boolean next = executeState(currentState) || (states[currentState] > 0 && System.currentTimeMillis() > lastSwitch + states[currentState]);
        if (next) {
            lastSwitch = System.currentTimeMillis();
            currentState += 1;
            startState(currentState);
        }
    }

    @Override
    protected boolean isFinished() {
        return currentState >= states.length;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

    protected abstract boolean executeState(int state);

    protected abstract void startState(int state);

    protected abstract long[] getWaitTimes();
}
