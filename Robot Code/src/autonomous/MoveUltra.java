package autonomous;

import drive.DriveBase;
import interfaces.AutFunction;
import sensors.Sensors;

public class MoveUltra implements AutFunction {

	private final MoveToUltra mtu;

	/**
	 * Will move the robot forward so it is distance cm away from a wall
	 * 
	 * @param driveBase
	 *            Provide a DriveBase object so the robot can move.
	 * @param distance
	 *            Target distance from an obstacle
	 */
	public MoveUltra(DriveBase driveBase, int distance) {
		mtu = new MoveToUltra(driveBase, (int) (ultraAverage()) - distance);

	}

	@Override
	public void update(long deltaTime) {
		mtu.update(deltaTime);
	}

	public void init() {
		// Reset the distance to zero.
		mtu.init();
	}

	private double ultraAverage() {
		return (Sensors.ultraDistanceLeft() + Sensors.ultraDistanceRight()) / 2;
	}

	@Override
	public boolean isDone() {
		return mtu.isDone();
	}

}