package com.hysteria.practice.essentials.command.staff;

import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.utilities.chat.CC;

public class RetardCommand extends BaseCommand {

	@Command(name = "retard", permission = "hypractice.command.nigga")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&c&lOPTIFINE-REDUCER IS A RETARD!"));
		player.sendMessage(CC.CHAT_BAR);
	}
}
