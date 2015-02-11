package org.ingrahamrobotics.robot2015;

import org.ingrahamrobotics.robot2015.constants.HardwarePorts.SolenoidPorts;
import org.ingrahamrobotics.robot2015.subsystems.DriveBase;
import org.ingrahamrobotics.robot2015.subsystems.SingleClawSubsystem;
import org.ingrahamrobotics.robot2015.subsystems.ToggleSwitches;
import org.ingrahamrobotics.robot2015.subsystems.VerticalClawShifter;
import org.ingrahamrobotics.robot2015.subsystems.VerticalIndexerControl;

public class Subsystems {

    public static DriveBase driveBase;
    public static SingleClawSubsystem leftClaw;
    public static SingleClawSubsystem rightClaw;
    public static VerticalClawShifter verticalClawShifter;
    public static VerticalIndexerControl verticalIndexerControl;
    public static ToggleSwitches toggleSwitches;

    public static void init() {
        driveBase = new DriveBase();
        leftClaw = new SingleClawSubsystem("Left", SolenoidPorts.LEFT_CLAW_EXTEND, SolenoidPorts.LEFT_CLAW_RETRACT);
        rightClaw = new SingleClawSubsystem("Right", SolenoidPorts.RIGHT_CLAW_EXTEND, SolenoidPorts.RIGHT_CLAW_RETRACT);
        verticalClawShifter = new VerticalClawShifter();
        verticalIndexerControl = new VerticalIndexerControl();
        toggleSwitches = new ToggleSwitches();
    }
}
