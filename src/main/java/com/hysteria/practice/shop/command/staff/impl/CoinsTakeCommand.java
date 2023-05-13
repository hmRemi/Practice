package com.hysteria.practice.shop.command.staff.impl;

import com.hysteria.practice.api.chat.ChatUtil;
import com.hysteria.practice.api.utilities.JavaUtil;
import com.hysteria.practice.player.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;

public class CoinsTakeCommand extends BaseCommand {

    @Command(name = "coinsmanager.take", aliases = {"coinsm.take", "cmanager.take", "cm.take"}, permission = "hypractice.command.coinsmanager.take", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String label = command.getLabel().replace(".take", "");
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " take <player> <amount>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(ChatUtil.translate("&cPlayer " + args[0] + " not found!"));
            return;
        }

        Integer amount = JavaUtil.tryParseInt(args[1]);

        if (amount == null) {
            sender.sendMessage(ChatUtil.translate("&cAmount must be a number."));
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        profile.removeCoins(amount);
        sender.sendMessage(ChatUtil.translate("&fYou have taken &4" + amount + " &fcoins from &4" + player.getName() + "&f."));
    }
}
