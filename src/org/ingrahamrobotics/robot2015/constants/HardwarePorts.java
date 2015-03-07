package org.ingrahamrobotics.robot2015.constants;

public class HardwarePorts {

    public static class SolenoidPorts {

        // All solenoid ports are unconfirmed.
        public static final int LEFT_CLAW_EXTEND = 0;
        public static final int LEFT_CLAW_RETRACT = 1;
        public static final int RIGHT_CLAW_EXTEND = 2;
        public static final int RIGHT_CLAW_RETRACT = 3;
    }

    public static class MotorPorts {

        // All motor ports are confirmed
        public static final int FRONT_LEFT_DRIVE = 0;
        public static final int BACK_LEFT_DRIVE = 1;
        public static final int FRONT_RIGHT_DRIVE = 2;
        public static final int BACK_RIGHT_DRIVE = 3;
        public static final int FRONT_LEFT_STEER = 4;
        public static final int BACK_LEFT_STEER = 5;
        public static final int FRONT_RIGHT_STEER = 6;
        public static final int BACK_RIGHT_STEER = 7;
        public static final int INDEXER_SPOOL_MOTOR = 8; // NOTE: This motor's speed is flipped
        public static final int CLAW_VERTICAL_SHIFTER = 9;

        // For swerve organization
        public static final int[] DRIVE_MOTORS = {
                FRONT_LEFT_DRIVE,
                BACK_LEFT_DRIVE,
                FRONT_RIGHT_DRIVE,
                BACK_RIGHT_DRIVE,
        };
        public static final int[] STEER_MOTORS = {
                FRONT_LEFT_STEER,
                BACK_LEFT_STEER,
                FRONT_RIGHT_STEER,
                BACK_RIGHT_STEER,
        };
    }

    public static class DigitalIoPorts {

        public static final int FRONT_LEFT_STEER_ENCODER_A = 0;
        public static final int FRONT_LEFT_STEER_ENCODER_B = 1;
        public static final int BACK_LEFT_STEER_ENCODER_A = 2;
        public static final int BACK_LEFT_STEER_ENCODER_B = 3;
        public static final int FRONT_RIGHT_STEER_ENCODER_A = 4;
        public static final int FRONT_RIGHT_STEER_ENCODER_B = 5;
        public static final int BACK_RIGHT_STEER_ENCODER_A = 6;
        public static final int BACK_RIGHT_STEER_ENCODER_B = 7;

        // Values below this line are unconfirmed.
        public static final int FRONT_LEFT_DRIVE_ENCODER_A = 8;
        public static final int FRONT_LEFT_DRIVE_ENCODER_B = 9;
        public static final int BACK_LEFT_DRIVE_ENCODER_A = 10;
        public static final int BACK_LEFT_DRIVE_ENCODER_B = 11;
        public static final int FRONT_RIGHT_DRIVE_ENCODER_A = 12;
        public static final int FRONT_RIGHT_DRIVE_ENCODER_B = 13;
        public static final int BACK_RIGHT_DRIVE_ENCODER_A = 14;
        public static final int BACK_RIGHT_DRIVE_ENCODER_B = 15;

        public static final int FRONT_LEFT_RESET_SWITCH = 10;
        public static final int BACK_LEFT_RESET_SWITCH = 11;
        public static final int FRONT_RIGHT_RESET_SWITCH = 12;
        public static final int BACK_RIGHT_RESET_SWITCH = 13;

        public static final int INDEXER_ENCODER_A = 9;
        public static final int INDEXER_ENCODER_B = 8;

        public static final int CONTAINER_ENCODER_A = 22;
        public static final int CONTAINER_ENCODER_B = 23;

        // For swerve organization
        public static final int[] DRIVE_ENCODERS_A = {
                FRONT_LEFT_DRIVE_ENCODER_A,
                BACK_LEFT_DRIVE_ENCODER_A,
                FRONT_RIGHT_DRIVE_ENCODER_A,
                BACK_RIGHT_DRIVE_ENCODER_A,
        };
        public static final int[] DRIVE_ENCODERS_B = {
                FRONT_LEFT_DRIVE_ENCODER_B,
                BACK_LEFT_DRIVE_ENCODER_B,
                FRONT_RIGHT_DRIVE_ENCODER_B,
                BACK_RIGHT_DRIVE_ENCODER_B,
        };
        public static final int[] STEER_ENCODERS_A = {
                FRONT_LEFT_STEER_ENCODER_A,
                BACK_LEFT_STEER_ENCODER_A,
                FRONT_RIGHT_STEER_ENCODER_A,
                BACK_RIGHT_STEER_ENCODER_A,
        };
        public static final int[] STEER_ENCODERS_B = {
                FRONT_LEFT_STEER_ENCODER_B,
                BACK_LEFT_STEER_ENCODER_B,
                FRONT_RIGHT_STEER_ENCODER_B,
                BACK_RIGHT_STEER_ENCODER_B,
        };
        public static final int[] POD_RESET_SWITCHES = {
                FRONT_LEFT_RESET_SWITCH,
                BACK_LEFT_RESET_SWITCH,
                FRONT_RIGHT_RESET_SWITCH,
                BACK_RIGHT_RESET_SWITCH,
        };
    }

    public static class AnalogIoPorts {

        /** Currently disabled due to lack of IO Ports */
        public static final int BOTTOM_INDEXER_SWITCH = 0;
        public static final int BOTTOM_VERTICAL_CLAW_SWITCH = 1;
        public static final int TOP_INDEXER_SWITCH = 2;
        public static final int TOP_VERTICAL_CLAW_SWITCH = 3;
    }
}
