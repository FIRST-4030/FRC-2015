package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.util.HashSet;
import java.util.Set;
import org.ingrahamrobotics.robot2015.commands.SensorStatusOutput;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.AnalogIoPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ToggleSwitches extends Subsystem {

    private boolean indexerHasGoneUpSinceLastBottomPress;
    private long lastIndexerBottomOn;
    private Set<String> warnedFor = new HashSet<>();
    private AnalogInput indexerBottom = new AnalogInput(AnalogIoPorts.BOTTOM_INDEXER_SWITCH);
//    private AnalogInput indexerTop = new AnalogInput(AnalogIoPorts.TOP_INDEXER_SWITCH);

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new SensorStatusOutput());
    }

    private boolean proccessAnalogInput(String name, AnalogInput input) {
        int rawValue = input.getValue();
        boolean value;

        if (rawValue < 1024) {
            value = false;
        } else if (rawValue > 3072) {
            value = true;
        } else {
            if (!warnedFor.contains(name)) {
                warnedFor.add(name);
                System.err.println("Warning! Invalid value for analog " + name + ": " + rawValue);
            }
            value = false;
        }
        Output.output(OutputLevel.RAW_SENSORS, "RawSwitch:" + name, value);
        return value;
    }

    public boolean getIndexerTop() {
        return false;
    }

    public void indexerHasGoneUp() {
        indexerHasGoneUpSinceLastBottomPress = true;
    }

    public boolean getIndexerBottom() {
        boolean current = proccessAnalogInput("indexer-bottom", indexerBottom);
        long now = System.currentTimeMillis();
        if (current) {
            lastIndexerBottomOn = now;
            indexerHasGoneUpSinceLastBottomPress = false;
        }
        if (lastIndexerBottomOn + 200 >= now) {
            return true;
        } else {
            // If the claw hasn't moved up at all since the button was last
            // pressed, it should still count as pressed
            return !indexerHasGoneUpSinceLastBottomPress;
        }
    }

    public boolean getVerticalClawTop() {
        return false;
    }
}
