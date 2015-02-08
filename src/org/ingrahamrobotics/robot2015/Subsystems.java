package org.ingrahamrobotics.robot2015;

import org.ingrahamrobotics.robot2015.constants.HardwarePorts.SolenoidPorts;
import org.ingrahamrobotics.robot2015.subsystems.ClawToggle;
import org.ingrahamrobotics.robot2015.subsystems.DriveBase;
import org.ingrahamrobotics.robot2015.subsystems.SimpleDriveSubsystem;
import org.ingrahamrobotics.robot2015.subsystems.SingleClawSubsystem;
import org.ingrahamrobotics.robot2015.subsystems.VerticalClawShifter;

public class Subsystems {

    public static SimpleDriveSubsystem simpleDrive;
    public static DriveBase driveBase;
    public static SingleClawSubsystem leftClaw;
    public static SingleClawSubsystem rightClaw;
    public static VerticalClawShifter verticalClawShifter;
    public static ClawToggle clawToggleLeft;
    public static ClawToggle clawToggleRight;
    
    
    public static void init() {
        simpleDrive = new SimpleDriveSubsystem();
        driveBase = new DriveBase();
        leftClaw = new SingleClawSubsystem("Left",
                SolenoidPorts.LEFT_CLAW_EXTEND, SolenoidPorts.LEFT_CLAW_RETRACT);
        rightClaw = new SingleClawSubsystem("Right",
                SolenoidPorts.RIGHT_CLAW_EXTEND,
                SolenoidPorts.RIGHT_CLAW_RETRACT);
        verticalClawShifter = new VerticalClawShifter();
        clawToggleLeft = new ClawToggle(true);
        clawToggleRight = new ClawToggle(false);
        
    }
    
}
