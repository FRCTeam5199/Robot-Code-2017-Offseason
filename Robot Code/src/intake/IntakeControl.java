package intake;

import Controllers.JoystickController;
import Controllers.XBoxController;

public class IntakeControl {
	private final double intakeSpeed = -1;
	private final JoystickController joystick;
	private final XBoxController controller;
	private final Intake intake;

	public IntakeControl(JoystickController joystick, XBoxController controller) {
		this.joystick = joystick;
		this.controller = controller;
		intake = new Intake();
	}

	public void update() {
		if (joystick.getButton(3) || joystick.getButton(4) || controller.getButton(5)) {
			intake.setSpeed(intakeSpeed);
		} else {
			intake.setSpeed(0);
		}
	}
}
