package turret;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import sensors.CircularAverageBuffer;
import util.CANTalonWatchdog;

public class Turret {
	private final CANTalon turnMotor;
	private final CANTalon flyWheelMotor;
	private final CANTalonWatchdog turnWatchdog;
	private final CANTalonWatchdog flyWheelWatchdog;
	private final Encoder encoder;

	// private final CircularAverageBuffer rpsBuffer = new
	// CircularAverageBuffer(10);

	private final double encoderToDegrees = 45d / 35087.5d;

	private double offset = 0;

	public Turret() {
		turnMotor = new CANTalon(RobotMap.turret);
		flyWheelMotor = new CANTalon(RobotMap.shooter);
		encoder = Robot.sensors.getFlywheelEncoder();

		turnWatchdog = new CANTalonWatchdog(turnMotor, 20, 500, 1000);
		turnWatchdog.start();

		flyWheelWatchdog = new CANTalonWatchdog(flyWheelMotor, 80, 250, 2500);
		flyWheelWatchdog.start();
	}

	// public CANTalon getFlyWheelMotor() {
	// return flyWheelMotor;
	// }

	public void setTurret(double n) {
		if (turnWatchdog.isOk()) {
			Robot.nBroadcaster.println(turnMotor.getOutputCurrent());
			turnMotor.set(n);
		} else {
			turnMotor.set(0);
			Robot.nBroadcaster.println("Error: Turret motor stalled");
		}
	}

	private double getTurretPosition() {
		return turnMotor.getPosition() - offset;
	}

	public double getTurretAngle() {
		return getTurretPosition() * encoderToDegrees;
	}

	public void setFlyWheel(double n) {
		if (n > 0 && flyWheelWatchdog.isOk()) {
			flyWheelMotor.set(n);
		} else {
			flyWheelMotor.set(0);
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

	// public double getBufferedFlyWheelRPS() {
	// return rpsBuffer.DataAverage(getFlyWheelRPS());
	// }

	// public double getBufferedFlyWheelRPM() {
	// return getBufferedFlyWheelRPS() * 60;
	// }

	public Encoder getEncoder() {
		return encoder;
	}

	public void zeroTurret() {
		offset = turnMotor.getPosition();
	}
}
