package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitRulesCommand extends BaseCommand {

	@Command(name = "kit.rules", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&b&lKit Rules"));
		player.sendMessage(CC.translate(""));
		player.sendMessage(CC.translate("&bSettings:"));
		player.sendMessage(CC.translate(" &7- &bHealth Regeneration &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bNoFall Damage &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bEffects &7(add/remove/list)"));
		player.sendMessage(CC.translate(" &7- &bAntiFood &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bShowhealth &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bAntiFood &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bEditoritems &7(blank)"));
		player.sendMessage(CC.translate(" &7- &bKBProfile &7(profile)"));
		player.sendMessage(CC.translate(" &7- &bRanked &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bHitdelay &7(int)"));
		player.sendMessage(CC.translate(" &7- &bAllowPotionFill &7(int)"));
		player.sendMessage(CC.translate(""));
		player.sendMessage(CC.translate("&bGamemodes:"));
		player.sendMessage(CC.translate(" &7- &bBridge &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bBedFight &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bLibes &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bSpleef &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bSoup &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bBuild &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bSumo &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &bHCF &7(true/false)"));
		player.sendMessage(CC.CHAT_BAR);
	}
}
