package org.cbot.menu.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.cbot.event.Mouse;
import org.cbot.menu.BotMenuItem;

public class MouseMenuItem extends BotMenuItem {

	private static final long serialVersionUID = -3357170173936735836L;

	public MouseMenuItem() {
		super("Mouse", false);
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
		g.setColor(Color.RED);
		g.fillOval(p.x - 6, p.y - 6, 6, 6);
		g.drawString(p.x + " " + p.y, 50, 50);
	}

}
