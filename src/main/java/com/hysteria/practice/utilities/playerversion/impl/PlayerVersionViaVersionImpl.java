package com.hysteria.practice.utilities.playerversion.impl;

import com.hysteria.practice.utilities.playerversion.IPlayerVersion;
import com.hysteria.practice.utilities.playerversion.PlayerVersion;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;

public class PlayerVersionViaVersionImpl implements IPlayerVersion {
    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(Via.getAPI().getPlayerVersion(player.getUniqueId()));
    }
}
