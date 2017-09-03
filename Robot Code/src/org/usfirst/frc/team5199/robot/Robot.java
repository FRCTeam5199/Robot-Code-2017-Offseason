package org.usfirst.frc.team5199.robot;

import controllers.JoystickController;
import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SampleRobot;
import intake.Intake;
import intake.IntakeControl;
import maths.Vector2;
import networking.RemoteOutput;
import networking.Vision;
import turret.Turret;
import turret.TurretControl;
import util.ClockRegulator;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {

	public static RemoteOutput nBroadcaster;

	public static Sensors sensors;
	private DriveBase base;
	private Turret turret;
	private Intake intake;

	private ClockRegulator clockRegulator;

	private XBoxController controller;
	private JoystickController joystick;

	private DriveControl driveControl;
	private TurretControl turretControl;
	private IntakeControl intakeControl;

	public Robot() {

	}

	@Override
	public void robotInit() {
		nBroadcaster = new RemoteOutput("10.51.99.197", 1180);
		// set first parameter in RemoteOutput constructor to your computer's
		// local address. (ex: "10.51.99.197")
		// currently working on getting this to work without it

		controller = new XBoxController(0);
		joystick = new JoystickController(1);

		driveControl = new DriveControl(base, controller);
		turretControl = new TurretControl(turret, joystick, null);
		intakeControl = new IntakeControl(intake, joystick, controller);

		clockRegulator = new ClockRegulator(100);
	}

	@Override
	public void autonomous() {
		sensors.getGyro().reset();
	}

	@Override
	public void operatorControl() {
		Robot.nBroadcaster.println("\nStarting TeleOp");

		sensors.getGyro().reset();
		clockRegulator.reset();

		MainLoop mainLoop = new MainLoop();

		mainLoop.add(driveControl);
		mainLoop.add(turretControl);
		mainLoop.add(intakeControl);
		
		mainLoop.init();

		while (isOperatorControl() && isEnabled()) {

			mainLoop.update();

			clockRegulator.sync();
		}

	}

	@Override
	public void test() {

		sensors.getGyro().reset();
		clockRegulator.reset();

		while (this.isTest() && this.isEnabled()) {
			Robot.nBroadcaster.println(sensors.getGyro().getRate());
			clockRegulator.sync();
		}
	}
}
