package rip.crystal.practice.game.arena.command;

import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.chat.CC;

public class ArenaAddKitCommand extends BaseCommand {

	@Command(name = "arena.addkit", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		Arena arena = Arena.getByName(args[0]);
		if (arena == null) {
			player.sendMessage(CC.translate("&7An arena with that name does not exist."));
			return;
		}

		Kit kit = Kit.getByName(args[1]);
		if (kit == null) {
			player.sendMessage(CC.translate("&7A kit with that name does not exist."));
			return;
		}

		arena.getKits().add(kit.getName());
		arena.save();

		player.sendMessage(CC.translate("&7Added kit &4" + kit.getName() + " &7to arena &4" + arena.getName()));
	}

}
