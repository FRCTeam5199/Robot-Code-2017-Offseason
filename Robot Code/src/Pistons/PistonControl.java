package Pistons;

import controllers.JoystickController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import interfaces.LoopModule;

public class PistonControl implements LoopModule {

	private final JoystickController joystick;
	private final DoubleSolenoid solenoid;

	public PistonControl(DoubleSolenoid solenoid, JoystickController joystick) {
		this.solenoid = solenoid;
		this.joystick = joystick;
	}

	@Override
	public void init() {
	}

	@Override
	public void update(long delta) {
		if (joystick.getButton(8)) {
			solenoid.set(DoubleSolenoid.Value.kForward);
		} else {
			solenoid.set(DoubleSolenoid.Value.kOff);
		}

	}

}
