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
public class OperateCrabDrive extends CommandBase {
    public OperateCrabDrive() {
        requires(swervedrive);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double angle = oi.getLeftStick().getDirectionDegrees() + 180.0; //Left stick controls wheel pod angle
        // Turning handled through right-stick arcade drive //
        double rightX = oi.getRightStickX();
        double rightY = oi.getRightStickY();
        double powerLeft = rightX + rightY;
        double powerRight = -rightX + rightY;
        // Cap power //
        if (powerLeft > 1.0) powerLeft = 1.0;
        if (powerLeft < -1.0) powerLeft = -1.0;
        if (powerRight > 1.0) powerRight = 1.0;
        if (powerRight < -1.0) powerRight = -1.0;
        swervedrive.setCrab(angle, powerLeft, powerRight);
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
