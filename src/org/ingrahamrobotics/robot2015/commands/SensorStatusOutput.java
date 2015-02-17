package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class SensorStatusOutput extends Command {

    public SensorStatusOutput() {
        requires(Subsystems.toggleSwitches); // Purely for initDefaultCommand in ToggleSwitches
        execute(); // Output initial status
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
        Output.output(OutputLevel.RAW_SENSORS, "IndexerEncoder", Subsystems.indexerEncoder.get());
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
