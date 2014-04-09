package org.cbot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;

import org.cbot.menu.BotMenuItem;
import org.cbot.script.Script;
import org.cbot.script.ScriptManager;

public class BotDisplay {
	
	private final BufferedImage offscreen;
	
	public BotDisplay() {
		offscreen = new BufferedImage(Bot.WIDTH, Bot.HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	
	public void draw() {
		if(Bot.getSingleton().getBotClient().getCanvas() == null)
			return;
		try {
			Graphics g = offscreen.getGraphics();
			g.drawImage(Bot.getSingleton().getBotClient().getCanvas().getBuffer(), 0, 0, null);
			
			Collection<BotMenuItem> items = Bot.getSingleton().getBotMenu().getInformationMenuItems().values();
			for(BotMenuItem item : items) {
				if(item.isActive()) {
					item.draw(g);
				}
			}
			
			Collection<Script> scripts = ScriptManager.getScripts().values();
			for(Script script : scripts) {
				if(script.isActive()) {
					script.draw(g);
				}
			}
			
			Bot.getSingleton().getBotClient().getCanvas().getCanvasGraphics().drawImage(offscreen, 0, 0, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
