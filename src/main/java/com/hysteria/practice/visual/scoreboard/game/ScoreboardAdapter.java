package com.hysteria.practice.visual.scoreboard.game;
/* 
   Made by hypractice Development Team
   Created on 27.10.2021
*/

import com.google.common.collect.Lists;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.match.impl.BasicFreeForAllMatch;
import com.hysteria.practice.match.impl.BasicTeamMatch;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.PlayerUtil;
import com.hysteria.practice.utilities.StringUtils;

import org.bukkit.entity.Player;

import java.util.List;

public class ScoreboardAdapter {

    public static List<String> getScoreboardLinesSpectator(Player player) {
        List<String> lines = Lists.newArrayList();
        Profile profile = Profile.get(player.getUniqueId());
        Match teamMatch = profile.getMatch();

        if(teamMatch != null) {
            if (teamMatch instanceof BasicTeamMatch) {
                if (teamMatch.getKit().getGameRules().isBoxing()) {
                    HyPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING_BOXING").forEach(s ->
                            lines.add(s
                                    .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                                    .replace("{duration}", profile.getMatch().getDuration().replace("{arena}", profile.getMatch().getArena().getName()))
                                    .replace("{kit}", profile.getMatch().getKit().getName())
                                    .replace("{arena}", profile.getMatch().getArena().getName())
                                    .replace("{playerA_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getHits()))
                                    .replace("{playerB_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getHits()))));
                } else if (teamMatch.getKit().getGameRules().isBedFight()) {
                    HyPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING_BEDFIGHT").forEach(s ->
                            lines.add(s
                                    .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                                    .replace("{duration}", profile.getMatch().getDuration().replace("{arena}", profile.getMatch().getArena().getName()))
                                    .replace("{kit}", profile.getMatch().getKit().getName())
                                    .replace("{arena}", profile.getMatch().getArena().getName())
                                    .replace("{redHasBed}", ((BasicTeamMatch) profile.getMatch()).getParticipantA().isHasBed() ? "&a✔" : "&c✗")
                                    .replace("{blueHasBed}", ((BasicTeamMatch) profile.getMatch()).getParticipantB().isHasBed() ? "&a✔" : "&c✗")));
                } else if (teamMatch.getKit().getGameRules().isLives()) {
                    HyPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING_LIVES").forEach(s ->
                            lines.add(s
                                    .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                                    .replace("{duration}", profile.getMatch().getDuration().replace("{arena}", profile.getMatch().getArena().getName()))
                                    .replace("{kit}", profile.getMatch().getKit().getName())
                                    .replace("{arena}", profile.getMatch().getArena().getName())
                                    .replace("{redLives}", StringUtils.getStringPointLose(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLives(), org.bukkit.ChatColor.RED, 3))
                                    .replace("{blueLives}", StringUtils.getStringPointLose(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLives(), org.bukkit.ChatColor.BLUE, 3))));
                } else {
                    HyPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING").forEach(s ->
                        lines.add(s
                            .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                            .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                            .replace("{duration}", profile.getMatch().getDuration())
                            .replace("{kit}", profile.getMatch().getKit().getName())
                            .replace("{arena}", profile.getMatch().getArena().getName())));
                    //.replace("{ranked}", (profile.getMatch().getQueue().isRanked() ? "&aTrue" : "&cFalse"))
                }
            } else if (teamMatch instanceof BasicFreeForAllMatch) {
                HyPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING").forEach(s ->
                    lines.add(s
                        .replace("{duration}", profile.getMatch().getDuration())
                        .replace("{kit}", profile.getMatch().getKit().getName())
                        .replace("{arena}", profile.getMatch().getArena().getName())));
            }
        }
        return lines;
    }

    public static List<String> getScoreboardLinesFFA(Player player) {
        List<String> lines = Lists.newArrayList();
        //Profile profile = Profile.get(player.getUniqueId());

        HyPractice.get().getScoreboardConfig().getStringList("FIGHTS.FFA").forEach(s ->
                lines.add(s
                        .replace("{players}", String.valueOf(HyPractice.get().getFfaManager().getFFAPlayers().size()))
                        .replace("{ping}", String.valueOf(PlayerUtil.getPing(player)))
                        .replace("{kit}", HyPractice.get().getFfaManager().getKit().getName())));

        return lines;
    }

}
