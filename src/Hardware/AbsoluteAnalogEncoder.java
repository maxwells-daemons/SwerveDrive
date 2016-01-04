/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hardware;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

/**
 *
 * @author Aidan
 */

// Implements an analog encoder that reports 0-360 degrees with a linear voltage range
public class AbsoluteAnalogEncoder extends AnalogPotentiometer implements AngularSensor {
    
    // Hardware-dependent //
    private final double _minVoltage; //Minimum voltage the sensor will output
    private final double _maxVoltage; //Maximum voltage the sensor will output
    private final double _offsetDegrees; //Facing angle of the measured object at minimum voltage
    
    
    public AbsoluteAnalogEncoder(int channel, double minVoltage, double maxVoltage, double offsetDegrees) { //TODO: Implement direction
        super(channel);
        if (minVoltage >= maxVoltage) throw new IllegalArgumentException("Minimum voltage must be less than maximum voltage");
        if (offsetDegrees < 0 || offsetDegrees > 360) throw new IllegalArgumentException("Offset must be between 0 and 360 degrees");
        
        _minVoltage = minVoltage;
        _maxVoltage = maxVoltage;
        _offsetDegrees = offsetDegrees;
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
        double degrees = (((voltage - _minVoltage) * (360.0 / _maxVoltage)) + _offsetDegrees) % 360.0;
        
        //Returns 0 degrees through 360 degrees
        return degrees;
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
}
