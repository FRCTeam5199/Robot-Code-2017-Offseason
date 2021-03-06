package org.usfirst.frc.team5199.robot;
import org.usfirst.frc.team5199.robot.Robot;



public class TurretControl {

	private final JoystickController joystick;
	private Turret turret = new Turret();
	private double p;
	private double i;
	private double d;
	private Vector2 target;
	private Vector2 lastTarget;
	double integral;
	public TurretControl(JoystickController joystick, double p, double i, double d, Vector2 target) {
		this.joystick = joystick;
		this.p = p;
		this.i = i;
		this.d = d;
		this.target = target;
		lastTarget = target.clone();
	}

	public void update() {
		if (joystick.getButton(2)) {
			autoaim();
		} else {
			
			manualControl();
		}
	}

	public void manualControl() {
		turret.setTurret(joystick.getZ() * .3);
		if (joystick.getTrigger()) {
			turret.setFlyWheel((joystick.getSlider() - 1) / -2);
		} else {
			turret.setFlyWheel(0);
		}
		// Robot.nBroadcaster.println(turret.getFlyWheelRPM());
		// Robot.nBroadcaster.println(turret.getEncoder().getDistance());
	}

	public void autoaim() {
		// turret will try to move so that Target.x becomes 0
		double motorSpeed;
		integral  += target.getX();
		if(Math.abs(integral)>1/i) {
			if(integral>0) {
				integral = 1/i;
			}else {
				integral = -1/i;
			}
		}
		motorSpeed = p * target.getX();
		Robot.nBroadcaster.println(d * (target.getX() - lastTarget.getX()));
		motorSpeed += d * (target.getX() - lastTarget.getX());
		motorSpeed += i*integral;
		turret.setTurret(motorSpeed);
		lastTarget = target.clone();
	}

	public double getMotorSpeed() {
		// turret will try to move so that Target.x becomes 0
		double motorSpeed;
		// No "I" so just a PD controller for now lol
		motorSpeed = p * target.getX();
		motorSpeed += d * (target.getX() - lastTarget.getX());
		turret.setTurret(motorSpeed);
		lastTarget = target.clone();
		return motorSpeed;
	}

	public Turret getTurret() {
		return turret;
	}
}
