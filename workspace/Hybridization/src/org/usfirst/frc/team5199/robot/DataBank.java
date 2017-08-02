package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;

public class DataBank {
	public static Pixy pixyGear, pixyShooter;
	public static PixyProcess pixyGearProc, pixyShooterProc;
	public static EncoderDriveFunctions driveEncoders;
	public static UltrasonicData ultraData;
	public 	static Encoder flywheel;
	public static CANTalon turret;
	public static CircularAverageBuffer flywheelAVG;
	static long time = System.currentTimeMillis();
	public static Spark right = new Spark(0);
	public static Spark left = new Spark(1);

	public DataBank(){
		pixyGear = new Pixy(0x51);
		pixyGearProc = new PixyProcess(pixyGear);
		pixyShooter = new Pixy(0x53);
		pixyShooterProc = new PixyProcess(pixyShooter);
		time = System.currentTimeMillis();
		ultraData = new UltrasonicData(1,0,3,2);
		flywheel =  new Encoder(10, 9, false, Encoder.EncodingType.k4X);
		flywheel.reset();
		flywheel.setDistancePerPulse(RobotMap.inchesPerRotation / 2);
		flywheelAVG = new CircularAverageBuffer(75);
//		turret.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
//		driveEncoders = new EncoderDriveFunctions(, null, null);
	}
	
	public static void timeReset(){
		time = System.currentTimeMillis();
	}
	
	public static double ultraDistanceRight(){
		return ultraData.distanceRight();
	}
	public static double ultraDistanceLeft(){
		return ultraData.distanceLeft();
	}
	public static double pixyGearXPos(){
		return pixyGearProc.compensatedGearPixyData();
	}
	public static double pixyShooterXPos(){
		return pixyShooterProc.shooterData()[0];
	}
	public static double shooterRPM(){
		return flywheelAVG.DataAverage(flywheel.getRate());
	}
	public static double turretPosition(){
		return turret.getPosition();
	}
	
}
