package com.hysteria.practice.match.listeners.impl;

import com.hysteria.practice.match.MatchState;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.Cooldown;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Gate;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.utilities.chat.CC;

public class MatchPearlListener implements Listener {
    @EventHandler
    public void onPearlLand(PlayerTeleportEvent event) {
        Location to = event.getTo();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            to.setX(to.getBlockX() + 0.5);
            to.setZ(to.getBlockZ() + 0.5);
            event.setTo(to);
            Location pearlLocation = event.getTo();
            Location playerLocation = event.getFrom();

            if (playerLocation.getBlockY() < pearlLocation.getBlockY()) {
                Block block = pearlLocation.getBlock();

                for (BlockFace face : BlockFace.values()) {
                    Material type = block.getRelative(face).getType();

                    if (type == Material.GLASS || type == Material.BARRIER) {
                        pearlLocation.setY(pearlLocation.getBlockY() - 1.0);
                        break;
                    }
                }
            } else pearlLocation.setY(pearlLocation.getBlockY() + 0.0); // set to 0

            event.setTo(pearlLocation);
        }
    }

    @EventHandler
    public void onPearlLaunch(ProjectileLaunchEvent event) {
        Player player = (Player) event.getEntity().getShooter();

        Profile profile = Profile.get(player.getUniqueId());
        Match match = profile.getMatch();

        // If match doesn't exist then stop.
        if (match == null) {
            return;
        }

        if (match.getState() != MatchState.PLAYING_ROUND/* && profile.getState() == ProfileState.FIGHTING*/) {
            if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl) {
                player.sendMessage(CC.RED + "You can't throw pearls right now!");
                event.setCancelled(true);
                return;
            }
        }

        // Set pearl cooldown to player.
        if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl && profile.getState() == ProfileState.FIGHTING || event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl && profile.getState() == ProfileState.FFA) {
            if (profile.getEnderpearlCooldown().hasExpired()) {
                if(!profile.getMatch().getKit().getGameRules().isLives()) {
                    profile.setEnderpearlCooldown(new Cooldown(16_000));
                }
            }
        }
    }

    @EventHandler
    final void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType().equals(EntityType.ENDER_PEARL)) {
            event.setCancelled(true);
        }
    }

    protected boolean checkFenceGate(Location location, Player player) {
        Block block = location.getBlock();
        Block b2 = block.getRelative(BlockFace.DOWN);
        Material type = block.getType();
        if (type.toString().contains("FENCE_GATE")) {
            if (!((Gate)block.getState().getData()).isOpen()) {
                return b2.getType().toString().contains("FENCE_GATE") && ((Gate)b2.getState().getData()).isOpen();
            } else {
                this.setToBlock(location);
                return true;
            }
        } else {
            return true;
        }
    }

    private void setToBlock(Location location) {
        location.setX((double)location.getBlockX() + 0.5D);
        location.setY((double)location.getBlockY());
        location.setZ((double)location.getBlockZ() + 0.5D);
    }
}
