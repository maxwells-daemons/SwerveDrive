package Hardware;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

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
    
    public SwervePod(SpeedController turningMotor, SpeedController driveMotor, Encoder encoder, AngularSensor directionSensor) {
        _turningMotor = turningMotor;
        _driveMotor = driveMotor;
        _encoder = encoder;
        _directionSensor = directionSensor;
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
