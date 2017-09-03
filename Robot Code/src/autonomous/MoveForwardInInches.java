package autonomous;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import drive.DriveBase;
import interfaces.AutFunction;

public class MoveForwardInInches implements AutFunction {

	private final DriveBase driveBase;

	public boolean isDone = false;

	final double P = 0.001d, I = 0.0d, D = 0.0d;
	double currentTravelDist, lastTravelDist, integral;
	double maxAcceptableDistError = 2.0d; // The robot can be x inches off from target and it will be okay.
	double inchesToMove;
	double distError; // The difference between the inchesToMove and the average of
						// rightWheelTravelDist and leftWheelTravelDist

	/**
	 * Will move the robot forward by inches specified in inchesToMove.
	 * 
	 * @param driveBase
	 *            Provide a DriveBase object so the robot can move.
	 * @param inchesToMove
	 *            How many inches you want the robot to move.
	 */
	public MoveForwardInInches(DriveBase driveBase, int inchesToMove) {
		this.driveBase = driveBase;
		this.inchesToMove = inchesToMove;

		// Reset the distance to zero.
		Robot.sensors.getRightWheelEncoder().reset();
		Robot.sensors.getLeftWheelEncoder().reset();

		// Assuming 4 inch wheels.
		Robot.sensors.getRightWheelEncoder().setDistancePerPulse(RobotMap.inchesPerRotation);
		Robot.sensors.getLeftWheelEncoder().setDistancePerPulse(RobotMap.inchesPerRotation);
	}

	@Override
	public void update(long deltaTime) {

		// ????????????????????????
		integral += distError;
		if (Math.abs(integral) > 1 / I) {
			if (integral > 0) {
				integral = 1 / I;
			} else {
				integral = -1 / I;
			}
		}

		// Save how far the robot is from the start last time the function was called.
		lastTravelDist = currentTravelDist;

		// Get the new travel distance from the average travel distance of the left and
		// right motors.
		currentTravelDist = ((-Robot.sensors.getRightWheelEncoder().getDistance() / 120)
				+ (Robot.sensors.getLeftWheelEncoder().getDistance() / 120)) / 2;

		// Positive distError is not reached target, Negative is overshot target.
		distError = inchesToMove - currentTravelDist;

		// Motor speed is recalculated every time this function is called.
		double motorSpeed = 0;
		motorSpeed += P * distError;
		motorSpeed += D * (currentTravelDist - lastTravelDist);
		motorSpeed += I * integral;

		driveBase.move(motorSpeed, motorSpeed);

		// If the robot is within an acceptable range of the currentTravelDist.
		if (Math.abs(distError) <= maxAcceptableDistError) {
			isDone = true;
		}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

}