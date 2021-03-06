package climber;

import controllers.JoystickController;
import interfaces.LoopModule;

public class ClimberControl implements LoopModule {

	private final JoystickController joystick;
	private final Climber climber;

	// This is defined in RobotMap as 4 and 5 but Kevin said he wanted 5 and 6.
	private final int CLIMB_BUTTON_PRIMARY = 5;
	private final int CLIMB_BUTTON_SECONDARY = 6;

	public ClimberControl(Climber climber, JoystickController joystick) {
		this.climber = climber;
		this.joystick = joystick;
	}

	@Override
	public void init() {
	}

	@Override
	public void update(long delta) {
		if (joystick.getButton(CLIMB_BUTTON_PRIMARY) || joystick.getButton(CLIMB_BUTTON_SECONDARY)) {
			climber.setSpeed(1);
		} else {
			climber.setSpeed(0);
		}
	}

}
