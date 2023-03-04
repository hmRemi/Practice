package com.hysteria.practice.utilities.playerversion.impl;

import com.hysteria.practice.utilities.playerversion.IPlayerVersion;
import com.hysteria.practice.utilities.playerversion.PlayerVersion;
import org.bukkit.entity.Player;

public class PlayerVersionDefaultImpl implements IPlayerVersion {
    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(0);
    }
}
