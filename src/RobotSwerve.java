/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/




import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalOutput;
import hardware.AbsoluteAnalogEncoder;
import hardware.SabertoothSpeedController;
import hardware.SwervePod;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
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
    
    // Temporary solution for defining hardware, used for testing //
    Joystick _joystick;
    
    Encoder _encoder1;
    AbsoluteAnalogEncoder _digipot1;
    
    Encoder _encoder2;
    AbsoluteAnalogEncoder _digipot2;
    
    Encoder _encoder3;
    AbsoluteAnalogEncoder _digipot3;
    
    Encoder _encoder4;
    AbsoluteAnalogEncoder _digipot4;
    
    Talon _turningMotor1;
    SabertoothSpeedController _driveMotor1;
    
    Talon _turningMotor2;
    SabertoothSpeedController _driveMotor2;
    
    Talon _turningMotor3;
    SabertoothSpeedController _driveMotor3;
    
    Talon _turningMotor4;
    SabertoothSpeedController _driveMotor4;
    
    SwervePod _pod1;
    SwervePod _pod2;
    SwervePod _pod3;
    SwervePod _pod4;
    
    // Used for triggering oscilloscope reading for step response graphs //
    DigitalOutput _scopeTrigger = new DigitalOutput(14);
    
    public void robotInit() {
        System.out.println("Initializing robot...");
        if (!SabertoothSpeedController.isSerialPortInitialized()) SabertoothSpeedController.initializeSerialPort(9600);
        
        _joystick = new Joystick(1);
        
        
        // TODO: Handle hardware differently //
        _encoder1 = new Encoder(1, 2, false, CounterBase.EncodingType.k1X);
        _digipot1 = new AbsoluteAnalogEncoder(2, 0.204, 4.96, 0.0);
        
        _encoder2 = new Encoder(3, 4, false, CounterBase.EncodingType.k1X);
        _digipot2 = new AbsoluteAnalogEncoder(1, 0.204, 4.96, 20);
        
        _encoder3 = new Encoder(5, 6, false, CounterBase.EncodingType.k1X);
        _digipot3 = new AbsoluteAnalogEncoder(3, 0.204, 4.96, 334.5);
        
        _encoder4 = new Encoder(7, 8, false, CounterBase.EncodingType.k1X);
        _digipot4 = new AbsoluteAnalogEncoder(4, 0.204, 4.96, 20);
        
        _turningMotor1 = new Talon(1);
        _driveMotor1 = new SabertoothSpeedController(SabertoothSpeedController.SabertoothAddress.SABERTOOTH_TWO, SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_ONE);
        
        _turningMotor2 = new Talon(2);
        _driveMotor2 = new SabertoothSpeedController(SabertoothSpeedController.SabertoothAddress.SABERTOOTH_TWO, SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_TWO);
        
        _turningMotor3 = new Talon(3);
        _driveMotor3 = new SabertoothSpeedController(SabertoothSpeedController.SabertoothAddress.SABERTOOTH_ONE, SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_ONE);
        
        _turningMotor4 = new Talon(4);
        _driveMotor4 = new SabertoothSpeedController(SabertoothSpeedController.SabertoothAddress.SABERTOOTH_ONE, SabertoothSpeedController.SabertoothMotor.SABERTOOTH_MOTOR_TWO);
    
        _pod1 = new SwervePod(_turningMotor1, _driveMotor1, _encoder1, _digipot1);
        _pod2 = new SwervePod(_turningMotor2, _driveMotor2, _encoder2, _digipot2);
        _pod3 = new SwervePod(_turningMotor3, _driveMotor3, _encoder3, _digipot3);
        _pod4 = new SwervePod(_turningMotor4, _driveMotor4, _encoder4, _digipot4);
    }
    
    public void teleopInit() {
        //_pod1.initSmartDashboard();
        _pod1.setTurningSetpoint(180);
        _pod2.setTurningSetpoint(180);
        _pod3.setTurningSetpoint(180);
        _pod4.setTurningSetpoint(180);
    }
    
    public void testInit() {
        //Not using test since it overrides SmartDashboard display
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
        //_pod1.updateSmartDashboard();
        
        // Test turning //
        if (_joystick.getRawButton(1)) {
            _scopeTrigger.set(true);
            _pod1.setTurningSetpoint(90);
        } else if (_joystick.getRawButton(2)) {
            _scopeTrigger.set(true);
            _pod1.setTurningSetpoint(90);
            _pod2.setTurningSetpoint(90);
            _pod3.setTurningSetpoint(90);
            _pod4.setTurningSetpoint(90);
        } else if (_joystick.getRawButton(3)) {
            _scopeTrigger.set(true);
            _pod3.setTurningSetpoint(90);
        } else if (_joystick.getRawButton(4)) {
            _scopeTrigger.set(true);
            _pod1.setTurningSetpoint(_joystick.getDirectionDegrees() + 180.0);
            _pod2.setTurningSetpoint(_joystick.getDirectionDegrees() + 180.0);
            _pod3.setTurningSetpoint(_joystick.getDirectionDegrees() + 180.0);
            _pod4.setTurningSetpoint(_joystick.getDirectionDegrees() + 180.0);
        } else {
            _scopeTrigger.set(false);
            _pod1.setTurningSetpoint(180);
            _pod2.setTurningSetpoint(180);
            _pod3.setTurningSetpoint(180);
            _pod4.setTurningSetpoint(180);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        
    }
    
    public void disabledInit() {
        //_pod1.disable();
    }
    
    public void disabledPeriodic() {
        
    }
}
