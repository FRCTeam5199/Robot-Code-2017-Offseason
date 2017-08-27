package org.usfirst.frc.team5199.robot;

public class AccelerationAndDecleration {
	long time = 0;
	double power = 0;
	double basePower = .25;
	double powerIncrement = .05;
	double powerDecrement = .05;
	int timeInterval = 100;
	boolean firstRun = true;
	double mod = 15;

	public double Decelerate(double currentEncoderRevs, double goalStopDistanceInches) {
		double modSpeed = currentEncoderRevs / mod;
		if (currentEncoderRevs > 0 && goalStopDistanceInches > 0) {
			if (modSpeed >= goalStopDistanceInches && firstRun) {
				power = 0;
				firstRun = false;
			} else if (!firstRun) {
				if (goalStopDistanceInches < modSpeed) {
					power -= powerDecrement;
				} else if (goalStopDistanceInches > modSpeed) {
					power += powerDecrement;
				}
			}
		}

		return power;
	}

	public double Accelerate(double currentPower) {
		if (time == 0) {
			currentPower = basePower;
			time = System.currentTimeMillis();
		} else {
			if (System.currentTimeMillis() - time > timeInterval) {
				time = System.currentTimeMillis();
				currentPower += powerIncrement;
			}
		}
		return currentPower;
	}

	public double Accelerate(double currentPower, double goalPower) {
		// Less than instead of equal to account for large possibility that
		// current power does not equal goal power, but is slightly above, possibly add
		// same thing for slightly under, however .05 power should not make a big
		// difference
		if (currentPower < goalPower) {
			if (time == 0) {
				currentPower = basePower;
				time = System.currentTimeMillis();
			} else {
				if (System.currentTimeMillis() - time > timeInterval) {
					time = System.currentTimeMillis();
					currentPower += powerIncrement;
				}
			}
			return currentPower;
		} else {
			return currentPower;
		}
	}

	public void reset() {
		time = 0;
		firstRun = true;
	}
}
