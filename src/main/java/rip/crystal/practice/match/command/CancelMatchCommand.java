package rip.crystal.practice.match.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.chunk.ChunkRestorationManager;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CancelMatchCommand extends BaseCommand {

	@Command(name = "cancelmatch", permission = "cpractice.command.cancelmatch")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Usage: /cancelmatch <player>.");
			return;
		}

		Player target = Bukkit.getPlayer(args[0]);

		if(target == null) {
			player.sendMessage(CC.RED + "This player isn't online.");
			return;
		}

		Profile targetProfile = Profile.get(target.getUniqueId());

		if(targetProfile.getMatch() == null) {
			player.sendMessage(CC.RED + "Player is not in a match.");
			return;
		}

		new MessageFormat(Locale.MATCH_CANCELLED.format(targetProfile.getLocale()))
				.add("<cancelled_by>", player.getName())
				.send(player);

		if(targetProfile.getMatch().getArena() instanceof StandaloneArena) {
			ChunkRestorationManager.getIChunkRestoration().reset(targetProfile.getMatch().getArena());
		}
		targetProfile.getMatch().end();
		targetProfile.getMatch().getArena().setActive(false);

		player.sendMessage(CC.translate("&cYou have cancelled " + target.getName() + "'s match."));
	}
}
