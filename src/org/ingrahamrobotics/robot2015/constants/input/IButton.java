package org.ingrahamrobotics.robot2015.constants.input;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.ingrahamrobotics.robot2015.constants.JMap;

public enum IButton {
    steerReset(IJoystick.driveJoystick, 1),
    leftClawToggle(IJoystick.attackJoystick, JMap.Button.JoystickTop.LEFT),
    rightClawToggle(IJoystick.attackJoystick, JMap.Button.JoystickTop.RIGHT),
    manualClawHold(IJoystick.attackJoystick, JMap.Button.JoystickStand.LEFT_TOP),
    manualIndexerHold(IJoystick.attackJoystick, JMap.Button.JoystickStand.LEFT_BOTTOM),
    indexerUpOneLevel(IJoystick.attackJoystick, JMap.Button.JoystickTop.MIDDLE),
    indexerDownAndUp(IJoystick.attackJoystick, JMap.Button.JoystickTop.BOTTOM),
    indexerDownOneLevel(IJoystick.attackJoystick, JMap.Button.JoystickStand.RIGHT_TOP),
    indexerCollapseAll(IJoystick.attackJoystick, JMap.Button.JoystickTop.TRIGGER),
    clawVerticalMaxUp(IJoystick.attackJoystick, JMap.Button.JoystickStand.RIGHT_TOP),
    clawVerticalMaxDown(IJoystick.attackJoystick, JMap.Button.JoystickStand.RIGHT_BOTTOM),;

    private Button button;

    private IButton(IJoystick iJoystick, int buttonNumber) {
        button = new JoystickButton(iJoystick.getJoystick(), buttonNumber);
    }

    public Button getButton() {
        return button;
    }
}
