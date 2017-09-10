package autonomous;

import interfaces.AutFunction;
import sensors.Sensors;
import turret.Turret;
import turret.TurretControl;

public class FlyWheelSpeed implements AutFunction {
	TurretControl turretControl;
	Turret turret;
	double rpm;
	double maxAcceptableDistError = 10d; // The robot can be x inches off from target and it will be okay.
	double maxAcceptableRateError = 0.1d; // The robot can be moving at x inches/sec and it will be okay.
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
		if(Math.abs(turret.getFlyWheelRPM()-rpm)>10) {
			return true;
		}
		return false;
	}

}
