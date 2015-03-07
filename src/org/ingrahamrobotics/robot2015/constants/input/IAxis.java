package org.ingrahamrobotics.robot2015.constants.input;

import edu.wpi.first.wpilibj.Joystick;

public enum IAxis {
    driveX(IJoystick.driveJoystick, Joystick.AxisType.kZ),
    driveY(IJoystick.driveJoystick, Joystick.AxisType.kThrottle),
    steer(IJoystick.driveJoystick, Joystick.AxisType.kX),

    manualControl(IJoystick.attackJoystick, Joystick.AxisType.kY);

    private Joystick.AxisType axisType;
    private IJoystick iJoystick;

    private IAxis(IJoystick iJoystick, Joystick.AxisType axisType) {
        this.iJoystick = iJoystick;
        this.axisType = axisType;
    }

    public double get() {
        double value = iJoystick.getJoystick().getAxis(axisType);
        // ignore very low values
        if (Math.abs(value) < 0.05) {
            value = 0;
        }
        return value;
    }
}
