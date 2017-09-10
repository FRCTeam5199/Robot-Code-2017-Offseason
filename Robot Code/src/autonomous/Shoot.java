package autonomous;

import intake.Intake;
import interfaces.AutFunction;
import pixy.PixyFunctions;
import transport.Transport;
import turret.TurretControl;

public class Shoot implements AutFunction{
	PixyFunctions pixyFunc;
	TurretControl turretControl;
	double rpm;
	Intake intake;
	Transport transport;
	public Shoot(PixyFunctions pixyFunc, TurretControl turretControl, double rpm, Intake intake, Transport transport) {
		this.pixyFunc = pixyFunc;
		this.turretControl = turretControl;
		this.rpm = rpm;
		this.intake = intake;
		this.transport = transport;
	}
	@Override
	public void update(long deltaTime) {
		pixyFunc.alignShooterX();
		turretControl.autoaim(); 
		turretControl.setRPM(rpm);
		intake.setSpeed(-1);
		transport.setSpeed(1);
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
