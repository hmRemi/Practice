package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.chat.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class KitSetDisplayNameCommand extends BaseCommand {

	@Command(name = "kit.setdisplayname", permission = "hypractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit setdisplayname (kit) (displayname)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}

		String first = args[1];
		if(first == null) {
			player.sendMessage(CC.RED + "You must set the displayname of the kit");
			return;
		}

		if (args.length == 3) {
			kit.setDisplayName(first + " " + args[2]);
			player.sendMessage(CC.RED + "Display name of " + kit.getName() + " successfully changed to " + first + " " + args[2]);
		} else {
			kit.setDisplayName(first);
			player.sendMessage(CC.RED + "Display name of " + kit.getName() + " successfully changed to " + first);
		}
	}
}
