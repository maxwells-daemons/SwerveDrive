/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iolani.robotics.commands;

/**
 *
 * @author Aidan
 */
public class OperateTurretDrive extends CommandBase {
    public OperateTurretDrive() {
        requires(swervedrive);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double speed = oi.getRightStickX(); //Right stick X controls rotation speed
        swervedrive.setWheelAngles(45, 315, 225, 135); //Wheels locked in "circular" pattern
        swervedrive.setWheelPowers(-1 * speed, speed, -1 * speed, speed);
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
