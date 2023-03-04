package com.hysteria.practice.essentials.abilities.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.essentials.abilities.utils.DurationFormatter;
import org.bukkit.event.block.Action;
import com.hysteria.practice.essentials.abilities.Ability;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.PlayerUtil;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.UUID;

public class TimeWarp extends Ability {

    private final Map<UUID, Map<Location, Long>> lastPearl;

    public TimeWarp() {
        super("TIME_WARP");

        this.lastPearl = Maps.newConcurrentMap();

        Bukkit.getScheduler().runTaskTimerAsynchronously(HyPractice.get(), () -> {

            for (Map.Entry<UUID, Map<Location, Long>> lastPearlEntry : this.lastPearl.entrySet()) {
                UUID uuid = lastPearlEntry.getKey();
                Map<Location, Long> map = lastPearlEntry.getValue();

                if (System.currentTimeMillis() > map.values().iterator().next()) {
                    this.lastPearl.remove(uuid);
                }
            }

        }, 20L, 20L);
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = Profile.get(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&7You are on cooldown for &4" + DurationFormatter.getRemaining(profile.getTimewarp().getRemainingMilis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntityType() != EntityType.ENDER_PEARL) {
            return;
        }

        EnderPearl enderpearl = (EnderPearl) event.getEntity();

        if (!(enderpearl.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) enderpearl.getShooter();
        long PEARL_EXPIRE = 15 * 1000;
        Map<Location, Long> map = ImmutableMap.of(shooter.getLocation().clone(),
                System.currentTimeMillis() + PEARL_EXPIRE);

        this.lastPearl.put(shooter.getUniqueId(), map);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        
        if (!event.getAction().name().contains("RIGHT_CLICK")) {
            return;
        }

        if (!isAbility(event.getItem())) {
            return;
        }

        if (profile.getTimewarp().onCooldown(player)) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&7You are on &c&lTime Warp &7cooldown for &4" + DurationFormatter.getRemaining(profile.getTimewarp().getRemainingMilis(player), true, true)));
            player.updateInventory();
            event.setCancelled(true);
            return;
        }

        if (profile.getPartneritem().onCooldown(player)) {
            player.sendMessage(CC.translate("&7You are on &c&lPartner Item &7cooldown &7for &4" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(player), true, true)));
            player.updateInventory();
            event.setCancelled(true);
            return;
        }

        if (!this.lastPearl.containsKey(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYour last enderpearl location has expired!"));
            return;
        }

        Location location = this.lastPearl.get(player.getUniqueId()).keySet().iterator().next().clone();

        PlayerUtil.decrement(player);

        profile.getTimewarp().applyCooldown(player, 60 * 1000);
        profile.getPartneritem().applyCooldown(player, 10 * 1000);

        player.sendMessage(CC.translate(
                "&7You &a4ctivated &7a Time Warp, so you will be teleported to your last thrown enderpearl's location in &43 &7seconds!"));

        Bukkit.getScheduler().runTaskLater(HyPractice.get(), () -> {
            player.teleport(location);
            player.sendMessage(
                    CC.translate("&7You have been &4teleported &7to your last thrown enderpearl's location!"));

            this.lastPearl.remove(player.getUniqueId());
        }, 60L);

        HyPractice.get().getAbilityManager().cooldownExpired(player, this.getName(), this.getAbility());
        HyPractice.get().getAbilityManager().playerMessage(player, this.getAbility());
    }
}