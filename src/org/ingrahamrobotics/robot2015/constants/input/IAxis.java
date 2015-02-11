package org.ingrahamrobotics.robot2015.constants.input;

import edu.wpi.first.wpilibj.Joystick;

public enum IAxis {
    driveX(IJoystick.driveJoystick, Joystick.AxisType.kX),
    driveY(IJoystick.driveJoystick, Joystick.AxisType.kY),
    steer(IJoystick.steeringJoystick, Joystick.AxisType.kX);

    private Joystick.AxisType axisType;
    private IJoystick iJoystick;

    private IAxis(IJoystick iJoystick, Joystick.AxisType axisType) {
        this.iJoystick = iJoystick;
        this.axisType = axisType;
    }

    public double get() {
        return iJoystick.getJoystick().getAxis(axisType);
    }
}
