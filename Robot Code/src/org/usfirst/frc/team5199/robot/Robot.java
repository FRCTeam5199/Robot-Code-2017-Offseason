package org.usfirst.frc.team5199.robot;

import autonomous.*;
import controllers.*;
import dashboard.DriverCamera;
import sensors.Sensors;
import drive.DriveBase;
import drive.DriveControl;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import intake.Intake;
import intake.IntakeControl;
import transport.Transport;
import transport.TransportControl;
import turret.Turret;
import turret.TurretControl;
import climber.Climber;
import climber.ClimberControl;

import util.ClockRegulator;
import networking.RemoteOutput;

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
	public static SmartDashboard dashboard;
	public static Sensors sensors;

	private ClockRegulator clockRegulator;

	private XBoxController controller;
	private JoystickController joystick;
	private AutController autController;

	private DriveBase base;
	private Turret turret;
	private Intake intake;
	private Transport transport;
	private Climber climber;

	private DriveControl driveControl;
	private TurretControl turretControl;
	private IntakeControl intakeControl;
	private TransportControl transportControl;
	private ClimberControl climberControl;

	private UsbCamera camera;

	public Robot() {

	}

	@Override
	public void robotInit() {
		nBroadcaster = new RemoteOutput("10.51.99.197", 1180);
		// set first parameter in RemoteOutput constructor to your computer's
		// local address. (ex: "10.51.99.197")
		// currently working on getting this to work without it

		sensors = new Sensors();

		new DriverCamera(camera);
		new Compressor();

		clockRegulator = new ClockRegulator(100);

		controller = new XBoxController(0);
		joystick = new JoystickController(1);
		autController = new AutController(2);

		base = new DriveBase();
		turret = new Turret();
		intake = new Intake();
		transport = new Transport();
		climber = new Climber();

		driveControl = new DriveControl(base, controller);
		turretControl = new TurretControl(turret, joystick);
		intakeControl = new IntakeControl(intake, joystick, controller);
		transportControl = new TransportControl(transport, joystick);
		climberControl = new ClimberControl(climber, joystick);

		Robot.dashboard.putNumber("Turn P", .07);
		Robot.dashboard.putNumber("Turn I", .0000002);
		Robot.dashboard.putNumber("Turn D", .02);
		Robot.dashboard.putNumber("Turn tolerance", 6);
	}

	@Override
	public void autonomous() {
		sensors.getGyro().reset();
		AutonomousManager autManager = new AutonomousManager(base, turret, intake, climber, clockRegulator);

		autManager.add(new Stop(base, turret, intake, climber));

		switch (autController.getAutMode()) {

		case 1:
			autManager.add(new Move(base, 81));
			autManager.add(new Turn(base, -60));
			autManager.add(new PixyForward(driveControl));
			autManager.add(new ShootPrep(turretControl, 3375, turret));
			autManager.add(new Shoot(turretControl, turret, intake, transport, 3375));
			break;

		case 2:
			autManager.add(new Move(base, 81));
			autManager.add(new Turn(base, -60));
			autManager.add(new PixyForward(driveControl));
			// autManager.add(new MoveUltra(base, 4));
			break;

		case 3:
			autManager.add(new PixyForward(driveControl));
			autManager.add(new ShootPrep(turretControl, 3575, turret));
			autManager.add(new Shoot(turretControl, turret, intake, transport, 3575));
			break;

		case 4:
			autManager.add(new Move(base, 81));
			autManager.add(new Turn(base, 60));
			autManager.add(new PixyForward(driveControl));
			// autManager.add(new MoveUltra(base, 4));
			break;

		case 5:
			autManager.add(new Move(base, 81));
			autManager.add(new Turn(base, 60));
			autManager.add(new PixyForward(driveControl));
			autManager.add(new ShootPrep(turretControl, 3375, turret));
			autManager.add(new Shoot(turretControl, turret, intake, transport, 3375));
			break;

		case 6:
			// autManager.add(new Move(base, 80));
			autManager.add(new Turn(base, 60));
			break;
		}

		autManager.init();

		while (isAutonomous() && isEnabled() && !autManager.isDone()) {
			autManager.update();
		}

		Robot.nBroadcaster.println("End of autonomous");
	}

	@Override
	public void operatorControl() {
		Robot.nBroadcaster.println("\nStarting TeleOp");

		sensors.getGyro().reset();

		MainLoop mainLoop = new MainLoop(clockRegulator);

		mainLoop.add(driveControl);
		mainLoop.add(turretControl);
		mainLoop.add(intakeControl);
		mainLoop.add(transportControl);
		mainLoop.add(climberControl);

		mainLoop.init();

		while (isOperatorControl() && isEnabled()) {
			mainLoop.update();
		}

	}

	@Override
	public void test() {
		turret.zeroTurret();
		sensors.getGyro().reset();

		byte colorCount = 0;

		// DoubleSolenoid solenoid = new DoubleSolenoid(4, 5);
		// Solenoid r = new Solenoid(1);
		// Solenoid g = new Solenoid(2);
		// Solenoid b = new Solenoid(3);

		ClockRegulator testRegulator = new ClockRegulator(2);

		while (this.isTest() && this.isEnabled()) {
			turretControl.setRPM(3375);

			// Robot.nBroadcaster.println(
			// sensors.getLeftWheelEncoder().getDistance() + "\t" +
			// sensors.getRightWheelEncoder().getDistance());

			// r.set((colorCount & 0x01) != 0);
			// g.set((colorCount & 0x02) != 0);
			// b.set((colorCount & 0x04) != 0);
			//
			// if (colorCount > 7) {
			// colorCount = 0;
			// }
			//
			// colorCount++;
			//
			// testRegulator.sync();
			clockRegulator.sync();
		}
	}
}
