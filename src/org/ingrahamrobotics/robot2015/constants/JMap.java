package org.ingrahamrobotics.robot2015.constants;

/**
 * This class contains all public static final integer variables.
 * <p>
 * It stores what buttons/axis on the Logitech Joysticks correspond to what numbers in getButton or getRawAxis.
 */
public class JMap {

    public static final int DRIVE_JOYSTICK = 0;
    public static final int ATTACK_JOYSTICK = 1;

    public static class Axis {

        public static final int X = 1;
        public static final int Y = 2;
    }

    public static class Button {

        public static class JoystickTop {

            public static final int TRIGGER = 1;
            public static final int BOTTOM = 2;
            public static final int MIDDLE = 3;
            public static final int LEFT = 4;
            public static final int RIGHT = 5;
        }

        public static class JoystickStand {

            public static final int LEFT_TOP = 6;
            public static final int LEFT_BOTTOM = 7;
            public static final int BOTTOM_LEFT = 8;
            public static final int BOTTOM_RIGHT = 9;
            public static final int RIGHT_BOTTOM = 10;
            public static final int RIGHT_TOP = 11;
        }
    }

    public static class DualAction {

        public static final int LEFT_TOP_BUMPER = 5;
    }
}
