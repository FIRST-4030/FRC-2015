package org.ingrahamrobotics.robot2015.subsystems;

import org.ingrahamrobotics.robot2015.output.Output;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class BuiltinSensors extends Subsystem {
    private BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();

    public BuiltinSensors() {
        Output.initialized("BuiltinSensors");
    }

    @Override
    protected void initDefaultCommand() {
    }

    public double getX() {
        return accelerometer.getX();
    }

    public double getY() {
        return accelerometer.getY();
    }

    public double getZ() {
        return accelerometer.getZ();
    }

}
