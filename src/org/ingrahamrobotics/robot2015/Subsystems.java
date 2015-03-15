package org.ingrahamrobotics.robot2015;

import org.ingrahamrobotics.robot2015.constants.HardwarePorts.SolenoidPorts;
import org.ingrahamrobotics.robot2015.subsystems.DriveBase;
import org.ingrahamrobotics.robot2015.subsystems.IndexerEncoder;
import org.ingrahamrobotics.robot2015.subsystems.PIDSteer;
import org.ingrahamrobotics.robot2015.subsystems.PowerBoard;
import org.ingrahamrobotics.robot2015.subsystems.SingleClawSubsystem;
import org.ingrahamrobotics.robot2015.subsystems.SpeedDrive;
import org.ingrahamrobotics.robot2015.subsystems.ToggleSwitches;
import org.ingrahamrobotics.robot2015.subsystems.VerticalClawShifter;
import org.ingrahamrobotics.robot2015.subsystems.VerticalIndexerControl;

public class Subsystems {

    public static DriveBase driveBase;
    public static PIDSteer pidSteer1;
    public static PIDSteer pidSteer2;
    public static PIDSteer pidSteer3;
    public static PIDSteer pidSteer4;
    public static SpeedDrive pidDrive1;
    public static SpeedDrive pidDrive2;
    public static SpeedDrive pidDrive3;
    public static SpeedDrive pidDrive4;
    public static ToteIndexer toteIndexer;
    public static ToggleSwitches toggleSwitches;
    public static IndexerEncoder indexerEncoder;
    public static PowerBoard powerBoard;

    public static void init() {
        pidSteer1 = new PIDSteer(1);
        pidSteer2 = new PIDSteer(2);
        pidSteer3 = new PIDSteer(3);
        pidSteer4 = new PIDSteer(4);
        pidDrive1 = new SpeedDrive(1);
        pidDrive2 = new SpeedDrive(2);
        pidDrive3 = new SpeedDrive(3);
        pidDrive4 = new SpeedDrive(4);
        driveBase = new DriveBase();
        toteIndexer = new ToteIndexer();
        toggleSwitches = new ToggleSwitches();
        indexerEncoder = new IndexerEncoder();
        powerBoard = new PowerBoard();
    }
}
