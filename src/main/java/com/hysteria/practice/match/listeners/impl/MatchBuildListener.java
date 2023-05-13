package com.hysteria.practice.match.listeners.impl;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.match.MatchState;
import com.hysteria.practice.match.impl.BasicTeamMatch;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.MessageFormat;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.arena.impl.StandaloneArena;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;
import com.hysteria.practice.utilities.chat.CC;

public class MatchBuildListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFromEvent(BlockFromToEvent event) {
        Match.getMatches().forEach(match -> {
            Arena arena = match.getArena();
            int x = (int) event.getToBlock().getLocation().getX();
            int y = (int) event.getToBlock().getLocation().getY();
            int z = (int) event.getToBlock().getLocation().getZ();

            if (y > arena.getMaxBuildHeight()) {
                event.setCancelled(true);
                return;
            }

            if (arena instanceof StandaloneArena) {
                StandaloneArena standaloneArena = (StandaloneArena) arena;
                if (standaloneArena.getSpawnBlue() != null && standaloneArena.getSpawnBlue().contains(event.getToBlock())) {
                    event.setCancelled(true);
                    return;
                }
                if (standaloneArena.getSpawnRed() != null && standaloneArena.getSpawnRed().contains(event.getToBlock())) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (x >= arena.getX1() && x <= arena.getX2() && y >= arena.getY1() && y <= arena.getY2() &&
                    z >= arena.getZ1() && z <= arena.getZ2()) {
                match.getPlacedBlocks().add(event.getBlock().getLocation());
                Location down = event.getBlock().getLocation().subtract(0, 1, 0);
                if (down.getBlock().getType() == Material.GRASS) {
                    match.getChangedBlocks().add(down.getBlock().getState());
                }
            } else {
                event.setCancelled(true);
            }
        });
    }


