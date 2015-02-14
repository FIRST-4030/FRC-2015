Program Design
===

This file is intended as documentation for what needs to be created, and as an overview of how the program works.

Each item represents a single java class file.

Everything here is subject to change. If you have a suggestion, feel free to edit this file directly, or open a github issue for discussion.

Items which are created and fully functional should be marked with ✓. Items which are partially finished should specify what still needs to be done.

Subsystems:

- [x] VerticalIndexerControl
  - Controls the single spool motor.
- [x] VerticalClawShifter
  - Controls the motor to vertically shift the claw.
- [x] SingleClawSubsystem
  - Controls one of the two claws (left/right).
  - There should be two instances of this in Subsystems.
- [x] DriveBase
  - Controls the swerve drive.
  - Should be able to switch between accepting `(speedX, speedY, turnSpeed)` and `(distanceX, distanceY, turnDistance)`, so as to allow for precise autonomous control as well as speed-based general control.
- [x] ToggleSwitches
  - Gives access to the output from various digital input switches on the robot.

Commands:

- [ ] AutonomousRoutine
  - Runs during autonomous mode. Function currently undetermined.
- [x] FixedIndexerShift
  - [x] Shifts the indexer up/down a specific amount.
  - [x] Should be able to be set to shift up or down via a constructor parameter.
  - [x] Setting to use encoder or not use encoder
- [x] FullIndexerCollapse
  - Shifts the indexer all the way down, collapsing all crates.
- [ ] VerticalClawMaxOut
  - Vertically shifts the claw all the way up/down.
  - Should be able to be set to shift up or down via a constructor parameter.
- [x] ToggleClaw
  - Toggles one of two claws (left/right).
- [x] ManualClawControl
  - Controls the VerticalClawShifter manually via the non-drive joystick continuously.
  - Should update ManualControlState on `initialize()`, `end()` and `canceled()`.
  - Should be controlled via a `.runWhilePressed()` on the manual claw control button.
  - Should stop running up/down if the up/down vertical claw toggle switches are activate.
- [x] ManualIndexerControl
  - Controls the SpoolIndexer manually via the non-drive joystick continuously.
  - Should discard any input while ManualClawControl is running (check via ManualControlState).
  - Should be controlled via a `.runWhilePressed()` on the manual indexer control button.
  - Should stop running up/down if the up/down indexer toggle switches are activate.
- [x] RunPIDDrive
  - Should take joystick input from the drive joysticks and/or drive turnknob, and control DriveBase.

Variable Stores:

- [x] HardwarePorts
  - Contains all hardware motor, solenoid and sensor ports.
- [x] InputSettings
  - Contains settings for where all buttons are located.
  - Might want to make this an enum with a method to directly obtain the JoystickButton. That would allow for statements like `InputSettings.CLAW_TOGGLE_LEFT.get().onPressed(...)` instead of `clawToggleButton = new JoystickButton(InputSettings.CLAW_TOGGLE_LEFT)` and then `clawToggleButton.onPressed(...)`
- [x] ManualControlState
  - Contains whether or not ManualIndexerControl and ManualClawControl are running.
  - Should just have a method to get/set the boolean state.
  - Should publish which manual control has priority on the dashboard.
