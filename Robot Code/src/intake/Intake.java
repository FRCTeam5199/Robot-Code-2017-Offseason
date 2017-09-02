package intake;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;

public class Intake {

	private Spark intakeMotor;
	
	public Intake() {
		intakeMotor = new Spark(RobotMap.intake);
	}
	
	public void setSpeed(double n) {
		intakeMotor.set(n);
	}
}
