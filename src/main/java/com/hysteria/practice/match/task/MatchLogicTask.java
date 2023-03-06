package com.hysteria.practice.match.task;

import com.hysteria.practice.Locale;
import com.hysteria.practice.chunk.ChunkRestorationManager;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.match.MatchState;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.game.arena.impl.StandaloneArena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchLogicTask extends BukkitRunnable {

	private final Match match;
	private int totalTicked;
	@Getter @Setter private int nextAction;

	public MatchLogicTask(Match match) {
		this.match = match;

		if (match.getKit().getGameRules().isSumo()) {
			nextAction = 4;
		} else {
			nextAction = 6;
		}
	}

	@Override
	public void run() {
		totalTicked++;
		nextAction--;
		if (match.getState() == MatchState.STARTING_ROUND) {
			if (nextAction == 0) {
				TaskUtil.run(match::onRoundStart);
				match.setState(MatchState.PLAYING_ROUND);
				match.sendMessage(Locale.MATCH_STARTED, new MessageFormat());
				match.sendSound(Sound.ORB_PICKUP, 1.0F, 1.0F);
				match.sendMessage(Locale.MATCH_WARNING, new MessageFormat());
				match.broadcastTitle("&a&lMATCH STARTED!", "&7Good Luck!", 50);
			} else {
				match.broadcastTitle("&a&lStarting in", "&7" + nextAction, 20);
				match.sendMessage(Locale.MATCH_START_TIMER, new MessageFormat()
					.add("{time}", String.valueOf(nextAction))
					.add("{context}", nextAction == 1 ? "" : "s")
				);
				match.sendSound(Sound.ORB_PICKUP, 1.0F, 15F);
			}
		}
		else if (match.getState() == MatchState.ENDING_ROUND) {
			if (nextAction == 0) {
				if (match.canStartRound()) {
					TaskUtil.run(match::setEffects);
					match.setState(MatchState.STARTING_ROUND);
					match.getLogicTask().setNextAction(4);
				}
			}
		}
		else if (match.getState() == MatchState.ENDING_MATCH) {
			if(match.getArena() instanceof StandaloneArena) {
				ChunkRestorationManager.getIChunkRestoration().reset(match.getArena());
			}
			if (nextAction == 0) TaskUtil.run(match::end);
		}
	}

}
