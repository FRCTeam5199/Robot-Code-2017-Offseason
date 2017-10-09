package dashboard;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class DriverCamera {
	
	public DriverCamera(UsbCamera camera) {
		camera = CameraServer.getInstance().startAutomaticCapture(0);
		camera.setResolution(640, 360);
		camera.setFPS(15);
	}
	
}
