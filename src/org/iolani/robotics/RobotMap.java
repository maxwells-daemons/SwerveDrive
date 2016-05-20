package org.iolani.robotics;

import org.iolani.robotics.hardware.SabertoothSpeedController;
import org.iolani.robotics.hardware.SabertoothSpeedController.SabertoothAddress;
import org.iolani.robotics.hardware.SabertoothSpeedController.SabertoothMotor;

/**
 *
 * @author Aidan
 */
public class RobotMap {
    // Pod 1 //
    public static final SabertoothAddress drive1SabertoothAddress = SabertoothSpeedController.SabertoothAddress.SABERTOOTH_TWO; //Which Sabertooth are we accessing?
    public static final SabertoothMotor drive1MotorNumber = SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_ONE; //Which motor on the Sabertooth are we accessing?
    public static final int turningTalon1 = 1;
    public static final int digipot1 = 2;
    public static final int encoder1Pin1 = 1;
    public static final int encoder1Pin2 = 2;
    
    // Pod 2 //
    public static final SabertoothAddress drive2SabertoothAddress = SabertoothSpeedController.SabertoothAddress.SABERTOOTH_TWO;
    public static final SabertoothMotor drive2MotorNumber = SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_TWO;
    public static final int turningTalon2 = 2;
    public static final int digipot2 = 1;
    public static final int encoder2Pin1 = 3;
    public static final int encoder2Pin2 = 4;
    
    // Pod 3 //
    public static final SabertoothAddress drive3SabertoothAddress = SabertoothSpeedController.SabertoothAddress.SABERTOOTH_ONE;
    public static final SabertoothMotor drive3MotorNumber = SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_ONE;
    public static final int turningTalon3 = 3;
    public static final int digipot3 = 3;
    public static final int encoder3Pin1 = 5;
    public static final int encoder3Pin2 = 6;
    
    // Pod 4 //
    public static final SabertoothAddress drive4SabertoothAddress = SabertoothSpeedController.SabertoothAddress.SABERTOOTH_ONE;
    public static final SabertoothMotor drive4MotorNumber = SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_TWO;
    public static final int turningTalon4 = 4;
    public static final int digipot4 = 4;
    public static final int encoder4Pin1 = 7;
    public static final int encoder4Pin2 = 8;
}
