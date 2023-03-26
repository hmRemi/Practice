package com.hysteria.practice.game.arena.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.entity.Player;

public class ArenaDeleteCommand extends BaseCommand {

	@Command(name = "arena.delete", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		Arena arena = Arena.getByName(args[0]);
		if (arena != null) {
			arena.delete();

			player.sendMessage(CC.translate("&7Deleted arena &b" + arena.getName()));
		} else {
			player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
		}
	}

}
