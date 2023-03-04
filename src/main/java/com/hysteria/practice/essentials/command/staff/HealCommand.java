package com.hysteria.practice.essentials.command.staff;

import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCommand {

	@Command(name = "heal", permission = "cpractice.command.heal")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.setHealth(20.0);
			player.setFoodLevel(20);
			player.setSaturation(5.0F);
			player.updateInventory();
			player.sendMessage(CC.GOLD + "You healed yourself.");
		} else {
			Player target = commandArgs.getPlayer();
			if (target == null) {
				new MessageFormat(Locale.PLAYER_NOT_FOUND
						.format(Profile.get(player.getUniqueId()).getLocale()))
						.send(player);
				return;
			}
			target.setHealth(20.0);
			target.setFoodLevel(20);
			target.setSaturation(5.0F);
			target.updateInventory();
			target.sendMessage(CC.GOLD + "You have been healed by " + player.getName());
		}
	}
}
