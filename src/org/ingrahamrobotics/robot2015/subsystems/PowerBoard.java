package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PowerBoard extends Subsystem {

    private final PowerDistributionPanel panel = new PowerDistributionPanel();

    @Override
    protected void initDefaultCommand() {
    }

    public double getTempurature() {
        return panel.getTemperature();
    }

    public double getTotalCurrent() {
        return panel.getTotalCurrent();
    }

    public double getVoltage() {
        return panel.getVoltage();
    }

    public double getTotalEnergy() {
        return panel.getTotalEnergy();
    }

    public double getTotalPower() {
        return panel.getTotalPower();
    }
}
