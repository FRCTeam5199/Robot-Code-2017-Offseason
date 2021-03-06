package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Encoder;

public class DataBank {
	public Pixy pixyGear, pixyShooter;
	public PixyProcess pixyGearProc, pixyShooterProc;
	public EncoderDriveFunctions driveEncoders;
	public UltrasonicData ultraData;
	public 	Encoder flywheel;
	public CANTalon turret;
	public CircularAverageBuffer flywheelAVG;
	long time = System.currentTimeMillis();

	public DataBank(){
		pixyGear = new Pixy(RobotMap.pixyGear);
		pixyGearProc = new PixyProcess(pixyGear);
		pixyShooter = new Pixy(RobotMap.pixyShoot);
		pixyShooterProc = new PixyProcess(pixyShooter);
		time = System.currentTimeMillis();
		ultraData = new UltrasonicData(RobotMap.ultraRightEcho, RobotMap.ultraRightPing, RobotMap.ultraLeftEcho,
				RobotMap.ultraLeftPing);
		flywheel =  new Encoder(10, 9, false, Encoder.EncodingType.k4X);
		flywheel.reset();
		flywheel.setDistancePerPulse(RobotMap.inchesPerRotation / 2);
		flywheelAVG = new CircularAverageBuffer(75);
//		turret.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
//		driveEncoders = new EncoderDriveFunctions(, null, null);
	}
	public double ultraDistanceRight(){
		return ultraData.distanceRight();
	}
	public double ultraDistanceLeft(){
		return ultraData.distanceLeft();
	}
	public double ultraDistanceAverage(){
		return ultraData.ultraAverage();
	}
	public double pixyGearXPos(){
		return pixyGearProc.compensatedGearPixyData();
	}
	public double pixyShooterXPos(){
		return pixyShooterProc.shooterData()[0];
	}
	public boolean pixyGearReadingTwoBlocks(){
		if(pixyGearProc.averageData(0, false, pixyGear)[0] != -1){
			return true;
		}else{
			return false;
		}
	}
	public boolean pixyShooterReadingBlocks(){
		if(pixyShooterProc.shooterData()[0] != -1){
			return true;
		}else{
			return false;
		}
	}
	public double pixyShooterYPos(){
		return pixyShooterProc.shooterData()[1];
	}
	public double shooterRPM(){
		return flywheelAVG.DataAverage(flywheel.getRate());
	}
	public double turretPosition(){
		return turret.getPosition();
	}
	
}
