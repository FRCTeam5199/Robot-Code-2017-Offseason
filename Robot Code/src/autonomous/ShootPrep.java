package autonomous;

import interfaces.AutFunction;

import turret.Turret;
import turret.TurretControl;

public class ShootPrep implements AutFunction {
	private TurretControl turretControl;
	private Turret turret;
	private double rpm;
	private double rpmMargin = 10;
	private double aimMargin = 5;

	public ShootPrep(TurretControl turretControl, double rpm, Turret turret) {
		this.turretControl = turretControl;
		this.turret = turret;
	}

	@Override
	public void update(long deltaTime) {
		turretControl.autoaim(deltaTime);
		turretControl.setRPM(rpm);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		return Math.abs(turret.getFlyWheelRPM() - rpm) < rpmMargin && Math.abs(turretControl.getError()) < aimMargin;
	}

}
