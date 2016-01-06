/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardware;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.visa.VisaException;
import java.util.Vector;

/*
 *
 * @author Aidan
 */
public class SabertoothSpeedController implements SpeedController {
    
    private static SerialPort _PORT = null; //Serial port shared by all Sabertooths
    
    // List of already-assigned motors //
    private static Vector _assignedMotors = new Vector();
            
    // Hardware constants //
    private static final double sabertooth_maxSpeed = 127.0; //Speed setting when the motor is at maximum forward speed
    private static final double sabertooth_minSpeed = 1.0; //Speed setting when the motor is at maximum backward speed
    private static final double sabertooth_offSpeed = 0.0; //Speed setting when the motor is off
    
    private static final double motorInput_maxSpeed = 1.0; //Speed setting to turn the motor on at max forward speed
    private static final double motorInput_minSpeed = -1.0; //Speed setting to turn the motor on at max backward speed
    private static final double motorInput_offSpeed = 0.0; //Speed setting to turn the motor off
    
    // Special bytes to be sent over serial //
    private static final byte sabertooth_baudingCharacter = (byte) 170;
    private static final byte sabertooth_command_motorOneForward = 0;
    private static final byte sabertooth_command_motorOneBackward = 1;
    private static final byte sabertooth_command_motorTwoForward = 4;
    private static final byte sabertooth_command_motorTwoBackward = 5;
    
    public static class SabertoothCommand {
        public final byte value;
        
        protected static final byte MOTOR_ONE_FORWARD_VALUE = 0;
        protected static final byte MOTOR_ONE_BACKWARD_VALUE = 1;
        protected static final byte MOTOR_TWO_FORWARD_VALUE = 4;
        protected static final byte MOTOR_TWO_BACKWARD_VALUE = 5;
        
        public static final SabertoothCommand MOTOR_ONE_FORWARD = new SabertoothCommand(MOTOR_ONE_FORWARD_VALUE);
        public static final SabertoothCommand MOTOR_ONE_BACKWARD = new SabertoothCommand(MOTOR_ONE_BACKWARD_VALUE);
        public static final SabertoothCommand MOTOR_TWO_FORWARD = new SabertoothCommand(MOTOR_TWO_FORWARD_VALUE);
        public static final SabertoothCommand MOTOR_TWO_BACKWARD = new SabertoothCommand(MOTOR_TWO_BACKWARD_VALUE);
        
        private SabertoothCommand(byte value) {
            this.value = value;
        }
    }
    
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
        
        // Check to see if this motor has already been assigned //
        for (int i = 0; i < _assignedMotors.size(); i++) {
            SabertoothSpeedController examinedMotor = (SabertoothSpeedController) _assignedMotors.elementAt(i);
            if (examinedMotor.getAddress() == address && examinedMotor.getMotorNumber() == motorNumber) {
                throw new IllegalStateException("Motor " + motorNumber.value + " on the Sabertooth at address " + address.value + " is already in use");
            }
        }
        
        // Add this motor to the list of assigned ones //
        _assignedMotors.addElement(this);
        
        _address = address;
        _motorNumber = motorNumber;
    
        _currentSpeed = motorInput_offSpeed;
    }
    
    public static void initializeSerialPort(int baudRate) {
        System.out.println("Initializing the serial port...");
        if(_PORT != null) throw new IllegalStateException("Serial port already initialized");
        
        try {
            _PORT = new SerialPort(baudRate);
            
            // Send the baud rate character //
            byte[] baudChar = { sabertooth_baudingCharacter };
            _PORT.write(baudChar, baudChar.length);
        } catch(VisaException e) {
            throw new RuntimeException("Error creating serial port: " + e.getMessage());
        }
    }
    
    private void writeSerialPacket(byte command, byte data) throws VisaException {
        byte addr = (byte) _address.value;
        byte checksum = (byte) ((addr + command + data) & 0x7F); //Generate checksum from address, command, and data bytes
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
        
        if (spd > motorInput_maxSpeed) throw new IllegalArgumentException("Speed must be between -1.0 and 1.0");
        
        _currentSpeed = speed;
        
        // Set command byte //
        byte command;
        command = SabertoothCommand.MOTOR_ONE_FORWARD.value;
        
        if (_motorNumber == SabertoothMotor.SABERTOOTH_MOTOR_ONE) {
            if (speed >= motorInput_offSpeed) {
                command = SabertoothCommand.MOTOR_ONE_FORWARD.value;
            } else {
                command = SabertoothCommand.MOTOR_ONE_BACKWARD.value;
            }
        } else {
            if (speed >= motorInput_offSpeed) {
                command = SabertoothCommand.MOTOR_TWO_FORWARD.value;
            } else {
                command = SabertoothCommand.MOTOR_TWO_BACKWARD.value;
            }
        }
        
        // Set data byte //
        byte data = (byte) Math.floor(spd * sabertooth_maxSpeed);
        
        // Write data //
        try {
            writeSerialPacket(command, data);
        } catch (VisaException e) {
            throw new RuntimeException("Error writing serial data: " + e.getMessage());
        }
    }

    public void disable() {
        set(motorInput_offSpeed);
    }
    
    public static boolean isSerialPortInitialized() { //Has the serial port been initialized yet?
        return !(_PORT == null);
    }
    
    public SabertoothAddress getAddress() {
        return _address;
    }
    
    public SabertoothMotor getMotorNumber() {
        return _motorNumber;
    }
}
