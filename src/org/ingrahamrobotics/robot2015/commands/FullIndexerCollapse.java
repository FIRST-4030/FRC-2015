package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;

public class FullIndexerCollapse extends Command {

    public FullIndexerCollapse() {
        requires(Subsystems.verticalIndexerControl);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Subsystems.verticalClawShifter.setSpeed(-1); // TODO: Maybe a configurable speed?
    }

    @Override
    protected boolean isFinished() {
        return Subsystems.toggleSwitches.getIndexerBottom();
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
