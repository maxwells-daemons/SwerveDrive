/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.robotics.commands;

import com.sun.squawk.util.MathUtils;

/**
 *
 * @author Aidan
 */
public class OperateOmniDrive extends CommandBase {
    // Constants; TODO: Define these somewhere else //
    private static final double ROBOT_LENGTH = 16; //Inches between two wheel pod axles
    private static final double ROBOT_WIDTH = 16;
    private static final double WHEELBASE_RADIUS = Math.sqrt((ROBOT_LENGTH * ROBOT_LENGTH / 4.0) + (ROBOT_WIDTH * ROBOT_WIDTH / 4.0));
    private static final double MAX_WHEEL_SPEED = 24.3; //Inches / sec
    
    
    public OperateOmniDrive() {
        requires(swervedrive);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // Using inverse kinematics from Ether at ChiefDelphi (http://www.chiefdelphi.com/media/papers/2426) //
        double targetAngularSpeed = -1 * oi.getRightStickX(); //Right stick X controls rotation speed; positive = right = clockwise
        double targetVelocityX = -1 * oi.getLeftStick().getX();
        double targetVelocityY = oi.getLeftStick().getY();
        
        // Calculation factors //
        double A = targetVelocityX - (.5 * targetAngularSpeed * ROBOT_LENGTH);
        double B = targetVelocityX + (.5 * targetAngularSpeed * ROBOT_LENGTH);
        double C = targetVelocityY - (.5 * targetAngularSpeed * ROBOT_LENGTH);
        double D = targetVelocityY + (.5 * targetAngularSpeed * ROBOT_LENGTH);
    
        double wheel1Speed = Math.sqrt((B * B) + (D * D));
        double wheel2Speed = Math.sqrt((B * B) + (C * C));
        double wheel3Speed = Math.sqrt((A * A) + (C * C));
        double wheel4Speed = Math.sqrt((A * A) + (D * D));
        
        double maxSpeed = Math.max(Math.max(wheel1Speed, wheel2Speed), Math.max(wheel3Speed, wheel4Speed));
        
        if (maxSpeed > 1.0) {
            wheel1Speed = wheel1Speed / maxSpeed;
            wheel2Speed = wheel2Speed / maxSpeed;
            wheel3Speed = wheel3Speed / maxSpeed;
            wheel4Speed = wheel4Speed / maxSpeed;
        }
        
        double wheel1Angle = MathUtils.atan2(B, D) * 180.0 / Math.PI;
        double wheel2Angle = MathUtils.atan2(B, C) * 180.0 / Math.PI;
        double wheel3Angle = MathUtils.atan2(A, C) * 180.0 / Math.PI;
        double wheel4Angle = MathUtils.atan2(A, D) * 180.0 / Math.PI;
        
        if (maxSpeed > 0.05) {
            swervedrive.setWheelAngles(wheel1Angle, wheel2Angle, wheel3Angle, wheel4Angle);
            swervedrive.setWheelPowers(wheel1Speed, wheel2Speed, wheel3Speed, wheel4Speed);
        } else {
            swervedrive.setAllWheelAngles(180);
            swervedrive.setAllWheelPowers(0);
        }
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        this.end();
    }
}
