package Hardware;


import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.visa.VisaException;

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
    private Talon               _turningMotor;
    private SerialPort          _serial;
    
    private int                 _sabertoothAddress;
    private int                 _sabertoothMotor;
    
    private Encoder             _encoder;
    private AnalogPotentiometer _digipot;
    
    //Sabertooth IDs: 128 and 129
    //Sabertooth motors: 1 and 2
    public SwervePod(int talonPort, int sabertoothAddress, int sabertoothMotor, int baudRate, int encoderA, int encoderB, int digipotChannel) {
        _sabertoothAddress = sabertoothAddress;
        _sabertoothMotor = sabertoothMotor;
        
        _turningMotor = new Talon(talonPort);
        _encoder = new Encoder(encoderA, encoderB);
        _digipot = new AnalogPotentiometer(digipotChannel); 
    
        // Setup serial //
        try {
        _serial = new SerialPort(baudRate); //Create serial port
        //Send bauding character
        byte[] baudChar = { (byte) 170 };
        _serial.write(baudChar, baudChar.length);
        } catch (VisaException e) {
            System.out.println("Error creating serial port: " + e);
        }
    }
    
    public void setTurningMotor(double speed) { //Speed should be between -1.0 and 1.0
        _turningMotor.set(speed);
    }
    
    public void setDriveMotor(double speed) { //Speed should be between -1.0 and 1.0
        double spd = Math.abs(speed);
        
        if (spd > 1.0) {
            System.out.println("Error setting drive motor: speed too high");
            return;
        }
        
        // Set command byte... TODO: Make this more readable? //
        int command = (_sabertoothMotor == 1) ? 0 : 4; //Command is 0/1 for motor 1, 4/5 for motor 2
        if (speed < 0.0) command++;
        
        // Set data byte //
        int data = (int) Math.floor(spd * 128.0);
        
        // Write data //
        try {
            writeSerialPacket(command, data);
        } catch (VisaException e) {
            System.out.println("Error writing serial data: " + e);
        }
    }
    
    public int getEncoder() {
        return _encoder.get();
    }
    
    public void resetEncoder() {
        _encoder.reset();
    }
    
    public double getDigipotRaw() { //TODO: Write linear mapping for this -> degrees
        return _digipot.get();
    }
    
    private void writeSerialPacket(int command, int data) throws VisaException {
        byte addr = (byte) _sabertoothAddress;
        byte com = (byte) command;
        byte dat = (byte) data;
        byte checksum = (byte) ((addr + com + dat) & 0x7F);
        byte[] packet = { addr, com, dat, checksum };
        _serial.write(packet, packet.length);
    }
}
