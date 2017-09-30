package pixy;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import maths.Vector2;

public class PixyGearPID {
	private Vector2 target, lastTarget;
	private double p = .003;
	private double i = .000001;
	private double d = 5;
	private double integral = 0;
	private DriveBase base;
	PixyFunctionsFront pixyFuncFront;
	public PixyGearPID(DriveBase base) {
		this.target = new Vector2(0,0);
		lastTarget = target.clone();
		this.base = base;
		pixyFuncFront = new PixyFunctionsFront();
	}

	public void pixyGear(long deltaTime) {
		double motorSpeed;
		target = pixyFuncFront.getTarget();
		integral += target.getX()*deltaTime;
		if (Math.abs(integral) > 1 / i) {
			if (integral > 0) {
				integral = 1 / i;
			} else {
				integral = -1 / i;
			}
		}
		motorSpeed = p * target.getX();
		Robot.nBroadcaster.println(d * (target.getX() - lastTarget.getX()));
		motorSpeed += d * (target.getX() - lastTarget.getX())/deltaTime;
		motorSpeed += i * integral;
		base.moveArcade(.4, motorSpeed);
		lastTarget = target.clone();
	}
}
