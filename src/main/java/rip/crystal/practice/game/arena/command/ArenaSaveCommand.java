package rip.crystal.practice.game.arena.command;

import rip.crystal.practice.chunk.ChunkRestorationManager;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.utilities.chat.CC;

public class ArenaSaveCommand extends BaseCommand {

	@Command(name = "arena.save", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		for (Arena arena : Arena.getArenas()) {
			if (arena instanceof StandaloneArena) {
				ChunkRestorationManager.getIChunkRestoration().copy(arena);
			}
		}
		player.sendMessage(CC.translate("&7Saved all arenas!"));
	}

}
