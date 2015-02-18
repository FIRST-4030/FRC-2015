package org.ingrahamrobotics.robot2015;

import org.ingrahamrobotics.robot2015.commands.FixedIndexerShift;
import org.ingrahamrobotics.robot2015.commands.FullIndexerCollapse;
import org.ingrahamrobotics.robot2015.commands.IndexerDownAndUp;
import org.ingrahamrobotics.robot2015.commands.ManualClawControl;
import org.ingrahamrobotics.robot2015.commands.ManualIndexerControl;
import org.ingrahamrobotics.robot2015.commands.ToggleClaw;
import org.ingrahamrobotics.robot2015.commands.VerticalClawMax;
import org.ingrahamrobotics.robot2015.constants.input.IButton;
import org.ingrahamrobotics.robot2015.output.Settings;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands and command groups
 * that allow control of the robot.
 */
public class OI {

    public OI() {
        IButton.leftClawToggle.getButton().whenPressed(new ToggleClaw(true));
        IButton.rightClawToggle.getButton().whenPressed(new ToggleClaw(false));
        IButton.indexerDownOneLevel.getButton().whenPressed(new FixedIndexerShift(null, false));
        IButton.indexerUpOneLevel.getButton().whenPressed(new FixedIndexerShift(Settings.Key.TOTE_CLEARANCE_ADDITION, false));
        IndexerDownAndUp downAndUp = new IndexerDownAndUp();
        IButton.indexerDownAndUp.getButton().whenPressed(downAndUp);
        IButton.indexerDownAndUp.getButton().whenReleased(downAndUp.getReleasedCommand());
        IButton.indexerCollapseAll.getButton().whenPressed(new FullIndexerCollapse());
        IButton.manualClawHold.getButton().whileHeld(new ManualClawControl());
        IButton.manualIndexerHold.getButton().whileHeld(new ManualIndexerControl());
        IButton.clawVerticalMaxUp.getButton().whenPressed(new VerticalClawMax(true));
        IButton.clawVerticalMaxDown.getButton().whenPressed(new VerticalClawMax(false));
    }

    // // TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}
