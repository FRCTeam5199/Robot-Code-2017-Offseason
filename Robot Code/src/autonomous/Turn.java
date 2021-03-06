package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import interfaces.AutFunction;

public class Turn implements AutFunction {
	private final double angle;
	private final ADXRS450_Gyro gyro;
	private final DriveBase base;

	private double p = 0.07;
	private double i = 0.0000002;
	private double d = 0.02;
	private double acceptRange = 6;
	private final double acceptRangeRate = 2;

	private double turnIntegral;
	private boolean isDone;

	// Turns the robot to angle degrees
	public Turn(DriveBase base, double angle) {
		this.base = base;
		isDone = false;
		gyro = Robot.sensors.getGyro();

		while (angle < 0) {
			angle += 360;
		}

		// normalize angle
		angle = angle - (int) (angle / 360) * 360;

		this.angle = angle;

	}

	public void update(long deltaTime) {

		p = Robot.dashboard.getNumber("Turn P");
		i = Robot.dashboard.getNumber("Turn I");
		d = Robot.dashboard.getNumber("Turn D");
		acceptRange = Robot.dashboard.getNumber("Turn tolerance");

		double curretAngle = gyro.getAngle();

		double error = angle - curretAngle;

		Robot.nBroadcaster.println(error);

		// check if there is a faster way to get to the target by crossing the 0
		// - 360 degrees jump thing
		if (Math.abs(error - 360) < Math.abs(error)) {
			error -= 360;
		} else if (Math.abs(error + 360) < Math.abs(error)) {
			error += 360;
		}

		turnIntegral += error * deltaTime;

		double turnSpeed = error * p - gyro.getRate() * d + turnIntegral * i;

		base.move(-turnSpeed, turnSpeed);

		if (Math.abs(error) < acceptRange && Math.abs(gyro.getRate()) < acceptRangeRate) {
			isDone = true;
		}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void init() {
		// nothing to initialize
	}

}
