package org.cbot;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import org.cbot.event.BotEventQueue;
import org.cbot.game.Game;
import org.cbot.menu.BotMenu;
import org.cbot.script.ScriptManager;

public class Bot extends Thread {
	
	public static final String WORLD = "http://oldschool65.runescape.com";
	public static final int WIDTH = 763;
	public static final int HEIGHT = 504;
	public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
	private static final Bot singleton = new Bot();
	
	private final Frame botFrame;
	private final BotClient botClient;
	private final BotMenu botMenu;
	private final BotDisplay botDisplay;
	
	private final Game game;
	
	private Bot() {
		this.botFrame = new Frame("Bot");
		this.botClient = new BotClient();
		this.botMenu = new BotMenu();
		this.botDisplay = new BotDisplay();
		this.game = new Game();
	}
	
	@Override
	public void start() {
		botFrame.setSize(new Dimension(WIDTH, HEIGHT + 20));
		botClient.setAppletAndCanvas();
		botFrame.add(botClient.getApplet());
		botFrame.setResizable(false);
		botFrame.setMenuBar(botMenu);
		botFrame.pack();
		botFrame.setVisible(true);
		ScriptManager.load();
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new BotEventQueue());
		super.start();
	}
	
	@Override
	public void run() {
		while(true) {
			getBotClient().getApplet().getComponent(0).requestFocus();
			getBotDisplay().draw();
			try {
				Thread.sleep(34);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Frame getBotFrame() {
		return botFrame;
	}
	
	public BotClient getBotClient() {
		return botClient;
	}
	
	public BotMenu getBotMenu() {
		return botMenu;
	}
	
	public BotDisplay getBotDisplay() {
		return botDisplay;
	}
	
	public Game getGame() {
		return game;
	}
	
	public static Bot getSingleton() {
		return singleton;
	}
	
	public static void main(String[] args) {
		/*if(args.length == 0) {
			String dir = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			try {
				Runtime.getRuntime().exec(dir + " -cp bot.jar -Xbootclasspath/p:bot.jar; -jar bot.jar 0");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(args.length > 0) {*/
			Bot.getSingleton().start();
		//}
	}

}
