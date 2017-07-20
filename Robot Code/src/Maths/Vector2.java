/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maths;

/**
 *
 * @author 18wakayamats
 */
public class Vector2 implements Comparable {

	public static final Vector2 ZERO = new Vector2(0, 0);
	public static final Vector2 FORWARDS = new Vector2(0, 1);
	public static final Vector2 BACKWARDS = new Vector2(0, -1);
	public static final Vector2 LEFT = new Vector2(-1, 0);
	public static final Vector2 RIGHT = new Vector2(1, 0);

	private double x;
	private double y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double length() {
		return Math.sqrt((x * x) + (y * y));
	}

	public Vector2 clone() {
		return new Vector2(x, y);
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public int compareTo(Object t) {
		Vector2 v = (Vector2) t;
		if (x == v.x) {
			if (y < v.y) {
				return -1;
			} else if (y > v.y) {
				return 1;
			} else {
				return 0;
			}
		}
		if (x < v.x) {
			return -1;
		} else if (x > v.x) {
			return 1;
		} else {
			return 0;
		}

	}

	public static Vector2 add(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2 subtract(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}

}
