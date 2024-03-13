package com.hysteria.practice.game.tournament.commands.subcommands;

import com.hysteria.practice.game.tournament.Tournament;
import com.hysteria.practice.game.tournament.TournamentState;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
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
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate( "&b&lTournament &7&m-&r &c&lStatus"));
        player.sendMessage(CC.translate(" &7▢ &bPlayers: &7" + tournament.getPlayers().size() + "/" + tournament.getLimit()));
        player.sendMessage(CC.translate(" &7▢ &bMatches: &7" + tournament.getMatches().size()));
        player.sendMessage(CC.translate(" &7▢ &bStage: &7" + tournament.getState().getName()));
        player.sendMessage(CC.translate(" &7▢ &bKit: &7" + tournament.getKit().getName()));
        player.sendMessage(CC.CHAT_BAR);
    }
}
