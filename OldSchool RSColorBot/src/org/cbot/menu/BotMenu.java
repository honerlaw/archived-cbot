package org.cbot.menu;

import java.awt.Menu;
import java.awt.MenuBar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cbot.menu.impl.ColorSelectMenuItem;
import org.cbot.menu.impl.InputMenuItem;
import org.cbot.menu.impl.MouseMenuItem;
import org.cbot.menu.impl.ScriptLoadMenuItem;
import org.cbot.script.Script;

public class BotMenu extends MenuBar {

	private static final long serialVersionUID = 535267953702393214L;
	
	private final Map<String, BotMenuItem> informationItems;
	
	private final Menu scripts;
	
	public BotMenu() {
		scripts = new Menu("Scripts");
		informationItems = new HashMap<String, BotMenuItem>();
		informationItems.put("input", new InputMenuItem());
		informationItems.put("mouse", new MouseMenuItem());
		informationItems.put("script", new ScriptLoadMenuItem());
		informationItems.put("color", new ColorSelectMenuItem());
		
		Menu information = new Menu("Information");
		Collection<BotMenuItem> temp = informationItems.values();
		for(BotMenuItem item : temp) {
			information.add(item);
		}
		
		add(information);
		add(scripts);
		
	}
	
	public void updateScriptMenu(Map<String, Script> scripts) {
		this.scripts.removeAll();
		Collection<Script> values = scripts.values();
		for(Script script : values) {
			this.scripts.add(script);
		}
	}
	
	public Map<String, BotMenuItem> getInformationMenuItems() {
		return informationItems;
	}
	
	public BotMenuItem getInformationMenuItem(String name) {
		return informationItems.get(name);
	}
	
}
