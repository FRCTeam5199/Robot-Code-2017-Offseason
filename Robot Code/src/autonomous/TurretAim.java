package autonomous;

import interfaces.AutFunction;
import pixy.PixyFunctions;
import turret.Turret;
import turret.TurretControl;

public class TurretAim implements AutFunction {
	PixyFunctions pixyFunc;
	TurretControl turretControl;
	Turret turret;
	double rpm;

	public TurretAim(PixyFunctions pixyFunc, TurretControl turretControl, double rpm, Turret turret) {
		this.pixyFunc = pixyFunc;
		this.turretControl = turretControl;
		this.turret = turret;
	}

	@Override
	public void update(long deltaTime) {
		pixyFunc.alignShooterX();
		turretControl.autoaim();
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
