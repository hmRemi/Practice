package com.hysteria.practice.game.event;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.event.game.EventGame;
import com.hysteria.practice.game.event.game.EventGameLogic;
import com.hysteria.practice.game.event.impl.brackets.BracketsEvent;
import com.hysteria.practice.game.event.impl.gulag.GulagEvent;
import com.hysteria.practice.game.event.impl.spleef.SpleefEvent;
import com.hysteria.practice.game.event.impl.sumo.SumoEvent;
import com.hysteria.practice.game.event.impl.tntrun.TNTRunEvent;
import com.hysteria.practice.game.event.impl.tnttag.TNTTagEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface Event {

	List<Event> events = new ArrayList<>();

	static void init() {
		add(new SumoEvent());
		add(new SpleefEvent());
		add(new TNTRunEvent());
		add(new GulagEvent());
		add(new BracketsEvent());
		add(new TNTTagEvent());
	}

	static void add(Event event) {
		events.add(event);
		for (Listener listener : event.getListeners()) {
			HyPractice.get().getServer().getPluginManager().registerEvents(listener, HyPractice.get());
		}
	}

	static <T extends Event> T getEvent(Class<? extends Event> clazz) {
		for (Event event : events) {
			if (event.getClass() == clazz) {
				return (T) clazz.cast(event);
			}
		}

		return null;
	}

	static Event getByName(String name) {
		return events.stream().filter(event1 -> event1.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	String getName();

	String getDisplayName();

	List<String> getDescription();

	Location getLobbyLocation();

	void setLobbyLocation(Location location);

	ItemStack getIcon();

	boolean canHost(Player player);

	List<String> getAllowedMaps();

	List<Listener> getListeners();

	default List<Object> getCommands() {
		return new ArrayList<>();
	}

	EventGameLogic start(EventGame game);

	void save();

}
