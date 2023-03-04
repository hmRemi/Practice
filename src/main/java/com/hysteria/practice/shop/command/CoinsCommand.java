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

        CC.sender(player, ChatUtil.NORMAL_LINE);
        CC.sender(player, " &c&lCoins");
        CC.sender(player, "");
        CC.sender(player, "&4Your coins: &7" + profile.getCoins());
        CC.sender(player, ChatUtil.NORMAL_LINE);
    }
}
