package sensors;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import maths.Vector2;
import util.ClockRegulator;

public class Location implements Runnable {
	private final Thread t;
	private final ClockRegulator regulator;

	private final BuiltInAccelerometer accelerometer;
	private final Encoder wheelsLeft;
	private final Encoder wheelsRight;

	private final double Gtofmsms;

	private boolean isAlive;
	private Vector2 acceleration;
	private Vector2 velocity;
	private Vector2 location;

	public Location() {
		t = new Thread(this, "location");
		regulator = new ClockRegulator(200);

		accelerometer = Robot.sensors.getAccelerometer();
		wheelsLeft = Robot.sensors.getLeftWheelEncoder();
		wheelsRight = Robot.sensors.getRightWheelEncoder();

		location = Vector2.ZERO.clone();
		velocity = Vector2.ZERO.clone();
		acceleration = Vector2.ZERO.clone();

		// Gtofmsms = 0.0032174049;
		// constant that converts G's to feet per millisecond^2
		Gtofmsms = 3.108095e-8;
		// Gtofmsms = 0.00000003108095;

		this.start();
	}

	public void start() {
		isAlive = true;
	}

	public void stop() {
		isAlive = false;
	}

	@Override
	public void run() {
		while (isAlive) {
			acceleration.setX(-accelerometer.getX() * Gtofmsms);
			acceleration.setY(-accelerometer.getZ() * Gtofmsms);
			acceleration = Vector2.multiply(acceleration, regulator.getMsPerUpdate());

			velocity = Vector2.add(Vector2.multiply(acceleration, regulator.getMsPerUpdate()), velocity);

			location = Vector2.add(Vector2.multiply(velocity, regulator.getMsPerUpdate()), location);

			regulator.sync();
		}

	}

	public Vector2 getLocation() {
		return location.clone();
	}

	public Vector2 getVelocity() {
		return velocity.clone();
	}

	public Vector2 getAcceleration() {
		return acceleration.clone();
	}
}
