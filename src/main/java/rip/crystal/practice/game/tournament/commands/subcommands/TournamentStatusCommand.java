package rip.crystal.practice.game.tournament.commands.subcommands;

import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.game.tournament.TournamentState;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentStatusCommand extends BaseCommand {

    @Command(name = "tournament.status")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament<?> tournament = Tournament.getTournament();

        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(ChatColor.RED + "No tournament found.");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate( "&4&lTournament &7&m-&r &4&lStatus"));
        player.sendMessage(CC.translate(" &7▢ &4Players: &7" + tournament.getPlayers().size() + "/" + tournament.getLimit()));
        player.sendMessage(CC.translate(" &7▢ &4Matches: &7" + tournament.getMatches().size()));
        player.sendMessage(CC.translate(" &7▢ &4Stage: &7" + tournament.getState().getName()));
        player.sendMessage(CC.translate(" &7▢ &4Kit: &7" + tournament.getKit().getName()));
        player.sendMessage(CC.CHAT_BAR);
    }
}
