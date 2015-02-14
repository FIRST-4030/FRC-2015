package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class SwitchStatusOutput extends Command {

    public SwitchStatusOutput() {
        requires(Subsystems.toggleSwitches); // Purely for initDefaultCommand in ToggleSwitches
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Output.output(OutputLevel.RAW_SENSORS, "Switches:IndexerBottom", Subsystems.toggleSwitches.getIndexerBottom());
        Output.output(OutputLevel.RAW_SENSORS, "Switches:IndexerTop", Subsystems.toggleSwitches.getIndexerTop());
        Output.output(OutputLevel.RAW_SENSORS, "Switches:ClawBottom", Subsystems.toggleSwitches.getVerticalClawBottom());
        Output.output(OutputLevel.RAW_SENSORS, "Switches:ClawTop", Subsystems.toggleSwitches.getVerticalClawTop());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }
}
