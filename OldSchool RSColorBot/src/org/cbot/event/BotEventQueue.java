package org.cbot.event;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;

import org.cbot.Bot;
import org.cbot.image.ImageTool;

public class BotEventQueue extends EventQueue {

	@Override
	protected void dispatchEvent(AWTEvent event) {
		if(event.getSource() instanceof Canvas) {
			if(Bot.getSingleton().getBotClient().getCanvas() != event.getSource()) {
				Bot.getSingleton().getBotClient().setCanvas((Canvas) event.getSource());
			}
		}
		if(event.getSource() instanceof Applet) {
			if(event instanceof BotEvent) {
				BotEvent be = (BotEvent) event;
				if(be.getEvent() instanceof MouseEvent) {
					Mouse.setMousePosition(((MouseEvent) be.getEvent()).getPoint());
				}
			} else {
				if(event instanceof MouseEvent) {
					if(Bot.getSingleton().getBotMenu().getInformationMenuItem("input").isActive()) {
						Mouse.setMousePosition(((MouseEvent) event).getPoint());
					}
					if(Bot.getSingleton().getBotMenu().getInformationMenuItem("color").isActive()) {
						MouseEvent e = (MouseEvent) event;
						if(e.getID() == MouseEvent.MOUSE_CLICKED) {
							int pixel = ImageTool.getPixel();
							System.out.println(pixel);
							Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(String.valueOf(pixel)), null);
						}
					}
				}
			}
			if(!Bot.getSingleton().getBotMenu().getInformationMenuItem("input").isActive()) {
				if(event instanceof BotEvent) {
					super.dispatchEvent(((BotEvent) event).getEvent());
				}
			} else {
				if(event instanceof BotEvent) {
					super.dispatchEvent(((BotEvent) event).getEvent());
				} else {
					super.dispatchEvent(event);
				}
			}
		} else {
			super.dispatchEvent(event);
		}
	}

}
