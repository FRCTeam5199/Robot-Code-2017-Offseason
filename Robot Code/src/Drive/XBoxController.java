package Drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class XBoxController {
	private Joystick stick;

	public XBoxController(int n) {
		stick = new Joystick(n);
	}
	
	public double getStickLX(){
		return stick.getRawAxis(0);
	}
	
	public double getStickLY(){
		return -stick.getRawAxis(1);
	}
	
	public double getStickRX(){
		return stick.getRawAxis(4);
	}
	
	public double getStickRY(){
		return -stick.getRawAxis(5);
	}
	
	public double getLTrigger(){
		return stick.getRawAxis(2);
	}
	
	public double getRTrigger(){
		return stick.getRawAxis(3);
	}
	
	public boolean getButton(int n){
		return stick.getRawButton(n);
	}
	
	public void setLRumble(double n){
		stick.setRumble(RumbleType.kLeftRumble, n);
	}
	
	public void setRRumble(double n){
		stick.setRumble(RumbleType.kRightRumble, n);
	}
}
