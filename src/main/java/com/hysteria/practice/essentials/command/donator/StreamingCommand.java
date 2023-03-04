package com.hysteria.practice.essentials.command.donator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.utilities.chat.CC;

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
            player.sendMessage(CC.translate("&8[&c&lStream&8] &7/stream <link>"));
        } else {
            Bukkit.broadcastMessage(CC.CHAT_BAR);
            Bukkit.broadcastMessage(CC.translate("&c&l" + player.getName() + " &7is streaming."));
            Bukkit.broadcastMessage(CC.translate("&c&lURL: &7" + args[0]));
            Bukkit.broadcastMessage(CC.CHAT_BAR);
        }
    }
}
