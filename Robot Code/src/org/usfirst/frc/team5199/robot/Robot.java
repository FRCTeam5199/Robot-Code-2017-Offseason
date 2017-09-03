package org.usfirst.frc.team5199.robot;

import autonomous.AutonomousFunctions;
import autonomous.AutonomousManager;
import autonomous.Stop;
import autonomous.Turn;
import controllers.JoystickController;
import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.SampleRobot;
import intake.Intake;
import intake.IntakeControl;
import maths.Vector2;
import networking.RemoteOutput;
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

	private ClockRegulator clockRegulator;

	private DriveBase base;
	private Turret turret;
	private Intake intake;

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
		
		sensors = new Sensors();

		clockRegulator = new ClockRegulator(150);

		controller = new XBoxController(0);
		joystick = new JoystickController(1);

		base = new DriveBase();
		turret = new Turret();
		intake = new Intake();

		driveControl = new DriveControl(base, controller);
		turretControl = new TurretControl(turret, joystick, Vector2.ZERO.clone());
		intakeControl = new IntakeControl(intake, joystick, controller);

	}

	@Override
	public void autonomous() {
		sensors.getGyro().reset();
		AutonomousManager autManager = new AutonomousManager(clockRegulator);

		autManager.add(new Turn(base, 180));
		autManager.add(new Turn(base, 0));
		autManager.add(new Turn(base, 90));
		autManager.add(new Turn(base, 270));
		autManager.add(new Turn(base, 0));
		autManager.add(new Stop(base, turret, intake));

		while (isAutonomous() && isEnabled()) {
			autManager.update();
		}

	}

	@Override
	public void operatorControl() {
		Robot.nBroadcaster.println("\nStarting TeleOp");

		sensors.getGyro().reset();

		MainLoop mainLoop = new MainLoop(clockRegulator);

		mainLoop.add(driveControl);
		mainLoop.add(turretControl);
		mainLoop.add(intakeControl);

		mainLoop.init();

		while (isOperatorControl() && isEnabled()) {
			mainLoop.update();
		}

	}

	@Override
	public void test() {

		sensors.getGyro().reset();

		while (this.isTest() && this.isEnabled()) {
			Robot.nBroadcaster.println(sensors.getGyro().getRate());
		}
	}
}
