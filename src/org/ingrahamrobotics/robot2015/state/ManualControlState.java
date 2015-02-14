package org.ingrahamrobotics.robot2015.state;

import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ManualControlState {

    private static boolean manualClawRunning = false;
    private static boolean manualIndexerRunning = false;

    public static void setManualClawRunning(final boolean manualClawRunning) {
        ManualControlState.manualClawRunning = manualClawRunning;
        updateOutput();
    }

    public static void setManualIndexerRunning(final boolean manualIndexerRunning) {
        ManualControlState.manualIndexerRunning = manualIndexerRunning;
        updateOutput();
    }

    private static void updateOutput() {
        if (manualClawRunning) {
            Output.output(OutputLevel.HIGH, "ManualControlDevice", "Claw");
        } else if (manualIndexerRunning) {
            Output.output(OutputLevel.HIGH, "ManualControlDevice", "Indexer");
        } else {
            Output.output(OutputLevel.HIGH, "ManualControlDevice", "None");
        }
    }

    public static boolean isManualClawRunning() {
        return manualClawRunning;
    }

    public static boolean isManualIndexerRunning() {
        return manualIndexerRunning;
    }
}
