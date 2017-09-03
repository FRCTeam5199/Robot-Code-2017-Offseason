package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class Sensors {
	private final ADXRS450_Gyro gyro;
	private final BuiltInAccelerometer accelerometer;

	public Sensors() {
		gyro = new ADXRS450_Gyro();
		Robot.nBroadcaster.println("Calibrating Gyro...");
		gyro.calibrate();
		Robot.nBroadcaster.println("Done!");

		accelerometer = new BuiltInAccelerometer(Range.k8G);
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public BuiltInAccelerometer getAccelerometer() {
		return accelerometer;
	}
}
