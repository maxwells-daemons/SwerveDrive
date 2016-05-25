package org.iolani.robotics.hardware;

import edu.wpi.first.wpilibj.AnalogModule;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.AnalogTriggerOutput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Module;
import edu.wpi.first.wpilibj.communication.ModulePresence;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Aidan
 */

// Implements an analog encoder that reports 0-360 degrees with a linear voltage range
public class AbsoluteAnalogEncoder extends AnalogPotentiometer implements AngularSensor {
    // Constants //
    private final int DEFAULT_ANALOG_MODULE = 1; //Find a better way to do this? getModuleForRouting has problems...
    
    // Hardware-dependent //
    private final double _minVoltage; //Minimum voltage the sensor will output
    private final double _maxVoltage; //Maximum voltage the sensor will output
    private final double _offsetDegrees; //Facing angle of the measured object at minimum voltage
    private final double _channel;
    
    // Convert potentiometer to counter //
    private AnalogTrigger _analogTrigger;
    private Counter _turnCounter;
    
    // Includes a turn counter //
    public AbsoluteAnalogEncoder(int channel, double minVoltage, double maxVoltage, double offsetDegrees, int analogSampleRate, double analogTriggerThresholdDifference) { //TODO: Implement direction
        super(channel);
        _channel = channel;
        if (minVoltage >= maxVoltage) throw new IllegalArgumentException("Minimum voltage must be less than maximum voltage");
        if (offsetDegrees < 0 || offsetDegrees > 360) throw new IllegalArgumentException("Offset must be between 0 and 360 degrees");
        
        // Initialize analog trigger //
        _analogTrigger = new AnalogTrigger(channel);
        _analogTrigger.setFiltered(true);
        _analogTrigger.setLimitsVoltage(minVoltage + analogTriggerThresholdDifference, maxVoltage - analogTriggerThresholdDifference);
        AnalogTriggerOutput _analogTriggerFalling = new AnalogTriggerOutput(_analogTrigger, AnalogTriggerOutput.Type.kFallingPulse);
        AnalogTriggerOutput _analogTriggerRising = new AnalogTriggerOutput(_analogTrigger, AnalogTriggerOutput.Type.kRisingPulse);
        
        // Set analog module sampling rate //        
        AnalogModule module = (AnalogModule) Module.getModule(ModulePresence.ModuleType.kAnalog, DEFAULT_ANALOG_MODULE);
        module.setSampleRate(analogSampleRate);
        
        // Initialize turn counter //
        _turnCounter = new Counter();
        _turnCounter.setUpDownCounterMode();
        _turnCounter.setUpSource(_analogTriggerRising);
        _turnCounter.setDownSource(_analogTriggerFalling);
        _turnCounter.start();
        
        _minVoltage = minVoltage;
        _maxVoltage = maxVoltage;
        _offsetDegrees = offsetDegrees;
    }
    
    // No turn counter //
    public AbsoluteAnalogEncoder(int channel, double minVoltage, double maxVoltage, double offsetDegrees) {
        super(channel);
        
        if (minVoltage >= maxVoltage) throw new IllegalArgumentException("Minimum voltage must be less than maximum voltage");
        if (offsetDegrees < 0 || offsetDegrees > 360) throw new IllegalArgumentException("Offset must be between 0 and 360 degrees");
        
        _channel = channel;
        _minVoltage = minVoltage;
        _maxVoltage = maxVoltage;
        _offsetDegrees = offsetDegrees;
        
        // Turn counter only ever stores 0 //
        _turnCounter = new Counter();
        _turnCounter.reset();
        _turnCounter.stop();
    }
    
    public double getDegrees() { //Assume direct linear conversion between minVoltage and maxVoltage
        double voltage = this.get();
        
        //Restrict voltage to inside range
        if (voltage < _minVoltage) {
            voltage = _minVoltage;
        } else if (voltage > _maxVoltage) {
            voltage = _maxVoltage;
        }
        
        //Currently assumes more voltage = more degrees, cannot reverse
        double degrees = (((voltage - _minVoltage) * (360.0 / _maxVoltage))) % 360.0;
        
        //Returns 0 degrees through 360 degrees
        return degrees + _offsetDegrees + (getTurns() * 360.0);
    }
    
    public double pidGet() {
        return this.getDegrees();
    }
    
    public double getMinVoltage() {
        return _minVoltage;
    }
    
    public double getMaxVoltage() {
        return _maxVoltage;
    }
    
    public double getOffsetDegrees() {
        return _offsetDegrees;
    }
    
    public double getTurns() {
        return _turnCounter.get();
    }
    
    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Pod " + _channel + " turns:", getTurns()); 
        SmartDashboard.putNumber("Pod " + _channel + " degrees:", getDegrees());
    }
}
