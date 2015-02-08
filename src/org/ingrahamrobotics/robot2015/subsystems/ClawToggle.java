package org.ingrahamrobotics.robot2015.subsystems;

import org.ingrahamrobotics.robot2015.constants.HardwarePorts.SolenoidPorts;
import org.ingrahamrobotics.robot2015.output.Output;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClawToggle extends Subsystem {

	private Solenoid extend;
	private Solenoid retract;

	public ClawToggle(boolean isLeft) {
		extend = new Solenoid(isLeft ? SolenoidPorts.LEFT_CLAW_EXTEND
				: SolenoidPorts.RIGHT_CLAW_EXTEND);
		retract = new Solenoid(isLeft ? SolenoidPorts.LEFT_CLAW_RETRACT
				: SolenoidPorts.RIGHT_CLAW_RETRACT);
		Output.initialized("ClawToggleLeft");
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public void set(boolean extended) {
		extend.set(extended);
		retract.set(!extended);

	}

}
