package led;

import edu.wpi.first.wpilibj.Solenoid;

public class LED {

	private final Solenoid r;
	private final Solenoid g;
	private final Solenoid b;

	public LED(int r, int g, int b) {
		this.r = new Solenoid(r);
		this.g = new Solenoid(g);
		this.b = new Solenoid(b);
	}

	public void setR(boolean value) {
		r.set(value);
	}

	public void setG(boolean value) {
		g.set(value);
	}

	public void setB(boolean value) {
		b.set(value);
	}
}
