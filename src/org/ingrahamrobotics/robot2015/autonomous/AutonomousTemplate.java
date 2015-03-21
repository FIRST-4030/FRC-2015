package org.ingrahamrobotics.robot2015.autonomous;

import org.ingrahamrobotics.robot2015.Subsystems;
import org.ingrahamrobotics.robot2015.output.Settings;
import org.ingrahamrobotics.robot2015.subsystems.PIDSteer;
import org.ingrahamrobotics.robot2015.utils.ExecuteResult;
import org.ingrahamrobotics.robot2015.utils.TimedCommand;

/**
 * Autonomous command template for TimedCommand which is controllable via a set of AutonomousState objects.
 */
public class AutonomousTemplate extends TimedCommand {

    private final AutonomousState[] autoStates;

    public AutonomousTemplate(final AutonomousState[] states) {
        requires(Subsystems.driveBase);
        autoStates = states;
    }

    /**
     * This runs for each
     *
     * @param state The timeout state - there are 2 states for each autonomous state, because we want to prepare the
     *              wheel direction before starting.
     * @return Whether or not to continue the state
     */
    @Override
    protected ExecuteResult executeState(final int state) {
        AutonomousState currentAutonomousState = autoStates[state / 2];


        // TODO: Support diagonal movement
        double forward, strafe, turn;
        if (currentAutonomousState.forwardFeet != 0) {
            forward = Settings.Key.AUTO_FWD_STR_SPEED.getDouble();
            strafe = turn = 0;
        } else if (currentAutonomousState.strafeFeet != 0) {
            strafe = Settings.Key.AUTO_FWD_STR_SPEED.getDouble();
            forward = turn = 0;
        } else if (currentAutonomousState.turnRadians != 0) {
            turn = Settings.Key.AUTO_TURN_SPEED.getDouble();
            forward = strafe = 0;
        } else {
            System.out.println("Skipping empty state?");
            return ExecuteResult.DONE;
        }

        if (state % 2 == 0) {
            Subsystems.driveBase.prepareWheelAnglesFor(forward, strafe, turn);
        } else {
            Subsystems.driveBase.drive(forward, strafe, turn);
        }

        if (state % 2 == 0) {
            // This is the first half of the state for the current AutonomousState,
            // where we are just setting the direction of the wheels to match before starting driving.
            return Subsystems.driveBase.areWheelAnglesReady(Settings.Key.AUTO_WHEEL_ANGLE_ALLOWANCE_BEFORE_RUNNING_MOTORS.getDouble()) ? ExecuteResult.DONE : ExecuteResult.NOT_DONE;
        } else {
            // This is the second half of the state, where we actually move -
            // we mostly just want to check with the AutonomousState's custom predicate
            return currentAutonomousState.shouldContinue();
        }
    }

    @Override
    protected ExecuteResult startState(final int state) {
        if (state == 0) {
            for (PIDSteer steer : Subsystems.driveBase.steerSystem) {
                steer.enable();
            }
            Subsystems.driveBase.drive(0, 0, 0);
        }
        System.out.println("Starting state: " + state);
        AutonomousState currentAutonomousState = autoStates[state / 2];

        if (state >= 2) {
            autoStates[state / 2 - 1].end();
        }
        currentAutonomousState.start();
        // This is the first half of the state for the current AutonomousState,
        // where we are just setting the direction of the wheels to match before starting driving.
        Subsystems.driveBase.updateSteerPID();

        // TODO: Support diagonal movement
        double forward, strafe, turn;
        if (currentAutonomousState.forwardFeet != 0) {
            forward = Settings.Key.AUTO_FWD_STR_SPEED.getDouble();
            if (currentAutonomousState.forwardFeet < 0) {
                forward *= -1;
            }
            strafe = turn = 0;
        } else if (currentAutonomousState.strafeFeet != 0) {
            strafe = Settings.Key.AUTO_FWD_STR_SPEED.getDouble();
            if (currentAutonomousState.strafeFeet < 0) {
                strafe *= -1;
            }
            forward = turn = 0;
        } else if (currentAutonomousState.turnRadians != 0) {
            turn = Settings.Key.AUTO_TURN_SPEED.getDouble();
            if (currentAutonomousState.turnRadians < 0) {
                turn *= -1;
            }
            forward = strafe = 0;
        } else {
            System.out.println("Skipping empty state?");
            return ExecuteResult.DONE;
        }

        if (state % 2 == 0) {
            Subsystems.driveBase.prepareWheelAnglesFor(forward, strafe, turn);
        } else {
            Subsystems.driveBase.drive(forward, strafe, turn);
        }


        return ExecuteResult.NOT_DONE;
    }

    /**
     * This returns 2 wait times for each state
     *
     * @return Wait times
     */
    @Override
    protected long[] getWaitTimes() {
        long[] result = new long[autoStates.length * 2];
        for (int i = 0; i < autoStates.length; i++) {
            result[i * 2] = 1500; // the first part shouldn't timeout, but this is a backup timeout just in case.
            AutonomousState currentAutonomousState = autoStates[i];
            double time;
            if (currentAutonomousState.forwardFeet != 0) {
                time = Math.abs(currentAutonomousState.forwardFeet) * Settings.Key.AUTO_MS_PER_FOOT_FORWARD.getDouble();
            } else if (currentAutonomousState.strafeFeet != 0) {
                time = Math.abs(currentAutonomousState.strafeFeet) * Settings.Key.AUTO_MS_PER_FOOT_FORWARD.getDouble();
            } else if (currentAutonomousState.turnRadians != 0) {
                time = Math.abs(currentAutonomousState.turnRadians) * Settings.Key.AUTO_MS_PER_RADIANS_TURNING.getDouble();
            } else {
                time = 1;
            }
            result[i * 2 + 1] = (long) time;
        }
        return result;
    }

    @Override
    protected void end() {
        Subsystems.driveBase.drive(0, 0, 0);
        System.out.println("Ending!");
    }
}
