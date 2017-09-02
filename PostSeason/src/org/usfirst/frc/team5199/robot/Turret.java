package org.usfirst.frc.team5199.robot;

import org.usfirst.frc.team5199.robot.RobotMap;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;

public class Turret {
	private final CANTalon turnMotor;
	private static CANTalon flyWheelMotor;
	private static Encoder encoder;

	public Turret() {
		turnMotor = new CANTalon(RobotMap.turret);
		flyWheelMotor = new CANTalon(RobotMap.shooter);
		encoder = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		encoder.reset();
		// encoder.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
		encoder.setPIDSourceType(PIDSourceType.kRate);
		encoder.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
	}

	public void setTurret(double n) {
		turnMotor.set(n);
	}

	public static void setFlyWheel(double n) {
		flyWheelMotor.set(n);
	}
	public static CANTalon getShooterMotor() {
		return flyWheelMotor;
	}
	public static double getFlyWheelRPM() {
		return encoder.getRate();
	}

	public static Encoder getEncoder() {
		return encoder;
	}
}
