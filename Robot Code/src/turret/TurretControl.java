package turret;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.JoystickController;
import interfaces.LoopModule;
import maths.Vector2;

public class TurretControl implements LoopModule{

	private final JoystickController joystick;
	private Turret turret;

	private double pTurret = .003;
	private double iTurret = .0001;
	private double dTurret = .05;
	private double integralTurret = 0;

	private double pFlywheel = .02;
	private double iFlywheel = .00025;
	private double integralFlywheel = 0;

	private Vector2 target;
	private Vector2 lastTarget;

	public TurretControl(Turret turret,JoystickController joystick, Vector2 target) {
		this.turret = turret;
		this.joystick = joystick;
		this.target = target;
		lastTarget = target.clone();
	}
	
	@Override
	public void init() {		
	}

	@Override
	public void update(long delta) {
		if (joystick.getButton(2)) {
			// autoaim() function needs to be updated to use Pixycam
			//autoaim();
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
		integralTurret  += target.getX();
		if(Math.abs(integralTurret)>1/iTurret) {
			if(integralTurret>0) {
				integralTurret = 1/iTurret;
			}else {
				integralTurret = -1/iTurret;
			}
		}
		motorSpeed = pTurret * target.getX();
		Robot.nBroadcaster.println(dTurret * (target.getX() - lastTarget.getX()));
		motorSpeed += dTurret * (target.getX() - lastTarget.getX());
		motorSpeed += iTurret*integralTurret;
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
