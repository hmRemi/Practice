package rip.crystal.practice.game.tournament.commands;

import rip.crystal.practice.game.tournament.commands.subcommands.*;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
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
            player.sendMessage(CC.translate("&c&lTournament &7&m-&r &c&lAdmin Help"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament start &8<&7kit&8> &8(&7&oStart a tournament with kit&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament status &8(&7&oShow status of ongoing tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament forcestart &8(&7&oForcestart tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament stop &8(&7&oStop the tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament leave &8(&7&oLeave tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament join &8(&7&oJoin tournament&8&o)"));
            player.sendMessage(CC.CHAT_BAR);
        } else {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&c&lTournament &7&m-&r &c&lHelp"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament status &8(&7&oShow status of ongoing tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament leave &8(&7&oLeave tournament&8&o)"));
            player.sendMessage(CC.translate(" &7▢ &4/tournament join &8(&7&oJoin tournament&8&o)"));
            player.sendMessage(CC.CHAT_BAR);
        }
    }
}