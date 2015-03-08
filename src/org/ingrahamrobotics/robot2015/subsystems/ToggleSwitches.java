package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.ingrahamrobotics.robot2015.commands.SensorStatusOutput;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.AnalogIoPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ToggleSwitches extends Subsystem {

    private boolean clawHasGoneUpSinceLastBottomPress;
    private long lastIndexerBottomOn;
    private long lastClawBottomOn;
    private Set<String> warnedFor = new HashSet<>();
    private AnalogInput indexerBottom = new AnalogInput(AnalogIoPorts.BOTTOM_INDEXER_SWITCH);
//    private AnalogInput indexerTop = new AnalogInput(AnalogIoPorts.TOP_INDEXER_SWITCH);
    private AnalogInput clawVerticalBottom = new AnalogInput(AnalogIoPorts.BOTTOM_VERTICAL_CLAW_SWITCH);
//    private AnalogInput clawVerticalTop = new AnalogInput(AnalogIoPorts.TOP_VERTICAL_CLAW_SWITCH);

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
        Output.output(OutputLevel.RAW_SENSORS, "RawSwitch:"+name, value);
        return value;
    }

    public boolean getIndexerBottom() {
        boolean current = proccessAnalogInput("indexer-bottom", indexerBottom);
        long now = System.currentTimeMillis();
        if (current) {
            lastIndexerBottomOn = now;
        }
        if (lastIndexerBottomOn + 200 >= now) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getIndexerTop() {
        return false;
    }

    public void clawHasGoneUp() {
        clawHasGoneUpSinceLastBottomPress = true;
    }

    public boolean getVerticalClawBottom() {
        boolean current = !proccessAnalogInput("claw-bottom",
                clawVerticalBottom);
        long now = System.currentTimeMillis();
        if (current) {
            lastClawBottomOn = now;
            clawHasGoneUpSinceLastBottomPress = false;
        }
        if (lastClawBottomOn + 200 >= now) {
            return true;
        } else {
            // If the claw hasn't moved up at all since the button was last
            // pressed, it should still count as pressed
            return !clawHasGoneUpSinceLastBottomPress;
        }
    }

    public boolean getVerticalClawTop() {
        return false;
    }
}
