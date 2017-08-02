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
		pixyShooterFunc = new PixyFunctions(data.pixyShooter, turret);

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
		// autoMode = SmartDashboard.getString("Autonomous Mode");
		// if (autoMode.equals("Enter Value")) {
		// autoMode = "S";
		// }
		relay.set(Relay.Value.kForward);
		spinUp = RobotMap.spinUpInitial;
		AVG = RobotMap.AVGInitial;
		autoStep = RobotMap.autoStepInitial;
		startTime = System.currentTimeMillis();
		EncoderDriveFunctions.initEncoders();

		while (isAutonomous() && isEnabled()) {
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
			data.pixyGearProc.pixyTest();
			data.pixyGearProc.pixyI2CTest();
			data.pixyShooterProc.pixyTest();
			data.pixyShooterProc.pixyI2CTest();
			SmartDashboard.putNumber("Ultra Right", data.ultraDistanceRight());
			SmartDashboard.putNumber("Ultra Left", data.ultraDistanceLeft());

			// gets the speed of the flywheel
			SmartDashboard.putNumber("Shooter RPMS FAM", data.shooterRPM());
			SmartDashboard.putNumber("Flywheel Rate", data.flywheel.getRate());

			// changes the status of the light
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
			SmartDashboard.putBoolean("Light On", lightOn);

			// changes what speed the driver would like to shoot
			if (stick.getRawButton(7)) {
				shootMode = 1;
			} else if (stick.getRawButton(9)) {
				shootMode = 2;
			} else if (stick.getRawButton(11)) {
				shootMode = 3;
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

			// Stick button to enable the sweeper.
			// Also triggers with XBox right trigger
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

			// Reads "twist" of the joystick to set the Azimuth of shooter
			// if (stick.getRawButton(2) || stick.getRawButton(1)) {
			// pixyFunctionsShooter.alignShooterX();
			// } else
			// if (Math.abs(stick.getRawAxis(1)) > .15) {
			// turret.set(stick.getRawAxis(1) / 6);
			// }
			// else {
			// turret.set(0);
			// } // TODO: We've been having problems where we can only move one
			// servo
			// at a time
			// All servo inputs maniplate just one servo
			// Need to get to the bottom of that and find out how we can move
			// two servos separately

			// Manipulates the servo up or down
			if (stick.getPOV(0) == 0) {
				shooterServo.increment(-1);
			} else if (stick.getPOV(0) == 180) {
				shooterServo.increment(1);
			}

			// ----------------------------------------------------------------------
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// //
			// ------------------------------------------------------------------------
			// // if(xBox.getRawButton(1)){
			// // if(step ==0){
			// // SmartDashboard.putNumber("Init Angle", currAngle);
			// // encoder.initEncoders();
			// // step =1;
			// // }
			// // if(step==1){
			// // if(encoder.calibrateTurnWithGyrosAndEncoders()){
			// // step =2;
			// // }
			// // if(step==2){
			// // SmartDashboard.putNumber("Change in angle",
			// // gyro.getAngle()-currAngle);
			// // robotDriver.stop();
			// // }
			// // }
			// // }else{
			// // currAngle = gyro.getAngle();
			// // step =0;
			// // robotDriver.stop();
			// // }
			// //
			// ----------------------------------------------------------------------
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// // TODO do not delete this code use for calibration of gyro at
			// // competition
			// // TODO
			// //
			// ------------------------------------------------------------------------

			if (xBox.getRawButton(1)) {
				// THIS CODE FUCKIN WORKS FOR THE PIXY CENTERING
				// HOLY SHIT WE DID IT BOYS WE REALLY FUCKIN DID IT
				// Testing Button
				if (step == 0) {
					driveEncoders.initEncoders();
					step = 1;
				} else {
					driveEncoders.driveStraightAuton(16);
				}
			}

			// Pressing the X Button and moving the left stick will activate
			// a 110 degree turn
			else if (xBox.getRawButton(RobotMap.loaderTurnButton)) {
				if (Math.abs(xBox.getRawAxis(RobotMap.loaderTurnAxis)) >= .25) {
					// by reaching this point, driver pressed button and pulled
					// stick.
					// this is for quick turn on robot.
					// buttons must be released for another quick turn
					if (step == 0) {
						currAngle = GyroFunctions.getAngle();
						EncoderDriveFunctions.initEncoders();
						step = 1;
					}
					if (step == 1) {
						if (xBox.getRawAxis(RobotMap.loaderTurnAxis) > .25) {
							left = true;
						} else if (xBox.getRawAxis(RobotMap.loaderTurnAxis) < -.25) {
							left = false;
						}
						// if (driveEncoders.loaderTurn(currAngle, left)){
						if (EncoderDriveFunctions.autonSixtyDegreeTurn(currAngle, !left)) {
							step = 2;
						}
						if (step == 2) {
							robotDriver.stop();
						}
					}
				} else {
					robotDriver.stop();
				}

				// Pressing Y will invert the robot controls
				// for easier reverse driving
			} else if (xBox.getRawButton(RobotMap.flipperButt)) {
				if (step == 0) {
					step = 1;
					if (driveMod == 1) {
						driveMod = -1;
					} else {
						driveMod = 1;
					}
				}
			}

			// Defaults to manual driving. Also resets all steps and gyro
			else {
				SmartDashboard.putString("Encoder Test", "unused");
				GyroFunctions.resetGyro();
				step = 0;
				RobotDrive.drive(xBox.getRawAxis(1) * straightMod, xBox.getRawAxis(4) * turnMod, driveMod);
			}

			// Watkins conditioning apparatus
			if (stick.getRawButton(12) && stick.getRawButton(10)) {
				xBox.setRumble(RumbleType.kRightRumble, 1);
				xBox.setRumble(RumbleType.kLeftRumble, 1);
			} else {
				xBox.setRumble(RumbleType.kRightRumble, 0);
				xBox.setRumble(RumbleType.kLeftRumble, 0);
			}

			// Activates the climber, giving the Operator stick priority
			if (stick.getRawButton(6) || stick.getRawButton(5)) {
				climber.set(1);
			} else if (xBox.getRawAxis(2) != 0) {
				climber.set(xBox.getRawAxis(2));
			} else {
				climber.set(0);
			}
			// if(stick.getRawButton(6)|| stick.getRawButton(5)){
			// if(climbTime == 0){
			// climbTime = System.currentTimeMillis();
			// }
			// if(System.currentTimeMillis() - climbTime > )
			// }
			// else{
			// climbTime = 0;
			// }
			SmartDashboard.putNumber("Current5", pdp.getCurrent(5));

			if (xBox.getRawButton(8)) {
				solenoid.set(DoubleSolenoid.Value.kForward);
			} else {
				solenoid.set(DoubleSolenoid.Value.kOff);
			}
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
