package autonomous;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import interfaces.AutFunction;

public class ReleasePistons implements AutFunction {

	private final DoubleSolenoid solenoid;
	private final int time = 500;

	private long endTime;
	private boolean isDone;

	public ReleasePistons(DoubleSolenoid solenoid) {
		this.solenoid = solenoid;
	}

	@Override
	public void update(long deltaTime) {
		if (System.currentTimeMillis() > endTime) {
			solenoid.set(DoubleSolenoid.Value.kOff);
			isDone = true;
		} else {
			solenoid.set(DoubleSolenoid.Value.kForward);
		}
	}

	@Override
	public void init() {
		endTime = time + System.currentTimeMillis();
		isDone = false;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}
}
