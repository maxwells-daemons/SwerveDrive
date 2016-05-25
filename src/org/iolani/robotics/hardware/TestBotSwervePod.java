/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.robotics.hardware;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Aidan
 */
public class TestBotSwervePod extends SwervePod {
    // Physical constants //
    private static final double WHEEL_DIAMETER_INCHES = 2.0;
    private static final double WHEEL_GEAR_RATIO = 3.5;
    private static final double ENCODER_GEAR_RATIO = 1.5;
    private static final double ENCODER_TICKS_PER_REV = 125;
    private static final double ENCODER_INCHES_PER_TICK = (Math.PI * WHEEL_DIAMETER_INCHES) / (WHEEL_GEAR_RATIO * ENCODER_GEAR_RATIO * ENCODER_TICKS_PER_REV);
    
    public TestBotSwervePod(int talonPWM, SabertoothSpeedController.Address sabertoothAddress, SabertoothSpeedController.Motor sabertoothMotor, int encoderPin1, int encoderPin2, int digipotPin, double digipotOffset) {
        super(new Talon(talonPWM), new SabertoothSpeedController(sabertoothAddress, sabertoothMotor), new Encoder(encoderPin1, encoderPin2, false, CounterBase.EncodingType.k1X), ENCODER_INCHES_PER_TICK, new BI6120Magnepot(digipotPin, digipotOffset));
    }
}
