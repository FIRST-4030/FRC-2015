package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.util.HashSet;
import java.util.Set;
import org.ingrahamrobotics.robot2015.commands.SensorStatusOutput;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.AnalogIoPorts;

public class ToggleSwitches extends Subsystem {

    private Set<String> warnedFor = new HashSet<>();
    private AnalogInput indexerBottom = new AnalogInput(AnalogIoPorts.BOTTOM_INDEXER_SWITCH);
    private AnalogInput indexerTop = new AnalogInput(AnalogIoPorts.TOP_INDEXER_SWITCH);
    private AnalogInput clawVerticalBottom = new AnalogInput(AnalogIoPorts.BOTTOM_VERTICAL_CLAW_SWITCH);
    private AnalogInput clawVerticalTop = new AnalogInput(AnalogIoPorts.TOP_VERTICAL_CLAW_SWITCH);

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

        return value;
    }

    public boolean getIndexerBottom() {
        return proccessAnalogInput("indexer-bottom", indexerBottom);
    }

    public boolean getIndexerTop() {
        return proccessAnalogInput("indexer-top", indexerTop);
    }

    public boolean getVerticalClawBottom() {
        return proccessAnalogInput("claw-bottom", clawVerticalBottom);
    }

    public boolean getVerticalClawTop() {
        return proccessAnalogInput("claw-top", clawVerticalTop);
    }
}
