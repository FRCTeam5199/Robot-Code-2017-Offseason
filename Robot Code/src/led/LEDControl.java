package led;

public class LEDControl {
	private final int OFF = 0x000;
	private final int RED = 0x001;
	private final int GREEN = 0x010;
	private final int YELLOW = 0x011;
	private final int BLUE = 0x100;
	private final int MAGENTA = 0x101;
	private final int CYAN = 0x110;
	private final int WHITE = 0x111;

	private final LED led;

	public LEDControl(LED led) {
		this.led = led;
	}

	public void setColor(int color) {
		led.setR((color & 0x01) != 0);
		led.setG((color & 0x02) != 0);
		led.setB((color & 0x04) != 0);
	}
}
