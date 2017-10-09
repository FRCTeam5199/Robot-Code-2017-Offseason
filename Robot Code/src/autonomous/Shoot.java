package autonomous;

import intake.Intake;
import interfaces.AutFunction;
import transport.Transport;
import turret.Turret;
import turret.TurretControl;

public class Shoot implements AutFunction {

	private TurretControl turretControl;
	private Turret turret;
	private Intake intake;
	private Transport transport;

	private double rpm;
	private double lockAngle;

	public Shoot(TurretControl turretControl, Turret turret, Intake intake, Transport transport, double rpm) {
		this.turretControl = turretControl;
		this.turret = turret;
		this.rpm = rpm;
		this.intake = intake;
		this.transport = transport;
	}

	@Override
	public void update(long deltaTime) {
		turretControl.goTo(lockAngle);
		turretControl.setRPM(rpm);
		intake.setSpeed(-1);
		transport.setSpeed(1);
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		lockAngle = turret.getTurretAngle();
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}
}
