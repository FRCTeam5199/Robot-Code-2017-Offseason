package transport;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;

public class Transport {
	private final Spark transport;

	public Transport() {
		transport = new Spark(RobotMap.transport);
	}
	
	public void setSpeed(double n) {
		transport.set(n);
	}
}
