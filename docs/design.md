Program Design
===

This file is intended as documentation for what needs to be created, and as an overview of how the program works.

Each item represents a single java class file.

Everything here is subject to change. If you have a suggestion, feel free to edit this file directly, or open a github issue for discussion.

Items which are created and fully functional should be marked with ✓. Items which are partially finished should specify what still needs to be done

Subsystems:

- [ ] ToteIndexer
  - Controls the position of the tote indexer.
- [ ] IntakeWheels
  - Controls the intake wheels for the tote indexer.
- [x] DriveBase
  - Controls the swerve drive.
  - Should be able to switch between accepting `(speedX, speedY, turnSpeed)` and `(distanceX, distanceY, turnDistance)`, so as to allow for precise autonomous control as well as speed-based general control.
- [x] ToggleSwitches
  - Gives access to the output from various digital input switches on the robot.

Commands:

- [ ] AutonomousRoutine
  - Runs during autonomous mode. Function currently undetermined.
- [ ] FixedIndexerShift
  - [ ] Shifts the indexer up/down a specific amount.
  - [ ] Should be able to be set to shift up or down via a constructor parameter.
  - [ ] Setting to use encoder or not use encoder.
- [ ] FullIndexerCollapse
  - Shifts the indexer all the way down.
- [x] ManualIndexerControl
  - Controls the ToteIndexer manually via the non-drive joystick continuously.
  - Should be controlled via a `.runWhilePressed()` on the manual indexer control button.
  - Should stop running up/down if the up/down indexer toggle switches are activate.
- [ ] ManipulateTote
  - Controls the IntakeWheels.
  - [ ] Can suck in or eject a tote.
  - [ ] Can spin a tote/container in place.
- [ ] PickupTote
  - Macro to pick up a tote.
  - Runs IntakeWheels to hold in tote, while running the indexer through an up then down cycle to grab and pull up a tote.
- [x] RunPIDDrive
  - Should take joystick input from the drive joysticks and/or drive turnknob, and control DriveBase.
- [ ] ResetTurningMotors
  - Spins DriveBase turning motors until their reset switch is triggered, then zeros their encoder.
- [ ] ResetIndexerEncoder
  - Drives the ToteIndexer down until it triggers its bottom switch, then zeros the encoder.


Variable Stores:

- [x] HardwarePorts
  - Contains all hardware motor and sensor ports.
- [x] InputSettings
  - Contains settings for where all buttons are located.
  - Might want to make this an enum with a method to directly obtain the JoystickButton. That would allow for statements like `InputSettings.CLAW_TOGGLE_LEFT.get().onPressed(...)` instead of `clawToggleButton = new JoystickButton(InputSettings.CLAW_TOGGLE_LEFT)` and then `clawToggleButton.onPressed(...)`
- [x] ManualControlState
  - Contains whether or not ManualIndexerControl is running.
  - Should just have a method to get/set the boolean state.
  - Should publish which manual control has priority on the dashboard.
