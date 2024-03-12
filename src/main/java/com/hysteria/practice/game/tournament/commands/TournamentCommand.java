package com.hysteria.practice.game.tournament.commands;

import com.hysteria.practice.game.tournament.commands.subcommands.*;
import com.hysteria.practice.game.tournament.commands.subcommands.*;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentCommand extends BaseCommand {

    public TournamentCommand() {
        new TournamentStatusCommand();
        new TournamentForceStartCommand();
        new TournamentJoinCommand();
        new TournamentStartCommand();
        new TournamentStopCommand();
        new TournamentLeaveCommand();
    }

    @Command(name = "tournament")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        if(player.hasPermission("tournament.admin")) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&b&lTournament &7&m-&r &b&lAdmin Help"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament start &8<&7type&8> <&7kit&8> &8(&7&oStart a tournament with kit&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament status &8(&7&oShow status of ongoing tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament forcestart &8(&7&oForcestart tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament stop &8(&7&oStop the tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament leave &8(&7&oLeave tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament join &8(&7&oJoin tournament&8&o)"));
            player.sendMessage(CC.CHAT_BAR);
        } else {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&b&lTournament &7&m-&r &b&lHelp"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament status &8(&7&oShow status of ongoing tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament leave &8(&7&oLeave tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &b/tournament join &8(&7&oJoin tournament&8&o)"));
            player.sendMessage(CC.CHAT_BAR);
        }
    }
}
