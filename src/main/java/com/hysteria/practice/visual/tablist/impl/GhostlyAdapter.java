package com.hysteria.practice.visual.tablist.impl;

import com.hysteria.practice.visual.tablist.impl.utils.BufferedTabObject;
import org.bukkit.entity.Player;

import java.util.Set;

public interface GhostlyAdapter {

    Set<BufferedTabObject> getSlots(Player player);

    String getFooter();
    String getHeader();

}
