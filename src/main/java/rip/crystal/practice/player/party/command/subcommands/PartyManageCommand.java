package rip.crystal.practice.player.party.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.party.enums.PartyPrivacy;
import rip.crystal.practice.player.party.menu.manage.PartyManageMenu;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

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
