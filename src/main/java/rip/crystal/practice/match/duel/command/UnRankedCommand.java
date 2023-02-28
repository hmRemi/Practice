package rip.crystal.practice.match.duel.command;

import net.audidevelopment.core.plugin.cCore;
import rip.crystal.practice.player.queue.menus.QueueSelectKitMenu;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.chat.CC;

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
