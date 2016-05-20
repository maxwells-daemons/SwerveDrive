package org.iolani.robotics.hardware;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDController.AbsoluteTolerance;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Aidan
 */
public class SwervePod {
    // Hardware //
    private final SpeedController _turningMotor;
    private final SpeedController _driveMotor;
    
    private final Encoder         _encoder;
    private final AngularSensor   _directionSensor;
    
    // Hardware constants //
    private final double maxSpeed = 1.0; //Speed to feed a SpeedController to turn it on at 100% forward
    private final double minSpeed = -1.0; //Speed to feed a SpeedController to turn it on at 100% backward
    private final double offSpeed = 0.0; //Speed to feed a SpeedController to turn it off
    private final double minDegrees = 0.0;
    private final double maxDegrees = 360;
    private final double PI = 3.14;
    
    // Temporary solution to converting encoder ticks to useful units //
    private final double wheelGearRatio = 3.5;
    private final double encoderGearRatio = 1.5;
    private final double encoderTicksPerRev = 125;
    private final double wheelRadius = 1.0; //In inches
    private final double encoderInchesPerTick = (2 * PI * wheelRadius) / (wheelGearRatio * encoderGearRatio * encoderTicksPerRev);
    private final double maxWheelSpeed = 24.3; //Speed the wheel moves at max output (maxSpeed) in inches / sec; temporary fix, determined empirically
    
    // Control constants //
    //TODO: Tune all
    private final double Kp_turning = 0.019; //0.01;
    private final double Ki_turning = 0.000;
    private final double Kd_turning = 0.009; //0.0;
    private final double tolerance_turning = 0.0;
    
    private final double Kp_driving = 0.0;
    private final double Ki_driving = 0.000;
    private final double Kd_driving = 0.000;
    private final double tolerance_driving = 0.0;
    
    // PID loops //
    PIDController PIDTurning;
    PIDController PIDDriving;
    
    public SwervePod(SpeedController turningMotor, SpeedController driveMotor, Encoder encoder, AngularSensor directionSensor) {
        // Initialize motors //
        _turningMotor = turningMotor;
        _driveMotor = driveMotor;
        
        // Initialize sensors //
        _encoder = encoder;
        _encoder.start();
        _directionSensor = directionSensor;
        
        // Initialize PID loops //
        // Turning //
        PIDTurning = new PIDController(Kp_turning, Ki_turning, Kd_turning, _directionSensor, _turningMotor);
        PIDTurning.setInputRange(minDegrees, maxDegrees);
        PIDTurning.setOutputRange(minSpeed, maxSpeed);
        PIDTurning.setContinuous();
        PIDTurning.setAbsoluteTolerance(tolerance_turning);
        PIDTurning.enable();
        
        // Linear driving //
        PIDDriving = new PIDController(Kp_driving, Ki_driving, Kd_driving, _encoder, _driveMotor);
        PIDDriving.setOutputRange(minSpeed, maxSpeed);
        PIDDriving.disable(); //TODO: Enable
    }
    
    public void initSmartDashboard() {
        //TODO: Write custom SmartDashboard widget?
        SmartDashboard.putData("Turning PID", PIDTurning);
        SmartDashboard.putNumber("Pod Facing Angle", _directionSensor.getDegrees());
        SmartDashboard.putData("Driving PID", PIDDriving);
        SmartDashboard.putNumber("Wheel Speed", _encoder.getRate());
    }
    
    public void updateSmartDashboard() { //Call to update SmartDashboard values
        //TODO: Handle through custom SmartDashboard widget?
        SmartDashboard.putNumber("Pod Facing Angle", _directionSensor.getDegrees());
        SmartDashboard.putNumber("Wheel Speed", _encoder.getRate());
        SmartDashboard.putNumber("Facing Angle Command", PIDTurning.getSetpoint());
        SmartDashboard.putNumber("Wheel Speed Command", PIDDriving.getSetpoint());
    }
    
    public void setTurningSetpoint(double degrees) {
        PIDTurning.setSetpoint(degrees);
    }
    
    public double getTurningSetpoint() {
        return PIDTurning.getSetpoint();
    }
    
    public void setDrivingSetpoint(double speed) {
        PIDDriving.setSetpoint(speed);
    }
    
    public double getDrivingSetpoint() {
        return PIDTurning.getSetpoint();
    }
    
    public void setTurningMotor(double speed) { //Speed should be between -1.0 and 1.0
        _turningMotor.set(speed);
    }
    
    public double getTurningMotor() {
        return _turningMotor.get();
    }
    
    public void setDriveMotor(double speed) { //Speed should be between -1.0 and 1.0
        _driveMotor.set(speed);
    }
    
    public double getDriveMotor() {
        return _driveMotor.get();
    }
    
    public int getEncoderCounts() {
        return _encoder.get();
    }
    
    public double getEncoderDegrees() {
        return (_encoder.get() * maxDegrees) / (encoderTicksPerRev * wheelGearRatio * encoderGearRatio);
    }
    
    public double getEncoderDistance() { //Inches
        return _encoder.getDistance();
    }
    
    public double getEncoderRate() { //Inches per second
        return _encoder.getRate();
    }
    
    public void resetEncoder() {
        _encoder.reset();
    }
    
    public double getDegrees() {
        return _directionSensor.getDegrees();
    }
    
    public void disable() { //Disable both motors
        _turningMotor.disable();
        _driveMotor.disable();
    }
}
