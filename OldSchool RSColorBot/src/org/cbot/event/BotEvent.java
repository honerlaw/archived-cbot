package org.cbot.event;

import java.awt.AWTEvent;

public class BotEvent extends AWTEvent {
	
	private static final long serialVersionUID = -2655391599924398034L;
	private final AWTEvent event;

	public BotEvent(AWTEvent event) {
		super(event.getSource(), event.getID());
		this.event = event;
	}

	public AWTEvent getEvent() {
		return event;
	}
	
}
