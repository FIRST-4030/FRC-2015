package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.commands.SwitchStatusOutput;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts;

public class ToggleSwitches extends Subsystem {

    private DigitalInput indexerBottom = new DigitalInput(DigitalIoPorts.BOTTOM_INDEXER_SWITCH);
    private DigitalInput indexerTop = new DigitalInput(DigitalIoPorts.TOP_INDEXER_SWITCH);
    private DigitalInput clawVerticalBottom = new DigitalInput(DigitalIoPorts.BOTTOM_VERTICAL_CLAW_SWITCH);
    private DigitalInput clawVerticalTop = new DigitalInput(DigitalIoPorts.TOP_VERTICAL_CLAW_SWITCH);

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new SwitchStatusOutput());
    }

    public boolean getIndexerBottom() {
        return indexerBottom.get();
    }

    public boolean getIndexerTop() {
        return indexerTop.get();
    }

    public boolean getVerticalClawBottom() {
        return clawVerticalBottom.get();
    }

    public boolean getVerticalClawTop() {
        return clawVerticalTop.get();
    }
}
