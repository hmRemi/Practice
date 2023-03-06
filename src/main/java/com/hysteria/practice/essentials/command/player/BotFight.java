package com.hysteria.practice.essentials.command.player;

import com.hysteria.practice.match.bot.BotFightMenu;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.utilities.chat.CC;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/22/2023
 */
public class BotFight extends BaseCommand {

    @Command(name = "botfight")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.translate("&7Currently under development"));
    }
}