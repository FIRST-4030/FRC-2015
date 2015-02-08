package org.ingrahamrobotics.robot2015;

import org.ingrahamrobotics.robot2015.commands.RunClawToggle;
import org.ingrahamrobotics.robot2015.constants.JMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    public Joystick leftJoystick = new Joystick(0);
    public Joystick rightJoystick = new Joystick(1);
    
    public class Axis {

        Joystick joy;
        int axisNum;

        public Axis(Joystick joy, int axisNum) {
            this.joy = joy;
            this.axisNum = axisNum;
        }

        public double get() {
            return joy.getRawAxis(axisNum);
        }
    }

    public Joystick driveJoy = new Joystick(0);
    public Joystick steerJoy = new Joystick(1);

    public Axis driveX;
    public Axis driveY;
    public Axis steer;
    
    public JoystickButton leftClaw = new JoystickButton(steerJoy, JMap.Button.JoystickTop.LEFT);
    public JoystickButton rightClaw = new JoystickButton(steerJoy, JMap.Button.JoystickTop.RIGHT);
    
    public OI() {
        driveX = new Axis(driveJoy, 0);
        driveY = new Axis(driveJoy, 1);
        steer = new Axis(steerJoy, 0);
        
      leftClaw.whenPressed(new RunClawToggle(true));
      rightClaw.whenPressed(new RunClawToggle(false));
    }

    // // CREATING BUTTONS
    // One type of button is a joystick button which is any button on a
    // joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    // // TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}
