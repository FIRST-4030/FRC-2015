package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;
import org.ingrahamrobotics.robot2015.state.ManualControlState;

public class ManualClawControl extends Command {

    private static final double threshold = 0.05;

    public ManualClawControl() {
        requires(Subsystems.verticalClawShifter);
        ManualControlState.setManualClawRunning(false);
        Subsystems.verticalClawShifter.setSpeed(0);
    }

    @Override
    protected void initialize() {
        ManualControlState.setManualClawRunning(true);
    }

    @Override
    protected void execute() {
        double y = IAxis.manualControl.get();

        if (y > 0 && Subsystems.toggleSwitches.getVerticalClawTop()) {
            return;
        }
        if (y < 0 && Subsystems.toggleSwitches.getVerticalClawBottom()) {
            return;
        }

        if (Math.abs(y) > threshold)
            Subsystems.verticalClawShifter.setSpeed(y);
    }

    //No purpose for this command, will return false
    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Subsystems.verticalClawShifter.setSpeed(0);
        ManualControlState.setManualClawRunning(false);
    }

    //Should always be called, but will redirect to end for form
    @Override
    protected void interrupted() {
        this.end();
    }
}
