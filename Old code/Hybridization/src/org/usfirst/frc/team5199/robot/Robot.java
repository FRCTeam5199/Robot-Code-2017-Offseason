package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.ss
 */
public class Robot extends SampleRobot {
	Command autonomousCommand;
	SendableChooser autoChooser;
	int step = 0;
	public Spark right = DataBank.right, left = DataBank.left;
	CANTalon turret = new CANTalon(1);
	CANTalon shooter = new CANTalon(0);
	Spark sweeper = new Spark(RobotMap.intake);
	Spark transport = new Spark(RobotMap.transport);
	Spark climber = new Spark(RobotMap.climber);
	EncoderShooterFunctions shooterFunc = new EncoderShooterFunctions(shooter, turret);
	DataBank bank = shooterFunc.bank;
	Joystick stick = new Joystick(1);
	Joystick xBox = new Joystick(0);
	// = new Relay(0);
	GyroFunctions gyro = new GyroFunctions(right, left);
	EncoderDriveFunctions driveFunc = new EncoderDriveFunctions(right, left, gyro);
	boolean lightOn = false;
	UltrasonicFunctions ultraFunctions;
	int PWMSheez = 0;
	double EncoderNum, count = 0;
	double JagPow = 0;
	private Relay relay;
	private PixyFunctions pixyGearFunc, pixyShooterFunc;
	RobotDrive Driver = new RobotDrive(right, left);
	Servo shooterServo = new Servo(3);
	ServoDude hood = new ServoDude(shooterServo);
	boolean shooting = false;
	public static int counter = 0;
	public static long start = 0;
	public static boolean leftTurn = true;
	int autoStep = 0;
	double shootMode = 0;
	boolean autoTrack = false;
	int autoMode = 0;

	public Robot() {
	}

	@Override
	public void robotInit() {
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		camera1.setResolution(640, 360);
		camera1.setFPS(15);
		ultraFunctions = new UltrasonicFunctions(bank.ultraData, right, left);
		pixyGearFunc = new PixyFunctions(bank.pixyGear, ultraFunctions, bank.driveEncoders, Driver);
		pixyShooterFunc = new PixyFunctions(bank.pixyShooter, turret);
		// CameraServer.getInstance().startAutomaticCapture();
		bank.timeReset();
		relay = new Relay(0);
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Do nothing pls no pick this one fam", "jack shit");
		autoChooser.addObject("Center", "center");
		autoChooser.addObject("Left", "left");
		autoChooser.addObject("Right", "right");

		SmartDashboard.putData("Autonomous Selection", autoChooser);

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * if-else structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomous() {
		autoStep = 0;
		double AVG = 0;
		String auto = null;

		while (isAutonomous() && isEnabled()) {
			auto = autoChooser.getSelected().toString();
			// autoMode = (int) SmartDashboard.getNumber("Auto Mode:");
			if (auto.equals("right")) {
				if (autoStep >= 1) {
					pixyShooterFunc.alignShooterX();
				}

				shooterFunc.boilerPegShoot();

				if (autoStep == 0) {
					Driver.drive(.42, 0, 1);
					Timer.delay(2.25);
					Driver.stop();
					Timer.delay(.5);
					autoStep = 1;
				}
				if (autoStep == 1) {
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.drive(0, -.35, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 6) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(0.5, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(3)) {
							autoStep = 3;
						}
					}
				}
				if (autoStep == 3) {
					Driver.stop();
				}

			} else if (auto.equals("left")) {
				// if (autoStep >= 1) {
				// pixyShooterFunc.alignShooterX();
				// }
				//
				// AVG = bank.shooterRPM();
				// // For the close gear peg position
				// if (AVG < 1500) {
				// shooter.set(1);
				// } else if (AVG < 2000) {
				// shooter.set(.75);
				// } else if (AVG < 2500) {
				// shooter.set(.65);
				// } else if (AVG < 2900) {
				// shooter.set(.52);
				// } else if (AVG < 3300 // + (100 * stick.getThrottle())) {
				// ) {
				// JagPow += 0.0005;
				// shooter.set(JagPow + .52);
				// } else {
				// shooter.set(.52);
				// JagPow -= 0.0001;
				// }

				if (autoStep == 0) {
					Driver.drive(.42, 0, 1);
					Timer.delay(2.25);
					Driver.stop();
					Timer.delay(.5);
					autoStep = 1;
				}
				if (autoStep == 1) {
					if (!(Math.abs(bank.pixyGearXPos() - 160) < 7)) {
						Driver.drive(0, .25, 1);
					} else {
						Driver.stop();
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					if (bank.ultraDistanceLeft() > 6) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(0.5, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(3)) {
							autoStep = 3;
						}
					}
				}
				if (autoStep == 3) {
					Driver.stop();
				}
			} else if (auto.equals("center")) {
				if (autoStep >= 1) {
					pixyShooterFunc.alignShooterXCenter();
				}
				//
				shooterFunc.centerPegShoot();

				if (autoStep == 0) {
					Driver.drive(.3, 0, 1);
					Timer.delay(.75);
					Driver.stop();
					Timer.delay(.25);
					autoStep = 1;
				}

				if (autoStep == 1) {
					if (bank.ultraDistanceLeft() > 6) {
						if (pixyGearFunc.turnAndGoStraightAuton()) {
							Driver.drive(0.4, 0, 1);
						}
					} else {
						if (ultraFunctions.driveFowardAuton(3)) {
							autoStep = 2;
						}
					}
				}
				if (autoStep == 2) {
					Driver.stop();
				}
			}
		}

	}

