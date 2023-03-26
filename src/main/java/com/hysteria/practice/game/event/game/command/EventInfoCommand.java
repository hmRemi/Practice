package com.hysteria.practice.game.event.game.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.game.event.game.EventGame;
import com.hysteria.practice.game.event.impl.sumo.SumoEvent;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.entity.Player;

public class EventInfoCommand extends BaseCommand {

	@Command(name = "event.info")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		if (EventGame.getActiveGame() == null) {
			player.sendMessage(CC.RED + "There is no active event.");
			return;
		}

		EventGame game = EventGame.getActiveGame();

		player.sendMessage(CC.translate("&7(*) &b&lEvent &7(*)"));
		player.sendMessage(CC.AQUA + "Player Limit: " + CC.WHITE + game.getMaximumPlayers());
		player.sendMessage(CC.AQUA + "Event Type: " + CC.WHITE + game.getEvent().getName());
		player.sendMessage(CC.AQUA + "Players: " + CC.WHITE + game.getRemainingPlayers());
		player.sendMessage(CC.AQUA + "State: " + CC.WHITE + game.getGameState().getReadable());

		if (game.getEvent() instanceof SumoEvent) {
			player.sendMessage(CC.AQUA + "Round: " + CC.WHITE + game.getGameLogic().getRoundNumber());
		}
	}
}
