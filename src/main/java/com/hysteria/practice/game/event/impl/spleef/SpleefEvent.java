package com.hysteria.practice.game.event.impl.spleef;

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
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpleefEvent implements Event {

    @Setter private Location lobbyLocation;
    @Getter private final List<String> allowedMaps;
    @Getter private final List<BlockState> changedBlocks;

    public SpleefEvent() {
        BasicConfigurationFile config = HyPractice.get().getEventsConfig();

        lobbyLocation = LocationUtil.deserialize(config.getString("EVENTS.SPLEEF.LOBBY_LOCATION"));

        allowedMaps = new ArrayList<>();
        allowedMaps.addAll(config.getStringList("EVENTS.SPLEEF.ALLOWED_MAPS"));
        this.changedBlocks = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "Spleef";
    }

    @Override
    public String getDisplayName() {
        return CC.translate(HyPractice.get().getEventsConfig().getString("EVENTS.SPLEEF.DISPLAYNAME"));
    }

    @Override
    public List<String> getDescription() {
        return HyPractice.get().getEventsConfig().getStringList("EVENTS.SPLEEF.DESCRIPTION");
    }

    @Override
    public int getDurability() {
        return HyPractice.get().getEventsConfig().getInteger("EVENTS.SPLEEF.DATA");
    }

    @Override
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.valueOf(HyPractice.get().getEventsConfig().getString("EVENTS.SPLEEF.ICON"))).build();
    }

    @Override
    public boolean canHost(Player player) {
        return player.hasPermission("hypractice.event.host.spleef");
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
        return new SpleefGameLogic(game);
    }

    @Override
    public void save() {
        FileConfiguration config = HyPractice.get().getEventsConfig().getConfiguration();
        config.set("EVENTS.SPLEEF.LOBBY_LOCATION", LocationUtil.serialize(lobbyLocation));
        config.set("EVENTS.SPLEEF.ALLOWED_MAPS", allowedMaps);

        try {
            config.save(HyPractice.get().getEventsConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}