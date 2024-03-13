package com.hysteria.practice.game.tournament.commands.subcommands;

import com.hysteria.practice.game.tournament.Tournament;
import com.hysteria.practice.game.tournament.impl.TournamentClans;
import com.hysteria.practice.game.tournament.impl.TournamentSolo;
import com.hysteria.practice.game.tournament.impl.TournamentTeams;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TournamentJoinCommand extends BaseCommand {

    @Command(name = "tournament.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament<?> tournament = Tournament.getTournament();

        if (tournament == null) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if (profile.isBusy()) {
            player.sendMessage(ChatColor.RED + "You may not join the tournament in your current state.");
            return;
        }
        if (profile.isInTournament()) {
            player.sendMessage(ChatColor.RED + "You are already in the tournament.");
            return;
        }

        if (tournament instanceof TournamentSolo) {
            TournamentSolo soloTournament = (TournamentSolo) tournament;
            soloTournament.join(player);
        } else if (tournament instanceof TournamentClans) {
            TournamentClans clansTournament = (TournamentClans) tournament;
            clansTournament.join(player);
        } else if (tournament instanceof TournamentTeams) {
            TournamentTeams teamsTournament = (TournamentTeams) tournament;
            teamsTournament.join(player);
        } else {
            player.sendMessage(ChatColor.RED + "This command is not applicable for the current tournament type.");
        }
    }
}
