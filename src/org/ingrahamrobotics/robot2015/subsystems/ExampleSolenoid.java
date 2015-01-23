package org.ingrahamrobotics.robot2015.subsystems;

import org.ingrahamrobotics.robot2015.commands.RunExampleSolenoid;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ExampleSolenoid extends Subsystem {

    private final Solenoid solenoid = new Solenoid(0);

    public void initDefaultCommand() {
        setDefaultCommand(new RunExampleSolenoid());
    }

    public void set(boolean on) {
        solenoid.set(on);
    }
}
