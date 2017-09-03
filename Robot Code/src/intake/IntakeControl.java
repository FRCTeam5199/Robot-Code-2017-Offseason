package intake;

import controllers.JoystickController;
import controllers.XBoxController;
import interfaces.MainLoopObject;

public class IntakeControl implements MainLoopObject {
	private final double intakeSpeed = -1;
	private final JoystickController joystick;
	private final XBoxController controller;
	private final Intake intake;

	public IntakeControl(Intake intake, JoystickController joystick, XBoxController controller) {
		this.intake = intake;
		this.joystick = joystick;
		this.controller = controller;
		intake = new Intake();
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
		if (joystick.getButton(3) || joystick.getButton(4) || controller.getButton(5)) {
			intake.setSpeed(intakeSpeed);
		} else {
			intake.setSpeed(0);
		}
	}
}
