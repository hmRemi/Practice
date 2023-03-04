package com.hysteria.practice.match.duel.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.utilities.chat.CC;
import net.audidevelopment.core.plugin.cCore;
import com.hysteria.practice.player.queue.menus.QueueSelectKitMenu;
import org.bukkit.entity.Player;

public class UnRankedCommand extends BaseCommand {

    @Command(name = "unranked")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        if(cCore.INSTANCE.getServerManagement().isServerRebooting()) {
            player.sendMessage(CC.translate("&7You cannot queue during a reboot"));
            return;
        }

        new QueueSelectKitMenu(false).openMenu(player);
    }
}
