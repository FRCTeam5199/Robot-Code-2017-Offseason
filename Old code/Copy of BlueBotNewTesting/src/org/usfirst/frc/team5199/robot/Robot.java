package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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
	int step = 0;
	public Victor right = DataBank.right, left = DataBank.left;
	CANTalon turret = new CANTalon(1);
	CANTalon shooter = new CANTalon(0);
	EncoderShooterFunctions shooterFunc = new EncoderShooterFunctions(shooter, turret);
	DataBank bank = shooterFunc.bank;
	Joystick stick = new Joystick(1);
	Joystick xBox = new Joystick(0);
	Relay exampleRelay ;
//	= new Relay(0);
	// GyroFunctions gyro = new GyroFunctions(right, left);
	boolean lightOn = false;
	UltrasonicFunctions ultraFunctions;
	int PWMSheez = 0;
	double EncoderNum, count = 0;
	double JagPow = 0;
	private Relay relay;
	private PixyFunctions pixyGearFunc, pixyShooterFunc;
	RobotDrive Driver = new RobotDrive(right, left);
	Servo shooterServo = new Servo(RobotMap.shooterServo);
	ServoDude hood = new ServoDude(shooterServo);
	boolean shooting = false;
	public static int counter = 0;
	public static long start = 0;
	public static boolean leftTurn = true;
	int autoStep = 0;
	double shootMode = 0;
	boolean autoTrack = false;

	public Robot() {
	}

	@Override
	public void robotInit() {
		SmartDashboard.putNumber("Auto Mode:", 0);
		ultraFunctions = new UltrasonicFunctions(bank.ultraData, right, left);
		pixyGearFunc = new PixyFunctions(bank.pixyGear, ultraFunctions, bank.driveEncoders, Driver);
		pixyShooterFunc = new PixyFunctions(bank.pixyShooter, turret);
		// CameraServer.getInstance().startAutomaticCapture();
		JagPow = 0;
		bank.timeReset();

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
		int autoMode = 0;
		while (isAutonomous() && isEnabled()) {
			autoMode = (int) SmartDashboard.getNumber("Auto Mode:");
			if (autoMode == 1) {
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

			} else if (autoMode == 2) {
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
			} else if (autoMode == 3) {
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

	/**
	 * Runs the motors with arcade steering.
	 */

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
		relay = new Relay(0);
		while (isOperatorControl() && isEnabled()) {
			
//			data.pixyGearProc.pixyTest();
//			data.pixyGearProc.pixyI2CTest();
//			data.pixyShooterProc.pixyTest();
//			data.pixyShooterProc.pixyI2CTest();
//			SmartDashboard.putNumber("Ultra Right", data.ultraDistanceRight());
//			SmartDashboard.putNumber("Ultra Left", data.ultraDistanceLeft());

			// gets the speed of the flywheel
			AVG = bank.shooterRPM();
			SmartDashboard.putNumber("Shooter RPMS FAM", AVG);


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
			}else if(stick.getRawButton(10)){
				shooter.set(.5);
			}
			else {
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
//			if (stick.getRawButton(1) || xBox.getRawAxis(3) != 0 || stick.getRawButton(3)) {
//				sweeper.set(-1);
//			} else {
//				sweeper.set(0);
//			}

			// Ball transport only active when trigger is held
			if (stick.getRawButton(1)) {
//				transport.set(-1);
			} else {
//				transport.set(0);
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
			// }			// TODO: We've been having problems where we can only move one servo
			// at a time
			// All servo inputs maniplate just one servo
			// Need to get to the bottom of that and find out how we can move
			// two servos separately

			// Manipulates the servo up or down
			if (stick.getPOV(0) == 0) {
//				shooterServo.increment(-1);
			} else if (stick.getPOV(0) == 180) {
//				shooterServo.increment(1);
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
				//Testing Button
				if(step == 0){
//					driveEncoders.initEncoders();
					step = 1;
				}else{
//				driveEncoders.driveStraightAuton(16);
				}
			}

			// Pressing the X Button and moving the left stick will activate
			// a 110 degree turn
			else if (xBox.getRawButton(RobotMap.loaderTurnButton)) {
				if (Math.abs(xBox.getRawAxis(RobotMap.loaderTurnAxis)) >= .25) {
//					// by reaching this point, driver pressed button and pulled
//					// stick.
//					// this is for quick turn on robot.
//					// buttons must be released for another quick turn
//					if (step == 0) {
//						currAngle = GyroFunctions.getAngle();
//						EncoderDriveFunctions.initEncoders();
//						step = 1;
//					}
//					if (step == 1) {
//						if (xBox.getRawAxis(RobotMap.loaderTurnAxis) > .25) {
//							left = true;
//						} else if (xBox.getRawAxis(RobotMap.loaderTurnAxis) < -.25) {
//							left = false;
//						}
//						// if (driveEncoders.loaderTurn(currAngle, left)){
//						if (EncoderDriveFunctions.autonSixtyDegreeTurn(currAngle, !left)) {
//							step = 2;
//						}
//						if (step == 2) {
//							robotDriver.stop();
//						}
//					}
				} else {
					Driver.stop();
				}

				// Pressing Y will invert the robot controls
				// for easier reverse driving
			} else if (xBox.getRawButton(RobotMap.flipperButt)) {
				if (step == 0) {
					step = 1;
//					if (driveMod == 1) {
//						driveMod = -1;
//					} else {
////						driveMod = 1;
//					}
				}
			}

			// Defaults to manual driving. Also resets all steps and gyro
			else {
				SmartDashboard.putString("Encoder Test", "unused");
//				GyroFunctions.resetGyro();
				step = 0;
				RobotDrive.drive(xBox.getRawAxis(1) * 1, xBox.getRawAxis(4) * 1, 1);
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
//			if (stick.getRawButton(6) || stick.getRawButton(5)) {
//				climber.set(1);
//			} else if (xBox.getRawAxis(2) != 0) {
//				climber.set(xBox.getRawAxis(2));
//			} else {
//				climber.set(0);
//			}
			// if(stick.getRawButton(6)|| stick.getRawButton(5)){
			// if(climbTime == 0){
			// climbTime = System.currentTimeMillis();
			// }
			// if(System.currentTimeMillis() - climbTime > )
			// }
			// else{
			// climbTime = 0;
			// }
//			SmartDashboard.putNumber("Current5", pdp.getCurrent(5));

//			if (xBox.getRawButton(8)) {
//				solenoid.set(DoubleSolenoid.Value.kForward);
//			} else {
//				solenoid.set(DoubleSolenoid.Value.kOff);
//			}
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
