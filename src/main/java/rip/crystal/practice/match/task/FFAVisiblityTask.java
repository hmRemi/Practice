package rip.crystal.practice.match.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.MessageFormat;

public class FFAVisiblityTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : cPractice.get().getServer().getOnlinePlayers()) {
			Profile profile = Profile.get(player.getUniqueId());
			for (Profile target : cPractice.get().getFfaManager().getFFAPlayers()) {
				Player targetPlayer = target.getPlayer();
				if (targetPlayer == null) {
					return;
				}
				if (profile.getState() != ProfileState.FFA) {
					return;
				}
				if (target.getState() != ProfileState.FFA) {
					return;
				}
				player.showPlayer(targetPlayer);
				targetPlayer.showPlayer(player);
			}
		}
	}
}
