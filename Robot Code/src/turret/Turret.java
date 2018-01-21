package turret;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import util.TalonSRXWatchdog;

public class Turret {
	private final TalonSRX turnMotor;
	private final TalonSRX flyWheelMotor;
	private final TalonSRXWatchdog turnWatchdog;
	private final TalonSRXWatchdog flyWheelWatchdog;
	private final Encoder encoder;

	private final double encoderToDegrees = 45d / 35087.5d;

	private double offset = 0;

	public Turret() {
		turnMotor = new TalonSRX(RobotMap.turret);
		flyWheelMotor = new TalonSRX(RobotMap.shooter);
		encoder = Robot.sensors.getFlywheelEncoder();

		turnMotor.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
		flyWheelMotor.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Coast);

		turnMotor.configSelectedFeedbackSensor(com.ctre.phoenix.motorcontrol.FeedbackDevice.QuadEncoder, 0, 0);
		turnMotor.setSensorPhase(false);

		turnWatchdog = new TalonSRXWatchdog(turnMotor, 20, 500, 1000);
		turnWatchdog.start();

		flyWheelWatchdog = new TalonSRXWatchdog(flyWheelMotor, 80, 250, 2500);
		flyWheelWatchdog.start();
	}

	public void setTurret(double n) {
		// Robot.nBroadcaster.println("Disabled turret. Speed: " + n);
		if (turnWatchdog.isOk()) {
			turnMotor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, n);
		} else {
			turnMotor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, 0);
			Robot.nBroadcaster.println("Error: Turret motor stalled");
		}
	}

	private double getTurretPosition() {
		return turnMotor.getSelectedSensorPosition(0) - offset;
		// return turnMotor.getPosition() - offset;
	}

	public double getTurretAngle() {
		return getTurretPosition() * encoderToDegrees;
	}

	public void setFlyWheel(double n) {
		// Robot.nBroadcaster.println("Disabled flywheel. Speed: " + n);
		if (n > 0 && flyWheelWatchdog.isOk()) {
			flyWheelMotor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, n);
		} else {
			flyWheelMotor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, 0);
		}
	}

	public double getFlyWheelPosition() {
		return encoder.getDistance();
	}

	public double getFlyWheelRPS() {
		return encoder.getRate();
	}

	public double getFlyWheelRPM() {
		return getFlyWheelRPS() * 60;
	}

	public Encoder getEncoder() {
		return encoder;
	}

	public void zeroTurret() {
		offset = turnMotor.getSelectedSensorPosition(0);
		// offset = turnMotor.getPosition();
	}
}
