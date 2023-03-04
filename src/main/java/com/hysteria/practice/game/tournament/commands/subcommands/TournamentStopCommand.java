package com.hysteria.practice.game.tournament.commands.subcommands;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentStopCommand extends BaseCommand {

    @Command(name = "tournament.stop", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        HyPractice.get().getTournamentManager().cancelTournament(player);
    }
}