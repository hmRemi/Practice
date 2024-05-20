package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.chat.ChatComponentBuilder;
import com.hysteria.practice.utilities.chat.ChatHelper;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitsCommand extends BaseCommand {

	@Command(name = "kits", permission = "hypractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(ChatColor.RED + "Kits:");

		for (Kit kit : HyPractice.get().getKitRepository().getKits()) {
			ChatComponentBuilder builder = new ChatComponentBuilder("").parse("&7- " + (kit.isEnabled() ? "&8[&a&lEnabled&8] &b" : "&8[&c&lDisabled&8] &b") + kit.getName() + " &7(" + (kit.getGameRules().isRanked() ? "Ranked" : "Un-Ranked") + ")");
			ChatComponentBuilder status = new ChatComponentBuilder("").parse("&7[&cSTATUS&7]");
			status.attachToEachPart(ChatHelper.hover("&7Click to view this kit's status."));
			status.attachToEachPart(ChatHelper.click("/kit status " + kit.getName()));

			builder.append(" ");

			for (BaseComponent component : status.create()) {
				builder.append((TextComponent) component);
			}

			player.spigot().sendMessage(builder.create());
		}
	}
}
