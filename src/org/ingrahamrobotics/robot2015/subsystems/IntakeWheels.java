package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.commands.ManipulateTote;
import org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.output.Settings;

public class IntakeWheels extends Subsystem {

    private final Talon motor = new Talon(MotorPorts.INTAKE_WHEEL_MOTORS);

    public IntakeWheels() {
        Output.initialized("IntakeWheels");
        setSpeed(0);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ManipulateTote());
    }
    
    public void pullInTote() {
        setSpeed(Settings.Key.TOTE_INTAKE_IN_SPEED.getDouble());
    }

    public void spitOutTote() {
        setSpeed(Settings.Key.TOTE_INTAKE_OUT_SPEED.getDouble());
    }

    public void setSpeed(double value) {
        motor.set(value);
        Output.output(OutputLevel.RAW_MOTORS, "IntakeWheels:Speed", value);
    }
}
