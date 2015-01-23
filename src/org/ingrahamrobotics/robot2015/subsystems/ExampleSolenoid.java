package org.ingrahamrobotics.robot2015.subsystems;

import static org.ingrahamrobotics.robot2015.output.Output.output;

import org.ingrahamrobotics.robot2015.commands.RunExampleSolenoid;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ExampleSolenoid extends Subsystem {

    private final Solenoid solenoid = new Solenoid(0);

    public ExampleSolenoid() {
        output(OutputLevel.INITIALIZED_SYSTEMS, "ExampleSolenoid", true);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new RunExampleSolenoid());
    }

    public void set(boolean on) {
        solenoid.set(on);
    }
}
