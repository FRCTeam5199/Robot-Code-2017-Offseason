package org.usfirst.frc.team5199.robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;

public class DriveBase {
	private Spark left;
	private Spark right;

	public DriveBase() {
		left = new Spark(RobotMap.leftMotor);
		right = new Spark(RobotMap.rightMotor);
	}

	public void move(double r, double l) {
		//right is reversed
		left.set(l);
		right.set(-r);
		//Robot.nBroadcaster.println("left: " + l + "\tright: " + -r);
	}
}
