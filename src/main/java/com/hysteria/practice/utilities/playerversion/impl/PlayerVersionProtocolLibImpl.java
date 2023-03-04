package com.hysteria.practice.utilities.playerversion.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.hysteria.practice.utilities.playerversion.IPlayerVersion;
import com.hysteria.practice.utilities.playerversion.PlayerVersion;
import org.bukkit.entity.Player;

public class PlayerVersionProtocolLibImpl implements IPlayerVersion {

    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(
                ProtocolLibrary.getProtocolManager().getProtocolVersion(player)
        );
    }
}
