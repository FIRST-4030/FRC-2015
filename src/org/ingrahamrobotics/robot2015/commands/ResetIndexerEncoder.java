package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;

public class ResetIndexerEncoder extends Command {

    private boolean lastPressed;

    public ResetIndexerEncoder() {
        requires(Subsystems.indexerEncoder);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        boolean pressed = Subsystems.toggleSwitches.getIndexerBottom();

        if (lastPressed && !pressed) {
            lastPressed = false;
            Subsystems.indexerEncoder.reset();
        } else {
            lastPressed = pressed;
        }
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
