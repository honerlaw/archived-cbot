package org.cbot.menu;

import java.awt.CheckboxMenuItem;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class BotMenuItem extends CheckboxMenuItem {

	private static final long serialVersionUID = -4561846878305603065L;
	
	private boolean active;
	
	public abstract boolean activate();
	public abstract boolean deactivate();
	
	public BotMenuItem(String name, boolean active) {
		super(name);
		this.active = active;
		setState(active);
		this.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				change();
			}
			
		});
	}
	
	public void change() {
		if(active) {
			if(deactivate()) {
				active = false;
			}
		} else {
			if(activate()) {
				active = true;
			}
		}
		BotMenuItem.this.setState(active);
	}
	
	public abstract void draw(Graphics g);
	
	public boolean isActive() {
		return active;
	}

}
