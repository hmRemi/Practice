package com.hysteria.practice.game.ffa.command;
/* 
   Made by hypractice Development Team
   Created on 27.11.2021
*/

import com.hysteria.practice.game.ffa.command.subcommands.FFAJoinCommand;
import com.hysteria.practice.game.ffa.command.subcommands.FFALeaveCommand;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class FFACommand extends BaseCommand {

    public FFACommand() {
        new FFALeaveCommand();
        new FFAJoinCommand();
    }

    @Command(name = "ffa")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        final Player player = commandArgs.getPlayer();
        final String[] args = commandArgs.getArgs();
        if (args.length == 0) {
            player.sendMessage(CC.translate(("&b/ffa leave &7- &fLeave FFA")));
            player.sendMessage(CC.translate(("&b/ffa join &7- &fJoin FFA")));
        }
    }
}
