package turret;

import org.usfirst.frc.team5199.robot.Robot;

import Controllers.JoystickController;
import maths.Vector2;

public class TurretControl {

	private final JoystickController joystick;
	private Turret turret = new Turret();

	private double pTurret = .001;
	private double iTurret = 0;
	private double dTurret = .002;
	private double integralTurret = 0;

	private double pFlywheel = .02;
	private double iFlywheel = .00025;
	private double integralFlywheel = 0;

	private Vector2 target;
	private Vector2 lastTarget;

	public TurretControl(JoystickController joystick, Vector2 target) {
		this.joystick = joystick;
		this.target = target;
		lastTarget = target.clone();
	}

	public void update() {
		if (joystick.getButton(2)) {
			autoaim();
			setRPM(joystick.getScaledSlider() * 100);
			Robot.nBroadcaster.print(turret.getFlyWheelRPM());
		} else {
			manualControl();
			integralTurret = 0;
			integralFlywheel = 0;
		}
	}

	public void manualControl() {
		turret.setTurret(joystick.getZ() * .3);
		if (joystick.getTrigger()) {
			turret.setFlyWheel(joystick.getScaledSlider());
		} else {
			turret.setFlyWheel(0);
		}
		// Robot.nBroadcaster.println(turret.getFlyWheelRPM());
		// Robot.nBroadcaster.println(turret.getEncoder().getDistance());
	}

	public void autoaim() {
		// turret will try to move so that Target.x becomes 0
		double motorSpeed;
		// No "I" so just a PD controller for now lol
		motorSpeed = pTurret * target.getX();
		motorSpeed += dTurret * (target.getX() - lastTarget.getX());
		turret.setTurret(motorSpeed);
		lastTarget = target.clone();
	}

	public void setRPM(double rpm) {
		double error = rpm - turret.getFlyWheelRPM();
		integralFlywheel += error;

		integralFlywheel = clamp(integralFlywheel, 1 / iFlywheel);

		turret.setFlyWheel(pFlywheel * error + iFlywheel * integralFlywheel);
		Robot.nBroadcaster.println(pFlywheel * error + iFlywheel * integralFlywheel);
	}

	private double clamp(double n, double clamp) {
		if (n > clamp) {
			return clamp;
		} else if (n < -clamp) {
			return -clamp;
		}
		return n;
	}

	public Turret getTurret() {
		return turret;
	}
}
