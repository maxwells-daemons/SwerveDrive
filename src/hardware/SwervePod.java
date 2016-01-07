package hardware;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    
    // Control constants //
    private final double Kp_turning = 0.015;
    private final double Ki_turning = 0.002;
    private final double Kd_turning = 0.0001;
    private final double tolerance_turning = 10.0;
    
    // PID loops //
    PIDController PIDTurning;
    
    public SwervePod(SpeedController turningMotor, SpeedController driveMotor, Encoder encoder, AngularSensor directionSensor) {
        // Initialize motors //
        _turningMotor = turningMotor;
        _driveMotor = driveMotor;
        _encoder = encoder;
        _encoder.start();
        _directionSensor = directionSensor;
        
        // Initialize PID loops //
        PIDTurning = new PIDController(Kp_turning, Ki_turning, Kd_turning, _directionSensor, _turningMotor);
        PIDTurning.setInputRange(minDegrees, maxDegrees);
        PIDTurning.setOutputRange(minSpeed, maxSpeed);
        PIDTurning.setContinuous(true);
        PIDTurning.setAbsoluteTolerance(tolerance_turning);    
        PIDTurning.disable();
        SmartDashboard.putData("Turning PID", PIDTurning);
    }
    
    public void setTurningSetpoint(double degrees) {
        PIDTurning.setSetpoint(degrees);
    }
    
    public double getTurningSetpoint() {
        return PIDTurning.getSetpoint();
    }
    
    public double getTurningPIDOutput() {
        return PIDTurning.get();
    }
    
    public double getTurningPIDError() {
        return PIDTurning.getError();
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
