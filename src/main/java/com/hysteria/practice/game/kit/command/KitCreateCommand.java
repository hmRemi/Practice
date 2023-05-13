package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitCreateCommand extends BaseCommand {

	@Command(name = "kit.create", permission = "hypractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit create (name) (displayname)");
			return;
		}

		String kitName = args[0];
		String kitDisplayname = args[1];

		if (Kit.getByName(kitName) != null) {
			player.sendMessage(CC.RED + "A kit with that name already exists.");
			return;
		}

		Kit kit = new Kit(kitName);
		kit.setDisplayName(kitDisplayname);

		kit.save();
		Kit.getKits().add(kit);

		player.sendMessage(CC.GREEN + "You created a new kit.");
	}
}
