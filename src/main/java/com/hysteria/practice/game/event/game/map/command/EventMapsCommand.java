package com.hysteria.practice.game.event.game.map.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.game.event.game.map.EventGameMap;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.entity.Player;

public class EventMapsCommand extends BaseCommand {

	@Command(name = "event.maps", permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.RED + CC.BOLD + "Event Maps");

		if (EventGameMap.getMaps().isEmpty()) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.GRAY + "There are no event maps.");
			player.sendMessage(CC.CHAT_BAR);
		} else {
			for (EventGameMap gameMap : EventGameMap.getMaps()) {
				player.sendMessage(" - " + (gameMap.isSetup() ? CC.GREEN : CC.RED) + gameMap.getMapName());
			}
		}
	}
}
