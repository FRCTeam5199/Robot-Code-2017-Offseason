package pixy;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.I2C;
import maths.Vector2I;
import util.ClockRegulator;

public class Pixycam implements Runnable {
	private static final Vector2I resolution = new Vector2I(1280, 800);
	private static final Vector2I center = Vector2I.divide(resolution, 2);

	private final int frequency = 50;

	private final Thread t;
	private final ClockRegulator regulator;
	private boolean isAlive;

	private final I2C i2c;
	private final ArrayList<PixyBlock> blocks;
	private final int address;

	private final int startWord = 0xaa55;
	private final int startWordColor = 0xaa56;
	private final int startWordX = 0x55aa;

	private boolean newData;

	/**
	 * @param address
	 *            The address of the pixycam on the I2C bus
	 */
	public Pixycam(int address) {
		this.address = address;
		i2c = new I2C(I2C.Port.kMXP, address);
		regulator = new ClockRegulator(50);
		blocks = new ArrayList<PixyBlock>();
		t = new Thread(this, "PixyCam " + address);

		start();
	}

	@Override
	public void run() {
		// initialize with some meaningless numbers
		int lastWord = 0xffff;
		int word = 0xffff;

		while (isAlive) {
			word = getWord();

			if (word == 0 && lastWord == 0) {
				// nothing
				// this empty if statement might reduce cpu usage...
			} else if (word == startWord && lastWord == startWord) {
				// normal block frame start
				// make sure we don't get concurrent read/write errors
				synchronized (blocks) {
					blocks.clear();
					blocks.add(new PixyBlock(getBlockData()));
					while (getWord() == startWord) {
						blocks.add(new PixyBlock(getBlockData()));
					}
				}
				newData = true;
			} else if (word == startWordColor && lastWord == startWord) {
				// color block frame start
				blocks.clear();
			} else if (word == startWordX) {
				// out of sync!
				getByte();
			}

			// regulator.sync();
		}
	}

	public ArrayList<PixyBlock> getBlocks() {
		return blocks;
	}

	public boolean newData() {
		if (newData) {
			newData = false;
			return true;
		} else {
			return false;
		}
	}

	public void setLedColor(int color) {
		writeWord(0xfd00);
		writeWord(color >> 16);
		writeWord((color & 0x00ff00) >> 8);
		writeWord(color & 0x0000ff);
	}

	private int getWord() {
		int c = getByte();
		int w = getByte();
		w = w << 8;
		return w | c;
	}

	private byte getByte() {
		byte[] buffer = new byte[1];
		i2c.readOnly(buffer, 1);
		return buffer[1];
	}

	private byte[] getBlockData() {
		byte[] buffer = new byte[12];
		i2c.readOnly(buffer, 12);
		return buffer;
	}

	private void writeWord(int word) {
		byte c = (byte) (word & 0x00ff);
		byte w = (byte) (word >> 8);
		byte[] data = { c, w };
		i2c.writeBulk(data);
	}

	public void start() {
		isAlive = true;
		t.start();
	}

	public void stop() {
		isAlive = false;
	}

	public static Vector2I getResolution() {
		return resolution;
	}

	public static Vector2I getCenter() {
		return center;
	}

}
