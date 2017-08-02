package org.usfirst.frc.team5199.robot;

import org.opencv.core.Mat;

import com.ctre.CANTalon;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * *****************************************************************************
 * ***************************************************** Robot Dolphins from
 * Outer Space should use the extension SampleRobot due to its efficiency in
 * looping quickly through the tele-op periodic IterativeRobot extension should
 * be avoided because a fast iteration rate is absolutely necessary (1000+
 * loops/sec vs 20 loops/sec)
 * 
 * 
 */
public class Robot extends SampleRobot {

	// Basic initialization
	private Joystick xBox, stick;
	private PowerDistributionPanel pdp;
	private Compressor compressor;
	private DoubleSolenoid solenoid;

	// Motor initialization
	private Spark leftMotor, rightMotor, climber, transport, sweeper;
	private CANTalon turret, shooter;
	private RobotDrive robotDriver;

	// Pixy and LED Relay init
	private Relay relay;
	PixyFunctions pixyGearFunc, pixyShooterFunc;

	// Ultra init
	private UltrasonicData ultraData;
	private UltrasonicFunctions ultraFunctions;

	// Encoder init
	private EncoderDriveFunctions driveEncoders;
	private Encoder shooterEncoder;
	EncoderShooterFunctions shooterFunc;

	// Gyro init
	private GyroFunctions gyro;

	// Servo init
	private Servo servo, servo2;
	private ServoDude shooterServo, servoBoy;

	// data init
	DataBank data;

	// -----------------------------------------------------------
	// Number/Constant init zone

	private static int step;
	private static int shooterMode;
	private static int lightStep;
	private static double AVG;
	public static double shootPow;
	private static double startTime;
	private double currAngle;
	private String autoMode;
	private int autoStep;
	int shootMode = 0;
	// TODO keep those two lines only and initilalize everything in robot init
	private static double straightMod = RobotMap.straightMod, turnMod = RobotMap.turnMod, driveMod = RobotMap.driveMod;

	private static double position = RobotMap.positionInitial, increment = RobotMap.incrementInitial;

	// Number/Constant init zone
	// ------------------------------------------------------------

	// ************************************************************
	// Boolean Zone
	public static boolean spinUp;
	public static boolean lightOn;
	public static boolean left;
	public static boolean autoTrack = false;
	// Boolean Zone
	// *************************************************************

	public Robot() {
	}

