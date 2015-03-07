package org.ingrahamrobotics.robot2015.constants.input;

import edu.wpi.first.wpilibj.Joystick;
import org.ingrahamrobotics.robot2015.constants.JMap;

public enum IJoystick {
    driveJoystick(JMap.DRIVE_JOYSTICK),
    attackJoystick(JMap.ATTACK_JOYSTICK);

    public Joystick joystick;

    private IJoystick(int joystickNumber) {
        joystick = new Joystick(joystickNumber);
    }

    public Joystick getJoystick() {
        return joystick;
    }
}
