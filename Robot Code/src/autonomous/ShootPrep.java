package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import interfaces.AutFunction;

import turret.Turret;
import turret.TurretControl;

public class ShootPrep implements AutFunction {
	private TurretControl turretControl;
	private Turret turret;
	private double rpm;
	private double rpmMargin = 10;
	private double aimMargin = 3;
	private double rateMargin = 1;

	public ShootPrep(TurretControl turretControl, Turret turret, double rpm, int offset) {
		this.turretControl = turretControl;
		this.turret = turret;
		this.rpm = rpm;
		turretControl.setOffset(offset);
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
		Robot.nBroadcaster.println(turret.getFlyWheelRPM() - rpm + "\t" + turretControl.getError());
		return Math.abs(turret.getFlyWheelRPM() - rpm) < rpmMargin && Math.abs(turretControl.getError()) < aimMargin
				&& Math.abs(turretControl.getLockErrorRate()) < rateMargin;
	}

}
