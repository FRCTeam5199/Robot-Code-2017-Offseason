package pixy;

import maths.Vector2I;

public class PixyBlock {
	private final int checkSum;
	private final int signatureNum;
	private final boolean sumMatch;
	private final Vector2I pos;
	private final Vector2I dim;

	/**
	 * @param data
	 *            The raw data from the Pixycam
	 */
	public PixyBlock(byte[] data) {
		checkSum = getWord(data, 0);
		signatureNum = getWord(data, 2);
		pos = new Vector2I(getWord(data, 4), getWord(data, 6));
		dim = new Vector2I(getWord(data, 8), getWord(data, 10));

		sumMatch = (checkSum == signatureNum + pos.getX() + pos.getY() + dim.getX() + dim.getY());
	}

	/**
	 * @param data
	 *            Raw data to get a word from
	 * @param s
	 *            The index of the first byte of the word
	 * @return A word (in an integer) combining the byte at "s" and the next
	 *         byte
	 */
	private int getWord(byte[] data, int s) {
		int c = data[s];
		int w = data[s + 1];
		w = w << 8;
		return w | c;
	}

	public boolean checkSum() {
		return sumMatch;
	}

	public int getSignatureNum() {
		return signatureNum;
	}

	public Vector2I getPos() {
		return pos;
	}

	public Vector2I getPosCenter() {
		return Vector2I.subtract(pos, Pixycam.getCenter());
	}

	public Vector2I getDim() {
		return dim;
	}

	public String toString() {
		return checkSum + "\n\t" + signatureNum + "\n\t" + pos.getX() + "\n\t" + pos.getY() + "\n\t" + dim.getX()
				+ "\n\t" + dim.getY();
	}
}
