package com.hysteria.practice.game.arena.command;

import com.hysteria.practice.chunk.ChunkRestorationManager;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import com.hysteria.practice.game.arena.impl.StandaloneArena;
import com.hysteria.practice.utilities.chat.CC;

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
