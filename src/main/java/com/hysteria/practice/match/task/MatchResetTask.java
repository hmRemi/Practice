package com.hysteria.practice.match.task;

import com.hysteria.practice.match.Match;
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