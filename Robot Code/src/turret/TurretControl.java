package turret;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.JoystickController;
import interfaces.LoopModule;
import maths.Vector2;
import pixy.PixyFunctionsTurret;

public class TurretControl implements LoopModule {

	private final JoystickController joystick;
	private Turret turret;

	private final int maxRPM = 6000;

	private double pTurret = .003;
	private double iTurret = .000001;
	private double dTurret = 5;
	private double integralTurret = 0;

	private double pFlywheel = .02;
	private double iFlywheel = .00025;
	private double integralFlywheel = 0;

	private Vector2 target;
	private Vector2 lastTarget;
	private PixyFunctionsTurret pixyFuncShooter;

	public TurretControl(Turret turret, JoystickController joystick) {
		this.turret = turret;
		this.joystick = joystick;
		target = new Vector2(0, 0);
		pixyFuncShooter = new PixyFunctionsTurret();
		lastTarget = target.clone();
	}

	@Override
	public void init() {
		Robot.dashboard.putDouble("Flywheel rip-ems", 0d);
		// Robot.dashboard.putData("Flywheel rip-ems", turret.getFlyWheelRPM());
	}

	@Override
	public void update(long delta) {
		Robot.dashboard.putDouble("Flywheel rip-ems", turret.getFlyWheelRPM());
		manualControl();

		// if (joystick.getButton(1) || joystick.getButton(2)) {
		// // autoaim() function needs to be updated to use Pixycam
		// // autoaim();
		// setRPM(joystick.getScaledSlider() * 6000);
		//
		// } else {
		// manualControl();
		// integralTurret = 0;
		// }
	}

	public void manualControl() {
		turret.setTurret(joystick.getZ() * .3);
		if (joystick.getTrigger()) {
			setRPM(joystick.getScaledSlider() * maxRPM);
		} else {
			turret.setFlyWheel(0);
		}
		// Robot.nBroadcaster.println(turret.getFlyWheelRPM());
		// Robot.nBroadcaster.println(turret.getEncoder().getDistance());
	}

	public void autoaim(long deltaTime) {
		// turret will try to move so that Target.x becomes 0
		double motorSpeed;

		target = pixyFuncShooter.getTarget();
		integralTurret += target.getX() * deltaTime;
		if (Math.abs(integralTurret) > 1 / iTurret) {
			if (integralTurret > 0) {
				integralTurret = 1 / iTurret;
			} else {
				integralTurret = -1 / iTurret;
			}
		}
		motorSpeed = pTurret * target.getX();
		Robot.nBroadcaster.println(dTurret * (target.getX() - lastTarget.getX()));
		motorSpeed += dTurret * (target.getX() - lastTarget.getX()) / deltaTime;
		motorSpeed += iTurret * integralTurret;
		turret.setTurret(motorSpeed);
		lastTarget = target.clone();
	}

	public void setRPM(double rpm) {
		// convert rpm to rps
		rpm = rpm / 60;

		double error = rpm - turret.getFlyWheelRPS();
		integralFlywheel += error;

		integralFlywheel = clamp(integralFlywheel, 1 / iFlywheel);

		turret.setFlyWheel(pFlywheel * error + iFlywheel * integralFlywheel);
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
