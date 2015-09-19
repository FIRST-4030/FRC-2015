package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.commands.ManualIndexerControl;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ToteIndexer extends Subsystem {

    public final int indexerHeight;
	private final Talon motor = new Talon(MotorPorts.INDEXER_MOTORS);

    public ToteIndexer() {
        Output.initialized("ToteIndexer");
        setSpeed(0);
        indexerHeight = 0;
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ManualIndexerControl());
    }

    public void setSpeed(double value) {
        motor.set(value);
        // We can get away with using -0.1 as well because if the switch was pressed and we were going down, setSpeed would never have been called.
        // In any case, we need this just to make sure that any direction reversals for the motors *are* taken into account.
        if (Math.abs(value) > 0.1) {
            Subsystems.toggleSwitches.indexerHasGoneUp();
        }
        Output.output(OutputLevel.RAW_MOTORS, "ToteIndexer:Speed", value);
    }
}
