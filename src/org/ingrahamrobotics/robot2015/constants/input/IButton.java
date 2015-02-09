/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ingrahamrobotics.robot2015.constants.input;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.ingrahamrobotics.robot2015.constants.JMap;

public enum IButton {
    leftClawToggle(IJoystick.secondaryJoystick, JMap.Button.JoystickTop.LEFT),
    rightClawToggle(IJoystick.secondaryJoystick, JMap.Button.JoystickTop.RIGHT);

    private Button button;

    private IButton(IJoystick iJoystick, int buttonNumber) {
        button = new JoystickButton(iJoystick.getJoystick(), buttonNumber);
    }

    public Button getButton() {
        return button;
    }
}
