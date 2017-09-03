package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import intake.Intake;
import turret.Turret;
import util.ClockRegulator;

public class AutonomousFunctions {
	private final DriveBase base;
	private final Turret turret;
	private final Intake intake;
	private final ADXRS450_Gyro gyro;
	private final ClockRegulator clockRegulator;

	private boolean status;
	private double turnIntegral;

	public AutonomousFunctions(ClockRegulator clockRegulator, DriveBase base, Turret turret, Intake intake) {
		this.clockRegulator = clockRegulator;
		this.base = base;
		this.turret = turret;
		this.intake = intake;
		gyro = Robot.sensors.getGyro();
		turnIntegral = 0;
		status = false;
	}

	public boolean getStatus() {
		return status;
	}

	
	private void done() {
		status = true;
	}

}
