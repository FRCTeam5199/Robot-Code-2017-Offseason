package autonomous;

import interfaces.AutFunction;
import sensors.Sensors;
import turret.Turret;
import turret.TurretControl;

public class FlyWheelSpeed implements AutFunction {
	TurretControl turretControl;
	Turret turret;
	double rpm;
	double errorRange = 10;

	public FlyWheelSpeed(TurretControl turretControl, double rpm, Turret turret) {
		this.turretControl = turretControl;
		this.turret = turret;
		this.rpm = rpm;
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub
		turretControl.setRPM(rpm);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		if (Math.abs(turret.getFlyWheelRPM() - rpm) > errorRange) {
			return true;
		}
		return false;
	}

}
