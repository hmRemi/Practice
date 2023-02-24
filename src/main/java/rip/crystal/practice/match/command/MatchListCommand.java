package rip.crystal.practice.match.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.match.menu.MatchList;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/23/2023
 */
public class MatchListCommand extends BaseCommand {
    @Command(name = "matchlist")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new MatchList().openMenu(player);
    }
}
