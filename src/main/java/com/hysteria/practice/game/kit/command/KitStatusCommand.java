package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitStatusCommand extends BaseCommand {

	@Command(name = "kit.status", permission = "hypractice.kit.admin")
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
		player.sendMessage(CC.translate("&b&lKits Status &7(" + (kit.isEnabled() ? "&a" : "&b") + kit.getName() + "&7)"));
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate(" &7▢ &bRanked&f: " + (kit.getGameRules().isRanked() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bBuild&f: " + (kit.getGameRules().isBuild() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bSpleef&f: " + (kit.getGameRules().isSpleef() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bSumo&f: " + (kit.getGameRules().isSumo() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bSoup&f: " + (kit.getGameRules().isSoup() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bParkour&f: " + (kit.getGameRules().isParkour() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bHCF&f: " + (kit.getGameRules().isHcf() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bHCFTrap&f: " + (kit.getGameRules().isHcftrap() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bBattle Rush&f: " + (kit.getGameRules().isBattlerush() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bBridge&f: " + (kit.getGameRules().isBridge() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bLives&f: " + (kit.getGameRules().isLives() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bBedFight&f: " + (kit.getGameRules().isBedFight() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bHealth Regeneration&f: " + (kit.getGameRules().isHealthRegeneration() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bNoFall Damage&f: " + (kit.getGameRules().isNofalldamage() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bNo Damage&f: " + (kit.getGameRules().isNodamage() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bAnti Food&f: " + (kit.getGameRules().isAntiFood() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bShow Health&f: " + (kit.getGameRules().isShowHealth() ? "&a\u2713" : "&b\u2717")));
		player.sendMessage(CC.translate(" &7▢ &bHit Delay&f: " + kit.getGameRules().getHitDelay()));
		player.sendMessage(CC.translate(" &7▢ &bKB Profile&f: " + kit.getGameRules().getKbProfile()));
		player.sendMessage(CC.CHAT_BAR);
	}
}
