package org.ingrahamrobotics.robot2015.commands;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.utils.ExecuteResult;
import org.ingrahamrobotics.robot2015.utils.TimedCommand;

public class AutonomousRoutine extends TimedCommand {

    public AutonomousRoutine() {
        requires(Subsystems.driveBase);
    }

    @Override
    protected ExecuteResult executeState(final int state) {
        return ExecuteResult.NOT_DONE;
    }

    @Override
    protected ExecuteResult startState(final int state) {
        Subsystems.driveBase.updateSteerPID();
        Subsystems.driveBase.drive(Settings.Key.AUTO_ROUTINE_FWD.getDouble(), Settings.Key.AUTO_ROUTINE_STR.getDouble(), 0);
        return ExecuteResult.NOT_DONE;
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
