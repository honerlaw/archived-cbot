package org.cbot.image;

import java.awt.Point;
import java.awt.image.DataBufferInt;

import org.cbot.Bot;
import org.cbot.event.Mouse;
import org.cbot.game.ScreenArea;

public class ImageTool {
	
	public static Point getPixelLocInArea(int pixel, double tolerance, int tx, int ty, int bx, int by) {
		for(int x = tx; x <= bx; x++) {
			for(int y = ty; y <= by; y++) {
				if(isInTolerance(getPixel(x, y), pixel, tolerance)) {
					return new Point(x, y);
				}
			}
		}
		return null;
	}
	
	public static Point getPixelLocInArea(int[] pixels, double tolerance, ScreenArea area) {
		int[] bounds = area.getBounds();
		return getPixelLocInArea(pixels, tolerance, bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	
	public static Point getPixelLocInArea(int[] pixels, double tolerance, int tx, int ty, int bx, int by) {
		for(int x = tx; x <= bx; x++) {
			for(int y = ty; y <= by; y++) {
				int pixel = getPixel(x, y);
				for(int a : pixels) {
					if(isInTolerance(pixel, a, tolerance)) {
						return new Point(x, y);
					}
				}
			}
		}
		return null;
	}
	
	public static int[] getPixelsInSection(int x, int y, int x2, int y2) {
		int size = (x2 - x) * (y2 - y);
		if(size < 0)
			return new int[0];
		int[] temp = new int[size];
		int count = 0;
		for(int yy = y; yy < y2; yy++) {
			for(int xx = x; xx < x2; xx++) {
				if(xx >= Bot.WIDTH || yy >= Bot.HEIGHT) {
					temp[count++] = 0;
				} else { 
					temp[count++] = getPixel(xx, yy);
				}
			}
		}
		return temp;
	}
	
	public static int[] getPixelsInSection(ScreenArea area) {
		return getPixelsInSection(area.getBounds()[0], area.getBounds()[1], area.getBounds()[2], area.getBounds()[3]);
	}

	
	public static boolean isInTolerance(int fp, int sp, double tolerance) {
		int[] fc = getRGBData(fp);
		int[] sc = getRGBData(sp);
		return (Math.sqrt(((fc[0]-sc[0])*(fc[0]-sc[0])) + ((fc[1]-sc[1])*(fc[1]-sc[1])) + ((fc[2]-sc[2])*(fc[2]-sc[2]))) / 100) <= tolerance;
	}
	
	public static int[] getRGBData(int x, int y) {
		return getRGBData(getPixel(x, y));
	}
	
	public static int[] getRGBData(int pixel) {
		return new int[] { pixel >> 16 & 0xFF, pixel >> 8 & 0xFF, pixel & 0xFF };
	}
	
	public static int getPixel(int x, int y) {
		if(x >= 0 && y >= 0 && x < Bot.WIDTH && y < Bot.HEIGHT)
			return ((DataBufferInt) Bot.getSingleton().getBotClient().getCanvas().getBuffer().getRaster().getDataBuffer()).getData()[(y * Bot.WIDTH) + x];
		return 0;
	}
	
	public static int getPixel() {
		Point p = Mouse.getMousePosition();
		int x = p.x;
		int y = p.y;
		if(x >= 0 && y >= 0 && x < Bot.WIDTH && y < Bot.HEIGHT)
			return ((DataBufferInt) Bot.getSingleton().getBotClient().getCanvas().getBuffer().getRaster().getDataBuffer()).getData()[(y * Bot.WIDTH) + x];
		return 0;
	}
	
}
