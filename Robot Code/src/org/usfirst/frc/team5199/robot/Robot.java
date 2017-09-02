package org.usfirst.frc.team5199.robot;

import drive.DriveControl;
import drive.JoystickController;
import drive.XBoxController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SampleRobot;
import maths.Vector2;
import networking.RemoteOutput;
import networking.Vision;
import turret.TurretControl;

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
	private Vision vision;
	private XBoxController controller;
	private JoystickController joystick;
	private ADXRS450_Gyro gyro;
	private Vector2 target;
	private DriveControl driveControl;
	private TurretControl turretControl;

	public Robot() {

	}

	@Override
	public void robotInit() {
		nBroadcaster = new RemoteOutput("10.51.99.197", 1180);
		// set first parameter in RemoteOutput constructor to your computer's
		// local address. (ex: "10.51.99.197")
		// currently working on getting this to work without it

		gyro = new ADXRS450_Gyro();
		Robot.nBroadcaster.println("Calibrating Gyro...");
		gyro.calibrate();
		Robot.nBroadcaster.println("Done!");

		vision = new Vision();
		vision.start();
		controller = new XBoxController(0);
		joystick = new JoystickController(1);
		target = Vector2.ZERO.clone();
		driveControl = new DriveControl(controller, gyro);
		turretControl = new TurretControl(joystick, target);
	}

	@Override
	public void autonomous() {
		gyro.reset();

	}

	@Override
	public void operatorControl() {
		Robot.nBroadcaster.println("\n\n\n\n\nStarting TeleOp");

		gyro.reset();

		while (isOperatorControl() && isEnabled()) {
			Vector2 newTarget = vision.getPos();
			target.setX(newTarget.getX() - 320);
			// Robot.nBroadcaster.println(target);
			if (newTarget.getX() == 0) {
				target.setX(0);
			}

			driveControl.update();
			turretControl.update();

			try {
				Thread.sleep(1000 / 100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		vision.stop();
	}

	@Override
	public void test() {

		gyro.reset();
	}
}
