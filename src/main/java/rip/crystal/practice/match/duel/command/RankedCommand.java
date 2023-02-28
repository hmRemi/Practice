package rip.crystal.practice.match.duel.command;

import net.audidevelopment.core.plugin.cCore;
import rip.crystal.practice.player.queue.menus.QueueSelectKitMenu;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.chat.CC;

public class RankedCommand extends BaseCommand {

    @Command(name = "ranked")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new QueueSelectKitMenu(true).openMenu(player);
    }
}
