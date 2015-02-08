package org.ingrahamrobotics.robot2015.subsystems;

import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class IndexerSpoolSubsystem extends Subsystem {

    private final Talon spoolMotor = new Talon(MotorPorts.INDEXER_SPOOL_MOTOR);

    public void initDefaultCommand() {
    }

    public void setSpeed(double speed) {
        spoolMotor.set(speed);
        Output.output(OutputLevel.RAW_MOTORS, "Indexer:Speed", speed);
    }
}
