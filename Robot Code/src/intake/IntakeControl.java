package intake;

import controllers.JoystickController;
import controllers.XBoxController;
import interfaces.LoopModule;

public class IntakeControl implements LoopModule {
	private final double intakeSpeed = -1;
	private final JoystickController joystick;
	private final XBoxController controller;
	private final Intake intake;

	public IntakeControl(Intake intake, JoystickController joystick, XBoxController controller) {
		this.intake = intake;
		this.joystick = joystick;
		this.controller = controller;
	}

	@Override
	public void init() {
	}

	@Override
	public void update(long delta) {
		if (joystick.getButton(3) || joystick.getButton(4) || controller.getButton(5)) {
			intake.setSpeed(intakeSpeed);
		} else {
			intake.setSpeed(0);
		}
	}
}
