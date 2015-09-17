package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.commands.ResetIndexerEncoder;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts;

public class Counterb extends Subsystem {

    private final Counter encoder = new Counter(15);

    @Override
    protected void initDefaultCommand() {

    }
    
    public void reset() {
        encoder.reset();
    }

    public int get() {
        return encoder.get();
    }
}
