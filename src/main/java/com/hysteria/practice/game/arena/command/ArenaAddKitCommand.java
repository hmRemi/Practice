package com.hysteria.practice.game.arena.command;

import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import com.hysteria.practice.utilities.chat.CC;

public class ArenaAddKitCommand extends BaseCommand {

	@Command(name = "arena.addkit", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		Arena arena = Arena.getByName(args[0]);
		if (arena == null) {
			player.sendMessage(CC.translate("&7An arena with that name does not exist."));
			return;
		}

		Kit kit = Kit.getByName(args[1]);
		if (kit == null) {
			player.sendMessage(CC.translate("&7A kit with that name does not exist."));
			return;
		}

		arena.getKits().add(kit.getName());
		arena.save();

		player.sendMessage(CC.translate("&7Added kit &4" + kit.getName() + " &7to arena &4" + arena.getName()));
	}

}
