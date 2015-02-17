package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.commands.SensorStatusOutput;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.AnalogIoPorts;
import org.ingrahamrobotics.robot2015.output.Settings;

public class ToggleSwitches extends Subsystem {

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
            System.err.println("Warning! Invalid value for analog " + name + ": " + rawValue);
            value = false;
        }

        return value;
    }

    public boolean getIndexerBottom() {

        return Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && proccessAnalogInput("indexer-bottom", indexerBottom);
    }

    public boolean getIndexerTop() {
        return Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && proccessAnalogInput("indexer-top", indexerTop);
    }

    public boolean getVerticalClawBottom() {
        return Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && proccessAnalogInput("claw-bottom", clawVerticalBottom);
    }

    public boolean getVerticalClawTop() {
        return Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && proccessAnalogInput("claw-top", clawVerticalTop);
    }
}
