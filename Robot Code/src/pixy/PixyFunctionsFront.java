package pixy;

import org.usfirst.frc.team5199.robot.Robot;

import maths.Vector2;
import sensors.Sensors;

public class PixyFunctionsFront {
	static Vector2 targetGear;

	public PixyFunctionsFront() {
		this.targetGear = new Vector2(0,0);
	}

	public static void turnAndGoStraightAuton() {
		double distance = Robot.sensors.pixyGearXPosCompensated();
		targetGear.setX(distance);
	}
	public Vector2 getTarget() {
		turnAndGoStraightAuton();
		return targetGear;
	}
}
