package org.cbot.event;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.cbot.Bot;

public class Mouse {
	
	private static Point position = new Point(0, 0);
	
	public static void right(int x, int y) {
		click(MouseEvent.BUTTON3_MASK, x, y);
	}
	
	public static void right(Point point) {
		right(point.x, point.y);
	}
	
	public static void left(int x, int y) {
		click(MouseEvent.BUTTON1_MASK, x, y);
	}
	
	public static void left(Point point) {
		left(point.x, point.y);
	}
	
	public static void click(int mask, int x, int y) {
		Point current = getMousePosition();
		if(current.x != x || current.y != y)
			move(x, y);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new BotEvent(new MouseEvent(Bot.getSingleton().getBotClient().getApplet(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), mask, x, y, 1, mask == MouseEvent.BUTTON3_MASK ? true : false)));
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new BotEvent(new MouseEvent(Bot.getSingleton().getBotClient().getApplet(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), mask, x, y, 1, mask == MouseEvent.BUTTON3_MASK ? true : false)));
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new BotEvent(new MouseEvent(Bot.getSingleton().getBotClient().getApplet(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), mask, x, y, 1, mask == MouseEvent.BUTTON3_MASK ? true : false)));
	}
	
	public static void scroll(int x, int y, int rotations) {
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new BotEvent(new MouseWheelEvent(Bot.getSingleton().getBotClient().getApplet(), MouseWheelEvent.MOUSE_WHEEL, System.currentTimeMillis(), MouseWheelEvent.NOBUTTON, x, y, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, rotations)));
	}
	
	public static void move(int x, int y) {
		double speed = (Math.random() * 15D + 15D) / 10D;
		Point current = getMousePosition();
		move(MouseEvent.MOUSE_MOVED, current.x, current.y, x, y, 9D, 3D, 5D/speed, 10D/speed, 10D*speed, 8D*speed);
	}
	
	public static void drag(int sx, int sy, int ex, int ey) {
		Point current = getMousePosition();
		if(current.x != sx || current.y != sy) {
			move(current.x, current.y);
		}
		double speed = (Math.random() * 15D + 15D) / 10D;
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new BotEvent(new MouseEvent(Bot.getSingleton().getBotClient().getApplet(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), MouseEvent.BUTTON1_MASK, sx, sy, 1, false)));
		move(MouseEvent.MOUSE_MOVED, sx, sy, ex, ey, 9D, 3D, 5D/speed, 10D/speed, 10D*speed, 8D*speed);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new BotEvent(new MouseEvent(Bot.getSingleton().getBotClient().getApplet(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), MouseEvent.BUTTON1_MASK, ex, ey, 1, false)));
	}

	private static void move(int id, double xs, double ys, double xe, double ye, double gravity, double wind, double minWait, double maxWait, double maxStep, double targetArea) {
        final double sqrt3 = Math.sqrt(3);
        final double sqrt5 = Math.sqrt(5);
        double dist, veloX = 0, veloY = 0, windX = 0, windY = 0;
        while ((dist = Math.hypot(xs - xe,ys - ye)) >= 1) {
            wind = Math.min(wind, dist);
            if (dist >= targetArea) {
                windX = windX / sqrt3 + (Math.random() * (wind * 2D + 1D) - wind) / sqrt5;
                windY = windY / sqrt3 + (Math.random() * (wind * 2D + 1D) - wind) / sqrt5;
            } else {
                windX /= sqrt3;
                windY /= sqrt3;
                if (maxStep < 3) {
                    maxStep = Math.random() * 3 + 3D;
                } else {
                    maxStep /= sqrt5;
                }
            }
            veloX += windX + gravity * (xe - xs) / dist;
            veloY += windY + gravity * (ye - ys) / dist;
            double veloMag = Math.hypot(veloX, veloY);
            if (veloMag > maxStep) {
                double randomDist = maxStep / 2D + Math.random() * maxStep / 2D;
                veloX = (veloX / veloMag) * randomDist;
                veloY = (veloY / veloMag) * randomDist;
            }
            xs += veloX;
            ys += veloY;
            int mx = (int) Math.round(xs);
            int my = (int) Math.round(ys);
            Point current = getMousePosition();
            if (current.x != mx || current.y != my) {
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new BotEvent(new MouseEvent(Bot.getSingleton().getBotClient().getApplet(), id, System.currentTimeMillis(), MouseEvent.NOBUTTON, mx , my, 0, false)));
            }
            double step = Math.hypot(xs - current.x, ys - current.y);
            try {
                Thread.sleep(Math.round((maxWait - minWait) * (step / maxStep) + minWait));
            } catch (InterruptedException ex) {  }
        }
	}
	
	/*public static Point getMousePosition() {
		int x = MouseInfo.getPointerInfo().getLocation().x - Bot.getSingleton().getBotClient().getApplet().getLocationOnScreen().x;
		int y = MouseInfo.getPointerInfo().getLocation().y - Bot.getSingleton().getBotClient().getApplet().getLocationOnScreen().y;
		return new Point(x, y);
	}*/
	
	public static void setMousePosition(Point position) {
		if(position != null)
			Mouse.position = position;
	}
	
	public static Point getMousePosition() {
		return position;
	}

}
