package com.hysteria.practice.game.event.command;

import com.hysteria.practice.game.event.Event;
import com.hysteria.practice.game.event.game.map.EventGameMap;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class EventRemoveMapCommand extends BaseCommand {

	@Command(name = "event.removemap", permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0 || args.length == 1) {
			player.sendMessage(CC.RED + "Please usage: /event removemap (event) (map)");
			return;
		}

		Event event = Event.getByName(args[0]);
		if (event == null) {
			player.sendMessage(CC.RED + "An event type by that name does not exist.");
			player.sendMessage(CC.RED + "Types: sumo, tntrun, spleef");
			return;
		}

		EventGameMap gameMap = EventGameMap.getByName(args[1]);
		if (gameMap == null) {
			player.sendMessage(CC.RED + "A map with that name does not exist.");
			return;
		}

		if (event.getAllowedMaps().remove(gameMap.getMapName())) {
			event.save();

			player.sendMessage(CC.GREEN + "You successfully removed the \"" + gameMap.getMapName() +
					"\" map from the \"" + event.getName() + "\" event.");
		}
	}
}
