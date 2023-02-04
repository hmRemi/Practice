package rip.crystal.practice.game.kit.command;

import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitRulesCommand extends BaseCommand {

	@Command(name = "kit.rules", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&4&lKit Rules"));
		player.sendMessage(CC.translate(""));
		player.sendMessage(CC.translate("&4Settings:"));
		player.sendMessage(CC.translate(" &7- &4Health Regeneration &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4NoFall Damage &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Effects &7(add/remove/list)"));
		player.sendMessage(CC.translate(" &7- &4AntiFood &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Showhealth &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4AntiFood &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Editoritems &7(blank)"));
		player.sendMessage(CC.translate(" &7- &4KBProfile &7(profile)"));
		player.sendMessage(CC.translate(" &7- &4Ranked &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Hitdelay &7(int)"));
		player.sendMessage(CC.translate(" &7- &4AllowPotionFill &7(int)"));
		player.sendMessage(CC.translate(""));
		player.sendMessage(CC.translate("&4Gamemodes:"));
		player.sendMessage(CC.translate(" &7- &4Parkour &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Bridge &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4BedFight &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Spleef &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Soup &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Build &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4Sumo &7(true/false)"));
		player.sendMessage(CC.translate(" &7- &4HCF &7(true/false)"));
		player.sendMessage(CC.CHAT_BAR);
	}
}
