package com.hysteria.practice.essentials.chat.impl.command;

import com.hysteria.practice.Locale;
import com.hysteria.practice.essentials.chat.impl.Chat;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteChatCommand extends BaseCommand {

	@Command(name = "mutechat", permission = "cpractice.staff.mutechat", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		Chat.togglePublicChatMute();

		String senderName;

		if (sender instanceof Player) {
			senderName = Profile.get(((Player) sender).getUniqueId()).getColor() + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Chat.isPublicChatMuted() ? "muted" : "unmuted";

		Bukkit.getOnlinePlayers().forEach(online -> {
			Profile profile = Profile.get(online.getUniqueId());
			new MessageFormat(Locale.MUTE_CHAT_BROADCAST.format(profile.getLocale()))
					.add("{sender_name}", senderName)
					.add("{context}", context)
					.send(online);
		});
	}
}
