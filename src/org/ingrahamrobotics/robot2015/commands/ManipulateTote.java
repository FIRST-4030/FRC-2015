package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IButton;

public class ManipulateTote extends Command {

    public ManipulateTote() {
        requires(Subsystems.intakeWheels);
        Subsystems.intakeWheels.setSpeed(0);
    }

    @Override
    protected void initialize() {

    }

    protected void execute() {
        boolean intakeIn = IButton.toteIntakeIn.getButton().get();
        boolean intakeOut = IButton.toteIntakeOut.getButton().get();

        if (intakeIn) {
            Subsystems.intakeWheels.pullInTote();
        } else if (intakeOut) {
            Subsystems.intakeWheels.spitOutTote();
        } else {
            Subsystems.intakeWheels.setSpeed(0);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Subsystems.intakeWheels.setSpeed(0);
    }

    //Should always be called, but will redirect to end for form
    @Override
    protected void interrupted() {
        this.end();
    }
}
