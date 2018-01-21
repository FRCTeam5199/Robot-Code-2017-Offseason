package pixy;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.I2C;
import maths.Vector2I;
import util.ClockRegulator;

public class Pixycam implements Runnable {
	private static final Vector2I resolution = new Vector2I(320, 200);
	private static final Vector2I center = Vector2I.divide(resolution, 2);

	private final int frequency = 200;
	private final long blockTimeout = 1000;

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
	private long timeSinceData;

	/**
	 * @param address
	 *            The address of the pixycam on the I2C bus
	 */
	public Pixycam(int address) {
		this.address = address;
		i2c = new I2C(I2C.Port.kMXP, address);
		regulator = new ClockRegulator(frequency);
		blocks = new ArrayList<PixyBlock>();
		t = new Thread(this, "PixyCam " + address);

		timeSinceData = System.currentTimeMillis();
		start();
	}

	@Override
	public void run() {
		// initialize with some meaningless numbers
		int lastWord = 0xffff;
		int word = 0xffff;

		while (isAlive) {

			if (timeSinceData + blockTimeout < System.currentTimeMillis()) {
				timeSinceData = System.currentTimeMillis();
				System.err.println("No data from pixycam " + address + ". Is the address correct?");
			}

			word = getWord();
			if (word == 0 && lastWord == 0) {
				// nothing
				// this empty if statement might reduce cpu usage...
			} else if (word == startWord && lastWord == startWord) {
				// normal block frame start
				timeSinceData = System.currentTimeMillis();
				synchronized (blocks) {
					blocks.clear();
					//blocks.add(new PixyBlock(getBlockData()));
					PixyBlock pb1 = new PixyBlock(getBlockData());
					if (pb1.checkSum()) {
						blocks.add(pb1);
						Robot.nBroadcaster.println("Pixycam " + address + ": new block");
					} else {
						Robot.nBroadcaster.println("Pixycam " + address + ": invalid checksum!");
					}
					while (getWord() == startWord) {
						PixyBlock pb = new PixyBlock(getBlockData());
						if (pb.checkSum()) {
							blocks.add(pb);
							Robot.nBroadcaster.println("Pixycam " + address + ": new block");
						} else {
							Robot.nBroadcaster.println("Pixycam " + address + ": invalid checksum!");
						}
					}
				}
				newData = true;
			} else if (word == startWordColor && lastWord == startWord) {
				// color block frame start
				timeSinceData = System.currentTimeMillis();
				blocks.clear();
			} else if (word == startWordX) {
				// out of sync!
				getByte();
			}

			lastWord = word;

			regulator.sync();
		}
	}

	public PixyBlock[] getBlocks() {
		PixyBlock[] output = new PixyBlock[blocks.size()];
		blocks.toArray(output);
		return output;
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
		writeByte((byte) (color >> 16));
		writeByte((byte) ((color & 0x00ff00) >> 8));
		writeByte((byte) (color & 0x0000ff));
	}

	private int getWord() {
		int c = getByte();
		int w = getByte();
		w = w << 8;

		// we have to return an int because java doesn't have unsigned shorts
		return w | c;
	}

	private short getByte() {
		byte[] data = new byte[1];
		i2c.readOnly(data, 1);
		// we have to return a short because java doesn't have unsigned bytes
		// ughs
		return (short) (data[0] & 0xff);
	}

	private short[] getBlockData() {
		byte[] buffer = new byte[12];
		i2c.readOnly(buffer, 12);

		// have to return short[] because hava doesn't have unsigned bytes
		short[] output = new short[12];
		for (int i = 0; i < 12; i++) {
			output[i] = (short) (buffer[i] & 0xff);
		}
		return output;
	}

	private void writeByte(byte b) {
		byte[] data = { b };
		i2c.writeBulk(data);
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
