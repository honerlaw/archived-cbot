package org.cbot.script;

import java.util.concurrent.TimeUnit;

import org.cbot.Bot;
import org.cbot.game.Game;
import org.cbot.menu.BotMenuItem;

public abstract class Script extends BotMenuItem implements Runnable {

	private static final long serialVersionUID = 1287947516612771000L;

	public Script(String name) {
		super(name, false);
	}
	
	public abstract boolean start();
	public abstract int process();
	public abstract void stop();
	
	public void terminate() {
		stop();
	}
	
	@Override
	public void run() {
		if(isActive()) {
			ScriptManager.getService().schedule(this, process(), TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public boolean activate() {
		return ScriptManager.startRunningScript(this);
	}

	@Override
	public boolean deactivate() {
		terminate();
		return true;
	}
	
	public int random(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}
	
	public Game getGame() {
		return Bot.getSingleton().getGame();
	}

}
