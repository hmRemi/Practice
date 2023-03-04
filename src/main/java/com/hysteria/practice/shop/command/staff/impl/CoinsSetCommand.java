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

public class CoinsSetCommand extends BaseCommand {

    @Command(name = "coinsmanager.set", aliases = {"coinsm.set", "cmanager.set", "cm.set"}, permission = "cpractice.command.coinsmanager.set", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String label = command.getLabel().replace(".set", "");
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " set <player> <amount>"));
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
        profile.setCoins(amount);
        sender.sendMessage(ChatUtil.translate("&fCoins set to &4" + amount + " &ffor &4" + player.getName() + "&f."));
    }

}
