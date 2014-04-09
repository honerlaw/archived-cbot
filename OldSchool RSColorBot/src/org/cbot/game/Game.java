package org.cbot.game;

import org.cbot.game.ocr.OptionMenu;
import org.cbot.ocr.BotOCR;

public class Game {
	
	private final BotOCR botOCR;
	private final OptionMenu optionMenu;
	
	public Game() {
		botOCR = new BotOCR();
		optionMenu = new OptionMenu();
	}
	
	public BotOCR getBotOCR() {
		return botOCR;
	}
	
	public OptionMenu getOptionMenu() {
		return optionMenu;
	}

}
