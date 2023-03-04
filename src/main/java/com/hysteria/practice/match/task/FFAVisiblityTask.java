package com.hysteria.practice.match.task;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FFAVisiblityTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : HyPractice.get().getServer().getOnlinePlayers()) {
			Profile profile = Profile.get(player.getUniqueId());
			for (Profile target : HyPractice.get().getFfaManager().getFFAPlayers()) {
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
