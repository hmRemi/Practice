package com.hysteria.practice.essentials.command.staff;

import com.hysteria.practice.utilities.LocationUtil;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class LocationCommand extends BaseCommand {

	@Command(name = "location", aliases = {"loc"}, permission = "cpractice.command.loc")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(LocationUtil.serialize(player.getLocation()));
	}
}
