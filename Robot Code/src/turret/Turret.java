package turret;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;

public class Turret {
	private final CANTalon turnMotor;
	private final CANTalon flyWheelMotor;
	private final Encoder encoder;

	public Turret() {
		turnMotor = new CANTalon(RobotMap.turret);
		flyWheelMotor = new CANTalon(RobotMap.shooter);
		encoder = Robot.sensors.getFlywheelEncoder();
	}

	public void setTurret(double n) {
		turnMotor.set(n);
	}

	public void setFlyWheel(double n) {
		flyWheelMotor.set(n);
	}

	public double getFlyWheelRPM() {
		return encoder.getRate();
	}

	public Encoder getEncoder() {
		return encoder;
	}
}
