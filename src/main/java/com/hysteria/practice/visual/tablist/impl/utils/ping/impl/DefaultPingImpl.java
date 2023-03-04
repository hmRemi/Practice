package com.hysteria.practice.visual.tablist.impl.utils.ping.impl;

import com.hysteria.practice.visual.tablist.impl.utils.ping.IPingProvider;
import org.bukkit.entity.Player;

public class DefaultPingImpl implements IPingProvider {

    @Override
    public int getDefaultPing(Player player) {
        return 0;
    }

}
