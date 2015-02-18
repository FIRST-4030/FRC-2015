package org.ingrahamrobotics.robot2015.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.util.LinkedList;
import java.util.Queue;

public class PowerBoard extends Subsystem {

    private long lastVoltageMeasure = 0;
    private double minimumVoltage = Double.MAX_VALUE;
    private final Queue<Double> lastVoltages = new LinkedList<>();
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
        long now = System.currentTimeMillis();
        if (now > lastVoltageMeasure + 100) {
            lastVoltageMeasure = now;

            double currentVoltage = panel.getVoltage();
            if (currentVoltage <= minimumVoltage) {
                minimumVoltage = currentVoltage;
            }

            lastVoltages.add(currentVoltage);
            if (lastVoltages.size() > 500) {
                if (lastVoltages.remove() <= minimumVoltage) {
                    minimumVoltage = lastVoltages.stream().min(Double::compare).get();
                }
            }
        }
        return minimumVoltage;
    }

    public double getTotalEnergy() {
        return panel.getTotalEnergy();
    }

    public double getTotalPower() {
        return panel.getTotalPower();
    }
}
