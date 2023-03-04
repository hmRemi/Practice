package com.hysteria.practice.player.profile.modmode;

import com.hysteria.practice.player.profile.visibility.VisibilityLogic;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.TaskUtil;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import com.hysteria.practice.Locale;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.hotbar.Hotbar;
import com.hysteria.practice.player.profile.hotbar.impl.HotbarItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ModmodeListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            Profile profile = Profile.get(player.getUniqueId());
            HotbarItem hotbarItem = Hotbar.fromItemStack(event.getItem());

            if (hotbarItem != null) {
                if (hotbarItem == HotbarItem.RANDOM_TELEPORT) {
                    event.setCancelled(true);
                    List<Player> players = Bukkit.getOnlinePlayers().stream()
                            .filter(player1 -> Profile.get(player1.getUniqueId()).getState() == ProfileState.FIGHTING)
                            .collect(Collectors.toList());

                    if (players.isEmpty()) {
                        new MessageFormat(Locale.STAFF_MODE_NO_PLAYERS_SPEC.format(profile.getLocale()))
                                .send(player);
                    } else {
                        Player target = players.get(ThreadLocalRandom.current().nextInt(players.size()));
                        Profile targetProfile = Profile.get(target.getUniqueId());

                        if (profile.getMatch() != null) {
                            profile.setMatch(null);
                            TaskUtil.runLater(() -> targetProfile.getMatch().addSpectator(player, target), 5L);
                            return;
                        }

                        targetProfile.getMatch().addSpectator(player, target);
                    }
                }
                else if (hotbarItem == HotbarItem.HIDE_ALL_PLAYERS) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        player.hidePlayer(onlinePlayer);
                    }
                    new MessageFormat(Locale.STAFF_MODE_NOW_SHOW_ALL_PLAYERS.format(profile.getLocale()))
                            .send(player);
                }
                else if (hotbarItem == HotbarItem.RESET) {
                    if (profile.getMatch() != null) {
                        profile.getMatch().removeSpectator(player);
                        profile.setMatch(null);
                    }
                    HyPractice.get().getEssentials().teleportToSpawn(player);
                    for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                        VisibilityLogic.handle(player, otherPlayer);
                        VisibilityLogic.handle(otherPlayer, player);
                    }
                    new MessageFormat(Locale.STAFF_MODE_NOW_IN_SPAWN.format(profile.getLocale()))
                            .send(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() == ProfileState.STAFF_MODE && event.getRightClicked() instanceof Player && player.getItemInHand() != null) {
            Player target = (Player) event.getRightClicked();

            if (Hotbar.fromItemStack(player.getItemInHand()) == HotbarItem.VIEW_INVENTORYSTAFF) {
                player.performCommand("invsee " + target.getName());
            }

            if (Hotbar.fromItemStack(player.getItemInHand()) == HotbarItem.FREEZE) {
                player.performCommand("ss " + target.getName());
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() == ProfileState.STAFF_MODE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() == ProfileState.STAFF_MODE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Modmode.getStaffmode().remove(player.getUniqueId());
    }
}
