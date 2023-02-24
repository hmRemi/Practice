package rip.crystal.practice.match.task;

import org.bukkit.Location;
import org.bukkit.Material;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.chunk.ChunkRestorationManager;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchState;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
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
				if(match.getKit().getGameRules().isBattlerush()) {

				}
				match.sendMessage(Locale.MATCH_WARNING, new MessageFormat());
			} else {
				match.broadcastTitle("&4&lMatch starting in", "&4" + nextAction);
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
