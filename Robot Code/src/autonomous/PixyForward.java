package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveControl;
import interfaces.AutFunction;

public class PixyForward implements AutFunction {
	DriveControl driveControl;
	boolean isDone = false;

	public PixyForward(DriveControl driveControl) {
		this.driveControl = driveControl;
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub
		driveControl.PixyGearAlign(deltaTime);
		if (Robot.sensors.ultraDistanceLeft() < 10) {
			isDone = true;
		}

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}

}
