package rip.crystal.practice.match.duel.command;

import rip.crystal.practice.player.queue.menus.QueueSelectKitMenu;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class UnRankedCommand extends BaseCommand {

    @Command(name = "unranked")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new QueueSelectKitMenu(false).openMenu(player);
    }
}