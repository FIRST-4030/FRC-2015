package org.ingrahamrobotics.robot2015.constants.input;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.ingrahamrobotics.robot2015.constants.JMap;

public enum IButton {
	steerReset(IJoystick.driveJoystick, 1),
    leftClawToggle(IJoystick.secondaryJoystick, JMap.Button.JoystickTop.LEFT),
    rightClawToggle(IJoystick.secondaryJoystick, JMap.Button.JoystickTop.RIGHT),
    manualClawHold(IJoystick.secondaryJoystick, JMap.Button.JoystickStand.LEFT_TOP),
    manualIndexerHold(IJoystick.secondaryJoystick, JMap.Button.JoystickStand.LEFT_BOTTOM),
    indexerUpOneLevel(IJoystick.secondaryJoystick, JMap.Button.JoystickTop.MIDDLE),
    indexerDownAndUp(IJoystick.secondaryJoystick, JMap.Button.JoystickTop.BOTTOM),
    indexerDownOneLevel(IJoystick.secondaryJoystick, JMap.Button.JoystickStand.RIGHT_TOP),
    indexerCollapseAll(IJoystick.secondaryJoystick, JMap.Button.JoystickTop.TRIGGER),
    clawVerticalMaxUp(IJoystick.secondaryJoystick, JMap.Button.JoystickStand.RIGHT_TOP),
    clawVerticalMaxDown(IJoystick.secondaryJoystick, JMap.Button.JoystickStand.RIGHT_BOTTOM),;

    private Button button;

    private IButton(IJoystick iJoystick, int buttonNumber) {
        button = new JoystickButton(iJoystick.getJoystick(), buttonNumber);
    }

    public Button getButton() {
        return button;
    }
}
