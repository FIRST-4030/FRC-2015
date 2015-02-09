package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class SingleClawSubsystem extends Subsystem {

    private final Solenoid extendSolenoid;
    private final Solenoid retractSolenoid;
    private final String name;

    public SingleClawSubsystem(String name, int extendPort, int retractPort) {
        this.name = name;
        this.extendSolenoid = new Solenoid(extendPort);
        this.retractSolenoid = new Solenoid(retractPort);
        Output.initialized("ClawSolenoid:" + name);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void set(boolean extended) {
        extendSolenoid.set(extended);
        retractSolenoid.set(!extended);
        Output.output(OutputLevel.RAW_MOTORS, "ClawSolenoid:" + name,
                extended ? "extended" : "retracted");
    }
}
