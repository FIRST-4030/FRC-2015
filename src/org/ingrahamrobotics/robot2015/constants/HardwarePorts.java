package org.ingrahamrobotics.robot2015.constants;

public class HardwarePorts {

    public static class SolenoidPorts {
        public static final int LEFT_CLAW_EXTEND = 0;
        public static final int LEFT_CLAW_RETRACT = 1;
        public static final int RIGHT_CLAW_EXTEND = 2;
        public static final int RIGHT_CLAW_RETRACT = 3;
    }

    public static class MotorPorts {
        public static final int SIMPLE_DRIVE_LEFT = 0;
        public static final int SIMPLE_DRIVE_RIGHT = 1;
        public static final int INDEXER_SPOOL_MOTOR = 2;
        public static final int CLAW_VERTICAL_SHIFTER = 3;
    }

    public static class SwerveSpecific {

        // PWM Out
        public static final int driveMotor1 = 1;
        public static final int steerMotor1 = 2;

        public static final int[] driveMotors = { driveMotor1 };
        public static final int[] steerMotors = { steerMotor1 };

        // Analog In
        public static final int encoder2Analog = 1;

        // Digital I/O
        public static final int encoder1A = 1;
        public static final int encoder1B = 2;

        public static final int encoder2A = 3;
        public static final int encoder2B = 4;

        public static final int[] driveEncoders = { encoder1A, encoder1B };
        public static final int[] steerEncoders = { encoder2A, encoder2B };
    }
}
