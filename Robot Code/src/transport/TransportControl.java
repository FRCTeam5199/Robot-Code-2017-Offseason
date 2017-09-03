package transport;

import controllers.JoystickController;
import interfaces.LoopModule;

public class TransportControl implements LoopModule {
	private final Transport transport;
	private final JoystickController joystick;

	private final double speed = 1;

	public TransportControl(Transport transport, JoystickController joystick) {
		this.transport = transport;
		this.joystick = joystick;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(long delta) {
		// TODO Auto-generated method stub
		if (joystick.getButton(12)) {
			transport.setSpeed(speed);
		}else {
			transport.setSpeed(0);
		}

	}
}
