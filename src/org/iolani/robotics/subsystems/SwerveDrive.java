package org.iolani.robotics.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.robotics.RobotMap;
import org.iolani.robotics.commands.OperateCrabDrive;
import org.iolani.robotics.hardware.AbsoluteAnalogEncoder;
import org.iolani.robotics.hardware.SabertoothSpeedController;
import org.iolani.robotics.hardware.SwervePod;
import org.iolani.robotics.hardware.TestBotSwervePod;

/**
 *
 * @author Aidan
 */
public class SwerveDrive extends Subsystem {
    // Hardware //
    private SabertoothSpeedController _driveMotor1;
    private SabertoothSpeedController _driveMotor2;
    private SabertoothSpeedController _driveMotor3;
    private SabertoothSpeedController _driveMotor4;
    private Talon _turningMotor1;
    private Talon _turningMotor2;
    private Talon _turningMotor3;
    private Talon _turningMotor4;
    private Encoder _encoder1;
    private Encoder _encoder2;
    private Encoder _encoder3;
    private Encoder _encoder4;
    private AbsoluteAnalogEncoder _digipot1;
    private AbsoluteAnalogEncoder _digipot2;
    private AbsoluteAnalogEncoder _digipot3;
    private AbsoluteAnalogEncoder _digipot4;
    
    // Wheel pods //
    private SwervePod _swervePod1; //Front left
    private SwervePod _swervePod2; //Front right
    private SwervePod _swervePod3; //Rear right
    private SwervePod _swervePod4; //Rear left
    
    public void init() {
        System.out.println("Initializing drivetrain...");
        
        // Initialize serial port //
        if (!SabertoothSpeedController.isSerialPortInitialized()) SabertoothSpeedController.initializeSerialPort(9600);
        
        
        // Initialize swerve pods //
        _swervePod1 = new TestBotSwervePod(RobotMap.turningTalon1, RobotMap.drive1SabertoothAddress, RobotMap.drive1MotorNumber, RobotMap.encoder1Pin1, RobotMap.encoder1Pin2, RobotMap.digipot1, RobotMap.digipot1Offset);
        _swervePod2 = new TestBotSwervePod(RobotMap.turningTalon2, RobotMap.drive2SabertoothAddress, RobotMap.drive2MotorNumber, RobotMap.encoder2Pin1, RobotMap.encoder2Pin2, RobotMap.digipot2, RobotMap.digipot2Offset);
        _swervePod3 = new TestBotSwervePod(RobotMap.turningTalon3, RobotMap.drive3SabertoothAddress, RobotMap.drive3MotorNumber, RobotMap.encoder3Pin1, RobotMap.encoder3Pin2, RobotMap.digipot3, RobotMap.digipot3Offset);
        _swervePod4 = new TestBotSwervePod(RobotMap.turningTalon4, RobotMap.drive4SabertoothAddress, RobotMap.drive4MotorNumber, RobotMap.encoder4Pin1, RobotMap.encoder4Pin2, RobotMap.digipot4, RobotMap.digipot4Offset);
        
        _swervePod1.initSmartDashboard();
        
        // Initialize swerve pod setpoints //
        setAllWheelAngles(180.0);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        this.setDefaultCommand(new OperateCrabDrive());
    }
    
    public void setAllWheelAngles(double degrees) { //Set all wheel pods to face the same direction
        _swervePod1.setTurningSetpoint(degrees);
        _swervePod2.setTurningSetpoint(degrees);
        _swervePod3.setTurningSetpoint(degrees);
        _swervePod4.setTurningSetpoint(degrees);
    }
    
    public void setAllWheelPowers(double power) { //Set all wheel pods to the same power
        //TODO: USE PID CONTROL
        _swervePod1.setDriveMotor(power);
        _swervePod2.setDriveMotor(power);
        _swervePod3.setDriveMotor(power);
        _swervePod4.setDriveMotor(power);
    }
    
    public void setCrab(double podAngles, double powerLeft, double powerRight) {
        setAllWheelAngles(podAngles);
        //TODO: Set drive power with PID
        _swervePod1.setDriveMotor(powerLeft); //Left
        _swervePod4.setDriveMotor(powerLeft); //Left 
        _swervePod2.setDriveMotor(powerRight); //Right
        _swervePod3.setDriveMotor(powerRight); //Right
    }
    
}
