package org.ingrahamrobotics.robot2015.utils;

import edu.wpi.first.wpilibj.DigitalInput;

public class PossiblyFlippedDigitalInput {

    private final DigitalInput internalInput;
    private final boolean flipped;

    public PossiblyFlippedDigitalInput(final int port) {
        internalInput = new DigitalInput(port);
        flipped = port >= 10;
    }

    public boolean get() {
        if (flipped) {
            return !internalInput.get();
        } else {
            return internalInput.get();
        }
    }
}
