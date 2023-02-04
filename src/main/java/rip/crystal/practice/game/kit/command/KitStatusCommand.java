package rip.crystal.practice.game.kit.command;

import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitStatusCommand extends BaseCommand {

	@Command(name = "kit.status", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit status (kit)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&4&lKits Status &7(" + (kit.isEnabled() ? "&a" : "&c") + kit.getName() + "&7)"));
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate(" &7▢ &4Ranked&f: " + (kit.getGameRules().isRanked() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Build&f: " + (kit.getGameRules().isBuild() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Spleef&f: " + (kit.getGameRules().isSpleef() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Sumo&f: " + (kit.getGameRules().isSumo() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Soup&f: " + (kit.getGameRules().isSoup() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Parkour&f: " + (kit.getGameRules().isParkour() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4HCF&f: " + (kit.getGameRules().isHcf() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4HCFTrap&f: " + (kit.getGameRules().isHcftrap() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Bridge&f: " + (kit.getGameRules().isBridge() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4BedFight&f: " + (kit.getGameRules().isBedFight() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Health Regeneration&f: " + (kit.getGameRules().isHealthRegeneration() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4NoFall Damage&f: " + (kit.getGameRules().isNofalldamage() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Anti Food&f: " + (kit.getGameRules().isAntiFood() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Show Health&f: " + (kit.getGameRules().isShowHealth() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &4Hit Delay&f: " + kit.getGameRules().getHitDelay()));
		player.sendMessage(CC.translate(" &7▢ &4KB Profile&f: " + kit.getGameRules().getKbProfile()));
		player.sendMessage(CC.CHAT_BAR);
	}
}
