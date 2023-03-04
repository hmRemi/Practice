package com.hysteria.practice.match.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CancelAllMatchesCommand extends BaseCommand {

    @Command(name = "cancelallmatches", permission = "cpractice.command.cancelallmatches")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Bukkit.broadcastMessage(CC.CHAT_BAR);
        Bukkit.broadcastMessage(CC.translate("&8[&c&lMatch&8] &7All matches has been cancelled."));
        Bukkit.broadcastMessage(CC.translate("&8[&c&lMatch&8] &7Preparing for reboot"));
        Bukkit.broadcastMessage(CC.CHAT_BAR);
        Match.getMatches().forEach(Match::end);

    }
}
