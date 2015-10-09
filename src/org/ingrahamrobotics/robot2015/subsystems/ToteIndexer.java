package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

import org.ingrahamrobotics.robot2015.Robot;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.commands.ManualIndexerControl;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ToteIndexer extends PIDSubsystem {

	private static final double ticksPerRadian = (611 - 35) / (Math.PI * 2);
	private final Talon motor = new Talon(MotorPorts.INDEXER_MOTORS);
	private final IndexerEncoder encoder = Subsystems.indexerEncoder;
	private final int indexerDeadZone = 100;

    public ToteIndexer() {
    	super("ToteIndexer", 1.0, 0.0, 0.0);
        Output.initialized("ToteIndexer");
        setSpeed(0);
        setSetpoint(0.0);
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
    
    public double getSpeed() {
        return motor.get();
    }

	@Override
	protected double returnPIDInput() {
		double pidInput = (encoder.getDistance() / ticksPerRadian);// % Math.PI;
        return pidInput;
	}

	@Override
	protected void usePIDOutput(double output) {
		motor.set(output);
	}
	
	public void setPID(double p, double i, double d) {
        getPIDController().setPID(p, i, d);
    }
	
	@Override
	public void setSetpoint(final double setpoint) {
	    super.setSetpoint(setpoint);
	}
	
	public boolean isAtSetpoint() {
		return Math.abs(super.getSetpoint() - encoder.get()) < indexerDeadZone;
	}
	
}
