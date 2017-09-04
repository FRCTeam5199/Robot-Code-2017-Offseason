package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import sensors.DataBank;

public class ShooterSpeed{
	CANTalon shooterMotor;
	double p, i, d, previousSpeed, integral;
	public ShooterSpeed() {
		p = .05;
		i = .0001;
		d = .001;
	}

	public void shoot(double speed) {
		double motorSpeed = 0;
		integral +=(speed - DataBank.shooterRPM());
		if(Math.abs(integral)> 1/i) {
			if(integral>0) {
				integral = 1/i;
			}else {
				integral = -1/i;			}
		}
		motorSpeed = p * (speed - DataBank.shooterRPM());
		motorSpeed += d * (DataBank.shooterRPM()- previousSpeed);
		motorSpeed += i * integral;
		SmartDashboard.putString("WORK MOFO", String.valueOf(motorSpeed));
		Turret.setFlyWheel(motorSpeed);
		previousSpeed = DataBank.shooterRPM();
	}
}
