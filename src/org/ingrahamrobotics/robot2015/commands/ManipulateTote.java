package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.state.ToteIntakeState;
import org.ingrahamrobotics.robot2015.state.ToteIntakeState.IntakeDirection;

public class ManipulateTote extends Command {

    private final boolean goingIn;
    private boolean finished;

    public ManipulateTote(final boolean button) {
        goingIn = button;
        requires(Subsystems.intakeWheels);
    }

    @Override
    protected void initialize() {
        finished = false;
    }

    protected void execute() {
        switch (ToteIntakeState.getCurrentDirection()) {
            case IN:
                if (goingIn) {
                    setDirection(IntakeDirection.STOPPED);
                } else {
                    setDirection(IntakeDirection.OUT);
                }
                break;
            case OUT:
                if (goingIn) {
                    setDirection(IntakeDirection.IN);
                } else {
                    setDirection(IntakeDirection.STOPPED);
                }
                break;
            case STOPPED:
                if (goingIn) {
                    setDirection(IntakeDirection.IN);
                } else {
                    setDirection(IntakeDirection.OUT);
                }
                break;
        }
        finished = true;
    }

    private void setDirection(IntakeDirection direction) {
        ToteIntakeState.setCurrentDirection(direction);
        switch (direction) {
            case IN:
                Subsystems.intakeWheels.setSpeed(Settings.Key.TOTE_INTAKE_IN_SPEED.getDouble());
                break;
            case OUT:
                Subsystems.intakeWheels.setSpeed(Settings.Key.TOTE_INTAKE_OUT_SPEED.getDouble());
                break;
            case STOPPED:
                Subsystems.intakeWheels.setSpeed(0);
                break;
        }
    }

    @Override
    protected boolean isFinished() {
        return finished;
    }

    @Override
    protected void end() {
    }

    //Should always be called, but will redirect to end for form
    @Override
    protected void interrupted() {
        this.end();
    }
}