	@Override
	public void operatorControl() {
		SmartDashboard.putNumber("Shooter Speed", 0);

		int lightStep = 0;

		double AVG = 0;
		bank.time = System.currentTimeMillis();
		lightOn = false;
		start = System.currentTimeMillis();
		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();
		hood.set(0);
		while (isOperatorControl() && isEnabled()) {
			if (stick.getRawButton(8) || xBox.getRawButton(10)) {
				if (lightStep == 0) {
					lightOn = !lightOn;
					lightStep = 1;
				}
			} else {
				lightStep = 0;
			}

			// actually turns the light on or off
			if (lightOn || stick.getRawButton(1) || stick.getRawButton(2)) {
				relay.set(Relay.Value.kForward);
			} else {
				relay.set(Relay.Value.kOff);
			}

			SmartDashboard.putNumber("Turret Encoder", turret.getPosition());
			SmartDashboard.putNumber("Left Ultra", bank.ultraDistanceLeft());
			SmartDashboard.putNumber("Right Ultra", bank.ultraDistanceRight());
			bank.pixyShooterProc.pixyShooterI2CTest();
			bank.pixyShooterProc.pixyShooterTest();
			bank.pixyGearProc.pixyGearI2CTest();
			bank.pixyGearProc.pixyGearTest();

			if (xBox.getRawButton(1)) {
				// if (bank.ultraDistanceLeft() > 16 &&
				// bank.ultraDistanceRight() > 16) {
				// if (pixyGearFunc.turnAndGoStraightAuton()) {
				// Driver.drive(0.25, 0, 1);
				// }
				// } else {
				// ultraFunctions.driveFowardAuton(8);
				// }
				// THIS CODE FUCKIN WORKS FOR THE PIXY CENTERING
				// HOLY SHIT WE DID IT BOYS WE REALLY FUCKIN DID IT
				// Testing Button
				if (step == 0) {
					driveFunc.initEncoders();
					step = 1;
				} else {
					driveFunc.driveStraightAuton(16);
				}

			} else if (xBox.getRawButton(RobotMap.loaderTurnButton)) {
				// gyro.moveDegreesAuton(-60, 0);
			} else {
				gyro.resetGyro();
				step = 0;
				Driver.drive(xBox.getRawAxis(1), xBox.getRawAxis(4) * 1, 1);
			}

			if (stick.getRawButton(7)) {
				shootMode = 1;
			} else if (stick.getRawButton(9)) {
				shootMode = 2;
			} else if (stick.getRawButton(11)) {
				shootMode = 3;
			}

			if (stick.getRawButton(1) || stick.getRawButton(2)) {
				autoTrack = true;
				if (shootMode == 1) {
					shooterFunc.centerPegShoot();
				} else if (shootMode == 2) {
					shooterFunc.boilerHopperShoot();
				} else if (shootMode == 3) {
					shooterFunc.boilerPegShoot();
				}
			} else if (stick.getRawButton(10)) {
				shooter.set(.5);
			} else {

				autoTrack = false;
				shooter.set(0);
			}

			if (stick.getRawButton(4) || autoTrack) {
				if (shootMode == 1) {
					pixyShooterFunc.alignShooterXCenter();
				} else {
					pixyShooterFunc.alignShooterX();
				}
			} else if (Math.abs(stick.getZ()) > .15) {
				turret.set(stick.getZ() / 3.5);
				SmartDashboard.putNumber("Stick Turn", stick.getZ() / stick.getThrottle());
			} else if (stick.getRawButton(12) || stick.getRawButton(5) || stick.getRawButton(6)
					|| xBox.getRawAxis(2) != 0) {
				if (turret.getPosition() < -2500) {
					turret.set(.2);
				} else if (turret.getPosition() > 2500) {
					turret.set(-.2);
				} else if (turret.getPosition() < -100) {
					turret.set(.08);
				} else if (turret.getPosition() > 100) {
					turret.set(-.08);
				} else {
					turret.set(0);
				}
			} else {
				turret.set(0);
			}

			if (stick.getRawButton(1) || xBox.getRawAxis(3) != 0 || stick.getRawButton(3)) {
				sweeper.set(-1);
			} else {
				sweeper.set(0);
			}

			// Ball transport only active when trigger is held
			if (stick.getRawButton(1)) {
				transport.set(-1);
			} else {
				transport.set(0);
			}

			if (stick.getRawButton(6) || stick.getRawButton(5)) {
				climber.set(1);
			} else if (xBox.getRawAxis(2) != 0) {
				climber.set(xBox.getRawAxis(2));
			} else {
				climber.set(0);
			}

			SmartDashboard.putNumber("Encoder Rate", bank.shooterRPM());

			if (stick.getPOV() == 180) {
				hood.increment(-.1);
			} else if (stick.getPOV() == 0) {
				hood.increment(.1);
			}

			SmartDashboard.putNumber("ServoDude", hood.position);

		}

	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test() {
		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();
		while (isTest() && isEnabled()) {
			relay.set(Relay.Value.kForward);
			bank.pixyShooterProc.pixyShooterI2CTest();
			bank.pixyShooterProc.pixyShooterTest();
			bank.pixyGearProc.pixyGearI2CTest();
			bank.pixyGearProc.pixyGearTest();
			// SmartDashboard.putNumber("Gyro", gyro.gyro.getAngle());
		}
	}

	public void disabled() {
		bank.pixyShooterProc.pixyTestReset();
		bank.pixyGearProc.pixyTestReset();
	}

	public void TurretReset() {

	}
}
