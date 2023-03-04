package com.hysteria.practice.essentials.command.management;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class AdminInformationCommand extends BaseCommand {

    @Command(name = "admin", aliases = {"admininformation"}, permission = "cpractice.owner")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate(" &7▢ &4Plugin: &r" + "HyPractice"));
        player.sendMessage(CC.translate(" &7▢ &4Version: &r" + HyPractice.get().getDescription().getVersion()));
        player.sendMessage(CC.translate(" &7▢ &4License: &r" + HyPractice.get().getMainConfig().getString("LICENSE")));
        player.sendMessage(CC.translate(" &7▢ &4Developer: &r" + "ziue"));
        player.sendMessage(CC.CHAT_BAR);
    }
}
