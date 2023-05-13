package com.hysteria.practice.essentials.command.player;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

	@Command(name = "spawn", permission = "hypractice.command.spawn")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if(profile.getState() == ProfileState.SPECTATING || profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.EVENT || profile.getState() == ProfileState.FFA) {
			player.sendMessage(CC.translate("&cYou cannot do /spawn while spectating or in game."));
			return;
		}

		HyPractice.get().getEssentials().teleportToSpawn(player);
		player.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
	}
}