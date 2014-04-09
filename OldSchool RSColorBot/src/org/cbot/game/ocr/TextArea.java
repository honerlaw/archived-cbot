package org.cbot.game.ocr;

import org.cbot.game.ScreenArea;

public class TextArea {

	private final String text;
	private final ScreenArea area;

	public TextArea(String text, ScreenArea area) {
		this.text = text;
		this.area = area;
	}

	public String getText() {
		return text;
	}

	public ScreenArea getArea() {
		return area;
	}
	
}