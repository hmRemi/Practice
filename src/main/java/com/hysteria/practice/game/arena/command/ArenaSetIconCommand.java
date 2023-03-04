package com.hysteria.practice.game.arena.command;

import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaSetIconCommand extends BaseCommand {

    @Command(name = "arena.seticon", permission = "cpractice.arena.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /arena seticon (arena)");
            return;
        }

        Arena arena = Arena.getByName(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "This arena doesn't exist.");
            return;
        }

        arena.setDisplayIcon(player.getItemInHand());
        arena.save();
        player.sendMessage(ChatColor.GREEN + "Kit icon update");
    }
}