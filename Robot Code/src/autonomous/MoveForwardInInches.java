package autonomous;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import drive.DriveBase;
import edu.wpi.first.wpilibj.Encoder;
import interfaces.AutFunction;

public class MoveForwardInInches implements AutFunction {

	private final DriveBase driveBase;
	private final Encoder rightEncoder;
	private final Encoder leftEncoder;

	public boolean isDone = false;

	final double P = 0.05d, I = 0.00015d, D = 0.01d;
	double currentTravelDist, errorRate, integral;
	double maxAcceptableDistError = 1d; // The robot can be x inches off from target and it will be okay.
	double maxAcceptableRateError = 3d; // The robot can be moving at x inches/sec and it will be okay.
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
		this.rightEncoder = Robot.sensors.getRightWheelEncoder();
		this.leftEncoder = Robot.sensors.getLeftWheelEncoder();
		this.inchesToMove = inchesToMove;

	}

	@Override
	public void update(long deltaTime) {
		// Get the new travel distance from the average travel distance of the left and
		// right motors.

		// -------------Left encoder is not working atm
		// currentTravelDist = (Robot.sensors.getRightWheelEncoder().getDistance()
		// + Robot.sensors.getLeftWheelEncoder().getDistance()) / 2;
		currentTravelDist = Robot.sensors.getRightWheelEncoder().getDistance();

		// Positive distError is not reached target, Negative is overshot target.
		distError = inchesToMove - currentTravelDist;

		// ????????????????????????
		integral += distError;
		if (Math.abs(integral) > 1 / I) {
			if (integral > 0) {
				integral = 1 / I;
			} else {
				integral = -1 / I;
			}
		}

		// errorRate = (rightEncoder.getRate() + leftEncoder.getRate())/2;
		errorRate = rightEncoder.getRate();

		// Motor speed is recalculated every time this function is called.
		double motorSpeed = 0;
		motorSpeed += P * distError;
		motorSpeed -= D * errorRate;
		motorSpeed += I * integral;

		// Robot.nBroadcaster.println(distError + "\t" + (distError - lastError));
		// Robot.nBroadcaster.println(errorRate);

		Robot.nBroadcaster.println(distError + "\t" + errorRate + "\t" + I * integral);
		driveBase.move(motorSpeed, motorSpeed);

		// If the robot is within an acceptable range of the currentTravelDist.
		if (Math.abs(distError) <= maxAcceptableDistError && Math.abs(errorRate) <= maxAcceptableRateError) {
			isDone = true;
		}
	}

	public void init() {
		// Reset the distance to zero.
		Robot.sensors.getRightWheelEncoder().reset();
		Robot.sensors.getLeftWheelEncoder().reset();
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

}