package com.hysteria.practice.utilities.event;

import com.hysteria.practice.HyPractice;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public void call() {
		HyPractice.get().getServer().getPluginManager().callEvent(this);
	}

}
