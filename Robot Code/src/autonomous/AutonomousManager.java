package autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import interfaces.AutFunction;
import util.ClockRegulator;;

public class AutonomousManager {
	private final ArrayList<AutFunction> functions;
	private final ClockRegulator clockRegulator;
	private int step;
	private boolean done;

	public AutonomousManager(ClockRegulator clockRegulator) {
		functions = new ArrayList<AutFunction>();
		this.clockRegulator = clockRegulator;
		step = 0;
	}

	public void update() {
		if (step < functions.size()) {
			functions.get(step).update(clockRegulator.getMsPerUpdate());
			if (functions.get(step).isDone()) {
				step++;
			}
		} else {
			if (!done) {
				Robot.nBroadcaster.println("Auton end");
				done = true;
			}
		}
		clockRegulator.sync();
	}

	public void add(AutFunction f) {
		functions.add(f);
	}

}
