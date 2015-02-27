package org.ingrahamrobotics.robot2015.commands;

import java.util.Arrays;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.subsystems.PIDSteer;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ResetTurningMotors extends Command {

    private boolean isReset = false;
    boolean[] hasReset;

    public ResetTurningMotors() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Subsystems.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        hasReset = new boolean[]{false, false, false, false};
        Output.output(OutputLevel.SWERVE_DEBUG, "reset", Arrays.toString(hasReset));
        for (PIDSteer pidSteer : Subsystems.driveBase.steerSystem) {
            pidSteer.disable();
            pidSteer.setSpeed(0.5);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        for (int i = 0; i < 4; i++) {
            PIDSteer pidSteer = Subsystems.driveBase.steerSystem[i];
            if (!pidSteer.getPreviousResetState() && pidSteer.getResetSwitch()) {
                pidSteer.resetEncoder();
                hasReset[i] = true;
                //pidSteer.setSpeed(0.0);
            }
            pidSteer.setPrevResetState(pidSteer.getResetSwitch());
        }
        Output.output(OutputLevel.SWERVE_DEBUG, "reset", Arrays.toString(hasReset));

        isReset = true;
        for (int i = 0; i < 4; i++) {
            if (!hasReset[i])
                isReset = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isReset;
    }

    // Called once after isFinished returns true
    protected void end() {
        Output.output(OutputLevel.SWERVE_DEBUG, "reset", "Finished");
        for (PIDSteer pidSteer : Subsystems.driveBase.steerSystem) {
            pidSteer.setSpeed(0);
            pidSteer.enable();
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
