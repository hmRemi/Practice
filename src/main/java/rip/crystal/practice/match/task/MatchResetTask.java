package rip.crystal.practice.match.task;

import rip.crystal.practice.match.Match;
import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class MatchResetTask extends BukkitRunnable {

	private final Match match;

	public void run() {
		match.getArena().setActive(false);
		this.cancel();
	}
}