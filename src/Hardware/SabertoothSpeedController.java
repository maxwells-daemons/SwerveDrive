/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hardware;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.visa.VisaException;

/**
 *
 * @author Aidan
 */
public class SabertoothSpeedController implements SpeedController {
    
    private static SerialPort _PORT = null; //Serial port shared by all Sabertooths
    
    //TODO: Keep list of assigned motors (address/motor pairs) and throw an error when we overwrite one?
    
    // Motor specification //
    private int _address;
    private int _motorNumber;
    
    private double _currentSpeed;
    
    public SabertoothSpeedController(int address, int motorNumber) {
        if (_PORT == null) throw new IllegalStateException("Serial port not initialized, call initializeSerialPort(baudRate)");
        if (address < 128 || address > 135) throw new IllegalArgumentException("Sabertooth address out of range");
        if (motorNumber < 1 || motorNumber > 2) throw new IllegalArgumentException("Sabertooth motor number out of range");
        
        _address = address;
        _motorNumber = motorNumber;
    
        _currentSpeed = 0.0;
    }
    
    public static void initializeSerialPort(int baudRate) {
        System.out.println("Initializing the serial port...");
        if(_PORT != null) throw new IllegalStateException("Serial port already initialized");
        
        try {
            _PORT = new SerialPort(baudRate);
            
            // send the baud rate character //
            byte[] baudChar = { (byte) 170 };
            _PORT.write(baudChar, baudChar.length);
        } catch(VisaException e) {
            throw new RuntimeException("Error creating serial port: " + e.getMessage());
        }
    }
    
    private void writeSerialPacket(byte command, byte data) throws VisaException {
        byte addr = (byte) _address;
        byte checksum = (byte) ((addr + command + data) & 0x7F);
        byte[] packet = { addr, command, data, checksum };
        _PORT.write(packet, packet.length);
    }
    
    public void pidWrite(double output) {
        set(output);
    }   

    public double get() {
        return _currentSpeed;
    }

    public void set(double speed, byte syncGroup) {
        set(speed); //Not sure if I'll ever need to implement sync groups for this
    }

    public void set(double speed) { //Speed should be between -1.0 and 1.0
        double spd = Math.abs(speed);
        
        if (spd > 1.0) throw new IllegalArgumentException("Speed must be between -1.0 and 1.0");
        
        _currentSpeed = speed;
        
        // Set command byte... TODO: Make this more readable //
        // Command is 0/1 for motor 1, 4/5 for motor 2
        byte command = 0;
        if (_motorNumber == 1) command = 4;
        
        if (speed < 0.0) command++;
        
        // Set data byte //
        byte data = (byte) Math.floor(spd * 127.0);
        
        // Write data //
        try {
            writeSerialPacket(command, data);
        } catch (VisaException e) {
            throw new RuntimeException("Error writing serial data: " + e.getMessage());
        }
    }

    public void disable() {
        set(0.0);
    }
    
    public static boolean isSerialSet() { //Has the serial port been set yet?
        return !(_PORT == null);
    }
}
