package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import interfaces.AutFunction;
import sensors.Sensors;

public class MoveToUltra implements AutFunction {

	private final DriveBase driveBase;

	public boolean isDone = false;

	final double P = 0.00d, I = 0.00d, D = 0.00d;
	double error, errorRate, integral;
	double maxAcceptableDistError = 1d; // The robot can be x cm off from
										// target and it will be okay.
	double maxAcceptableRateError = 0.01d; // The robot can be moving at x
											// cm/sec and it will be okay.
	double target;

	double lastPos = 0;

	/**
	 * Will move the robot so it is distance cm away from a wall
	 * 
	 * @param driveBase
	 *            Provide a DriveBase object so the robot can move.
	 * @param distance
	 *            How many cm you want the robot to be from a wall.
	 */
	public MoveToUltra(DriveBase driveBase, int distance) {
		this.driveBase = driveBase;
		target = distance;

	}

	@Override
	public void update(long deltaTime) {
		// Get the new travel distance from the average travel distance of the
		// left and
		// right motors.

		// Positive distError is not reached target, Negative is overshot
		// target.
		error = ultraAverage() - target;

		// ????????????????????????
		integral += error * deltaTime;
		if (Math.abs(integral) > 1 / I) {
			if (integral > 0) {
				integral = 1 / I;
			} else {
				integral = -1 / I;
			}
		}

		errorRate = (ultraAverage() - lastPos) / deltaTime;

		// Motor speed is recalculated every time this function is called.
		double motorSpeed = 0;
		motorSpeed += P * error;
		motorSpeed -= D * errorRate;
		motorSpeed += I * integral;

		// Robot.nBroadcaster.println(distError + "\t" + (distError -
		// lastError));
		// Robot.nBroadcaster.println(errorRate);

		Robot.nBroadcaster.println(P * error + "\t" + D * errorRate + "\t" + I * integral);
		driveBase.move(motorSpeed, motorSpeed);
		lastPos = ultraAverage();

		// If the robot is within an acceptable range of the currentTravelDist.
		if (Math.abs(error) <= maxAcceptableDistError && Math.abs(errorRate) <= maxAcceptableRateError) {
			isDone = true;
		}
	}

	public void init() {
		// Reset the distance to zero.
		lastPos = ultraAverage();
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	private double ultraAverage() {
		return (Sensors.ultraDistanceLeft() + Sensors.ultraDistanceRight()) / 2;
	}

}