	@Override
	public void robotInit() {

		pdp = new PowerDistributionPanel();

		// motor initialization
		rightMotor = new Spark(RobotMap.rightMotor);
		leftMotor = new Spark(RobotMap.leftMotor);
		shooter = new CANTalon(RobotMap.shooter);
		sweeper = new Spark(RobotMap.intake);
		transport = new Spark(RobotMap.transport);
		climber = new Spark(RobotMap.climber);
		turret = new CANTalon(RobotMap.turret);
		servo = new Servo(RobotMap.shooterServo);
		servo2 = new Servo(RobotMap.shooterServo2);

		// servos
		shooterServo = new ServoDude(servo);
		servoBoy = new ServoDude(servo2);

		// drive initialization
		robotDriver = new RobotDrive(rightMotor, leftMotor);
		stick = new Joystick(RobotMap.joyStickPort);
		xBox = new Joystick(RobotMap.xBoxPort);

		// ultrasonic stuff
		ultraFunctions = new UltrasonicFunctions(data, rightMotor, leftMotor);

		// gyro initialization
		gyro = new GyroFunctions(rightMotor, leftMotor);
		GyroFunctions.gyro.calibrate();

		// encoders initialization
		driveEncoders = new EncoderDriveFunctions(rightMotor, leftMotor, gyro);
		EncoderDriveFunctions.resetDrive();
		EncoderDriveFunctions.initEncoders();
		shooterFunc = new EncoderShooterFunctions(shooter, turret);

		// relay initialization
		relay = new Relay(RobotMap.relayPort);

		// solenoid initialization
		solenoid = new DoubleSolenoid(RobotMap.solenoidLeft, RobotMap.solenoidRight);

		// number initialization
		step = RobotMap.stepInitial;
		shootPow = RobotMap.shootPowInitial;
		startTime = RobotMap.startTimeInitial;
		shooterMode = RobotMap.shooterModeInitial;
		lightStep = RobotMap.lightStepInitial;

			
		// camera init
		CameraServer.getInstance().startAutomaticCapture(RobotMap.usbCamera1);

		// getting stuff ready for auton
		SmartDashboard.putString("Autonomous Mode", "Enter Value");
		autoStep = RobotMap.autoStepInitial;
		
		data = shooterFunc.bank;
		
		pixyGearFunc = new PixyFunctions(data.pixyGear, ultraFunctions, driveEncoders, robotDriver);
		pixyShooterFunc  = new PixyFunctions(data.pixyShooter, turret);


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
//		autoMode = SmartDashboard.getString("Autonomous Mode");
//		if (autoMode.equals("Enter Value")) {
//			autoMode = "S";
//		}
		relay.set(Relay.Value.kForward);
		spinUp = RobotMap.spinUpInitial;
		AVG = RobotMap.AVGInitial;
		autoStep = RobotMap.autoStepInitial;
		startTime = System.currentTimeMillis();
		EncoderDriveFunctions.initEncoders();

		while (isAutonomous() && isEnabled()) {
			SmartDashboard.putNumber("Auto Step", autoStep);
			if (autoMode.equals("e") || autoMode.equals("E")) {
				AVG = data.shooterRPM();
				if (spinUp) {
					if (AVG < 1000) {
						shooter.set(1);
					} else if (AVG < 2000) {
						shooter.set(.85);
					} else if (AVG < 2500) {
						shooter.set(.7);
					} else if (AVG < 2700) {
						shooter.set(.57);
						// TODO change this cannot be looking at joystick during
						// auton need to figure out the correct value
					} else if (AVG < 3455 - (50 * stick.getRawAxis(3))) {
						shootPow += 0.0003;
						shooter.set(shootPow + .57);
					} else {
						shooter.set(.57);
						shootPow -= 0.0001;
					}
				}
				if (autoStep == 0) {
					solenoid.set(DoubleSolenoid.Value.kForward);
					EncoderDriveFunctions.initEncoders();
					startTime = System.currentTimeMillis();
					autoStep = 1;
				}
				if (autoStep == 1) {
					if (System.currentTimeMillis() - startTime > 1000) {
						solenoid.set(DoubleSolenoid.Value.kOff);
					}
					if (EncoderDriveFunctions.driveStraightAuton(50)) {
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					currAngle = GyroFunctions.getAngle();
					EncoderDriveFunctions.initEncoders();
					robotDriver.stop();
					autoStep = 3;
					Timer.delay(.5);
				}
				if (autoStep == 3) {
					// this only works for right turns, need to do right or left
					// depeneding on which alliance we are on
					if (EncoderDriveFunctions.autonSixtyDegreeTurn(currAngle, false)) {
						robotDriver.stop();
						EncoderDriveFunctions.initEncoders();
						Timer.delay(1);
						autoStep = 4;
					}
				}
				if (autoStep == 4) {
					if (EncoderDriveFunctions.driveStraightAuton(40)) {
						autoStep = 5;
					}
				}
				if (autoStep == 5) {
					// TODO change this to whatever we find experimentally works
					if ((pixyGearFunc.turnAndGoStraightAuton() && UltrasonicData.distanceLeft() < 8)) {
						autoStep = 6;
					}
				}
				if (autoStep == 6) {
					if (UltrasonicFunctions.driveFowardAutonSDR(4)) {
						autoStep = 7;
					}
				}
				if (autoStep == 7) {
					robotDriver.stop();
				}
				EncoderDriveFunctions.displayDistance();
			} else if (autoMode.equals("C") || autoMode.equals("c")) {

				AVG = data.shooterRPM();
				SmartDashboard.putNumber("AVG Auton RPM", AVG);
				SmartDashboard.putBoolean("Spin up", spinUp);
				SmartDashboard.putNumber("Auto Step", autoStep);
				if (spinUp) {
					// For the center gear position
					if (AVG < 2000) {
						shooter.set(1);
					} else if (AVG < 2500) {
						shooter.set(.9);
					} else if (AVG < 3000) {
						shooter.set(.8);
					} else if (AVG < 3300) {
						shooter.set(.6);
					} else if (AVG < 3675) {
						shootPow += 0.0004;
						shooter.set(shootPow + .6);
					} else {
						shooter.set(.6);
						shootPow -= 0.0001;
					}
				}
				if (autoStep == 0) {
					solenoid.set(DoubleSolenoid.Value.kForward);
					EncoderDriveFunctions.initEncoders();
					autoStep = 1;
				}

				if (autoStep == 1) {
					spinUp = true;
					if (System.currentTimeMillis() - startTime > 1000) {
						solenoid.set(DoubleSolenoid.Value.kOff);
					}
					// if (driveEncoders.driveStraightAuton(38)) {
					if (EncoderDriveFunctions.driveStraightAuton(50)) {
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					// if (pixyFunctionsGear.turnAndGoStraightAuton() &&
					// ultraData.distanceLeft() < 8) {
					if (UltrasonicFunctions.driveFowardAutonSDR(6)) {
						autoStep = 3;
					}
					// }
				}
				if (autoStep == 3) {
					robotDriver.stop();
				}
				if (autoStep == 3 || System.currentTimeMillis() - startTime > 7000) {
					if (PixyProcess.shooterData()[0] != -1) {
						if (PixyFunctions.alignShooterX()) {
							transport.set(-1);
							sweeper.set(-1);
						}
					} else {
						transport.set(-1);
						sweeper.set(-1);
					}
					
				} else {
					transport.set(0);
					sweeper.set(0);
				}
			} else if (autoMode.equals("S") || autoMode.equals("s")) {
				AVG = data.shooterRPM();
				if (spinUp) {
					if (AVG < 1000) {
						shooter.set(1);
					} else if (AVG < 2000) {
						shooter.set(.85);
					} else if (AVG < 2500) {
						shooter.set(.7);
					} else if (AVG < 2700) {
						shooter.set(.57);
					} else if (AVG < 3455 - (50 * stick.getRawAxis(3))) {
						shootPow += 0.0003;
						shooter.set(shootPow + .57);
					} else {
						shooter.set(.57);
						shootPow -= 0.0001;
					}
				}

				if (autoStep == 0) {
					solenoid.set(DoubleSolenoid.Value.kForward);
					EncoderDriveFunctions.initEncoders();
					autoStep = 1;
				}

				if (autoStep == 1) {
					spinUp = true;
					if (System.currentTimeMillis() - startTime > 1000) {
						solenoid.set(DoubleSolenoid.Value.kOff);
					}
					// if (driveEncoders.driveStraightAuton(38)) {
					if (EncoderDriveFunctions.driveStraightAuton(90)) {
						autoStep = 2;
					}
				}
				if (autoStep == 2) {
					robotDriver.stop();
				}
				if (autoStep == 2 || System.currentTimeMillis() - startTime > 7000) {
					if (PixyProcess.shooterData()[0] != -1) {
						if (PixyFunctions.alignShooterX()) {
							transport.set(-1);
							sweeper.set(-1);
						}
					} else {
						transport.set(-1);
						sweeper.set(-1);
					}
				} else {
					transport.set(0);
					sweeper.set(0);
				}
			}
		}
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	@Override
	public void operatorControl() {
		// encoder.initEncoders();
		data.pixyGearProc.pixyTestReset();
		data.pixyShooterProc.pixyTestReset();
		gyro.resetGyro();
		driveEncoders.initEncoders();
		lightOn = RobotMap.lightOnInitial;
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
			SmartDashboard.putNumber("Left Ultra", data.ultraDistanceLeft());
			SmartDashboard.putNumber("Right Ultra", data.ultraDistanceRight());
			data.pixyShooterProc.pixyShooterI2CTest();
			data.pixyShooterProc.pixyShooterTest();
			data.pixyGearProc.pixyGearI2CTest();
			data.pixyGearProc.pixyGearTest();

			AVG = data.shooterRPM();
			// AVG = bank.flywheel.getRate();
			if (xBox.getRawButton(1)) {
				if (data.ultraDistanceLeft() > 16 && data.ultraDistanceRight() > 16) {
					if (pixyGearFunc.turnAndGoStraightAuton()) {
						robotDriver.drive(0.25, 0, 1);
					}
				} else {
					ultraFunctions.driveFowardAuton(8);
				}
			} else if (xBox.getRawButton(RobotMap.loaderTurnButton)) {
				// gyro.moveDegreesAuton(-60, 0);
			} else {
				// gyro.resetGyro();
				robotDriver.drive(xBox.getRawAxis(1) * -1, xBox.getRawAxis(4) * .6, 1);
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
			}else if(stick.getRawButton(10)){
				shooter.set(.5);
			}
			else {
			
				autoTrack = false;
				shooter.set(0);
			}

			if (stick.getRawButton(4) || xBox.getRawButton(5) || autoTrack) {
				if (shootMode == 1) {
					pixyShooterFunc.alignShooterXCenter();
				} else {
					pixyShooterFunc.alignShooterX();
				}
			} else if (Math.abs(stick.getZ()) > .15) {
				turret.set(stick.getZ() / 3.5);
				SmartDashboard.putNumber("Stick Turn", stick.getZ() / stick.getThrottle());
			} else if (stick.getRawButton(12)) {
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

			SmartDashboard.putNumber("Encoder Rate", data.shooterRPM());

			if (stick.getPOV() == 180) {
				shooterServo.increment(-.1);
			} else if (stick.getPOV() == 0) {
				shooterServo.increment(.1);
			}

			SmartDashboard.putNumber("ServoDude", shooterServo.position);

			lightOn = true;
			
//			if (lightOn) {
//				exampleRelay.set(Relay.Value.kReverse);
//			} else {
//				exampleRelay.set(Relay.Value.kOff);
//			}
		}

	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test() {

		relay.set(Relay.Value.kForward);

	}

}
