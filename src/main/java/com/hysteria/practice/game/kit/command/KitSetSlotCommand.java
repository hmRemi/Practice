package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class KitSetSlotCommand extends BaseCommand {

	@Command(name = "kit.setslot", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit setslot (kit) (slot)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}

		int slot;
		if (!StringUtils.isNumeric(args[1])) {
			player.sendMessage(CC.RED + "Please usage a valid slot.");
			return;
		}
		slot = Integer.parseInt(args[1]);

		kit.setSlot(slot);
		player.sendMessage(CC.RED + "Slot changed to " + kit.getName() + " successfully to " + slot);
	}
}
