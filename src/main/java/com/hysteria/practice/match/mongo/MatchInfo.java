package com.hysteria.practice.match.mongo;

import com.hysteria.practice.game.kit.Kit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter @Getter
public class MatchInfo {

    private final String winningParticipant;
    private final String losingParticipant;
    private final Kit kit;
    private final int newWinnerElo;
    private final int newLoserElo;
    private final String date;
    private final String duration;

}