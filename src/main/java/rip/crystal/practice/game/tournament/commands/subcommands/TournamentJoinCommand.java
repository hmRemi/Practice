package rip.crystal.practice.game.tournament.commands.subcommands;

import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.tournament.impl.TournamentSolo;

public class TournamentJoinCommand extends BaseCommand {

    @Command(name = "tournament.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        TournamentSolo tournament = (TournamentSolo) TournamentSolo.getTournament();

        if (tournament == null) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if(profile.isBusy()) {
            player.sendMessage(ChatColor.RED + "You may not join the tournament in your current state.");
            return;
        }
        if (profile.isInTournament()) {
            player.sendMessage(ChatColor.RED + "You are already in the tournament.");
            return;
        }

        profile.setInTournament(true);
        tournament.join(player);
    }
}