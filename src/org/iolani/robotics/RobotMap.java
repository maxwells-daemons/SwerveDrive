package org.iolani.robotics;

import org.iolani.robotics.hardware.SabertoothSpeedController;
import org.iolani.robotics.hardware.SabertoothSpeedController.Address;
import org.iolani.robotics.hardware.SabertoothSpeedController.Motor;

/**
 *
 * @author Aidan
 */
public class RobotMap {
    // Pod 1 //
    public static final Address drive1SabertoothAddress = SabertoothSpeedController.Address.TWO; //Which Sabertooth are we accessing?
    public static final Motor drive1MotorNumber = SabertoothSpeedController.Motor.ONE; //Which motor on the Sabertooth are we accessing?
    public static final int turningTalon1 = 1;
    public static final int digipot1 = 2;
    public static final int encoder1Pin1 = 1;
    public static final int encoder1Pin2 = 2;
    public static final double digipot1Offset = 235.0;
    
    // Pod 2 //
    public static final Address drive2SabertoothAddress = SabertoothSpeedController.Address.TWO;
    public static final Motor drive2MotorNumber = SabertoothSpeedController.Motor.TWO;
    public static final int turningTalon2 = 2;
    public static final int digipot2 = 1;
    public static final int encoder2Pin1 = 3;
    public static final int encoder2Pin2 = 4;
    public static final double digipot2Offset = 26.0;
    
    // Pod 3 //
    public static final Address drive3SabertoothAddress = SabertoothSpeedController.Address.ONE;
    public static final Motor drive3MotorNumber = SabertoothSpeedController.Motor.ONE;
    public static final int turningTalon3 = 3;
    public static final int digipot3 = 3;
    public static final int encoder3Pin1 = 5;
    public static final int encoder3Pin2 = 6;
    public static final double digipot3Offset = 1.5;
    
    // Pod 4 //
    public static final Address drive4SabertoothAddress = SabertoothSpeedController.Address.ONE;
    public static final Motor drive4MotorNumber = SabertoothSpeedController.Motor.TWO;
    public static final int turningTalon4 = 4;
    public static final int digipot4 = 4;
    public static final int encoder4Pin1 = 7;
    public static final int encoder4Pin2 = 8;
    public static final double digipot4Offset = 210.0;
}
