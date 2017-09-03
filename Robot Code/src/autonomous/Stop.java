package autonomous;

import drive.DriveBase;
import intake.Intake;
import interfaces.AutFunction;
import turret.Turret;

public class Stop implements AutFunction {
	
	private final DriveBase base;
	private final Turret turret;
	private final Intake intake;
	private boolean done;
	
	public Stop(DriveBase base, Turret turret, Intake intake) {
		this.base = base;
		this.turret = turret;
		this.intake = intake;
		done = true;
	}

	@Override
	public void update(long delta) {
		base.move(0, 0);
		turret.setFlyWheel(0);
		turret.setTurret(0);
		intake.setSpeed(0);
		done = true;
	}

	@Override
	public boolean isDone() {
		return done;
	}
}
