package org.iolani.robotics.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.iolani.robotics.RobotMap;
import org.iolani.robotics.commands.OperateCrabDrive;
import org.iolani.robotics.hardware.AbsoluteAnalogEncoder;
import org.iolani.robotics.hardware.SabertoothSpeedController;
import org.iolani.robotics.hardware.SwervePod;

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
    
    // Physical constants //
    private static final double WHEEL_DIAMETER_INCHES = 2.0;
    private static final double WHEEL_GEAR_RATIO = 3.5;
    private static final double ENCODER_GEAR_RATIO = 1.5;
    private static final double ENCODER_TICKS_PER_REV = 125;
    private static final double ENCODER_INCHES_PER_TICK = (Math.PI * WHEEL_DIAMETER_INCHES) / (WHEEL_GEAR_RATIO * ENCODER_GEAR_RATIO * ENCODER_TICKS_PER_REV);
    private static final double WHEEL_MAX_SPEED = 24.3; //Speed the wheel moves at max output (maxSpeed) in inches / sec; temporary fix, determined empirically
    
    private static final double DIGIPOT_MIN_VOLTAGE = 0.160; //0.204, 0.160
    private static final double DIGIPOT_MAX_VOLTAGE = 4.88; //4.96, 4.88
    private static final double WHEEL_ONE_DIGIPOT_OFFSET = 235.0; //Offsets fix the fact that the digipots aren't all facing exactly the same direction
    private static final double WHEEL_TWO_DIGIPOT_OFFSET = 26.0;
    private static final double WHEEL_THREE_DIGIPOT_OFFSET = 1.5;
    private static final double WHEEL_FOUR_DIGIPOT_OFFSET = 210.0;
    
    // Wheel pods //
    private SwervePod _swervePod1; //Front left
    private SwervePod _swervePod2; //Front right
    private SwervePod _swervePod3; //Rear right
    private SwervePod _swervePod4; //Rear left
    
    public void init() {
        System.out.println("Initializing drivetrain");
        
        // Initialize sensors //
        _encoder1 = new Encoder(RobotMap.encoder1Pin1, RobotMap.encoder1Pin2, false, CounterBase.EncodingType.k1X);
        _encoder1.setDistancePerPulse(ENCODER_INCHES_PER_TICK);
        _encoder1.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate); //The encoder should return a rate
        _encoder2 = new Encoder(RobotMap.encoder2Pin1, RobotMap.encoder2Pin2, false, CounterBase.EncodingType.k1X);
        _encoder2.setDistancePerPulse(ENCODER_INCHES_PER_TICK);
        _encoder2.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        _encoder3 = new Encoder(RobotMap.encoder3Pin1, RobotMap.encoder3Pin2, false, CounterBase.EncodingType.k1X);
        _encoder3.setDistancePerPulse(ENCODER_INCHES_PER_TICK);
        _encoder3.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        _encoder4 = new Encoder(RobotMap.encoder4Pin1, RobotMap.encoder4Pin2, false, CounterBase.EncodingType.k1X);
        _encoder4.setDistancePerPulse(ENCODER_INCHES_PER_TICK);
        _encoder4.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        
        _digipot1 = new AbsoluteAnalogEncoder(RobotMap.digipot1, DIGIPOT_MIN_VOLTAGE, DIGIPOT_MAX_VOLTAGE, WHEEL_ONE_DIGIPOT_OFFSET);
        _digipot2 = new AbsoluteAnalogEncoder(RobotMap.digipot2, DIGIPOT_MIN_VOLTAGE, DIGIPOT_MAX_VOLTAGE, WHEEL_TWO_DIGIPOT_OFFSET);
        _digipot3 = new AbsoluteAnalogEncoder(RobotMap.digipot3, DIGIPOT_MIN_VOLTAGE, DIGIPOT_MAX_VOLTAGE, WHEEL_THREE_DIGIPOT_OFFSET);
        _digipot4 = new AbsoluteAnalogEncoder(RobotMap.digipot4, DIGIPOT_MIN_VOLTAGE, DIGIPOT_MAX_VOLTAGE, WHEEL_FOUR_DIGIPOT_OFFSET);
        
        // Initialize motors //
        // TODO: Handle motor inversions?
        _driveMotor1 = new SabertoothSpeedController(RobotMap.drive1SabertoothAddress, RobotMap.drive1MotorNumber);
        _driveMotor2 = new SabertoothSpeedController(RobotMap.drive2SabertoothAddress, RobotMap.drive2MotorNumber);
        _driveMotor3 = new SabertoothSpeedController(RobotMap.drive3SabertoothAddress, RobotMap.drive3MotorNumber);
        _driveMotor4 = new SabertoothSpeedController(RobotMap.drive4SabertoothAddress, RobotMap.drive4MotorNumber);
        
        _turningMotor1 = new Talon(RobotMap.turningTalon1);
        _turningMotor2 = new Talon(RobotMap.turningTalon2);
        _turningMotor3 = new Talon(RobotMap.turningTalon3);
        _turningMotor4 = new Talon(RobotMap.turningTalon4);
        
        // Initialize swerve pods //
        _swervePod1 = new SwervePod(_turningMotor1, _driveMotor1, _encoder1, _digipot1);
        _swervePod2 = new SwervePod(_turningMotor2, _driveMotor2, _encoder2, _digipot2);
        _swervePod3 = new SwervePod(_turningMotor3, _driveMotor3, _encoder3, _digipot3);
        _swervePod4 = new SwervePod(_turningMotor4, _driveMotor4, _encoder4, _digipot4);
        
        // Initialize swerve pod setpoints //
        setAllWheelAngles(180.0);
        //setAllWheelPowers(0.0);
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
        //NOTE: Driving currently disabled due to mechanical issues
        _swervePod1.setDriveMotor(powerLeft); //Left
        _swervePod4.setDriveMotor(powerLeft); //Left 
        _swervePod2.setDriveMotor(powerRight); //Right
        _swervePod3.setDriveMotor(powerRight); //Right
    }
    
}
