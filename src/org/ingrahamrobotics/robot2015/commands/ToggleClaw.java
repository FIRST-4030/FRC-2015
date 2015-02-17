package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;

public class ToggleClaw extends Command {

    private boolean finished;
    private boolean toggled;
    private boolean isLeft;

    public ToggleClaw(boolean isLeft) {
        this.isLeft = isLeft;
        requires(isLeft ? Subsystems.leftClaw : Subsystems.rightClaw);
        if (isLeft) {
            Subsystems.leftClaw.set(false);
        } else {
            Subsystems.rightClaw.set(false);
        }
    }

    @Override
    protected void initialize() {
        finished = false;
    }

    @Override
    protected void execute() {
        finished = true;
        toggled = !toggled;
        if (isLeft) {
            Subsystems.leftClaw.set(toggled);
        } else {
            Subsystems.rightClaw.set(toggled);
        }
    }

    @Override
    protected boolean isFinished() {
        return finished;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }
}
