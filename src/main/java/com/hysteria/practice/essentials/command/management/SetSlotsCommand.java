package com.hysteria.practice.essentials.command.management;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.BukkitReflection;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class SetSlotsCommand extends BaseCommand {

	@Command(name = "setslots", permission = "cpractice.command.setslots")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert a valid slot.");
			return;
		}

		int slots;
		if (!StringUtils.isNumeric(args[0])) {
			player.sendMessage(CC.RED + "Please insert a valid integer");
			return;
		}
		slots = Integer.getInteger(args[0]);

		BukkitReflection.setMaxPlayers(HyPractice.get().getServer(), slots);
		player.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
	}
}
