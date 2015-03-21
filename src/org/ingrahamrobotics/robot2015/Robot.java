package org.ingrahamrobotics.robot2015;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.ingrahamrobotics.robot2015.autonomous.AutonomousState;
import org.ingrahamrobotics.robot2015.autonomous.AutonomousTemplate;
import org.ingrahamrobotics.robot2015.commands.AutonomousRoutine;
import org.ingrahamrobotics.robot2015.commands.FullIndexerCollapse;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.output.Settings;
import static org.ingrahamrobotics.robot2015.output.Output.output;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as
 * described in the IterativeRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {

    public static boolean rateBasedDrive = true;
    public static OI oi;
    private long lastNetworkCheck, startupTime;
    private boolean recheckedSinceStartup;
    private Command autonomousCommand;
    private boolean initialIndexerResetRun = true;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    public void robotInit() {
        startupTime = System.currentTimeMillis();
        lastNetworkCheck = System.currentTimeMillis();
        Output.initInstance();
        Subsystems.init();
        oi = new OI();
        // instantiate the command used for the autonomous period
//        autonomousCommand = new AutonomousRoutine();
        autonomousCommand = new AutonomousTemplate(new AutonomousState[]{
           new AutonomousState(0, Settings.Key.AUTO_ROUTINE_FWD.getDouble(), Settings.Key.AUTO_ROUTINE_STR.getDouble())
        });
        output(OutputLevel.HIGH, "RobotState", "Initialized");
    }

    public void disabledPeriodic() {
        long now = System.currentTimeMillis();
        if (now >= lastNetworkCheck + 20 * 1000) {
            lastNetworkCheck = now;
            if (now >= startupTime + 40 * 1000 && !recheckedSinceStartup) {
                recheckedSinceStartup = true;
                Output.getRobotTables().recheckNetworkInterfaces(true);
            } else {
                Output.getRobotTables().recheckNetworkInterfaces(false);
            }
        }
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (!initialIndexerResetRun) {
            new FullIndexerCollapse(Settings.Key.INDEXER_INITIAL_CLEARANCE_UP.getInt()).start();
            initialIndexerResetRun = true;
        }
        if (autonomousCommand != null)
            autonomousCommand.start();
        output(OutputLevel.HIGH, "RobotState", "Autonomous");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null)
            autonomousCommand.cancel();
        if (!initialIndexerResetRun) {
            new FullIndexerCollapse(Settings.Key.INDEXER_INITIAL_CLEARANCE_UP.getInt()).start();
            initialIndexerResetRun = true;
        }
        if (Settings.Key.DRIVE_RESET_ENCODERS_ON_ENABLE.getBoolean()) {
            Subsystems.driveBase.resetEncoders();
        }
        output(OutputLevel.HIGH, "RobotState", "Teleop");
    }

    /**
     * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting
     * down.
     */
    public void disabledInit() {
        output(OutputLevel.HIGH, "RobotState", "Disabled");
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
