package org.usfirst.frc.team166.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team166.robot.commands.Disable;
import org.usfirst.frc.team166.robot.commands.Autonomous.BaseLineAutonomous;
import org.usfirst.frc.team166.robot.commands.Autonomous.BoilerSideBlueAuto;
import org.usfirst.frc.team166.robot.commands.Autonomous.BoilerSideRedAuto;
import org.usfirst.frc.team166.robot.commands.Autonomous.CenterGearAutonomous;
import org.usfirst.frc.team166.robot.commands.Shooter.RunShooter;
import org.usfirst.frc.team166.robot.subsystems.Climber;
import org.usfirst.frc.team166.robot.subsystems.Drive;
import org.usfirst.frc.team166.robot.subsystems.Elevator;
import org.usfirst.frc.team166.robot.subsystems.GearManipulator;
import org.usfirst.frc.team166.robot.subsystems.Intake;
import org.usfirst.frc.team166.robot.subsystems.LiveUsbCamera;
import org.usfirst.frc.team166.robot.subsystems.Shooter;
import org.usfirst.frc.team166.robot.subsystems.Storage;
import org.usfirst.frc.team166.robot.subsystems.Vision;
import org.usfirst.frc.team166.robot.subsystems.XboxLeftTrigger;
import org.usfirst.frc.team166.robot.subsystems.XboxRightTrigger;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as
 * described in the IterativeRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {

	public static Drive drive;
	public static GearManipulator gearManipulator;
	public static Intake intake;
	public static Shooter shooter;
	public static Storage storage;
	public static Climber climber;
	public static Elevator elevator;
	public static Vision vision;
	public static LiveUsbCamera visionProcessing;
	public static OI oi;

	private XboxLeftTrigger xboxLeftTrigger = new XboxLeftTrigger();
	private XboxRightTrigger xboxRightTrigger = new XboxRightTrigger();

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be used for any initialization code.
	 */
	@Override
	public void robotInit() {
		drive = new Drive();
		gearManipulator = new GearManipulator();
		intake = new Intake();
		shooter = new Shooter();
		storage = new Storage();
		climber = new Climber();
		elevator = new Elevator();
		vision = new Vision();
		visionProcessing = new LiveUsbCamera();
		oi = new OI();
		Robot.gearManipulator.gearManipulatorUp();
		chooser.addDefault("Center Gear Auto", new CenterGearAutonomous());
		chooser.addObject("Base Line Autonomous", new BaseLineAutonomous());
		chooser.addObject("Boiler side Blue auto", new BoilerSideBlueAuto());
		chooser.addObject("Boiler side Red auto", new BoilerSideRedAuto());
		chooser.addObject("Do Nothing Autonomous", null);

		double speed = Preferences.getInstance().getDouble(RobotMap.centerGearAutoSpeed, 0);
		double distance = Preferences.getInstance().getDouble(RobotMap.centerGearAutoDistance, 0);

		SmartDashboard.putData("Auto Mode", chooser);
		// xboxLeftTrigger.whileActive(new ClimberTriggerOn());
		xboxRightTrigger.whileActive(new RunShooter());

		visionProcessing.runUsbCamera();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You can use it to reset any subsystem
	 * information you want to clear when the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		new Disable().start();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes using
	 * the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the auto name from the text box below the
	 * Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented
	 * example) or additional comparisons to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector", "Default"); switch(autoSelected) { case
		 * "My Auto": autonomousCommand = new MyAutoCommand(); break; case "Default Auto": default: autonomousCommand =
		 * new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		new Disable().start();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
