package com.hysteria.practice.player.profile.meta.option.command;

import com.hysteria.practice.player.profile.menu.settings.SettingsMenu;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class OptionsCommand extends BaseCommand {

	@Command(name = "options", aliases = {"settings", "hsettings"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		new SettingsMenu().openMenu(player);
	}
}
