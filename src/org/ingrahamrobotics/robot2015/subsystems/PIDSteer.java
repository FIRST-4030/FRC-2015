package org.ingrahamrobotics.robot2015.subsystems;

import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.POD_RESET_SWITCHES;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.STEER_ENCODERS_A;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.DigitalIoPorts.STEER_ENCODERS_B;
import static org.ingrahamrobotics.robot2015.constants.HardwarePorts.MotorPorts.STEER_MOTORS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.ingrahamrobotics.robot2015.output.Output;
import org.ingrahamrobotics.robot2015.output.OutputLevel;
import org.ingrahamrobotics.robot2015.utils.PossiblyFlippedDigitalInput;

public class PIDSteer extends PIDSubsystem {

    //private static final double tickesPerDegree = (497.0 + 66.0 / 56.0) / something;
    public Talon steerMotor;
    public Encoder steerEncoder;
    public PossiblyFlippedDigitalInput resetSwitch;

    private double ticksPerRadian = (611 - 35) / (Math.PI * 2);
    boolean pResetState = false;

    // Initialize your subsystem here
    public PIDSteer(int wheelNum) {
        super("PIDSteer" + wheelNum, 1, 0, 0);
        getPIDController().setInputRange(-Math.PI, Math.PI);
        getPIDController().setContinuous(true);

        steerMotor = new Talon(STEER_MOTORS[wheelNum - 1]);
        steerEncoder = new Encoder(STEER_ENCODERS_A[wheelNum - 1], STEER_ENCODERS_B[wheelNum - 1]);
        resetSwitch = new PossiblyFlippedDigitalInput(POD_RESET_SWITCHES[wheelNum - 1]);

        setSetpoint(0.0);
        enable();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        //ticksPerRadian = Settings.Key.STEER_PID_TICKS_PER_RADIAN.getDouble();
        return steerEncoder.getDistance() / ticksPerRadian;
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        steerMotor.set(output);
    }

    public void setPID(double p, double i, double d) {
        getPIDController().setPID(p, i, d);
    }

    @Override
    public void setSetpoint(final double setpoint) {
        Output.output(OutputLevel.SWERVE_DEBUG, getName() + "-setpoint", setpoint);
        super.setSetpoint(setpoint);
    }

    public boolean getPreviousResetState() {
        return pResetState;
    }

    public boolean getResetSwitch() {
        return resetSwitch.get();
    }

    public void resetEncoder() {
        steerEncoder.reset();
    }

    public void setSpeed(double speed) {
        if (!getPIDController().isEnable())
            steerMotor.set(speed);
    }

    public void setPrevResetState(boolean wasReset) {
        pResetState = wasReset;
    }
}