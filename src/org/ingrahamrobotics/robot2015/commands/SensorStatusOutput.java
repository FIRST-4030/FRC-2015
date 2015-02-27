package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.subsystems.PIDSteer;

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
        for (PIDSteer steer : Subsystems.driveBase.steerSystem) {
            Output.output(OutputLevel.RAW_SENSORS, "ResetSwitch:" + steer.getName(), steer.getResetSwitch());
        }
        Output.output(OutputLevel.RAW_SENSORS, "IndexerEncoder", Subsystems.indexerEncoder.get());
        Output.output(OutputLevel.POWER, "tempurature", Subsystems.powerBoard.getTempurature());
        Output.output(OutputLevel.POWER, "total-current", Subsystems.powerBoard.getTotalCurrent());
        Output.output(OutputLevel.POWER, "total-energy", Subsystems.powerBoard.getTotalEnergy());
        Output.output(OutputLevel.POWER, "total-power", Subsystems.powerBoard.getTotalPower());
        Output.output(OutputLevel.POWER, "input-voltage", Subsystems.powerBoard.getVoltage());
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
