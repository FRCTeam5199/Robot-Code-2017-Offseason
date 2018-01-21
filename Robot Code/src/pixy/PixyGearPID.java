package pixy;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import maths.Vector2;

public class PixyGearPID {
	private Vector2 target, lastTarget;
	
	//tuned: no touchy
	private double p = .012;
	private double i = 0;
	private double d = 1;
	
	private double integral = 0;
	private DriveBase base;
	PixyFunctionsFront pixyFuncFront;
	public PixyGearPID(DriveBase base) {
		this.target = new Vector2(0,0);
		lastTarget = target.clone();
		this.base = base;
		pixyFuncFront = new PixyFunctionsFront();
		Robot.dashboard.putNumber("Flywheel rip-ems", 0d);
		Robot.dashboard.putNumber("Pixy P", p);
		Robot.dashboard.putNumber("Pixy I", i);
		Robot.dashboard.putNumber("Pixy D", d);
		
	}

	public void pixyGear(long deltaTime) {
		p = Robot.dashboard.getNumber("Pixy P", p);
		i = Robot.dashboard.getNumber("Pixy I", i);
		d = Robot.dashboard.getNumber("Pixy D", d);
		Robot.dashboard.putNumber("Pixy Error", pixyFuncFront.getTarget().getX());
		
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
		
		Robot.dashboard.putNumber("Gear PID error", target.getX());
		
		motorSpeed = p * target.getX();
		motorSpeed += d * (target.getX() - lastTarget.getX())/deltaTime;
		motorSpeed += i * integral;
		base.moveArcade(.4, motorSpeed);
		lastTarget = target.clone();
	}
}
