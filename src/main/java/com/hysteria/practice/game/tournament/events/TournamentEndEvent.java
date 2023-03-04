package com.hysteria.practice.game.tournament.events;

import com.hysteria.practice.utilities.event.CustomEvent;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

@Getter @Setter
@AllArgsConstructor
public class TournamentEndEvent extends CustomEvent {

    private final GameParticipant<MatchGamePlayer> winner;
    private final boolean team;
    private final boolean clan;

}