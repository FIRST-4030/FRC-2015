package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.state.ManualControlState;

public class ManualIndexerControl extends Command {

    private static final double threshold = 0.05;

    public ManualIndexerControl() {
        requires(Subsystems.verticalIndexerControl);
        ManualControlState.setManualIndexerRunning(false);
    }

    @Override
    protected void initialize() {
        ManualControlState.setManualIndexerRunning(true);
    }

    @Override
    protected void execute() {
        double y = IAxis.manualControl.get();

        if (y > 0 && Subsystems.toggleSwitches.getIndexerTop()) {
            return;
        }
        if (y < 0 && Subsystems.toggleSwitches.getIndexerBottom()) {
            return;
        }

        if (Math.abs(y) > threshold && !ManualControlState.isManualClawRunning()) {
            Subsystems.verticalIndexerControl.setSpeed(y);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Subsystems.verticalIndexerControl.setSpeed(0);
        ManualControlState.setManualIndexerRunning(false);
    }

    //Should always be called, but will redirect to end for form
    @Override
    protected void interrupted() {
        this.end();
    }
}