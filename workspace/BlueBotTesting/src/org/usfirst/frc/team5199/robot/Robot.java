package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;

import java.sql.Driver;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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
 * instead if you're new.
 */
public class Robot extends SampleRobot {
	int step = 0;
	Victor right = new Victor(0), left = new Victor(1);
	Joystick stick = new Joystick(1);
	Joystick xBox = new Joystick(0);
	Relay exampleRelay = new Relay(1);
	boolean lightOn = false;
	long time = System.currentTimeMillis();
	// Servo pwmLED = new Servo(8);
	int PWMSheez = 0;
	Encoder Testarino = new Encoder(4, 3, false, Encoder.EncodingType.k4X);
	double EncoderNum, count = 0;
	Jaguar JagMan = new Jaguar(3);
	double JagPow = 0;
	Pixy pixy = new Pixy(0x51);
	PixyProcess pixySheez = new PixyProcess(pixy);
	RobotDrive Driver = new RobotDrive(right, left);
	boolean shooting = false;
	public static final int ultrasonicArraySize = 50;
	public static boolean firstBufferRight = true;
	public static double sumBufferRight = 0;
	public static int counterRight = 0;
	public static Double[] distanceArrayRight;
	public Robot() {
		CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void robotInit() {
		distanceArrayRight = new Double[ultrasonicArraySize];
		for (int i = 0; i < ultrasonicArraySize; i++) {
			distanceArrayRight[i] = 0.0;
		}
		JagPow = 0;
		time = System.currentTimeMillis();
		Testarino.reset();
		Testarino.setDistancePerPulse(RobotMap.inchesPerRotation / 2);

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

	}

	/**
	 * Runs the motors with arcade steering.
	 */
	@Override
	public void operatorControl() {
		double AVG = 0;
		time = System.currentTimeMillis();
		pixySheez.pixyTestReset();
		while (isOperatorControl() && isEnabled()) {
			Driver.drive(xBox.getRawAxis(1) * -1, xBox.getRawAxis(4) * .6, 1);
			pixySheez.pixyI2CTest();
			pixySheez.pixyTest();
			AVG = this.EncoderAVG();
			if (stick.getRawButton(3)) {
				PWMSheez -= 10;
			}
			if (stick.getRawButton(5)) {
				PWMSheez += 10;
			}
			if (stick.getRawButton(1)) {
				if (step == 0) {
					shooting = !shooting;
					step = 1;
				}
			}
			else{
				step = 0;
			}if (stick.getRawButton(11)) {
				xBox.setRumble(RumbleType.kRightRumble, 1);
				xBox.setRumble(RumbleType.kLeftRumble, 1);
			} else {
				xBox.setRumble(RumbleType.kRightRumble, 0);
				xBox.setRumble(RumbleType.kLeftRumble, 0);
			}
			// if (AVG < 1400) {
			// JagPow += .001;
			// SmartDashboard.putString("Status", "SANIC SPEED GOGOGO");
			//
			// } else if (AVG < 1900) {
			// JagPow += .0005;
			// SmartDashboard.putString("Status", "POWERING DOWN SLOW
			// MOFO");
			// }else if (AVG > 2100) {
			// JagPow -= .0005;
			// SmartDashboard.putString("Status", "POWERING DOWN SLOW
			// MOFO");
			// } else if (AVG > 2400) {
			// JagPow -= .001;
			// SmartDashboard.putString("Status", "POWERING DOWN SLOW
			// MOFO");
			// }else {
			// SmartDashboard.putString("Status", "Finna nut");
			// }
			// if(JagPow < 0){
			// JagPow = 0;
			// }
			// JagMan.set(JagPow);
			SmartDashboard.putBoolean("Shooting", shooting);
			if (shooting) {
				if (AVG < 1500) {
					JagMan.set(1);
					SmartDashboard.putString("Stage:", "1");
				} else if (AVG < 2000) {
					JagMan.set(.8);									
					SmartDashboard.putString("Stage:", "2");
				} else if (AVG < 2300) {
					JagMan.set(.72);
					SmartDashboard.putString("Stage:", "3");
				} else if (AVG < 2600) {
					JagMan.set(.6);
					SmartDashboard.putString("Stage:", "4");
				} else if (AVG < 2950) {
					JagPow += 0.0003;
				JagMan.set(JagPow + .6);
					SmartDashboard.putString("Stage:", "5");
				} else {
					JagMan.set(.1);
					JagPow -= 0.0001;
					SmartDashboard.putString("Stage:", "6");
				}
			} else if (stick.getRawButton(2)) {
				JagMan.set(.5);
			} else {
				JagMan.set(0);
				JagPow = 0;
				SmartDashboard.putString("Stage:", "0");
			}

			SmartDashboard.putNumber("Encoder Rate", AVG);
			SmartDashboard.putNumber("Encoder Dist", Testarino.getDistance());

			// pwmLED.setRaw(PWMSheez);
			// if(stick.getRawButton(10)){
			// exampleRelay.set(Relay.Value.kForward);
			// }
			// else{
			// exampleRelay.set(Relay.Value.kReverse);
			// 
			if (stick.getRawButton(10)) {
				// && (System.currentTimeMillis() - time) > 250) {
				if (step == 0) {
					lightOn = !lightOn;
					time = System.currentTimeMillis();
					step = 1;
				}
			} else {
				step = 0;
			}
			if (lightOn) {
				exampleRelay.set(Relay.Value.kForward);
			} else {
				exampleRelay.set(Relay.Value.kOff);
			}
		}
	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test() {
	}

	public double EncoderAVG() {
		
		double range = Testarino.getRate();
		double result;

		sumBufferRight += range - distanceArrayRight[counterRight];

		distanceArrayRight[counterRight++] = range;

		if (counterRight == ultrasonicArraySize) {
			firstBufferRight = false;
			counterRight = 0;
		}

		if (firstBufferRight) {
			result = sumBufferRight / counterRight;

		} else {
			result = sumBufferRight / ultrasonicArraySize;
		}

		return (result);
	}
}
