package rip.crystal.practice.essentials.command.player;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.match.bot.BotFightMenu;
import rip.crystal.practice.player.profile.menu.LangMenu;
import rip.crystal.practice.utilities.chat.CC;

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
        //new BotFightMenu().openMenu(player);

    }
}