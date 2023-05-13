package com.hysteria.practice.game.event.game.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.game.event.game.EventGame;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class EventCancelCommand extends BaseCommand {

	@Command(name = "event.cancel", permission = "hypractice.event.admin", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		if (EventGame.getActiveGame() != null) {
			EventGame.getActiveGame().getGameLogic().cancelEvent();
		} else {
			sender.sendMessage(CC.CHAT_BAR);
			sender.sendMessage(ChatColor.RED + "There is no active event.");
			sender.sendMessage(CC.CHAT_BAR);
		}
	}
}
