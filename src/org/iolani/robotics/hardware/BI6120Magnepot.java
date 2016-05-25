/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.robotics.hardware;

/**
 *
 * @author Aidan
 */
public class BI6120Magnepot extends AbsoluteAnalogEncoder {
    // Physical constants //
    private static double MIN_VOLTAGE = 0.3; //0.204, 0.160
    private static double MAX_VOLTAGE = 4.7; //4.96, 4.88
    
    // Analog trigger constants //
    private static final int ANALOG_SAMPLE_RATE = 50; //Provide a huge margin of error with sample rate and voltage difference in case of noise, mechanical jitter, etc
    // Used for counting turns; distance that trigger threshold voltages are from actual potentiometer voltage max/min //
    private static final double THRESHOLD_VOLTAGE_DIFFERENCE = 0.6; //At minimum, equal to double maximum rate of change of voltage * time after digipot seam for sag / rise, plus some margin of error... used to ensure one datapoint falls outside of normal range for sensing

    public BI6120Magnepot(int channel, double offsetDegrees) {
        super(channel, MIN_VOLTAGE, MAX_VOLTAGE, offsetDegrees, ANALOG_SAMPLE_RATE, THRESHOLD_VOLTAGE_DIFFERENCE);
    }
}
