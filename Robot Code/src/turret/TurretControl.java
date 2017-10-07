package turret;

import org.usfirst.frc.team5199.robot.Robot;
import controllers.JoystickController;
import interfaces.LoopModule;
import maths.Vector2;
import pixy.PixyFunctionsTurret;
import util.PIDController;

public class TurretControl implements LoopModule {

	private final JoystickController joystick;
	private Turret turret;

	private final int centerRPM = 3575;
	private final int sideRPM = 3375;
	private int turretOffset = 4;

	// tuned: no touchy
	// ---- for fresh motor ----
	private double pTurret = .0025;
	private double iTurret = .000001;
	private double dTurret = .7;

	// tuned: no touchy
	// ---- for almost ded motor ----
	// private double pTurret = .005;
	// private double iTurret = .000003;
	// private double dTurret = 1.4;

	private double integralTurret = 0;

	private PIDController flyWheelPID;

	private double lockAngle = 0;
	private double targetRPM = 0;
	private double adjustedRPM = 0;

	private PIDController lockPID;

	private Vector2 target;
	private Vector2 lastTarget;
	private PixyFunctionsTurret pixyFuncShooter;

	public TurretControl(Turret turret, JoystickController joystick) {
		this.turret = turret;
		this.joystick = joystick;
		target = new Vector2(0, 0);
		pixyFuncShooter = new PixyFunctionsTurret();
		lastTarget = target.clone();

		flyWheelPID = new PIDController("Flywheel", .007, 0.015, 0);
		lockPID = new PIDController("Turret", .07, 0, .003);
	}

	@Override
	public void init() {
		// Robot.dashboard.putDouble("Flywheel rip-ems", 0d);

		// flyWheelPID.putOnDashboard();
		// lockPID.putOnDashboard();
	}

	@Override
	public void update(long delta) {
		// Robot.dashboard.putNumber("Flywheel rip-ems",
		// turret.getBufferedFlyWheelRPM());
		// flyWheelPID.getFromDashboard();

		// turretOffset = Robot.dashboard.getInt("Turret offset");

		if (joystick.getButton(9) || joystick.getButton(10)) {
			targetRPM = centerRPM;
		} else if (joystick.getButton(11) || joystick.getButton(12)) {
			targetRPM = sideRPM;
		}

		adjustedRPM = targetRPM - joystick.getSlider() * 50;

		// Robot.dashboard.putNumber("Target RPM", targetRPM);

		if (joystick.getButton(5) || joystick.getButton(6)) {
			goTo(0);
		} else if (joystick.getButton(1)) {
			goTo(lockAngle);
			setRPM(adjustedRPM);
		} else if (joystick.getButton(2)) {
			lockPID.reset(turret.getTurretAngle());
			autoaim(delta);
			setRPM(adjustedRPM);
		} else if (joystick.getButton(7)) {
			lockPID.reset(turret.getTurretAngle());
			turret.zeroTurret();
		} else {
			lockPID.reset(turret.getTurretAngle());
			manualControl();
			integralTurret = 0;
		}
	}

	public void goTo(double n) {
		lockPID.setTarget(n);
		turret.setTurret(lockPID.update(turret.getTurretAngle()));
	}

	public void manualControl() {
		turret.setTurret(joystick.getZ() * .5);

		turret.setFlyWheel(0);

	}

	public void autoaim(long deltaTime) {
		// turret will try to move so that Target.x becomes 0
		double motorSpeed;

		target = pixyFuncShooter.getTarget();
		// Robot.nBroadcaster.println(target.getX());
		target.setX(target.getX() + turretOffset);
		integralTurret += target.getX() * deltaTime;
		if (Math.abs(integralTurret) > 1 / iTurret) {
			if (integralTurret > 0) {
				integralTurret = 1 / iTurret;
			} else {
				integralTurret = -1 / iTurret;
			}
		}

		motorSpeed = pTurret * target.getX();
		motorSpeed += dTurret * (target.getX() - lastTarget.getX()) / deltaTime;
		motorSpeed += iTurret * integralTurret;
		turret.setTurret(motorSpeed);
		lastTarget = target.clone();

		lockAngle = turret.getTurretAngle();
	}

	public void setRPM(double rpm) {
		flyWheelPID.setTarget(rpm);
		turret.setFlyWheel(flyWheelPID.update(turret.getFlyWheelRPM()));
	}

	public double getError() {
		return target.getX();
	}

	private double clamp(double n, double clamp) {
		if (n > clamp) {
			return clamp;
		} else if (n < -clamp) {
			return -clamp;
		}
		return n;
	}

}
