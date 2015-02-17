package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;

/**
 *
 */
public class RunPIDDrive extends Command {

    private double FWD;
    private double STR;

    public RunPIDDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Subsystems.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        Subsystems.driveBase.stop();

        FWD = 0;
        STR = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double x = IAxis.driveX.get();
        double y = IAxis.driveY.get();
        double RCW = IAxis.steer.get();

        double fieldAngle = Math.PI / 2;
        
        double p = Settings.Key.STEER_PID_P.getDouble();
        double i = Settings.Key.STEER_PID_I.getDouble();
        double d = Settings.Key.STEER_PID_D.getDouble();

        FWD = -y;
        STR = x;

        double temp = FWD * Math.cos(fieldAngle) + STR * Math.sin(fieldAngle);
        STR = -1 * FWD * Math.sin(fieldAngle) + STR * Math.cos(fieldAngle);
        FWD = temp;
        
        Subsystems.driveBase.setPID(p, i, d);

        Subsystems.driveBase.drive(FWD, STR, RCW);
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
