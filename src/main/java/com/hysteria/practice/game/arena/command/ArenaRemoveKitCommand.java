package com.hysteria.practice.game.arena.command;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaRemoveKitCommand extends BaseCommand {

	@Command(name = "arena.removekit", permission = "hypractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		Arena arena = Arena.getByName(args[0]);
		if (arena == null) {
			player.sendMessage(ChatColor.RED + "An arena with that name does not exist.");
			return;
		}

		Kit kit = HyPractice.get().getKitRepository().getKitByName(args[1]);
		if (kit == null) {
			player.sendMessage(ChatColor.RED + "A kit with that name does not exist.");
			return;
		}

		arena.getKits().remove(kit.getName());
		arena.save();

		player.sendMessage(CC.translate("&7Removed kit &b" + kit.getName() + " &7from arena &b" + arena.getName()));
	}
}
