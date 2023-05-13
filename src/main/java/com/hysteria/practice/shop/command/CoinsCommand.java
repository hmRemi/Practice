package com.hysteria.practice.shop.command;

import com.hysteria.practice.api.chat.ChatUtil;
import com.hysteria.practice.player.profile.Profile;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.utilities.chat.CC;

public class CoinsCommand extends BaseCommand {

    @Command(name="coins", aliases = {"coin"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        CC.sender(player, "");
        CC.sender(player, " &6[COINS] &fEarn coins by playing");
        CC.sender(player, " &fand winning matches.");
        CC.sender(player, "");
        CC.sender(player, " &8▪ &fBalance: &6" + profile.getCoins());
        CC.sender(player, " ");
        CC.sender(player, "&7Do /shop to open coin shop.");
        CC.sender(player, "");
    }
}
