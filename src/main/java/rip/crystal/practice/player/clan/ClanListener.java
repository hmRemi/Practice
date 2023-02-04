package rip.crystal.practice.player.clan;

import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.game.tournament.events.TournamentEndEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClanListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Profile profile = Profile.get(event.getPlayer().getUniqueId());

        String prefix = "@";

        if(profile.getParty() != null) {
            prefix = "#";
        }

        if (event.getMessage().startsWith(prefix)) {
            if (profile.getClan() != null) {
                event.setCancelled(true);
                profile.getClan().sendChat(event.getPlayer(), ChatColor.stripColor(event.getMessage().substring(1)));
            }
        }
    }

    @EventHandler
    public void onClanWinTournament(TournamentEndEvent event){
        if(!event.isClan()) return;
        MatchGamePlayer leader = event.getWinner().getLeader();
        Player player = leader.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        clan.addPoints(cPractice.get().getMainConfig().getInteger("WINNING-POINTS-CLAN-TOURNAMENT"));
    }
}