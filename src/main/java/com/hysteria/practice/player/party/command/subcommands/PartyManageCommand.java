package com.hysteria.practice.player.party.command.subcommands;

import com.hysteria.practice.player.profile.Profile;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.player.party.menu.manage.PartyManageMenu;
import com.hysteria.practice.utilities.chat.CC;

public class PartyManageCommand extends BaseCommand {

	@Command(name = "party.manage", aliases = {"p.manage"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getParty() == null) {
			player.sendMessage(CC.translate("&8[&c&lParty&8] &7You do not have a party."));
			return;
		}

		if(profile.getParty().getLeader() != player) {
			player.sendMessage(CC.translate("&8[&c&lParty&8] &7Only the leader can use this."));
			return;
		}

		new PartyManageMenu().openMenu(player);
	}
}
