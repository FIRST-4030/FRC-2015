package org.ingrahamrobotics.robot2015.constants;

public class HardwarePorts {

    public static class SolenoidPorts {

        public static final int LEFT_CLAW_EXTEND = 0;
        public static final int LEFT_CLAW_RETRACT = 1;
        public static final int RIGHT_CLAW_EXTEND = 2;
        public static final int RIGHT_CLAW_RETRACT = 3;
    }

    public static class MotorPorts {

        public static final int INDEXER_SPOOL_MOTOR = 0;
        public static final int CLAW_VERTICAL_SHIFTER = 1;
        public static final int FRONT_LEFT_DRIVE = 0;
        public static final int FRONT_RIGHT_DRIVE = 0;
        public static final int BACK_LEFT_DRIVE = 0;
        public static final int BACK_RIGHT_DRIVE = 0;
        public static final int FRONT_LEFT_STEER = 0;
        public static final int FRONT_RIGHT_STEER = 0;
        public static final int BACK_LEFT_STEER = 0;
        public static final int BACK_RIGHT_STEER = 0;

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
        public static final int FRONT_LEFT_DRIVE_ENCODER_A = 0;
        public static final int FRONT_LEFT_DRIVE_ENCODER_B = 1;
        public static final int FRONT_RIGHT_DRIVE_ENCODER_A = 2;
        public static final int FRONT_RIGHT_DRIVE_ENCODER_B = 3;
        public static final int BACK_LEFT_DRIVE_ENCODER_A = 4;
        public static final int BACK_LEFT_DRIVE_ENCODER_B = 5;
        public static final int BACK_RIGHT_DRIVE_ENCODER_A = 6;
        public static final int BACK_RIGHT_DRIVE_ENCODER_B = 7;
        public static final int FRONT_LEFT_STEER_ENCODER_A = 8;
        public static final int FRONT_LEFT_STEER_ENCODER_B = 9;
        public static final int FRONT_RIGHT_STEER_ENCODER_A = 10;
        public static final int FRONT_RIGHT_STEER_ENCODER_B = 11;
        public static final int BACK_LEFT_STEER_ENCODER_A = 12;
        public static final int BACK_LEFT_STEER_ENCODER_B = 13;
        public static final int BACK_RIGHT_STEER_ENCODER_A = 14;
        public static final int BACK_RIGHT_STEER_ENCODER_B = 15;

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
    }
}
