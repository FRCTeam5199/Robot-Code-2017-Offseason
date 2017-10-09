package autonomous;

import climber.Climber;
import drive.DriveBase;
import intake.Intake;
import interfaces.AutFunction;
import transport.Transport;
import turret.Turret;

public class Stop implements AutFunction {

	private final DriveBase base;
	private final Turret turret;
	private final Intake intake;
	private final Transport transport;
	private final Climber climber;
	private boolean done;

	public Stop(DriveBase base, Turret turret, Intake intake, Transport transport, Climber climber) {
		this.base = base;
		this.turret = turret;
		this.intake = intake;
		this.transport = transport;
		this.climber = climber;
		done = true;
	}

	@Override
	public void update(long deltaTime) {
		base.move(0, 0);
		turret.setFlyWheel(0);
		turret.setTurret(0);
		intake.setSpeed(0);
		transport.setSpeed(0);
		climber.setSpeed(0);
		done = true;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
}
