package com.hysteria.practice.game.tournament;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

@AllArgsConstructor
@Getter
public enum TournamentState {

    WAITING("Waiting"),
    STARTING("Starting"),
    IN_FIGHT("Fighting"),
    SELECTING_DUELS("Selecting duels"),
    ENDED("Ended");

    private final String name;

}