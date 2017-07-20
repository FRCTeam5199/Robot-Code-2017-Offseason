package Drive;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class DriveControl {
	private final DriveBase base;
	private final ADXRS450_Gyro gyro;
	// private final JoystickController joystick;
	private final double speed = .7;
	private final double rSpeed = 400;
	private final XBoxController controller;

	public DriveControl(XBoxController controller) {
		base = new DriveBase();
		gyro = new ADXRS450_Gyro();
		this.controller = controller;

		gyro.reset();
		gyro.calibrate();
		// joystick = new JoystickController(0);
	}

	public void update() {
		// tankControl();
		tankControlAssisted();

		// arcadeControlAssisted();
	}

	public void tankControl() {
		double speedMultiplier = speed;
		double right = controller.getStickRY();
		double left = controller.getStickLY();

		// Right trigger boost
		speedMultiplier += (1 - speed) * controller.getRTrigger();
		right *= speedMultiplier;
		left *= speedMultiplier;

		// Left trigger straighten
		double avg = (right + left) / 2;
		double lTrigger = controller.getLTrigger();
		double notLTrigger = 1 - lTrigger;
		right = notLTrigger * right + avg * lTrigger;
		left = notLTrigger * left + avg * lTrigger;

		base.move(right, left);
		// Robot.nBroadcaster.println(gyro.getRate());
	}

	public void tankControlAssisted() {
		double speedMultiplier = speed;
		double right = controller.getStickRY();
		double left = controller.getStickLY();

		// Right trigger boost
		speedMultiplier += (1 - speed) * controller.getRTrigger();
		right *= speedMultiplier;
		left *= speedMultiplier;

		// Assists
		double avg = (right + left) / 2;
		double targetSpeed = (left - right) * 400;

		double error = targetSpeed - gyro.getRate();
		double turnSpeed = error * .01;

		base.move(left - turnSpeed, right + turnSpeed);

		//Robot.nBroadcaster.println(left - turnSpeed + "\t" + right + turnSpeed);
		// Robot.nBroadcaster.println(gyro.getRate());
	}

	public void arcadeControlAssisted() {
		double targetSpeed = controller.getStickRX() * rSpeed;
		double currentSpeed = gyro.getRate();
		double turnSpeed = (targetSpeed - currentSpeed) * .01;
		// Robot.nBroadcaster.println(gyro.getRate());
		base.move(controller.getStickLY() - turnSpeed, controller.getStickLY() + turnSpeed);
	}
}
