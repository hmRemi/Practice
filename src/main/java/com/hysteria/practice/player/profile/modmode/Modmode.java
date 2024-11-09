package com.hysteria.practice.player.profile.modmode;

import cc.insidious.akuma.api.AkumaAPI;
import cc.insidious.akuma.api.channel.ChatChannel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.match.impl.BasicTeamMatch;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.hotbar.Hotbar;
import com.hysteria.practice.player.profile.visibility.VisibilityLogic;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.PlayerUtil;
import com.hysteria.practice.utilities.StringUtils;
import com.hysteria.practice.utilities.TaskUtil;
import lombok.Getter;
import meth.crystal.aspirin.plugin.AspirinAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Modmode {

    @Getter
    public static Set<UUID> staffmode = Sets.newConcurrentHashSet();

    public static void add(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        profile.setState(ProfileState.STAFF_MODE);

        Hotbar.giveHotbarItems(player);

        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.setFlying(true);

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            VisibilityLogic.handle(player, otherPlayer);
            VisibilityLogic.handle(otherPlayer, player);
        }

        staffmode.add(player.getUniqueId());

        new MessageFormat(Locale.STAFF_MODE_JOIN_STAFF.format(profile.getLocale()))
                .send(player);
    }

    public static void remove(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        profile.setState(ProfileState.LOBBY);
        if (profile.getMatch() != null) {
            profile.getMatch().removeSpectator(player);
            profile.setMatch(null);
        }

        PlayerUtil.reset(player);
        Hotbar.giveHotbarItems(player);
        TaskUtil.runLater(() -> {
            HyPractice.get().getEssentials().teleportToSpawn(player);

        }, 2L);
        VisibilityLogic.handle(player);

        staffmode.remove(player.getUniqueId());

        new MessageFormat(Locale.STAFF_MODE_LEAVE_STAFF.format(profile.getLocale()))
                .send(player);
    }

    public static List<String> getScoreboardLines(Player player) {
        List<String> lines = Lists.newArrayList();
        Profile profile = Profile.get(player.getUniqueId());
        Match teamMatch = profile.getMatch();

        if (teamMatch != null) {
            if (teamMatch instanceof BasicTeamMatch) {
                if (teamMatch.getKit().getGameRules().isBoxing()) {
                    HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING_BOXING").forEach(s ->
                            lines.add(s
                                    .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                                    .replace("{duration}", profile.getMatch().getDuration().replace("{arena}", profile.getMatch().getArena().getName()))
                                    .replace("{kit}", profile.getMatch().getKit().getDisplayName())
                                    .replace("{arena}", profile.getMatch().getArena().getName())
                                    .replace("{playerA_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getHits()))
                                    .replace("{playerB_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getHits()))));
                } else if (teamMatch.getKit().getGameRules().isBedFight()) {
                    HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING_BEDFIGHT").forEach(s ->
                            lines.add(s
                                    .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                                    .replace("{duration}", profile.getMatch().getDuration().replace("{arena}", profile.getMatch().getArena().getName()))
                                    .replace("{kit}", profile.getMatch().getKit().getDisplayName())
                                    .replace("{arena}", profile.getMatch().getArena().getName())
                                    .replace("{redHasBed}", ((BasicTeamMatch) profile.getMatch()).getParticipantA().isHasBed() ? "&a✔" : "&c✗")
                                    .replace("{blueHasBed}", ((BasicTeamMatch) profile.getMatch()).getParticipantB().isHasBed() ? "&a✔" : "&c✗")));
                } else if (teamMatch.getKit().getGameRules().isLives()) {
                    HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING_LIVES").forEach(s ->
                            lines.add(s
                                    .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                                    .replace("{duration}", profile.getMatch().getDuration().replace("{arena}", profile.getMatch().getArena().getName()))
                                    .replace("{kit}", profile.getMatch().getKit().getDisplayName())
                                    .replace("{arena}", profile.getMatch().getArena().getName())
                                    .replace("{redLives}", StringUtils.getStringPointLose(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLives(), org.bukkit.ChatColor.RED, 3))
                                    .replace("{blueLives}", StringUtils.getStringPointLose(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLives(), org.bukkit.ChatColor.BLUE, 3))));
                } else {
                    HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING").forEach(s ->
                            lines.add(s
                                    .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                                    .replace("{duration}", profile.getMatch().getDuration())
                                    .replace("{kit}", profile.getMatch().getKit().getDisplayName())
                                    .replace("{arena}", profile.getMatch().getArena().getName())));
                }
            } else {
                HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING").forEach(s ->
                        lines.add(s
                                .replace("{duration}", profile.getMatch().getDuration())
                                .replace("{kit}", profile.getMatch().getKit().getDisplayName())
                                .replace("{arena}", profile.getMatch().getArena().getName())));
            }
        } else {
            switch (HyPractice.get().getRankManager().getRankSystem()) {
                case "Aspirin": {
                    HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.LOBBY").forEach(s ->
                            lines.add(s
                                    .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                    .replace("{in-fights}", String.valueOf(HyPractice.get().getInFights()))
                                    .replace("{players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                    .replace("{staffs}", String.valueOf(Bukkit.getOnlinePlayers().stream().filter(player1 -> Profile.get(player1.getUniqueId()).getState() == ProfileState.STAFF_MODE).count()))
                                    .replace("{in-fight}", String.valueOf(HyPractice.get().getInFights()))
                                    .replace("{in-staff-chat}", String.valueOf(AspirinAPI.INSTANCE.isStaffChat(player)))));
                    break;
                }
                case "Akuma": {
                    HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.LOBBY").forEach(s ->
                            lines.add(s
                                    .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                    .replace("{in-fights}", String.valueOf(HyPractice.get().getInFights()))
                                    .replace("{players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                    .replace("{staffs}", String.valueOf(Bukkit.getOnlinePlayers().stream().filter(player1 -> Profile.get(player1.getUniqueId()).getState() == ProfileState.STAFF_MODE).count()))
                                    .replace("{in-fight}", String.valueOf(HyPractice.get().getInFights()))
                                    .replace("{in-staff-chat}", String.valueOf(AkumaAPI.getInstance().getProfileHandler().getProfile(player.getUniqueId()).getChatChannel() == ChatChannel.STAFF))
                    ));
                    break;
                }
                default: {
                    HyPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.LOBBY").forEach(s ->
                            lines.add(s
                                    .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                    .replace("{in-fights}", String.valueOf(HyPractice.get().getInFights()))
                                    .replace("{players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                    .replace("{staffs}", String.valueOf(Bukkit.getOnlinePlayers().stream().filter(player1 -> Profile.get(player1.getUniqueId()).getState() == ProfileState.STAFF_MODE).count()))
                                    .replace("{in-fight}", String.valueOf(HyPractice.get().getInFights()))));
                    break;
                }
            }
        }

        return lines;
    }

    private static String format(double tps) {
        int max = 20;
        return ((tps > 18.0D) ? ChatColor.GREEN : ((tps > 16.0D) ? ChatColor.YELLOW : ChatColor.RED)) + ((tps > max) ? "*" : "") + Math.min(Math.round(tps * 100.0D) / 100.0D, max);
    }
}
