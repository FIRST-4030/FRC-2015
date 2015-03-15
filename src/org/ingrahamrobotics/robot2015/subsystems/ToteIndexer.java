package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIOPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ToteIndexer extends Subsystem {

    private final Talon motor = new Talon(MotorPorts.TOTE_INDEXER_MOTOR);

    public ToteIndexer() {
        Output.initialized("ToteIndexer");
        setSpeed(0);
    }

    public void initDefaultCommand() {
    }
    
    public void setSpeed(double value) {
        motor.set(value);
        if (value > 0.1) {
            Subsystems.toggleSwitches.indexerHasGoneUp();
        }
        Output.output(OutputLevel.RAW_MOTORS, "ToteIndexer:Speed", value);
    }
}
