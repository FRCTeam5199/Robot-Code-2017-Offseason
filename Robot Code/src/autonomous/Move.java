package autonomous;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import drive.DriveBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import interfaces.AutFunction;

public class Move implements AutFunction {

	private final DriveBase driveBase;
	private final Encoder rightEncoder;
	private final Encoder leftEncoder;
	private final ADXRS450_Gyro gyro;

	public boolean isDone = false;

	// tuned: no touchy
	private double P = 0.07d, I = 0.0001d, D = 0.02d;
	private double pTurn = 0.07, iTurn = 0.0000002, dTurn = 0.02;
	private double lOffset, rOffset;

	private double currentTravelDist, errorRate, integral;
	private double turnErrorRate, turnIntegral;
	private double maxAcceptableDistError = 1d; // The robot can be x inches off
												// from
	// target and it will be okay.
	private double maxAcceptableRateError = 3d; // The robot can be moving at x
	// inches/sec and it will be okay.
	private double inchesToMove;
	private double angle;
	private double distError; // The difference between the inchesToMove and the
								// average
	// of
	// rightWheelTravelDist and leftWheelTravelDist

	/**
	 * Will move the robot forward by inches specified in inchesToMove.
	 * 
	 * @param driveBase
	 *            Provide a DriveBase object so the robot can move.
	 * @param inchesToMove
	 *            How many inches you want the robot to move.
	 */
	public Move(DriveBase driveBase, int inchesToMove) {
		this.driveBase = driveBase;
		this.rightEncoder = Robot.sensors.getRightWheelEncoder();
		this.leftEncoder = Robot.sensors.getLeftWheelEncoder();
		this.gyro = Robot.sensors.getGyro();
		this.inchesToMove = inchesToMove;

	}

	@Override
	public void update(long deltaTime) {
		// Get the new travel distance from the average travel distance of the
		// left and
		// right motors.

		currentTravelDist = getDistance();

		// Positive distError is not reached target, Negative is overshot
		// target.
		distError = inchesToMove - currentTravelDist;

		// ????????????????????????
		integral += distError * deltaTime;
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

		// Robot.nBroadcaster.println(distError + "\t" + (distError -
		// lastError));
		// Robot.nBroadcaster.println(errorRate);

		Robot.nBroadcaster.println(distError + "\t" + errorRate + "\t" + I * integral);

		double turnError = angle - gyro.getAngle();
		double turnSpeed = 0;
		turnIntegral += turnError;

		turnSpeed += turnError * pTurn;
		turnSpeed -= gyro.getRate() * dTurn;
		turnSpeed += turnIntegral * iTurn;

		driveBase.moveArcade(motorSpeed, turnSpeed);

		// If the robot is within an acceptable range of the currentTravelDist.
		if (Math.abs(distError) <= maxAcceptableDistError && Math.abs(errorRate) <= maxAcceptableRateError) {
			isDone = true;
		}
	}

	private double getDistance() {
		return (getRightDistance() + getLeftDistance()) / 2;
	}

	private double getRightDistance() {
		return rightEncoder.getDistance() - rOffset;
	}

	private double getLeftDistance() {
		return leftEncoder.getDistance() - lOffset;
	}

	public void init() {
		// Reset the distance to zero.
		rOffset = rightEncoder.getDistance();
		lOffset = leftEncoder.getDistance();

		// get the angle to maintain
		angle = gyro.getAngle();
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

}