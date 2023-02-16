package rip.crystal.practice.game.tournament.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.game.tournament.TournamentState;
import rip.crystal.practice.game.tournament.impl.TournamentClans;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.profile.participant.team.TeamGameParticipant;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentManager {

    /**
     * Cancel the tournament
     *
     * @param player {@link Player} the person cancelling
     */

    public void cancelTournament(Player player) {
        Tournament<?> tournament = Tournament.getTournament();

        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(ChatColor.RED + "No tournament found.");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }
        if ((tournament.getState() == TournamentState.IN_FIGHT || tournament.getState() == TournamentState.SELECTING_DUELS) && tournament.getTeams().size() == 1) {tournament.end(tournament.getTeams().get(0));
            return;
        }

        tournament.end(null);

        Profile profile = Profile.get(player.getUniqueId());
        profile.setState(ProfileState.LOBBY);
        Hotbar.giveHotbarItems(player);
    }

    /**
     * Have a player join the tournament
     *
     * @param player {@link Player} player to join
     */

    public void handleJoin(Player player) {
        Tournament<?> tournament = Tournament.getTournament();
        Profile profile = Profile.get(player.getUniqueId());

        profile.setInTournament(true);
        profile.setState(ProfileState.TOURNAMENT);
        Hotbar.giveHotbarItems(player);

        MatchGamePlayer playerA = new MatchGamePlayer(player.getUniqueId(), player.getName());
        tournament.getTeams().add(new GameParticipant<>(playerA));
        tournament.getPlayers().add(player.getUniqueId());

        Tournament.getTournament().broadcast("&4" + player.getName() + "&7 has joined the tournament.");
    }

    /**
     * Have a player join the tournament
     *
     * @param party {@link Party} leader of the party
     */

    public void handleJoin(Party party) {
        Tournament<?> tournament = Tournament.getTournament();
        Player partyLeader = party.getLeader();
        Profile profile = Profile.get(partyLeader.getUniqueId());

        MatchGamePlayer leader = new MatchGamePlayer(partyLeader.getUniqueId(), partyLeader.getName());
        TeamGameParticipant<MatchGamePlayer> teamGameParticipant = new TeamGameParticipant<>(leader);

        party.getListOfPlayers().forEach(player -> {
            tournament.getPlayers().add(player.getUniqueId());

            profile.setInTournament(true);
            profile.setState(ProfileState.TOURNAMENT);
            Hotbar.giveHotbarItems(player);

            if (!player.getPlayer().equals(partyLeader)) {
                MatchGamePlayer gamePlayer = new MatchGamePlayer(player.getUniqueId(), player.getName());
                teamGameParticipant.getPlayers().add(gamePlayer);
            }
        });
        tournament.getTeams().add(teamGameParticipant);
        tournament.broadcast("&7Party of " + partyLeader.getDisplayName() + "&7 has join to tournament.");
    }

    /**
     * Have a player join the tournament
     *
     * @param clan {@link Clan} the clan entering
     */

    public void handleJoin(Clan clan) {
        Tournament<?> tournament = Tournament.getTournament();

        Player clanLeader = Bukkit.getPlayer(clan.getLeader());
        Profile profile = Profile.get(clanLeader.getUniqueId());

        MatchGamePlayer leader = new MatchGamePlayer(clanLeader.getUniqueId(), clanLeader.getName());
        TeamGameParticipant<MatchGamePlayer> teamGameParticipant = new TeamGameParticipant<>(leader);

        clan.getOnPlayers().forEach(player -> {
            tournament.getPlayers().add(player.getUniqueId());

            profile.setInTournament(true);
            profile.setState(ProfileState.TOURNAMENT);
            Hotbar.giveHotbarItems(player);

            if (!player.getPlayer().equals(clanLeader)) {
                MatchGamePlayer gamePlayer = new MatchGamePlayer(player.getUniqueId(), player.getName());
                teamGameParticipant.getPlayers().add(gamePlayer);
            }
        });
        TournamentClans.clans.put(clan, teamGameParticipant);
        tournament.getTeams().add(teamGameParticipant);

        tournament.broadcast("&7Clan &4" + clan.getName() + "&7 has join to tournament.");
    }

    /**
     * Handle a player's leave from the tournament
     *
     * @param player {@link Player} the player leaving
     */

    public void handleLeave(Player player) {
        Tournament<?> tournament = Tournament.getTournament();

        Profile profile = Profile.get(player.getUniqueId());
        if(profile.isInTournament()) {
            tournament.removePlayer(player.getUniqueId());
            tournament.getPlayers().remove(player.getUniqueId());
            tournament.getTeams().remove(Tournament.getTournament().getParticipant(player));

            profile.setState(ProfileState.LOBBY);
            profile.setInTournament(false);

            PlayerUtil.reset(player);
            player.teleport(cPractice.get().getEssentials().getSpawn());
            Hotbar.giveHotbarItems(player);

            Tournament.getTournament().broadcast("&4" + player.getName() + "&7 has left the tournament.");
        }
    }

    /**
     * Have a party or player leave the tournament
     *
     * @param player {@link Player} the leader of the party
     */

    public void forceLeave(Player player) {
        Tournament<?> tournament = Tournament.getTournament();

        Profile profile = Profile.get(player.getUniqueId());
        if(profile.isInTournament()) {
            tournament.removePlayer(player.getUniqueId());
            tournament.getPlayers().remove(player.getUniqueId());
            tournament.getTeams().remove(Tournament.getTournament().getParticipant(player));

            profile.setState(ProfileState.LOBBY);
            profile.setInTournament(false);
        }
    }

}
