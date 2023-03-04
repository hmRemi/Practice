package com.hysteria.practice.player.party.command.subcommands;

import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PartyLeaveCommand extends BaseCommand {

	@Command(name = "party.leave", aliases = {"p.leave"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getParty() == null) {
			player.sendMessage(CC.RED + "You do not have a party.");
			return;
		}

		if (profile.getParty().getLeader().equals(player)) {
			profile.getParty().disband();
		} else {
			profile.getParty().leave(player, false);
		}
	}
}
