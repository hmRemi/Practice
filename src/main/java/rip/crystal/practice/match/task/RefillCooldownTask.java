package rip.crystal.practice.match.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class RefillCooldownTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : cPractice.get().getServer().getOnlinePlayers()) {
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
