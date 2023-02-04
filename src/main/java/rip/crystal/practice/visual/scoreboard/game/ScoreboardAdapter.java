package rip.crystal.practice.visual.scoreboard.game;
/* 
   Made by cpractice Development Team
   Created on 27.10.2021
*/

import com.google.common.collect.Lists;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicFreeForAllMatch;
import rip.crystal.practice.match.impl.BasicTeamMatch;

import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
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
                    for (String line : cPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING_BOXING")) {
                        lines.add(line
                            .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                            .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                            .replace("{duration}", profile.getMatch().getDuration())
                            .replace("{kit}", profile.getMatch().getKit().getName())
                            .replace("{playerA_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getHits()))
                            .replace("{playerB_hits}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getHits()))
                            .replace("{arena}", profile.getMatch().getArena().getName()));
                    }
                } else {
                    cPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING").forEach(s ->
                        lines.add(s
                            .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                            .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                            .replace("{duration}", profile.getMatch().getDuration())
                            .replace("{kit}", profile.getMatch().getKit().getName())
                            .replace("{arena}", profile.getMatch().getArena().getName())));
                    //.replace("{ranked}", (profile.getMatch().getQueue().isRanked() ? "&aTrue" : "&cFalse"))
                }
            } else if (teamMatch instanceof BasicFreeForAllMatch) {
                cPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING").forEach(s ->
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

        cPractice.get().getScoreboardConfig().getStringList("FIGHTS.FFA").forEach(s ->
                lines.add(s
                        .replace("{players}", String.valueOf(cPractice.get().getFfaManager().getFFAPlayers().size()))
                        .replace("{ping}", String.valueOf(PlayerUtil.getPing(player)))
                        .replace("{kit}", cPractice.get().getFfaManager().getKit().getName())));

        return lines;
    }

}
