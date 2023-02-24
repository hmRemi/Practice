package rip.crystal.practice.essentials.command.donator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/24/2023
 */
public class StreamingCommand extends BaseCommand {

    @Command(name = "stream", aliases = {"stream"}, permission = "cpractice.stream")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&8[&4&lStream&8] &7/stream <link>"));
        } else {
            Bukkit.broadcastMessage(CC.CHAT_BAR);
            Bukkit.broadcastMessage(CC.translate("&4&l" + player.getName() + " &7is streaming."));
            Bukkit.broadcastMessage(CC.translate("&4&lStream: " + args[0]));
            Bukkit.broadcastMessage(CC.CHAT_BAR);
        }
    }
}
