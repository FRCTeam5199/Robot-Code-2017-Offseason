package autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import climber.Climber;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import intake.Intake;
import interfaces.AutFunction;
import turret.Turret;
import util.ClockRegulator;
import drive.DriveBase;

public class AutonomousManager {
	private final ArrayList<AutFunction> functions;
	private final ClockRegulator clockRegulator;

	private final Stop stop;

	private int step;
	private boolean done;

	public AutonomousManager(DriveBase base, Turret turret, Intake intake, Climber climber,
			ClockRegulator clockRegulator) {
		this.clockRegulator = clockRegulator;

		stop = new Stop(base, turret, intake, climber);

		functions = new ArrayList<AutFunction>();
		step = 0;
	}

	public void init() {
		functions.get(0).init();
		stop.update(1);
	}

	public void update() {
		if (step < functions.size()) {
			functions.get(step).update(clockRegulator.getMsPerUpdate());
			if (functions.get(step).isDone()) {
				Robot.nBroadcaster.println(functions.get(step).getClass().getName() + " end\t");
				stop.update(1);
				step++;
				if (step < functions.size()) {
					functions.get(step).init();
				}
			}
		} else {
			if (!done) {
				Robot.nBroadcaster.println("AutonomousManager finished");
				stop.update(1);
				done = true;
			}
		}
		clockRegulator.sync();
	}

	public void add(AutFunction f) {
		functions.add(f);
	}

	public boolean isDone() {
		return done;
	}

}
