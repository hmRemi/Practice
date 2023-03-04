package com.hysteria.practice.game.event.game.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.game.event.game.EventGame;
import com.hysteria.practice.utilities.Cooldown;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class EventClearCooldownCommand extends BaseCommand {

	@Command(name = "event.clearcooldown", aliases = {"clearcd"}, permission = "cpractice.event.admin", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		EventGame.setCooldown(new Cooldown(0));
		sender.sendMessage(CC.CHAT_BAR);
		sender.sendMessage(ChatColor.GREEN + "You cleared the event cooldown.");
		sender.sendMessage(CC.CHAT_BAR);
	}
}
