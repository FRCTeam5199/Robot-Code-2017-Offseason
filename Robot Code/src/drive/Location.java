package drive;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import interfaces.LoopModule;

public class Location implements LoopModule{
	private final ADXRS450_Gyro gyro;
	private final BuiltInAccelerometer accelerometer;
	
	public Location(){
		gyro = Robot.sensors.getGyro();
		accelerometer = Robot.sensors.getAccelerometer();
	}
	
	@Override
	public void update(long deltaS){
		
	}

	@Override
	public void init() {		
	}

}