    @EventHandler(ignoreCancelled=true)
    public void onBlockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();
            Block block = blockPlaceEvent.getBlock();
            if (((match.getKit().getGameRules().isBuild() || (match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())))) && match.getState() == MatchState.PLAYING_ROUND) {
                if (match.getKit().getGameRules().isSpleef()) {
                    blockPlaceEvent.setCancelled(true);
                    return;
                }
                Arena arena = match.getArena();
                int n = (int)blockPlaceEvent.getBlockPlaced().getLocation().getX();
                int n2 = (int)blockPlaceEvent.getBlockPlaced().getLocation().getY();
                int n3 = (int)blockPlaceEvent.getBlockPlaced().getLocation().getZ();

                BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();

                if(match.getKit().getGameRules().isBattlerush() || match.getKit().getGameRules().isLives()) {
                    if (block.getType() == Material.WOOL) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                blockPlaceEvent.getBlock().setType(Material.AIR);
                                if (player.getGameMode() == GameMode.SURVIVAL) {
                                    if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
                                        ItemStack redWool = (new ItemBuilder(Material.WOOL).durability(14).build());
                                        if (player.getInventory().contains(redWool)) {
                                            if (!(redWool.getAmount() == 64)) {
                                                player.getInventory().addItem(new ItemBuilder(Material.WOOL).durability(14).amount(1).build());
                                            }
                                        }
                                    } else {
                                        ItemStack blueWool = (new ItemBuilder(Material.WOOL).durability(11).build());
                                        if (player.getInventory().contains(blueWool)) {
                                            if (!(blueWool.getAmount() == 64)) {
                                                player.getInventory().addItem(new ItemBuilder(Material.WOOL).durability(11).amount(1).build());
                                            }
                                        }
                                    }
                                }
                            }
                        }.runTaskLater(HyPractice.get(), 160);
                    }
                }

                if(match.getKit().getGameRules().isBedFight()) {
                    int highest = (int) (Math.max(arena.getSpawnA().getY(), arena.getSpawnB().getY()));
                    highest += 8;

                    if (n2 > highest) {
                        new MessageFormat(Locale.ARENA_REACHED_MAXIMUM.format(profile.getLocale())).send(player);
                        blockPlaceEvent.setCancelled(true);
                        return;
                    }
                } else if (n2 > arena.getMaxBuildHeight()) {
                    new MessageFormat(Locale.ARENA_REACHED_MAXIMUM.format(profile.getLocale())).send(player);
                    blockPlaceEvent.setCancelled(true);
                    return;
                }
                if (arena instanceof StandaloneArena) {
                    StandaloneArena standaloneArena = (StandaloneArena)arena;
                    if (standaloneArena.getSpawnBlue() != null && standaloneArena.getSpawnBlue().contains(blockPlaceEvent.getBlockPlaced())) {
                        blockPlaceEvent.setCancelled(true);
                        return;
                    }
                    if (standaloneArena.getSpawnRed() != null && standaloneArena.getSpawnRed().contains(blockPlaceEvent.getBlockPlaced())) {
                        blockPlaceEvent.setCancelled(true);
                        return;
                    }
                }
                if (arena.contains(n, n2, n3)) {
                    if (blockPlaceEvent.getBlockReplacedState() == null || blockPlaceEvent.getBlockReplacedState().getType() == Material.AIR) {
                        match.getPlacedBlocks().add(blockPlaceEvent.getBlock().getLocation());
                    } else {
                        match.getChangedBlocks().add(blockPlaceEvent.getBlockReplacedState());
                    }
                } else {
                    new MessageFormat(Locale.ARENA_BUILD_OUTSIDE.format(profile.getLocale())).send(player);
                    blockPlaceEvent.setCancelled(true);
                }
            } else {
                blockPlaceEvent.setCancelled(true);
            }
        } else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();
            if (match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isSpleef() || (((match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())))) && match.getState() == MatchState.PLAYING_ROUND) {
                if (match.getKit().getGameRules().isSpleef()) {
                    if (blockBreakEvent.getBlock().getType() == Material.SNOW_BLOCK || blockBreakEvent.getBlock().getType() == Material.SNOW) {
                        match.getChangedBlocks().add(blockBreakEvent.getBlock().getState());
                        blockBreakEvent.getBlock().setType(Material.AIR);
                        player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 4));
                        player.updateInventory();
                    } else {
                        blockBreakEvent.setCancelled(true);
                    }
                } else if (match.getKit().getGameRules().isHcftrap()) {
                    if (match.getPlacedBlocks().contains(blockBreakEvent.getBlock().getLocation())) {
                        match.getPlacedBlocks().remove(blockBreakEvent.getBlock().getLocation());
                    } else if (match.getChangedBlocks().stream().noneMatch(blockState -> blockState.getLocation().equals(blockBreakEvent.getBlock().getLocation()))) {
                        match.getChangedBlocks().add(blockBreakEvent.getBlock().getState());
                    }
                    if (!blockBreakEvent.getBlock().getDrops().isEmpty()) {
                        match.getDroppedItems().add(blockBreakEvent.getBlock().getLocation().getWorld().dropItemNaturally(blockBreakEvent.getBlock().getLocation(), blockBreakEvent.getBlock().getDrops().stream().findFirst().orElse(null)));
                    }
                    blockBreakEvent.getBlock().setType(Material.AIR);
                    blockBreakEvent.setCancelled(true);
                } else if (match.getKit().getGameRules().isBedFight()) {
                    if (blockBreakEvent.getBlock().getType() == Material.BED_BLOCK) {
                        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();
                        GameParticipant<MatchGamePlayer> opposingTeam = teamMatch.getParticipantA().containsPlayer(player.getUniqueId()) ? teamMatch.getParticipantB() : teamMatch.getParticipantA();

                        if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId()) ? blockBreakEvent.getBlock().getLocation().distance(teamMatch.getArena().getSpawnA()) > blockBreakEvent.getBlock().getLocation().distance(teamMatch.getArena().getSpawnB()) : blockBreakEvent.getBlock().getLocation().distance(teamMatch.getArena().getSpawnB()) > blockBreakEvent.getBlock().getLocation().distance(teamMatch.getArena().getSpawnA())) {
                            if (teamMatch.getState() != MatchState.ENDING_MATCH && opposingTeam.isHasBed()) {
                                opposingTeam.destroyBed();

                                teamMatch.broadcastTitle(teamMatch.getParticipantB().containsPlayer(player.getUniqueId()) ? "&cRed's bed broken" : "&9Blue's bed destroyed", "&7By " + (teamMatch.getParticipantB().containsPlayer(player.getUniqueId()) ? "&9" : "&c") + player.getName(), 50);

                                teamMatch.sendMessage("");
                                teamMatch.sendMessage(" &c[BREAK] &f" + player.getName() + " &7has destroyed the bed of " + (teamMatch.getParticipantA().containsPlayer(player.getUniqueId()) ? "&9Blue&f!" : "&cRed&f!"));
                                teamMatch.sendMessage("");

                                teamMatch.sendSound(Sound.ENDERDRAGON_GROWL, 10, 1);

                                blockBreakEvent.setCancelled(true);
                                blockBreakEvent.getBlock().setType(Material.AIR);
                            }
                        } else {
                            player.sendMessage(CC.translate("&7You cannot break your own bed."));
                            blockBreakEvent.setCancelled(true);
                        }
                    } else {
                        if (!match.getPlacedBlocks().remove(blockBreakEvent.getBlock().getLocation()) && blockBreakEvent.getBlock().getType() != Material.ENDER_STONE && blockBreakEvent.getBlock().getType() != Material.WOOD) {
                            blockBreakEvent.setCancelled(true);
                        }
                    }
                } else if (!match.getPlacedBlocks().remove(blockBreakEvent.getBlock().getLocation())) {
                    blockBreakEvent.setCancelled(true);
                }

            } else {
                blockBreakEvent.setCancelled(true);
            }
        } else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
            blockBreakEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent playerBucketEmptyEvent) {
        Player player = playerBucketEmptyEvent.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();
            if (((match.getKit().getGameRules().isBuild() || (match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())))) && match.getState() == MatchState.PLAYING_ROUND) {
                Arena arena = match.getArena();
                Block block = playerBucketEmptyEvent.getBlockClicked().getRelative(playerBucketEmptyEvent.getBlockFace());
                int n = (int)block.getLocation().getX();
                int n2 = (int)block.getLocation().getY();
                int n3 = (int)block.getLocation().getZ();
                if (n2 > arena.getMaxBuildHeight()) {
                    new MessageFormat(Locale.ARENA_REACHED_MAXIMUM.format(profile.getLocale())).send(player);
                    playerBucketEmptyEvent.setCancelled(true);
                    return;
                }
                if (n >= arena.getX1() && n <= arena.getX2() && n2 >= arena.getY1() && n2 <= arena.getY2() && n3 >= arena.getZ1() && n3 <= arena.getZ2()) {
                    match.getPlacedBlocks().add(block.getLocation());
                } else {
                    playerBucketEmptyEvent.setCancelled(true);
                }
            } else {
                playerBucketEmptyEvent.setCancelled(true);
            }
        } else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
            playerBucketEmptyEvent.setCancelled(true);
        }
    }
}
