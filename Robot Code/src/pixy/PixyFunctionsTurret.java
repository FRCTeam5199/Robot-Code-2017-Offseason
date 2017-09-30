package pixy;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import com.ctre.CANTalon;

import drive.DriveBase;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import maths.Vector2;
import sensors.Sensors;
import turret.TurretControl;

public class PixyFunctionsTurret {
	public static Vector2 targetShooter;

	public PixyFunctionsTurret() {
		targetShooter = new Vector2(0, 0);
	}

	public static void alignShooterX() {
		// checks to see if the turret is lined up with the boiler
		// if it is not aligned, turret centers on target
		if (Sensors.pixyShooterXPos() != -1) {
			double distance = Robot.sensors.pixyShooterXPos();
			double distanceOff = distance - 135;
			targetShooter.setX(distanceOff);
			Robot.nBroadcaster.println(targetShooter.getX());
			// Subtract less pixels to go to the left
			// Alter subtraction value to change left or right alignment

		}
	}

	public static void alignShooterXCenter() {
		// checks to see if the turret is lined up with the boiler
		// if it is not aligned, turret centers on target
		if (Sensors.pixyShooterXPos() != -1) {
			double distance = Robot.sensors.pixyShooterXPos();
			double distanceOff = distance - 130;
			targetShooter.setX(distanceOff);

			// Subtract less pixels to go to the left
			// Alter subtraction value to change left or right alignment
		}
	}

	public static Vector2 getTarget() {
		alignShooterX();
		return targetShooter;
	}
}
