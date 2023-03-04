package com.hysteria.practice.essentials.command.staff;

import com.hysteria.practice.player.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.utilities.chat.CC;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 3/3/2023
 */
public class FollowCommand extends BaseCommand {

    @Command(name="follow", permission = "cpractice.follow")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(CC.translate("&7You must specify a player to follow"));
            return;
        }

        if(args.length >= 2) {
            player.sendMessage(CC.translate("&7You may only use /follow <player> to follow"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(CC.translate("&7Player not found"));
            return;
        }

        CommandSender sender = command.getSender();
        if(!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&7You can only use this in-game."));
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if(profile.getFollow().getFollowed() == null) {
            profile.follow(target);
        }
    }
}
