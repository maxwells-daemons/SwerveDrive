/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/




import hardware.AbsoluteAnalogEncoder;
import hardware.SabertoothSpeedController;
import hardware.SwervePod;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotSwerve extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    Encoder _encoder;
    AbsoluteAnalogEncoder _digipot;
    
    Talon _turningMotor;
    SabertoothSpeedController _driveMotor;
    
    SwervePod _pod;
    
    public void robotInit() {
        System.out.println("Initializing robot...");
        if (!SabertoothSpeedController.isSerialPortInitialized()) SabertoothSpeedController.initializeSerialPort(9600);
        _encoder = new Encoder(1, 2);
        _digipot = new AbsoluteAnalogEncoder(1, 0.204, 4.96, 0.0);
        
        _turningMotor = new Talon(9);
        _driveMotor = new SabertoothSpeedController(128, 1);
        
        _pod = new SwervePod(_turningMotor, _driveMotor, _encoder, _digipot); 
    }
    
    public void teleopInit() {
        System.out.println("Beginning test sequence...");
        //Test sequence
        _pod.setDriveMotor(-1.0);
        _pod.setTurningMotor(0.2);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //System.out.println("Encoder: " + _pod.getEncoderCounts());
        //System.out.println("Digipot: " + _pod.getDegrees());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void disabledInit() {
        _pod.disable();
    }
    
}
