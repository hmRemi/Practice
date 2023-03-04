package com.hysteria.practice.game.arena.command;

import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaSetAuthorCommand extends BaseCommand {

    @Command(name = "arena.setauthor", permission = "cpractice.arena.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena setauthor (arena) (author)"));
            return;
        }

        Arena arena = Arena.getArenas().stream()
                .filter(val -> val.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);
        String author = args[1];

        if (arena == null) {
            player.sendMessage(CC.translate("&cPlease usage a valid arena name"));
            return;
        }

        arena.setAuthor(author);
        player.sendMessage(CC.translate("&cAuthor of &f" + arena.getName() + "&f has seen set to &c" + author + "&f."));
        arena.save();
    }
}