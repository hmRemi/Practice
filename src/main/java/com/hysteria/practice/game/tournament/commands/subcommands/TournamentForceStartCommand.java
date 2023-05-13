package com.hysteria.practice.game.tournament.commands.subcommands;
/*
   Made by hypractice Development Team
   Created on 10.10.2021
*/

import com.hysteria.practice.game.tournament.Tournament;
import com.hysteria.practice.game.tournament.TournamentState;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.hysteria.practice.utilities.chat.CC;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentForceStartCommand extends BaseCommand {

    @Command(name = "tournament.forcestart", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament<?> tournament = Tournament.getTournament();
        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        if(!tournament.isStarted()) {
            tournament.start();
            player.sendMessage(CC.translate("&7You have force started the tournament!"));
        }
    }
}
