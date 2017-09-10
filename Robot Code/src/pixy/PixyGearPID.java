package pixy;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import maths.Vector2;

public class PixyGearPID {
	private Vector2 target, lastTarget;
	private double p = .003;
	private double i = .0001;
	private double d = .05;
	private double integral = 0;
	private DriveBase base;

	public PixyGearPID(Vector2 target, DriveBase base) {
		this.target = target;
		lastTarget = target.clone();
		this.base = base;
	}

	public void pixyGear() {
		double motorSpeed;
		integral += target.getX();
		if (Math.abs(integral) > 1 / i) {
			if (integral > 0) {
				integral = 1 / i;
			} else {
				integral = -1 / i;
			}
		}
		motorSpeed = p * target.getX();
		Robot.nBroadcaster.println(d * (target.getX() - lastTarget.getX()));
		motorSpeed += d * (target.getX() - lastTarget.getX());
		motorSpeed += i * integral;
		base.moveArcade(.4, motorSpeed);
		lastTarget = target.clone();
	}
}
