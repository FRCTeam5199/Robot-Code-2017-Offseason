package climber;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;

public class Climber {
	private final Spark climberMotor;

	public Climber() {
		climberMotor = new Spark(RobotMap.climber);
	}
	
	public void setSpeed(double n){
		climberMotor.setSpeed(n);
	}
}
