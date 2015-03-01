package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.utils.TimedCommand;

public class AutonomousRoutine extends TimedCommand {

    public AutonomousRoutine() {
        requires(Subsystems.driveBase);
    }

    @Override
    protected boolean executeState(final int state) {
        return false;
    }

    @Override
    protected boolean startState(final int state) {
        double p = Settings.Key.STEER_PID_P.getDouble();
        double i = Settings.Key.STEER_PID_I.getDouble();
        double d = Settings.Key.STEER_PID_D.getDouble();
        Subsystems.driveBase.setSteerPID(p, i, d);
        Subsystems.driveBase.drive(0, -0.5, 0);
        return false;
    }

    @Override
    protected long[] getWaitTimes() {
        return new long[Settings.Key.AUTO_ROUTINE_TIME.getInt()];
    }

    @Override
    protected void end() {
        Subsystems.driveBase.drive(0, 0, 0);
    }
}
