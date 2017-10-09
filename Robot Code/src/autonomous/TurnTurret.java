package autonomous;

import interfaces.AutFunction;
import turret.Turret;
import turret.TurretControl;

public class TurnTurret implements AutFunction {
	private final double errorRange = 2;

	private final TurretControl turretControl;
	private final Turret turret;
	private final double angle;

	public TurnTurret(TurretControl turretControl, Turret turret, double angle) {
		this.turretControl = turretControl;
		this.turret = turret;
		this.angle = angle;
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub
		turretControl.goTo(angle);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		// TODO make isDone() check velocity
		return Math.abs(turret.getTurretAngle() - angle) < errorRange;
	}
}
