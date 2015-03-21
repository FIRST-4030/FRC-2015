package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.output.Settings;

/**
 *
 */
public class RunPIDDrive extends Command {

//    private double FWD;
//    private double STR;

    public RunPIDDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Subsystems.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        Subsystems.driveBase.stop();

//        FWD = 0;
//        STR = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double multiplier = Settings.Key.DRIVE_SPEED_MULTIPLIER.getDouble();
        double turnMultiplier = Settings.Key.TURN_SPEED_MULTIPLIER.getDouble();
        double x = IAxis.driveX.get() * Math.abs(IAxis.driveX.get()) * multiplier;
        double y = -IAxis.driveY.get() * Math.abs(IAxis.driveY.get()) * multiplier;
        double turn = -IAxis.steer.get() * Math.abs(IAxis.steer.get()) * turnMultiplier;
        double movementDirection = Math.atan2(y, x);
        double movementSpeed = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        Output.output(OutputLevel.SWERVE_DEBUG, "input-x", x);
        Output.output(OutputLevel.SWERVE_DEBUG, "input-y", y);
        Output.output(OutputLevel.SWERVE_DEBUG, "input-direction", x);
        Output.output(OutputLevel.SWERVE_DEBUG, "input-speed", y);
        Output.output(OutputLevel.SWERVE_DEBUG, "input-steer", turn);
        movementSpeed *= multiplier;

        // This code was being used as a possible use of a field angle, but what it was doing is basically
        // flipping FWD and STR - it also assigned FWD and STR to class-level variables, which is just done in this
        // non-commented block now.
//        FWD = x;
//        STR = y;
//        double fieldAngle = Math.PI / 2;
//
//        FWD = -y;
//        STR = x;
//
//        double temp = FWD * Math.cos(fieldAngle) + STR * Math.sin(fieldAngle);
//        STR = -1 * FWD * Math.sin(fieldAngle) + STR * Math.cos(fieldAngle);
//        FWD = temp;

        Subsystems.driveBase.updateSteerPID();

        Subsystems.driveBase.driveWithAngle(movementDirection, movementSpeed, turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
//        Subsystems.driveBase.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
//        Subsystems.driveBase.stop();
    }
}
