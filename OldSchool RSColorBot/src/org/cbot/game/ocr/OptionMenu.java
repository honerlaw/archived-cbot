package org.cbot.game.ocr;

import java.awt.Point;

import org.cbot.Bot;
import org.cbot.event.Mouse;
import org.cbot.game.ScreenArea;
import org.cbot.image.ImageTool;

public class OptionMenu {
	
	private final int[] range = new int[] { 3789594, -4541027, -3418600, -4935526, -3355466, -3753835, -3683835, 56833, -3865595, -6994528, -5942269, -4869989, -3223881, 7770780, 16777215, 16776960 };
	
	public boolean select(String option) {
		if(!isMenuVisible()) {
			return false;
		}
		TextArea[] options = getOptions();
		for(TextArea area : options) {
			if(area.getText().replace(" ", "_").contains(option)) {
				Mouse.left(area.getArea().getRandomPoint(5));
				return true;
			}
		}
		if(isMenuVisible()) {
			ScreenArea area = getMenuBounds();
			int[] bounds = area.getBounds();
			bounds[0] -= (Math.random() * 10 + 25);
			bounds[1] -= (Math.random() * 10 + 25);
			Mouse.move(bounds[0], bounds[1]);
		}
		return false;
	}
	
	public TextArea[] getOptions() {
		int[] bounds = getMenuBounds().getBounds();
		bounds[1] += 18;
		int height = bounds[3] - bounds[1];
		int size = height / 16;
		if(size < 0)
			return null;		
		TextArea[] options = new TextArea[size];
		for(int i = 0; i < size; i++) {
			int width2 = bounds[2] - bounds[0] - 2;
			int height2 = 15;
			int x1 = bounds[0] + 1;
			int y1 = bounds[1];
			int x2 = x1 + width2;
			int y2 = y1 + height2;
			ScreenArea temp = new ScreenArea(x1, y1, x2, y2);
			String option = Bot.getSingleton().getGame().getBotOCR().getText("option", temp, 80, 125, true, 5, range, .7, true);
			options[i] = new TextArea(option, temp);
			bounds[1] += 15;
		}
		return options;
	}
	
	public boolean isMenuVisible() {
		ScreenArea area = getMenuBounds();
		if(area.getWidth() > 70 && area.getHeight() > 20) {
			return true;
		}
		return false;
	}
	
	public ScreenArea getMenuBounds() {
		int color = 6116423;
		Point mouse = Mouse.getMousePosition();
		int x = mouse.x;
		int y = mouse.y;
		int lastX = 0;
		int lastY = 0;
		int lastXX = 0;
		int lastYY = 0;
		for(int i = 0; i < 200; i++) {
			if(ImageTool.isInTolerance(color, ImageTool.getPixel(x, y), 0)) {
				lastX = x;
			}
			x--;
		}
		x = mouse.x;
		for(int i = 0; i < 200; i++) {
			if(ImageTool.isInTolerance(color, ImageTool.getPixel(x, y), 0)) {
				lastXX = x;
			}
			x++;
		}
		x = mouse.x;
		for(int i = 0; i < 200; i++) {
			if(ImageTool.isInTolerance(color, ImageTool.getPixel(x, y), 0)) {
				lastY = y;
			}
			y--;
		}
		y = mouse.y;
		for(int i = 0; i < 200; i++) {
			if(ImageTool.isInTolerance(color, ImageTool.getPixel(x, y), 0)) {
				lastYY = y;
			}
			y++;
		}
		return new ScreenArea(lastX, lastY, lastXX, lastYY);
	}


}
