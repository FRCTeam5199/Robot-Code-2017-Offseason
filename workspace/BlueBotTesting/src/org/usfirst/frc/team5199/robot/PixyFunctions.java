package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PixyFunctions {
	public static PixyProcess pixyProc, pixyProcShooter;
	public static UltrasonicFunctions ultraFunctions;
	public static EncoderDriveFunctions encoder;
	public static EncoderShooterFunctions encoderShooter;
	public static double turnPower;
	public static RobotDrive robot;
	public static CANTalon turretMotor;

	public PixyFunctions(Pixy pixy, UltrasonicFunctions ultra, EncoderDriveFunctions encoderF, RobotDrive driver) {
		pixyProc = new PixyProcess(pixy);
		ultraFunctions = ultra;
		encoder = encoderF;
		robot = driver;
	}

	public PixyFunctions(Pixy pixy, Talon shooter, Talon angleAdjust, CANTalon turret) {
		pixyProcShooter = new PixyProcess(pixy);
		turretMotor = turret;
	}

	public static boolean turnAndGoStraightAuton() {
		if (pixyProc.averageData(0, false)[0] != -1) {
			double distance = pixyProc.compensatedGearPixyData();
			double distanceOff = distance - 160;
			SmartDashboard.putNumber("Distance Off", distanceOff);
			if ((Math.abs(distanceOff) > RobotMap.pixyGearDataBuffer)) {
				int sign = (distanceOff >= 160) ? -1 : 1;
				SmartDashboard.putNumber("Pixy Turn Sign", sign);
				turnPower = ((distanceOff / 20) * .06) * sign;
				SmartDashboard.putNumber("Turn value", turnPower);
				robot.drive(-.2, turnPower, 1);
				return false;
			} else {
				robot.stop();
				return true;
			}
		}
		return false;
	}

	public static boolean checkIfAlignedGear() {

		double xAdjustment = pixyProc.compensatedGearPixyData();
		if (Math.abs(xAdjustment - 160) < RobotMap.pixyGearDataBuffer) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkIfAlignedShooter() {
		double xAdjustment = pixyProc.ShooterPixyData()[0];
		double yAdjustment = pixyProc.ShooterPixyData()[1];
		if (Math.abs(xAdjustment - 160) < RobotMap.pixyShooterDataBuffer
				&& Math.abs(yAdjustment - 160) < RobotMap.pixyShooterDataBuffer) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean alignShooterX() {
		// checks to see if the turret is lined up with the boiler
		// if it is not aligned, turret centers on target
		double xAdjustment = pixyProc.xPosShooter();
		if (Math.abs(xAdjustment - 160) < RobotMap.pixyShooterDataBuffer) {

			if (xAdjustment < 160) {
				if (xAdjustment < 140) {
					if (encoderShooter.checkLimits()) {
						turretMotor.set(.5);
					}
				} else if (xAdjustment < 155) {
					if (encoderShooter.checkLimits()) {
						turretMotor.set(.25);
					}
				} else {
					if (encoderShooter.checkLimits()) {
						turretMotor.set(.15);
					}
				}
			} else {
				if (xAdjustment < 180) {
					if (encoderShooter.checkLimits()) {
						turretMotor.set(-.5);
					}
				} else if (xAdjustment < 165) {
					if (encoderShooter.checkLimits()) {
						turretMotor.set(-.25);
					}
				} else {
					if (encoderShooter.checkLimits()) {
						turretMotor.set(-.15);
					}
				}
			}
			return false;
		}
		turretMotor.set(0);
		return true;
	}
}
