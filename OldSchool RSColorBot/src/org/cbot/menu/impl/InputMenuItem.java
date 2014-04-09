package org.cbot.menu.impl;

import java.awt.Graphics;

import org.cbot.menu.BotMenuItem;

public class InputMenuItem extends BotMenuItem {

	private static final long serialVersionUID = -6874056510036365127L;

	public InputMenuItem() {
		super("User Input", true);
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
		
	}

}
