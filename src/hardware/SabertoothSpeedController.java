/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardware;

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
    private SabertoothAddress _address;
    private SabertoothMotor _motorNumber;
    
    private double _currentSpeed;
    
    public static class SabertoothAddress { //WPILib enum-like pattern; defines which Sabertooth we're talking to
        public final int value;
        
        protected static final int SABERTOOTH_ONE_VALUE = 128; //DIP switch address = 128
        protected static final int SABERTOOTH_TWO_VALUE = 129; //DIP switch address = 129
        protected static final int SABERTOOTH_THREE_VALUE = 130; //DIP switch address = 130
        protected static final int SABERTOOTH_FOUR_VALUE = 131; //DIP switch address = 131
        protected static final int SABERTOOTH_FIVE_VALUE = 132; //DIP switch address = 132
        protected static final int SABERTOOTH_SIX_VALUE = 133; //DIP switch address = 133
        protected static final int SABERTOOTH_SEVEN_VALUE = 134; //DIP switch address = 134
        protected static final int SABERTOOTH_EIGHT_VALUE = 135; //DIP switch address = 135
        
        public static final SabertoothAddress SABERTOOTH_ONE = new SabertoothAddress(SABERTOOTH_ONE_VALUE);
        public static final SabertoothAddress SABERTOOTH_TWO = new SabertoothAddress(SABERTOOTH_TWO_VALUE);
        public static final SabertoothAddress SABERTOOTH_THREE = new SabertoothAddress(SABERTOOTH_THREE_VALUE);
        public static final SabertoothAddress SABERTOOTH_FOUR = new SabertoothAddress(SABERTOOTH_FOUR_VALUE);
        public static final SabertoothAddress SABERTOOTH_FIVE = new SabertoothAddress(SABERTOOTH_FIVE_VALUE);
        public static final SabertoothAddress SABERTOOTH_SIX = new SabertoothAddress(SABERTOOTH_SIX_VALUE);
        public static final SabertoothAddress SABERTOOTH_SEVEN = new SabertoothAddress(SABERTOOTH_SEVEN_VALUE);
        public static final SabertoothAddress SABERTOOTH_EIGHT = new SabertoothAddress(SABERTOOTH_EIGHT_VALUE);
        
        private SabertoothAddress(int value) {
            this.value = value;
        }
    }
    
    public static class SabertoothMotor {
        public final int value;
        
        protected static final int SABERTOOTH_MOTOR_ONE_VALUE = 1; //Motor in motor slot 1
        protected static final int SABERTOOTH_MOTOR_TWO_VALUE = 2; //Motor in motor slot 2
        
        public static final SabertoothMotor SABERTOOTH_MOTOR_ONE = new SabertoothMotor(SABERTOOTH_MOTOR_ONE_VALUE);
        public static final SabertoothMotor SABERTOOTH_MOTOR_TWO = new SabertoothMotor(SABERTOOTH_MOTOR_TWO_VALUE);
        
        private SabertoothMotor(int value) {
            this.value = value;
        }
    }
    
    public SabertoothSpeedController(SabertoothAddress address, SabertoothMotor motorNumber) {
        if (_PORT == null) throw new IllegalStateException("Serial port not initialized, call initializeSerialPort(baudRate)");
        
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
        byte addr = (byte) _address.value;
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
        
        // Command is 0/1 for motor 1, 4/5 for motor 2
        byte command = 0;
        if (_motorNumber == SabertoothMotor.SABERTOOTH_MOTOR_TWO) command = 4;
        
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
    
    public static boolean isSerialPortInitialized() { //Has the serial port been initialized yet?
        return !(_PORT == null);
    }
}
