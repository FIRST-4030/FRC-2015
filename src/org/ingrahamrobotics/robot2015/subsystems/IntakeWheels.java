package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class IntakeWheels extends Subsystem {

    private final Talon motor = new Talon(MotorPorts.INTAKE_WHEEL_MOTOR);

    public IntakeWheels() {
        Output.initialized("IntakeWheels");
        setSpeed(0);
    }

    public void initDefaultCommand() {
    }
    
    public void setSpeed(double value) {
        motor.set(value);
        Output.output(OutputLevel.RAW_MOTORS, "IntakeWheels:Speed", value);
    }
}
