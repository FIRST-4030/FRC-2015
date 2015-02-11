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

Commands:

- [ ] AutonomousRoutine
  - Runs during autonomous mode. Function currently undetermined.
- [ ] FixedIndexerShift
  - Shifts the indexer up/down a specific amount.
  - Should be able to be set to shift up or down via a constructor parameter.
- [ ] ResetIndexer
  - Shifts the indexer all the way down, collapsing all crates.
- [ ] VerticalClawMaxOut
  - Vertically shifts the claw all the way up/down.
  - Should be able to be set to shift up or down via a constructor parameter.
- [x] ToggleClaw
  - Toggles one of two claws (left/right).
- [ ] ManualClawControl
  - Controls the VerticalClawShifter manually via the non-drive joystick continuously.
  - Should discard any input while ManualIndexerControl is running (check via ManualControlState).
  - Should be controlled via a `.runWhilePressed()` on the manual claw control button.
- [ ] ManualIndexerControl
  - Controls the SpoolIndexer manually via the non-drive joystick continuously.
  - Should update ManualControlState on `initialize()`, `end()` and `canceled()`.
  - Should be controlled via a `.runWhilePressed()` on the manual indexer control button.
- [x] RunPIDDrive
  - Should take joystick input from the drive joysticks and/or drive turnknob, and control DriveBase.

Variable Stores:

- [x] HardwarePorts
  - Contains all hardware motor, solenoid and sensor ports.
- [ ] InputSettings
  - Contains settings for where all buttons are located.
  - Might want to make this an enum with a method to directly obtain the JoystickButton. That would allow for statements like `InputSettings.CLAW_TOGGLE_LEFT.get().onPressed(...)` instead of `clawToggleButton = new JoystickButton(InputSettings.CLAW_TOGGLE_LEFT)` and then `clawToggleButton.onPressed(...)`
- [ ] ManualControlState
  - Contains whether or not ManualIndexerControl is running.
  - Should just have a method to get/set the boolean state, and nothing else.
