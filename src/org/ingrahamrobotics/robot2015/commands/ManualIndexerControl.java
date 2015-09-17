package org.ingrahamrobotics.robot2015.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.constants.input.IAxis;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.output.Settings;

public class ManualIndexerControl extends Command {

    public ManualIndexerControl() {
        requires(Subsystems.toteIndexer);
        requires(Subsystems.indexerEncoder);
        Output.output(OutputLevel.HIGH, "ManualControlDevice", "None");
        Subsystems.toteIndexer.setSpeed(0);
    }

    @Override
    protected void initialize() {
        Output.output(OutputLevel.HIGH, "ManualControlDevice", "Indexer");
    }

    @Override
    protected void execute() {
        double y = -IAxis.manualControl.get();
        Output.output(OutputLevel.RAW_SENSORS, "IndexerEncoder2", Subsystems.indexerEncoder.get());
        int test = Subsystems.indexerEncoder.get();
        Output.output(OutputLevel.RAW_SENSORS, "Counter", test);

//        if (y > 0 && (Subsystems.toggleSwitches.getIndexerTop()
//                || Subsystems.indexerEncoder.get() > Settings.Key.INDEXER_MAX_HEIGHT.getInt())) {
//            Subsystems.toteIndexer.setSpeed(0);
//            return;
//        }
        if (y < 0 && Subsystems.toggleSwitches.getIndexerBottom()) {
            Subsystems.toteIndexer.setSpeed(0);
            return;
        } 

        Subsystems.toteIndexer.setSpeed(y * Settings.Key.INDEXER_SPEED.getDouble());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Subsystems.toteIndexer.setSpeed(0);
        Output.output(OutputLevel.HIGH, "ManualControlDevice", "None");
    }

    //Should always be called, but will redirect to end for form
    @Override
    protected void interrupted() {
        this.end();
    }
}
