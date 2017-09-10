package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import interfaces.AutFunction;
import pixy.PixyFunctions;
import pixy.PixyGearPID;

public class PixyForward implements AutFunction{
	PixyGearPID pixyGear;
	PixyFunctions pixyFunc;
	boolean isDone = false;
	public PixyForward(PixyFunctions pixyFunc, PixyGearPID pixyGear) {
		this.pixyFunc = pixyFunc;
		this.pixyGear = pixyGear;
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub
		pixyFunc.turnAndGoStraightAuton();
		pixyGear.pixyGear();
		if(Robot.sensors.ultraDistanceLeft()<10) {
			isDone = true;
		}
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone();
	}

}
