package com.hysteria.practice.player.queue.task;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.match.impl.BasicTeamBedFight;
import com.hysteria.practice.match.impl.BasicTeamLivesFight;
import com.hysteria.practice.match.impl.BasicTeamMatch;
import com.hysteria.practice.match.impl.BasicTeamRoundMatch;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;
import com.hysteria.practice.player.queue.Queue;
import com.hysteria.practice.player.queue.QueueProfile;
import com.hysteria.practice.utilities.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QueueTask implements Runnable {

    @Override
    public void run() {
        for (Queue queue : Queue.getQueues()) {
            queue.getPlayers().forEach(QueueProfile::tickRange);

            if (queue.getPlayers().size() < 2) continue;

            for (QueueProfile firstQueueProfile : queue.getPlayers()) {
                Player firstPlayer = Bukkit.getPlayer(firstQueueProfile.getPlayerUuid());

                if (firstPlayer == null) continue;

                Profile firstProfile = Profile.get(firstPlayer.getUniqueId());

                // Find arena
                Arena arena = Arena.getRandomArena(queue.getKit());

                if (arena == null) {
                    queue.getPlayers().remove(firstQueueProfile);
                    new MessageFormat(Locale.QUEUE_NO_ARENAS_AVAILABLE
                            .format(Profile.get(firstPlayer.getUniqueId()).getLocale()))
                            .add("{kit}", queue.getKit().getName())
                            .send(firstPlayer);
                    break;
                }

                for (QueueProfile secondQueueProfile : queue.getPlayers()) {
                    if (firstQueueProfile.equals(secondQueueProfile)) continue;

                    Player secondPlayer = Bukkit.getPlayer(secondQueueProfile.getPlayerUuid());

                    if (secondPlayer == null) continue;

                    Profile secondProfile = Profile.get(secondPlayer.getUniqueId());


                    if(/*firstProfile.getOptions().isUsingPingFactor() &&*/firstProfile.getPingRange() != -1) {
                        if(PlayerUtil.getPing(secondPlayer) > firstProfile.getPingRange()) {
                            return;
                        }
                    }
                    if(/*secondProfile.getOptions().isUsingPingFactor() &&*/secondProfile.getPingRange() != -1) {
                        if(PlayerUtil.getPing(firstPlayer) > secondProfile.getPingRange()) {
                            return;
                        }
                    }

                    if (queue.isRanked()) {
                        if (!firstQueueProfile.isInRange(secondQueueProfile.getElo()) ||
                                !secondQueueProfile.isInRange(firstQueueProfile.getElo())) {
                            continue;
                        }
                    }

                    // Update arena
                    arena.setActive(true);

                    // Remove players from queue
                    queue.getPlayers().remove(firstQueueProfile);
                    queue.getPlayers().remove(secondQueueProfile);

                    MatchGamePlayer playerA = new MatchGamePlayer(firstPlayer.getUniqueId(),
                            Profile.get(firstPlayer.getUniqueId()).getColor() + firstPlayer.getName(), firstQueueProfile.getElo());

                    MatchGamePlayer playerB = new MatchGamePlayer(secondPlayer.getUniqueId(),
                            Profile.get(secondPlayer.getUniqueId()).getColor() + secondPlayer.getName(), secondQueueProfile.getElo());

                    GameParticipant<MatchGamePlayer> participantA = new GameParticipant<>(playerA);
                    GameParticipant<MatchGamePlayer> participantB = new GameParticipant<>(playerB);

                    // Create match
                    Match match;

                    if (queue.getKit().getGameRules().isBridge()) {
                        match = new BasicTeamRoundMatch(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB, HyPractice.get().getBridgeRounds());
                    }
                    else if (queue.getKit().getGameRules().isBedFight()) {
                        match = new BasicTeamBedFight(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB);
                    }
                    else if (queue.getKit().getGameRules().isLives()) {
                        match = new BasicTeamLivesFight(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB);
                    }
                    else if (queue.isRanked() && queue.getKit().getGameRules().isSumo()) {
                        match = new BasicTeamRoundMatch(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB, HyPractice.get().getRankedSumoRounds());
                    }
                    else {
                        match = new BasicTeamMatch(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB);
                    }

                    if (queue.isRanked()) {
                        new MessageFormat(Locale.QUEUE_FOUND_RANKED_MATCH
                                .format(Profile.get(firstPlayer.getUniqueId()).getLocale()))
                                .add("{name}", firstPlayer.getName())
                                .add("{elo}", String.valueOf(firstQueueProfile.getElo()))
                                .add("{opponent}", secondPlayer.getName())
                                .add("{opponent-elo}", String.valueOf(secondQueueProfile.getElo()))
                                .send(firstPlayer);
                        new MessageFormat(Locale.QUEUE_FOUND_RANKED_MATCH
                                .format(Profile.get(secondPlayer.getUniqueId()).getLocale()))
                                .add("{name}", secondPlayer.getName())
                                .add("{elo}", String.valueOf(secondQueueProfile.getElo()))
                                .add("{opponent}", firstPlayer.getName())
                                .add("{opponent-elo}", String.valueOf(firstQueueProfile.getElo()))
                                .send(secondPlayer);
                    } else {
                        new MessageFormat(Locale.QUEUE_FOUND_UNRANKED_MATCH
                                .format(Profile.get(firstPlayer.getUniqueId()).getLocale()))
                                .add("{name}", firstPlayer.getName())
                                .add("{opponent}", secondPlayer.getName())
                                .send(firstPlayer);
                        new MessageFormat(Locale.QUEUE_FOUND_UNRANKED_MATCH
                                .format(Profile.get(secondPlayer.getUniqueId()).getLocale()))
                                .add("{name}", secondPlayer.getName())
                                .add("{opponent}", firstPlayer.getName())
                                .send(secondPlayer);
                    }

                    synchronized(match) {
                        match.start();
                    }
                }
            }
        }
    }
}
