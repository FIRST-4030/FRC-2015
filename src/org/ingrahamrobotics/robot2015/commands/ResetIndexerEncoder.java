package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;

public class ResetIndexerEncoder extends Command {

    private boolean lastPressed;

    public ResetIndexerEncoder() {
        requires(Subsystems.indexerEncoder);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        boolean pressed = Subsystems.toggleSwitches.getIndexerBottom();

        if (lastPressed && !pressed) {
            lastPressed = false;
            Subsystems.indexerEncoder.reset();
            Output.output(OutputLevel.DEBUG, "indexer-encoder-last-reset-time", new SimpleDateFormat("HH:mm:ss").format(new Date()));
        } else {
            lastPressed = pressed;
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }
}
