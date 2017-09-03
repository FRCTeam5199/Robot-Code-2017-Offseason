package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import intake.Intake;
import turret.Turret;

public class AutonomousFunctions {
	private final DriveBase base;
	private final Turret turret;
	private final Intake intake;
	private final ADXRS450_Gyro gyro;

	public AutonomousFunctions(DriveBase base, Turret turret, Intake intake) {
		this.base = base;
		this.turret = turret;
		this.intake = intake;
		gyro = Robot.sensors.getGyro();

	}

	public void turnTo(double angle) {
		double p = 0.04;
		double i = 0.0001;
		double d = 0.005;
		double acceptRange = 3;
		double acceptRangeRate = 3;
		double integral = 0;

		while (Math.abs(gyro.getAngle() - angle) < acceptRange && Math.abs(gyro.getRate()) < acceptRangeRate) {
			
			if (angle < 0) {
				angle += 360;
			}

			double curretAngle = gyro.getAngle();

			// normalize angle
			angle = angle - (int) (angle / 360) * 360;

			double error = angle - curretAngle;
			
			Robot.nBroadcaster.println(error);

			// check if there is a faster way to get to the target by crossing the 0
			// - 360 degrees jump thing
			if (Math.abs(error - 360) < Math.abs(error)) {
				error -= 360;
			} else if (Math.abs(error + 360) < Math.abs(error)) {
				error += 360;
			}

			integral += error;

			double turnSpeed = error * p - gyro.getRate() * d + integral * i;

			base.move(-turnSpeed, turnSpeed);
		}
	}
}
