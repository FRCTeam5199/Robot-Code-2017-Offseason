package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class Sensors {
	private final ADXRS450_Gyro gyro;
	private final BuiltInAccelerometer accelerometer;
	private final Encoder flywheelEncoder;
	private final Encoder wheelsLeft;
	private final Encoder wheelsRight;

	public Sensors() {
		// init flywheel encoder
		flywheelEncoder = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		flywheelEncoder.reset();
		flywheelEncoder.setDistancePerPulse(RobotMap.rotationsPerStepShooter);

		// init wheel encoders
		wheelsRight = new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false,
				Encoder.EncodingType.k4X);
		wheelsRight.reset();
		wheelsRight.setDistancePerPulse(RobotMap.inchesPerRotation);

		wheelsLeft = new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false, Encoder.EncodingType.k4X);
		wheelsLeft.reset();
		wheelsLeft.setDistancePerPulse(RobotMap.inchesPerRotation);

		// init accelerometer
		accelerometer = new BuiltInAccelerometer(Range.k8G);

		// init gyro
		gyro = new ADXRS450_Gyro();
		Robot.nBroadcaster.println("Calibrating Gyro...");
		gyro.calibrate();
		Robot.nBroadcaster.println("Done!");

	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public BuiltInAccelerometer getAccelerometer() {
		return accelerometer;
	}

	public Encoder getFlywheelEncoder() {
		return flywheelEncoder;
	}

	public Encoder getLeftWheelEncoder() {
		return wheelsLeft;
	}

	public Encoder getRightWheelEncoder() {
		return wheelsRight;
	}
}
