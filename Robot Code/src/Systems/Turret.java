package Systems;

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
		encoder = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		encoder.reset();
		// encoder.setDistancePerPulse(RobotMap.inchesPerRotationShooter);
		encoder.setDistancePerPulse(-6);
	}

	public void setTurret(double n) {
		turnMotor.set(n);
	}

	public void setFlyWheel(double n) {
		flyWheelMotor.set(n);
	}
	
	public double getFlyWheelRPM(){
		return encoder.getRate();
	}
}
