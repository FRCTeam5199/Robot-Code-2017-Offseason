package sensors;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import pixy.Pixy;
import pixy.PixyProcess;

public class Sensors {
	private final ADXRS450_Gyro gyro;
	private final BuiltInAccelerometer accelerometer;
	private static Encoder flywheelEncoder;
	private final Encoder wheelsLeft;
	private final Encoder wheelsRight;
	private static UltrasonicData ultraData;
	private static Pixy pixyGear, pixyShooter;
	private static PixyProcess pixyGearProc, pixyShooterProc;
	public static CircularAverageBuffer flywheelAVG;
	public static CircularAverageBuffer shooterXAVG;
	
	public Sensors() {
		// init flywheel encoder
		flywheelEncoder = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		flywheelEncoder.reset();
		flywheelEncoder.setDistancePerPulse(RobotMap.rotationsPerStepShooter);

		// init wheel encoders
		wheelsRight = new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false,
				Encoder.EncodingType.k4X);
		wheelsRight.reset();
		wheelsRight.setDistancePerPulse(-RobotMap.inchesPerPulse);
		
		wheelsLeft = new Encoder(RobotMap.encoderLeftDIOA, RobotMap.encoderLeftDIOB, false, Encoder.EncodingType.k4X);
		wheelsLeft.reset();
		wheelsLeft.setDistancePerPulse(RobotMap.inchesPerPulse);
		
		// init accelerometer
		accelerometer = new BuiltInAccelerometer(Range.k8G);

		// init gyro
		gyro = new ADXRS450_Gyro();
		Robot.nBroadcaster.println("Calibrating Gyro...");
		gyro.calibrate();
		Robot.nBroadcaster.println("Done!");
		
		ultraData = new UltrasonicData(RobotMap.ultraRightEcho,RobotMap.ultraRightPing,RobotMap.ultraLeftEcho,RobotMap.ultraLeftPing);
		
		pixyGear = new Pixy(0x51);
		pixyGearProc = new PixyProcess(pixyGear);
		pixyShooter = new Pixy(0x53);
		pixyShooterProc = new PixyProcess(pixyShooter);
		
		flywheelAVG = new CircularAverageBuffer(75);
		shooterXAVG = new CircularAverageBuffer(10);
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public BuiltInAccelerometer getAccelerometer() {
		return accelerometer;
	}

	public Encoder getFlywheelEncoder() {
		return flywheelEncoder;
	}

	public Encoder getLeftWheelEncoder() {
		return wheelsLeft;
	}

	public Encoder getRightWheelEncoder() {
		return wheelsRight;
	}
	public static double ultraDistanceRight(){
		return ultraData.distanceRight();
	}
	public static double ultraDistanceLeft(){
		return ultraData.distanceLeft();
	}
	public static double pixyGearXPosCompensated(){
		return pixyGearProc.compensatedGearPixyData();
	}
	public static double pixyGearXPos(){
		return pixyGearProc.averageData(0, false)[0];
	}
	public static double pixyShooterXPos(){
		return pixyShooterProc.shooterData()[0];
	}
	public static double shooterRPM(){
		return flywheelAVG.DataAverage(flywheelEncoder.getRate());
	}
	public static double shooterX(){
		return shooterXAVG.DataAverage(pixyShooterXPos());
	}
}
