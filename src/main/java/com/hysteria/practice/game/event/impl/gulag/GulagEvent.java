package com.hysteria.practice.game.event.impl.gulag;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.event.Event;
import com.hysteria.practice.game.event.game.EventGame;
import com.hysteria.practice.game.event.game.EventGameLogic;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.LocationUtil;
import com.hysteria.practice.utilities.file.type.BasicConfigurationFile;
import com.hysteria.practice.utilities.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GulagEvent implements Event {

	@Setter private Location lobbyLocation;
	@Getter private final List<String> allowedMaps;

	public GulagEvent() {
		BasicConfigurationFile config = HyPractice.get().getEventsConfig();

		lobbyLocation = LocationUtil.deserialize(config.getString("EVENTS.GULAG.LOBBY_LOCATION"));

		allowedMaps = new ArrayList<>();
		allowedMaps.addAll(config.getStringList("EVENTS.GULAG.ALLOWED_MAPS"));
	}

	@Override
	public String getName() {
		return "Gulag";
	}

	@Override
	public String getDisplayName() {
		return CC.translate(HyPractice.get().getEventsConfig().getString("EVENTS.GULAG.DISPLAYNAME"));
	}

	@Override
	public List<String> getDescription() {
		return HyPractice.get().getEventsConfig().getStringList("EVENTS.GULAG.DESCRIPTION");
	}

	@Override
	public int getDurability() {
		return HyPractice.get().getEventsConfig().getInteger("EVENTS.GULAG.DATA");
	}


	@Override
	public Location getLobbyLocation() {
		return lobbyLocation;
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(Material.valueOf(HyPractice.get().getEventsConfig().getString("EVENTS.GULAG.ICON"))).build();
	}

	@Override
	public boolean canHost(Player player) {
		return player.hasPermission("hypractice.event.host.gulag");
	}

	@Override
	public List<Listener> getListeners() {
		return Collections.emptyList();
	}

	@Override
	public List<Object> getCommands() {
		return Collections.emptyList();
	}

	@Override
	public EventGameLogic start(EventGame game) {
		return new GulagGameLogic(game);
	}

	@Override
	public void save() {
		FileConfiguration config = HyPractice.get().getEventsConfig().getConfiguration();
		config.set("EVENTS.GULAG.LOBBY_LOCATION", LocationUtil.serialize(lobbyLocation));
		config.set("EVENTS.GULAG.ALLOWED_MAPS", allowedMaps);

		try {
			config.save(HyPractice.get().getEventsConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
