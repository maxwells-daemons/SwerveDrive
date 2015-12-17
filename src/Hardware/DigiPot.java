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
public class DigiPot extends AnalogPotentiometer implements AngularSensor {
    
    // Hardware-dependent //
    private double _minVoltage;
    private double _maxVoltage;
    private double _minVoltageAngle;
    
    
    public DigiPot(int channel, double minVoltage, double maxVoltage, double minVoltageAngle) { //TODO: Implement direction
        super(channel);
        
        _minVoltage = minVoltage;
        _maxVoltage = maxVoltage;
        _minVoltageAngle = minVoltageAngle;
        
    }
    
    public double getDegrees() { //Assume direct linear conversion between minVoltage and maxVoltage
        double voltage = this.get();
        
        //Currently assumes more voltage = more degrees, cannot reverse
        double degrees = (((voltage - _minVoltage) * (360.0 / _maxVoltage)) + _minVoltageAngle) % 360.0;
        return degrees;
    }
}
