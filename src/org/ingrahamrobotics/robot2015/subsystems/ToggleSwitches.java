package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.commands.SensorStatusOutput;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts;
import org.ingrahamrobotics.robot2015.output.Settings;

public class ToggleSwitches extends Subsystem {

    private DigitalInput indexerBottom = new DigitalInput(DigitalIoPorts.BOTTOM_INDEXER_SWITCH);
    private DigitalInput indexerTop = new DigitalInput(DigitalIoPorts.TOP_INDEXER_SWITCH);
    private DigitalInput clawVerticalBottom = new DigitalInput(DigitalIoPorts.BOTTOM_VERTICAL_CLAW_SWITCH);
    private DigitalInput clawVerticalTop = new DigitalInput(DigitalIoPorts.TOP_VERTICAL_CLAW_SWITCH);

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new SensorStatusOutput());
    }

    public boolean getIndexerBottom() {
        return !Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && indexerBottom.get();
    }

    public boolean getIndexerTop() {
        return !Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && indexerTop.get();
    }

    public boolean getVerticalClawBottom() {
        return !Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && clawVerticalBottom.get();
    }

    public boolean getVerticalClawTop() {
        return !Settings.Key.ACTUALLY_USE_SWITCHES.getBoolean() && clawVerticalTop.get();
    }
}
