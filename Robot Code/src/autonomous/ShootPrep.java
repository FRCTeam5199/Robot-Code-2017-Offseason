package autonomous;

import interfaces.AutFunction;

import turret.Turret;
import turret.TurretControl;

public class TurretAim implements AutFunction {
	TurretControl turretControl;
	Turret turret;
	double rpm;

	public TurretAim(TurretControl turretControl, double rpm, Turret turret) {
		this.turretControl = turretControl;
		this.turret = turret;
	}

	@Override
	public void update(long deltaTime) {
		turretControl.autoaim(deltaTime);
		turretControl.setRPM(rpm);
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub

		return false;
	}

}
