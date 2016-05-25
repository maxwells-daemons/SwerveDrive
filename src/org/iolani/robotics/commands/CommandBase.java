package org.iolani.robotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.iolani.robotics.OI;
import org.iolani.robotics.hardware.SabertoothSpeedController;
import org.iolani.robotics.subsystems.SwerveDrive;

/**
 *
 * @author Aidan
 */
public abstract class CommandBase extends Command {
    public static OI oi;
    
    public static final SwerveDrive swervedrive = new SwerveDrive();
    
    public static void init() {
        oi = new OI();
        
        // Initialize subsystems //
        swervedrive.init();
        
        System.out.println("Initialization successful!");
    }
    
    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
