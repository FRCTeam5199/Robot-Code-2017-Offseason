package pixy;

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

public class PixyFunctions {
	public static Vector2 targetShooter, targetGear;

	public PixyFunctions(Vector2 vectorGear, Vector2 vectorShooter) {
		this.targetGear = vectorGear;
		this.targetShooter = vectorShooter;
	}

	public static void turnAndGoStraightAuton() {
		if (Sensors.pixyGearXPos() != -1) {
			double distance = Sensors.pixyGearXPosCompensated();
			double distanceOff = distance - 160;
			targetGear.setX(distanceOff);
		}
	}

	public static void alignShooterX() {
		// checks to see if the turret is lined up with the boiler
		// if it is not aligned, turret centers on target
		if (Sensors.pixyShooterXPos() != -1) {
			double distance = Sensors.pixyShooterXPos();
			double distanceOff = distance - 135;
			targetShooter.setX(distanceOff);
			
			// Subtract less pixels to go to the left
			// Alter subtraction value to change left or right alignment

		}
	}

	public static void alignShooterXCenter() {
		// checks to see if the turret is lined up with the boiler
		// if it is not aligned, turret centers on target
		if (Sensors.pixyShooterXPos() != -1) {
			double distance = Sensors.pixyShooterXPos();
			double distanceOff = distance - 130;
			targetShooter.setX(distanceOff);

			// Subtract less pixels to go to the left
			// Alter subtraction value to change left or right alignment
		}
	}
}
