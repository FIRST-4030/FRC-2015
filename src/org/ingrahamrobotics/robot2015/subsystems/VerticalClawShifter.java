package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class VerticalClawShifter extends Subsystem {

    private final Talon motor = new Talon(MotorPorts.CLAW_VERTICAL_SHIFTER);

    public VerticalClawShifter() {
        Output.initialized("VerticalClawShifter");
        setSpeed(0);
    }

    public void initDefaultCommand() {
    }

    public void setSpeed(double value) {
        motor.set(value);
        Output.output(OutputLevel.RAW_MOTORS, "VerticalClawShifter:Speed", value);
    }
}
