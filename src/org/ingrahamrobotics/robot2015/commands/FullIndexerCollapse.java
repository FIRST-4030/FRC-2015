package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;

public class FullIndexerCollapse extends Command {

    public FullIndexerCollapse() {
        requires(Subsystems.verticalIndexerControl);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Subsystems.verticalIndexerControl.setSpeed(-Settings.Key.INDEXER_FIXED_SPEED.getDouble());
    }

    @Override
    protected boolean isFinished() {
        return Subsystems.toggleSwitches.getIndexerBottom();
    }

    @Override
    protected void end() {
        Subsystems.verticalIndexerControl.setSpeed(0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
