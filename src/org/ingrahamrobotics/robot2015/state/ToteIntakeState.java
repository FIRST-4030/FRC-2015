package org.ingrahamrobotics.robot2015.state;

import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ToteIntakeState {

    public enum IntakeDirection {
        IN, OUT, STOPPED;
    }

    private static IntakeDirection currentDirection = IntakeDirection.STOPPED;

    public static IntakeDirection getCurrentDirection() {
        return currentDirection;
    }

    public static void setCurrentDirection(final IntakeDirection currentDirection) {
        Output.output(OutputLevel.HIGH, "Intake Direction", currentDirection);
        ToteIntakeState.currentDirection = currentDirection;
    }
}
