package climber;

import org.usfirst.frc.team5199.robot.RobotMap;

import controllers.JoystickController;
import edu.wpi.first.wpilibj.Spark;
import interfaces.LoopModule;

public class ClimberControl implements LoopModule {
	
	private final JoystickController joystick;
	private final Spark climberMotor;
	
	// This is defined in RobotMap as 4 and 5 but Kevin said he wanted 5 and 6.
	private final int CLIMB_BUTTON_PRIMARY = 5;
	private final int CLIMB_BUTTON_SECONDARY = 6;
	
	public ClimberControl(JoystickController joystick) {
		this.joystick = joystick;
		this.climberMotor = new Spark(RobotMap.climber);
	}

	@Override
	public void init() {}

	@Override
	public void update(long delta) {
		manualControl();
	}

	private void manualControl() {
		if(joystick.getButton(CLIMB_BUTTON_PRIMARY) || joystick.getButton(CLIMB_BUTTON_SECONDARY)) {
			climberMotor.set(1);
		} else {
			climberMotor.set(0);
		}
	}
	
}
