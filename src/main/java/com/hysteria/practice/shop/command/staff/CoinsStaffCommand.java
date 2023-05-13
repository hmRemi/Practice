package com.hysteria.practice.shop.command.staff;

import com.hysteria.practice.api.chat.ChatUtil;
import com.hysteria.practice.shop.command.staff.impl.CoinsGiveCommand;
import com.hysteria.practice.shop.command.staff.impl.CoinsSetCommand;
import com.hysteria.practice.shop.command.staff.impl.CoinsTakeCommand;
import org.bukkit.command.CommandSender;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.shop.command.staff.impl.*;
import com.hysteria.practice.utilities.chat.CC;

public class CoinsStaffCommand extends BaseCommand {

    public CoinsStaffCommand() {
        new CoinsGiveCommand();
        new CoinsSetCommand();
        new CoinsTakeCommand();
    }

    @Command(name = "coinsmanager", aliases = {"coinsm", "cmanager", "cm"}, permission = "hypractice.command.coinsmanager", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String label = command.getLabel();

        CC.sender(sender, ChatUtil.NORMAL_LINE);
        CC.sender(sender, " &c&lCoins Manager");
        CC.sender(sender, "");
        CC.sender(sender, "&4/" + label + " give <player> <amount> &7- &fGive coins to a player");
        CC.sender(sender, "&4/" + label + " set <player> <amount> &7- &fSet coins for a player");
        CC.sender(sender, "&4/" + label + " take <player> <amount> &7- &fTake coins from a player");
        CC.sender(sender, ChatUtil.NORMAL_LINE);
    }

}
