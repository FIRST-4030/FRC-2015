package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.commands.ResetIndexerEncoder;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts;

public class IndexerEncoder extends Subsystem {

    private final Encoder encoder = new Encoder(DigitalIoPorts.INDEXER_ENCODER_A, DigitalIoPorts.INDEXER_ENCODER_B);

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ResetIndexerEncoder());
    }

    public void reset() {
        encoder.reset();
    }

    public int get() {
        return encoder.get();
    }
}
