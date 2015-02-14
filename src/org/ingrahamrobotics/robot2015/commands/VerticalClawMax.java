package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;

public class VerticalClawMax extends Command {

    private final boolean directionIsUp;

    public VerticalClawMax(final boolean up) {
        directionIsUp = up;
        requires(Subsystems.verticalClawShifter);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        double speed = Settings.Key.VERTICAL_CLAW_MAX_CMD_SPEED.getDouble();
        if (directionIsUp) {
            Subsystems.verticalClawShifter.setSpeed(speed);
        } else {
            Subsystems.verticalClawShifter.setSpeed(-speed);
        }
    }

    @Override
    protected boolean isFinished() {
        if (directionIsUp) {
            return Subsystems.toggleSwitches.getVerticalClawTop();
        } else {
            return Subsystems.toggleSwitches.getVerticalClawBottom();
        }
    }

    @Override
    protected void end() {
        Subsystems.verticalClawShifter.setSpeed(0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
