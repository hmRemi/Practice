package com.hysteria.practice.match.task;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.MessageFormat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RefillCooldownTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : HyPractice.get().getServer().getOnlinePlayers()) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getState() == ProfileState.FFA) {
				if (profile.getRefillCooldown().hasExpired()) {
					if (!profile.getRefillCooldown().isNotified()) {
						profile.getRefillCooldown().setNotified(true);
						new MessageFormat(Locale.FFA_REFILL_COOLDOWN_EXPIRED
								.format(profile.getLocale()))
								.send(player);
					}
				}
			}
		}
	}

}
