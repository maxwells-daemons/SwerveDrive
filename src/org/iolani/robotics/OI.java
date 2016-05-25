package org.iolani.robotics;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.iolani.robotics.commands.OperateCrabDrive;
import org.iolani.robotics.commands.OperateTurretDrive;

/**
 *
 * @author Aidan
 */
public class OI {
    // Joysticks //
    private final Joystick _lStick = new Joystick(1);
    
    // Buttons //
    private final Button _crabModeButton = new JoystickButton(_lStick, 1);
    private final Button _turretModeButton = new JoystickButton(_lStick, 2);
    
    public OI() {
        // Assigning commands to buttons goes here //
        _crabModeButton.whenPressed(new OperateCrabDrive());
        _turretModeButton.whenPressed(new OperateTurretDrive());
    }
    
    public Joystick getLeftStick()  {
        return _lStick;
    }
    
    public double getRightStickX() { //You can only get the left joystick of a logitech gamepad; to access the other, use raw axes
        return _lStick.getRawAxis(3);
    }
    
    public double getRightStickY() {
        return -1 * _lStick.getRawAxis(4);
    }
    
    public double getRightStickDirectionRadians() {
        double y = getRightStickY();
        double x = getRightStickX();
        return MathUtils.atan2(y, x);
    }
    
    public double getRightStickDirectionDegrees() {
        return Math.toDegrees(getRightStickDirectionRadians());
    }
}
