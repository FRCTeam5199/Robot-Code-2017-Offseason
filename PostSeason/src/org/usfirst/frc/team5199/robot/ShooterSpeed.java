package org.usfirst.frc.team5199.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class ShooterSpeed extends PIDSubsystem{
	DataBank bank;
	CANTalon shooterMotor;
	public ShooterSpeed(CANTalon shooterMotor, DataBank bank) {
		super("ShooterSpeed", .001, .001, .001);
		setAbsoluteTolerance(RobotMap.tolerance);
		getPIDController().setContinuous(false);
		this.bank = bank;
		this.shooterMotor = shooterMotor;
	}

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return bank.shooterRPM();
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
		shooterMotor.pidWrite(output);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
