package org.cbot.game;

import java.awt.Point;

public class ScreenArea {
	
	public static final ScreenArea GAME_SCREEN = new ScreenArea(4, 4, 516, 340);
	
	private int x, y, x2, y2;
	
	public ScreenArea(int x, int y, int x2, int y2) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public ScreenArea(int centerX, int centerY, int distance) {
		this.x = centerX - distance;
		this.y = centerY - distance;
		this.x2 = centerX + distance;
		this.y2 = centerY + distance;
	}
	
	public Point getCenterPoint() {
		return new Point(x + ((x2 - x) / 2), y + ((y2 - y) / 2));
	}
	
	public Point getRandomPoint(int inner) {
		return new Point(random(x + inner, x2 - inner), random(y + inner, y2 - inner));
	}
	
	public Point getRandomPoint() {
		return getRandomPoint(0);
	}
	
	public int[] getBounds() {
		return new int[] { x, y, x2, y2 };
	}
	
	public int[] getRectangle() {
		return new int[] { x, y, x2-x, y2-y };
	}
	
	public int getWidth() {
		return x2 - x;
	}
	
	public int getHeight() {
		return y2 - y;
	}
	
	public ScreenArea shrink(int amount) {
		int xAmt = (getWidth() / amount);
		int yAmt = (getHeight() / amount);
		return new ScreenArea(x + xAmt, y + yAmt, x2 - xAmt, y2 - yAmt);
	}
	
	private int random(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}

}
