package com.hysteria.practice.player.party.command.subcommands;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.player.party.Party;
import com.hysteria.practice.player.party.enums.PartyPrivacy;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PartyJoinCommand extends BaseCommand {

	@Command(name = "party.join", aliases = {"p.join"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /party join (leader)");
			return;
		}

		Profile profile = Profile.get(player.getUniqueId());
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			new MessageFormat(Locale.PLAYER_NOT_FOUND
					.format(profile.getLocale()))
					.send(player);
			return;
		}

		if (profile.getParty() != null) {
			player.sendMessage(CC.RED + "You already have a party.");
			return;
		}

		if(profile.getState() != ProfileState.LOBBY) {
			player.sendMessage(CC.RED + "You must be in spawn");
			return;
		}

		Profile targetProfile = Profile.get(target.getUniqueId());
		Party party = targetProfile.getParty();

		if (party == null) {
			player.sendMessage(CC.RED + "A party with that name could not be found.");
			return;
		}

		if (party.getPrivacy() == PartyPrivacy.CLOSED) {
			if (party.getInvite(player.getUniqueId()) == null) {
				player.sendMessage(CC.RED + "You have not been invited to that party.");
				return;
			}
		}

		int limit;

		if (party.getLeader().hasPermission("hypractice.party.vip")){
			limit = HyPractice.get().getMainConfig().getInteger("PARTY.VIP_LIMIT");
		} else {
			limit = HyPractice.get().getMainConfig().getInteger("PARTY.DEFAULT_LIMIT");
		}

		if (party.getPlayers().size() >= limit) {
			player.sendMessage(CC.RED + "That party is full and cannot hold anymore players.");
			return;
		}

		if(party.getBannedPlayers().contains(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "You are banned from this party, you cannot enter.");
			return;
		}

		party.join(player);
	}
}
