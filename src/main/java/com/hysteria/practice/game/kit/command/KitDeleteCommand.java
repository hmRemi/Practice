package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitDeleteCommand extends BaseCommand {

	@Command(name = "kit.delete", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit remove (kitName)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name doesn't exists.");
			return;
		}

		kit.delete();
		player.sendMessage(CC.RED + "You deleted this kit.");
	}
}
