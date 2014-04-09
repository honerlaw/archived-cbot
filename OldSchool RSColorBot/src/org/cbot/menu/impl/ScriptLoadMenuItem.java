package org.cbot.menu.impl;

import java.awt.Graphics;

import org.cbot.menu.BotMenuItem;
import org.cbot.script.ScriptManager;

public class ScriptLoadMenuItem extends BotMenuItem {

	private static final long serialVersionUID = -4322154873458157686L;

	public ScriptLoadMenuItem() {
		super("Load Scripts", false);
	}

	@Override
	public boolean activate() {
		ScriptManager.load();
		return false;
	}

	@Override
	public boolean deactivate() {
		return false;
	}

	@Override
	public void draw(Graphics g) {
		
	}

}
