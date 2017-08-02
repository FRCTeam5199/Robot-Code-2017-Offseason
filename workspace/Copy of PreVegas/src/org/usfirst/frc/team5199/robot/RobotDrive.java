package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;

public class RobotDrive {
	public static Spark rightMotor;
	public static Spark leftMotor;

	public RobotDrive(Spark rightMotor2, Spark leftMotor2) {
		rightMotor = rightMotor2;
		leftMotor = leftMotor2;
	}

	// this is the routine that we expect to used to drive the bot in manual
	// mode.
	public static void drive(double Y, double X, double driveMod) {
		rightMotor.set((Y + X) * driveMod);
		leftMotor.set((((Y * -1) + X) * driveMod));
	}
	// May be useful for auton or gyro turning.
	//DOES NOT TURN AROUND CENTER OF ROBOT USE ENCODER TURN INSTEAD
	public static void deadTurn(double X, double driveMod) {
		leftMotor.set(driveMod * (X) * 1.2);
		rightMotor.set(driveMod * X);

	}
	// Useful for autonomous.
	public static void stop() {
		leftMotor.set(0);
		rightMotor.set(0);
	}
}
