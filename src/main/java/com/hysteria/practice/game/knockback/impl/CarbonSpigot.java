package com.hysteria.practice.game.knockback.impl;

import org.bukkit.entity.Player;
import com.hysteria.practice.game.knockback.KnockbackProfiler;
import xyz.refinedev.spigot.api.knockback.KnockbackAPI;
import xyz.refinedev.spigot.knockback.KnockbackProfile;

public class CarbonSpigot implements KnockbackProfiler {

    @Override
    public void setKnockback(Player player, String kb) {
        KnockbackAPI api = KnockbackAPI.getInstance();
        KnockbackProfile profile = api.getProfile(kb);
        if (profile == null) {
            profile = api.getDefaultProfile();
        }
        api.setPlayerProfile(player, profile);

    }
}
