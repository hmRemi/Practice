package rip.crystal.practice.player.profile.modmode;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.activated.core.plugin.AquaCoreAPI;
import net.audidevelopment.core.plugin.cCore;
import net.audidevelopment.core.plugin.cCoreAPI;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.lag.LagRunnable;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.visibility.VisibilityLogic;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Modmode {

    @Getter public static Set<UUID> staffmode = Sets.newConcurrentHashSet();

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
            cPractice.get().getEssentials().teleportToSpawn(player);

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

        if(teamMatch != null) {
            if (teamMatch instanceof BasicTeamMatch) {
                if (teamMatch.getKit().getGameRules().isBoxing()) {
                    cPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING").forEach(s ->
                        lines.add(s
                            .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                            .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                            .replace("{duration}", profile.getMatch().getDuration().replace("{arena}", profile.getMatch().getArena().getName()))
                                .replace("{kit}", profile.getMatch().getKit().getName())
                            .replace("{arena}", profile.getMatch().getArena().getName())
                            .replace("{playerA_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getHits()))
                            .replace("{playerB_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getHits()))));
                } else {
                    cPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING").forEach(s ->
                        lines.add(s
                            .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                            .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                            .replace("{duration}", profile.getMatch().getDuration())
                            .replace("{kit}", profile.getMatch().getKit().getName())
                            .replace("{arena}", profile.getMatch().getArena().getName())));
                }
            } else {
                cPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING").forEach(s ->
                    lines.add(s
                        .replace("{duration}", profile.getMatch().getDuration())
                        .replace("{kit}", profile.getMatch().getKit().getName())
                        .replace("{arena}", profile.getMatch().getArena().getName())));
            }
        } else {
            if (cPractice.get().getRankManager().getRankSystem().equals("cCore")) {
                cPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.LOBBY").forEach(s ->
                        lines.add(s
                                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replace("{in-fights}", String.valueOf(cPractice.get().getInFights()))
                                .replace("{players}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replace("{staffs}", String.valueOf(Bukkit.getOnlinePlayers().stream().filter(player1 -> Profile.get(player1.getUniqueId()).getState() == ProfileState.STAFF_MODE).count()))
                                .replace("{in-fight}", String.valueOf(cPractice.get().getInFights()))
                                .replace("{isStaffChat}", String.valueOf(cCoreAPI.INSTANCE.isStaffChat(player)))));
            }
        }

        return lines;
    }

    private static String format(double tps) {
        int max = 20;
        return ((tps > 18.0D) ? ChatColor.GREEN : ((tps > 16.0D) ? ChatColor.YELLOW : ChatColor.RED)) + ((tps > max) ? "*" : "") + Math.min(Math.round(tps * 100.0D) / 100.0D, max);
    }
}
