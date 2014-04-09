package org.cbot.menu.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.cbot.event.Mouse;
import org.cbot.image.ImageTool;
import org.cbot.menu.BotMenuItem;

public class ColorSelectMenuItem extends BotMenuItem {

	private static final long serialVersionUID = 3453606236161623767L;

	public ColorSelectMenuItem() {
		super("Color Selector", false);
	}

	@Override
	public boolean activate() {
		return true;
	}

	@Override
	public boolean deactivate() {
		return true;
	}

	@Override
	public void draw(Graphics g) {
		Point p = Mouse.getMousePosition();
		int pixel = ImageTool.getPixel(p.x, p.y);
		Color color = new Color(pixel);
		g.setColor(color);
		g.fillRect(p.x - 38, p.y - 35, 76, 25);
		int newColor = 0xFFFFFFF - pixel;
		g.setColor(new Color(newColor));
		g.drawString(String.valueOf(pixel), p.x - 35, p.y - 22);
	}

}
